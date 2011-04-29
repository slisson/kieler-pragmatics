/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2010 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 *     
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.rail.editor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.ICreateFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.algorithms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.palette.impl.ConnectionCreationToolEntry;
import org.eclipse.graphiti.palette.impl.ObjectCreationToolEntry;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;

import de.cau.cs.kieler.rail.editor.features.RotateSwitchFeature;
import de.cau.cs.kieler.rail.editor.features.ToggleSwitchFeature;
import de.menges.topologie.Topologie.SpecializedVertices.EOrientation;
import de.menges.topologie.Topologie.SpecializedVertices.Weichenknoten;

/**
 * @author hdw
 */
public class ToolBehaviorProvider extends DefaultToolBehaviorProvider {

    /**
     *
     * Creates a new ToolBehaviorProvider.
     * @param diagramTypeProvider
     *            the diagram type provider
     */
    public ToolBehaviorProvider(final DiagramTypeProvider diagramTypeProvider) {
        super(diagramTypeProvider);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public final IPaletteCompartmentEntry[] getPalette() {
        List<IPaletteCompartmentEntry> ret =
                new ArrayList<IPaletteCompartmentEntry>();

        // add compartments from super class
        IPaletteCompartmentEntry[] superCompartments = super.getPalette();
        for (int i = 0; i < superCompartments.length; i++) {
            ret.add(superCompartments[i]);
        }

        // add all create-features to the new stack-entry
        IFeatureProvider featureProvider = getFeatureProvider();
        ICreateFeature[] createFeatures = featureProvider.getCreateFeatures();
        for (ICreateFeature cf : createFeatures) {
            ObjectCreationToolEntry objectCreationToolEntry =
                    new ObjectCreationToolEntry(cf.getCreateName(),
                            cf.getCreateDescription(), cf.getCreateImageId(),
                            cf.getCreateLargeImageId(), cf);
            //stackEntry.addCreationToolEntry(objectCreationToolEntry);
        }

        // add all create-connection-features to the new stack-entry
        ICreateConnectionFeature[] createConnectionFeatures =
                featureProvider.getCreateConnectionFeatures();
        for (ICreateConnectionFeature cf : createConnectionFeatures) {
            ConnectionCreationToolEntry connectionCreationToolEntry =
                    new ConnectionCreationToolEntry(cf.getCreateName(),
                            cf.getCreateDescription(), cf.getCreateImageId(),
                            cf.getCreateLargeImageId());
            connectionCreationToolEntry.addCreateConnectionFeature(cf);
            //stackEntry.addCreationToolEntry(connectionCreationToolEntry);
        }
        return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);
    }

    /**
     * {@inheritDoc}
     */
    @Override
	public final GraphicsAlgorithm[] getClickArea(final PictogramElement pe) {
        // maybe later I will use it.
        // System.out.println(pe);
        return super.getClickArea(pe);

    }

    /**
     * {@inheritDoc}
     */
    @Override
	public final IContextButtonPadData getContextButtonPad(
            final IPictogramElementContext context) {
        IContextButtonPadData data = super.getContextButtonPad(context);

        PictogramElement pe = context.getPictogramElement();
        Object bo =
                getFeatureProvider().getBusinessObjectForPictogramElement(pe);

        // 1. set the generic context buttons
        // note, that we do not add 'remove' (just as an example)
        setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE);

        // 2. set the toggle button
        // if bo is a switch show COTEXT_BUTTON
        if (bo instanceof Weichenknoten) {
            CustomContext ccContextButon =
                    new CustomContext(new PictogramElement[] {pe});
            ccContextButon.setInnerPictogramElement(pe);
            ICustomFeature[] cf =
                    getFeatureProvider().getCustomFeatures(ccContextButon);

            // catch the right feature
            for (int i = 0; i < cf.length; i++) {
                if (cf[i].getName() == ToggleSwitchFeature.NAME) {
                    ContextButtonEntry toogleButton =
                            new ContextButtonEntry(cf[i], ccContextButon);

                    if (((Weichenknoten) bo).getAbzweigendeLage()
                    == EOrientation.LINKS) {
                        toogleButton.setText("links Weiche -> rechts Weiche");
                    } else {
                        toogleButton.setText("rechts Weiche -> links Weiche");
                    }
                    toogleButton.setIconId(ImageProvider.IMG_TOGGLE);

                    data.getGenericContextButtons().add(toogleButton);
                } else if (cf[i].getName() == RotateSwitchFeature.NAME) {
                ContextButtonEntry toogleButton =
                        new ContextButtonEntry(cf[i], ccContextButon);

                	toogleButton.setText("Rotiert die Weiche um 60°");
	                toogleButton.setIconId(ImageProvider.IMG_ROTATE);

	                data.getGenericContextButtons().add(toogleButton);
                }
            }

        }
        return data;
    }
    /**
     * {@inheritDoc}
     */
    public final boolean isDefaultBendPointRenderingActive(){
    	return false;
    }
}