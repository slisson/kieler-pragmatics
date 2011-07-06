/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
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
package de.cau.cs.kieler.klighd;

/**
 * The interface for listeners on the events of a viewer.
 * 
 * @author mri
 */
public interface IViewerEventListener {

    /**
     * Handles the given event.
     * 
     * @param event
     *            the viewer event
     */
    void handleEvent(final IViewerEvent event);

}