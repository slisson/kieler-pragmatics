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
package de.cau.cs.kieler.kaom.graphiti.diagram;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.graphiti.dt.IDiagramTypeProvider;
import org.eclipse.graphiti.features.ICreateConnectionFeature;
import org.eclipse.graphiti.features.IFeature;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.context.IDoubleClickContext;
import org.eclipse.graphiti.features.context.IPictogramElementContext;
import org.eclipse.graphiti.features.context.impl.CreateConnectionContext;
//import org.eclipse.graphiti.features.context.impl.CustomContext;
import org.eclipse.graphiti.features.custom.ICustomFeature;
import org.eclipse.graphiti.mm.pictograms.Anchor;
import org.eclipse.graphiti.mm.pictograms.AnchorContainer;
import org.eclipse.graphiti.mm.pictograms.ConnectionDecorator;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.GraphicsAlgorithm;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.palette.IPaletteCompartmentEntry;
import org.eclipse.graphiti.platform.IPlatformImageConstants;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.tb.ContextButtonEntry;
import org.eclipse.graphiti.tb.ContextMenuEntry;
import org.eclipse.graphiti.tb.DefaultToolBehaviorProvider;
import org.eclipse.graphiti.tb.IContextButtonPadData;
import org.eclipse.graphiti.tb.IContextMenuEntry;
import org.eclipse.graphiti.tb.IRenderingDecorator;
import org.eclipse.graphiti.tb.ImageRenderingDecorator;

import de.cau.cs.kieler.kaom.Entity;
import de.cau.cs.kieler.kaom.graphiti.features.RenameEntityFeature;
import de.cau.cs.kieler.kaom.graphiti.features.RenameLinkFeature;

/**
 * 
 * @author atr Class provides all the features to control the tool bar
 */
public class ToolBehaviourProvider extends DefaultToolBehaviorProvider {

    /**
     * 
     * @param diagramTypeProvider
     *            Constructor.
     */
    public ToolBehaviourProvider(final IDiagramTypeProvider diagramTypeProvider) {
        super(diagramTypeProvider);

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    protected boolean isContextMenuApplicable(final IFeature feature) {
        boolean ret = (feature instanceof ICustomFeature);
        return ret;

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public GraphicsAlgorithm[] getSelectionArea(final PictogramElement pe) {
        // This method basically selects the inner pictogram element ie the rounded rectangle
        // inside the outside invisible rectangle
        IFeatureProvider featureProvider = getFeatureProvider();
        Object obj = featureProvider.getBusinessObjectForPictogramElement(pe);
        if (obj instanceof Entity) {
            GraphicsAlgorithm invisible = pe.getGraphicsAlgorithm();
            if (invisible.getGraphicsAlgorithmChildren().size() != 0) {
                GraphicsAlgorithm rectangle = invisible.getGraphicsAlgorithmChildren().get(0);
                return new GraphicsAlgorithm[] { rectangle };
            }
        }
        return super.getSelectionArea(pe);
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public GraphicsAlgorithm getSelectionGraphicsAlgorithm(final PictogramElement pe) {
        // This method is used to select the Graphics Algorithm of the above selected inner Pictogram
        // Element
        IFeatureProvider featureProvider = getFeatureProvider();
        Object obj = featureProvider.getBusinessObjectForPictogramElement(pe);

        if (obj instanceof Entity) {
            GraphicsAlgorithm invisible = pe.getGraphicsAlgorithm();
            EList<GraphicsAlgorithm> graphicsAlgorithmChildren = invisible
                    .getGraphicsAlgorithmChildren();

            if (!graphicsAlgorithmChildren.isEmpty()) {
                return graphicsAlgorithmChildren.get(0);
            }
        }

        return super.getSelectionGraphicsAlgorithm(pe);

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IContextButtonPadData getContextButtonPadData(final IPictogramElementContext context) {
        IContextButtonPadData data = super.getContextButtonPadData(context);
        PictogramElement pe = context.getPictogramElement();

        setGenericContextButtons(data, pe, CONTEXT_BUTTON_DELETE);

        CreateConnectionContext ccc = new CreateConnectionContext();
        ccc.setSourcePictogramElement(pe);
        Anchor anchor = null;
        if (pe instanceof Anchor) {
            anchor = (Anchor) pe;
        } else if (pe instanceof AnchorContainer) {
            anchor = Graphiti.getPeService().getChopboxAnchor((AnchorContainer) pe);
        }
        ccc.setSourceAnchor(anchor);

        ContextButtonEntry button = new ContextButtonEntry(null, context);
        button.setText("Create Connection");
        button.setIconId(ImageProvider.IMAGE_EREFERENCE);
        ICreateConnectionFeature[] features = getFeatureProvider().getCreateConnectionFeatures();
        for (ICreateConnectionFeature feature : features) {
            if (feature.isAvailable(ccc) && feature.canStartConnection(ccc)) {
                button.addDragAndDropFeature(feature);
            }
        }

        if (button.getDragAndDropFeatures().size() > 0) {
            data.getDomainSpecificContextButtons().add(button);
        }

        return data;

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IContextMenuEntry[] getContextMenu(final IContext context) {
        ContextMenuEntry subMenu = new ContextMenuEntry(null, context);
        subMenu.setText("Custom Features");
        subMenu.setDescription("Custom feature submenu");
        subMenu.setSubmenu(true);

        if (context instanceof ICustomContext) {
            ICustomContext customContext = (ICustomContext) context;
            ICustomFeature[] customFeatures = getFeatureProvider().getCustomFeatures(customContext);
            for (ICustomFeature customFeature : customFeatures) {

                if (customFeature.isAvailable(customContext)) {
                    ContextMenuEntry menuEntry = new ContextMenuEntry(customFeature, context);
                    subMenu.add(menuEntry);
                }
            }
        }

        IContextMenuEntry[] ret = new IContextMenuEntry[] { subMenu };
        return ret;
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IPaletteCompartmentEntry[] getPaletteCompartments() {

        List<IPaletteCompartmentEntry> ret = new ArrayList<IPaletteCompartmentEntry>();
        IPaletteCompartmentEntry[] superCompartments = super.getPaletteCompartments();
        for (IPaletteCompartmentEntry superCompartment : superCompartments) {
            ret.add(superCompartment);
        }

        // Code used to create stacked palette compartments
        /*
         * PaletteCompartmentEntry compartmentEntry = new PaletteCompartmentEntry("Stacked", null);
         * ret.add(compartmentEntry); StackEntry stackEntry = new StackEntry("EObject", "EObject",
         * null); compartmentEntry.addToolEntry(stackEntry);
         * 
         * 
         * IFeatureProvider featureProvider = getFeatureProvider(); ICreateFeature[] createFeatures
         * = featureProvider.getCreateFeatures(); for (ICreateFeature cf : createFeatures) {
         * ObjectCreationToolEntry objectCreationToolEntry = new
         * ObjectCreationToolEntry(cf.getCreateName(), cf.getCreateDescription(),
         * cf.getCreateImageId(), cf.getCreateLargeImageId(), cf);
         * stackEntry.addCreationToolEntry(objectCreationToolEntry);
         * 
         * }
         * 
         * ICreateConnectionFeature[] createConnectionFeatures =
         * featureProvider.getCreateConnectionFeatures(); for (ICreateConnectionFeature cf :
         * createConnectionFeatures) { ConnectionCreationToolEntry connectionCreationToolEntry = new
         * ConnectionCreationToolEntry(cf.getCreateName(), cf.getCreateDescription(),
         * cf.getCreateImageId(), cf.getCreateLargeImageId());
         * connectionCreationToolEntry.addCreateConnectionFeature(cf);
         * stackEntry.addCreationToolEntry(connectionCreationToolEntry); }
         */

        return ret.toArray(new IPaletteCompartmentEntry[ret.size()]);

    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public ICustomFeature getDoubleClickFeature(final IDoubleClickContext context) {
        ICustomFeature customFeature = null;
        if (context.getInnerPictogramElement() instanceof ContainerShape) {
            customFeature = new RenameEntityFeature(getFeatureProvider());
        } else if (context.getInnerGraphicsAlgorithm() instanceof ConnectionDecorator) {
           customFeature = new RenameLinkFeature(getFeatureProvider());
        }
        if (customFeature != null) {
            if (customFeature.canExecute(context)) {
                return customFeature;
            }
        }
            return super.getDoubleClickFeature(context);
        
    }

    /**
     * 
     * {@inheritDoc}
     */
    @Override
    public IRenderingDecorator[] getRenderingDecorators(final PictogramElement pe) {
        IFeatureProvider featureProvider = getFeatureProvider();
        Object obj = featureProvider.getBusinessObjectForPictogramElement(pe);
        if (obj instanceof Entity) {
            Entity eClass = (Entity) obj;
            String name = eClass.getName();
            if (name != null && name.length() > 0
                    && !(name.charAt(0) >= 'A' && name.charAt(0) <= 'Z')) {
                IRenderingDecorator imageRenderingDecorator = new ImageRenderingDecorator(
                        IPlatformImageConstants.IMG_ECLIPSE_WARNING_TSK);
                imageRenderingDecorator.setMessage("Name should start with an upper case");
                return new IRenderingDecorator[] { imageRenderingDecorator };
            }
        }
        return super.getRenderingDecorators(pe);

    }
}
