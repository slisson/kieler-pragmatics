/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse Rich Client
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2011 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.viewers;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import de.cau.cs.kieler.klighd.LightDiagramServices;
import de.cau.cs.kieler.klighd.ViewContext;
import de.cau.cs.kieler.klighd.util.KlighdColor;

/**
 * The KLighD viewer can be embedded into a SWT component and is able to accept any type of input
 * model. When a model is set as input it tries to find a visualization using the light diagram
 * services.
 * 
 * @author mri
 */
public class KlighdViewer extends AbstractViewer<Object> {

    /** the context viewer used to visualize models. */
    private ContextViewer contextViewer;

    /**
     * Constructs a KLighD viewer into a given parent composite.
     * 
     * @param parent
     *            the parent composite
     */
    public KlighdViewer(final Composite parent) {
        contextViewer = new ContextViewer(parent, null);
    }

    /**
     * {@inheritDoc}
     */
    public Control getControl() {
        return contextViewer.getControl();
    }

    /**
     * {@inheritDoc}
     */
    public void setModel(final Object model) {
        ViewContext viewContext = LightDiagramServices.getInstance().createViewContext(model);
        if (viewContext != null) {
            contextViewer.setModel(viewContext);
        } else {
            contextViewer.setModel("Could not find a visualization for the model.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHighlight(final Object[] diagramElements, final KlighdColor foreground,
            final KlighdColor background, final float lineWidthFactor) {
        contextViewer.setHighlight(diagramElements, foreground, background, lineWidthFactor);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeHighlight(final Object[] diagramElements) {
        contextViewer.removeHighlight(diagramElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setSelection(final Object[] diagramElements) {
        contextViewer.setSelection(diagramElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearSelection() {
        contextViewer.clearSelection();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void select(final Object[] diagramElements) {
        contextViewer.select(diagramElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unselect(final Object[] diagramElements) {
        contextViewer.unselect(diagramElements);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void zoom(final float zoomLevel, final int duration) {
        contextViewer.zoom(zoomLevel, duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void zoomToFit(final int duration) {
        contextViewer.zoomToFit(duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reveal(final Object diagramObject, final int duration) {
        contextViewer.reveal(diagramObject, duration);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void centerOn(final Object diagramElement, final int duration) {
        contextViewer.centerOn(diagramElement, duration);
    }

}