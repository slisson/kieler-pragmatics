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
 */
package de.cau.cs.kieler.klodd.orthogonal.modules;

import de.cau.cs.kieler.core.alg.IAlgorithm;
import de.cau.cs.kieler.klodd.orthogonal.structures.slimgraph.KSlimGraph;

/**
 * Interface for algorithms that perform planarization of a graph.
 * 
 * @author msp
 */
public interface IPlanarizer extends IAlgorithm {

    /**
     * Performs planarization of the given graph. This is done by adding dummy
     * nodes to replace edge crossings.
     * 
     * @param graph graph to be planarized
     */
    void planarize(KSlimGraph graph);

}
