/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2011 by
 * + Kiel University
 *     + Department of Computer Science
 *         + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */

package de.cau.cs.kieler.kwebs.server.publishing;

/**
 * .
 *
 * @author swe
 */
public class ServerNotCreatedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 5656193865387375784L;

    /**
     * Default constructor.
     */
    public ServerNotCreatedException() {
        super();
    }

    /**
     * Constructor for enclosing another throwable.
     *
     * @param throwable
     *            a throwable to be enclosed by this exception
     */
    public ServerNotCreatedException(final Throwable throwable) {
        super(throwable);
    }
    
}
