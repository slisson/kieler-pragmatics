/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2009 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 * 
 *****************************************************************************/

package de.cau.cs.kieler.karma;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LayoutManager;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.draw2d.ui.figures.IBorderItemLocator;

/**
 * Class for providing figures represented by a string.
 * 
 * @author ckru
 * 
 */
public interface IRenderingProvider {

    /**
     * Method that returns a figure according to a given string.
     * 
     * @param input
     *            a string representation of the figure
     * @param oldFigure
     *            the old figure
     * @param object
     *            the model element
     * @return the figure
     */
    IFigure getFigureByString(final String input, final IFigure oldFigure, final EObject object);

    /**
     * Returns the figure to be displayed on default.
     * 
     * @return the default figure
     */
    IFigure getDefaultFigure();

    /**
     * Method that returns a LayoutManager according to a given string.
     * 
     * @param input
     *            a string representation of the LayoutManager
     * @param oldLayoutManager
     *            the old LayoutManager
     * @param object
     *            the model element
     * @return the new LayoutManager
     */
    LayoutManager getLayoutManagerByString(final String input,
            final LayoutManager oldLayoutManager, final EObject object);

    /**
     * Returns the default LayoutManager.
     * 
     * @return the default LayoutManager
     */
    LayoutManager getDefaultLayoutManager();

    /**
     * Method that returns a BorderItemLocator according to a given string.
     * 
     * @param input
     *            a string representation of the BorderItemLocator
     * @param parentFigure
     *            the parentFigure
     * @param locator
     *            the old BorderItemLocator
     * @param object
     *            the model element
     * @return the new BorderItemLocator
     */
    IBorderItemLocator getBorderItemLocatorByString(final String input, final IFigure parentFigure,
            final Object locator, final EObject object);

}
