/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2008 by
 * + Christian-Albrechts-University of Kiel
 *     + Department of Computer Science
 *         + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */

package de.cau.cs.kieler.kwebs.logging;

import de.cau.cs.kieler.kwebs.logging.Logger.LoggerEvent;


/**
 * Interface for listening to events from the {@link Logger}.
 *
 * @kieler.rating  2011-05-04 red
 * @author  swe
 */
public interface ILoggerListener {

    /**
     * Every listener is notified about an logger event through this method.
     * 
     * @param event the logger event
     */
    void loggerEvent(final LoggerEvent event);
    
}
