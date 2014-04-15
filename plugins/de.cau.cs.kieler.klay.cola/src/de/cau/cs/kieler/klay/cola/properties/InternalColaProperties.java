/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2014 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klay.cola.properties;

import java.util.LinkedList;
import java.util.List;

import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KGraphElement;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.properties.IProperty;
import de.cau.cs.kieler.core.properties.Property;

/**
 * @author uru
 */
public final class InternalColaProperties {

    /*--------------------------------------------------------------------------------------------
     *                          Internal Use only
     */

    /**
     * The original object from which a graph element was created.
     */
    public static final IProperty<KGraphElement> ORIGIN =
            new Property<KGraphElement>("cola.origin");

    /**
     * The original object from which a graph element was created.
     */
    public static final IProperty<List<KEdge>> EDGE_CHAIN = new Property<List<KEdge>>(
            "cola.edgeChain", new LinkedList<KEdge>());

    /**
     * Positions of ports that represent hierarchy transitions on hierarchy-crossing edges.
     */
    public static final IProperty<List<KVector>> EDGE_CHECKPOINTS = new Property<List<KVector>>(
            "cola.checkpoints", new LinkedList<KVector>());

    /**
     * Utility class.
     */
    private InternalColaProperties() {
    }

}
