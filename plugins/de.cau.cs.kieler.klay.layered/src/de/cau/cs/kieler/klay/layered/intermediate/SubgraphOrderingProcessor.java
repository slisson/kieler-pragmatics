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
package de.cau.cs.kieler.klay.layered.intermediate;

import java.util.LinkedList;
import java.util.List;

import de.cau.cs.kieler.core.alg.AbstractAlgorithm;
import de.cau.cs.kieler.klay.layered.ILayoutProcessor;
import de.cau.cs.kieler.klay.layered.graph.LNode;
import de.cau.cs.kieler.klay.layered.graph.Layer;
import de.cau.cs.kieler.klay.layered.graph.LayeredGraph;
import de.cau.cs.kieler.klay.layered.properties.Properties;

/**
 * Postprocesses the node ordering phase to ensure that subgraphs are not intertwined across the
 * layers. The approach is inspired by Georg Sander, "Layout of Compound Graphs", Technical Report
 * A/03/96, Universität des Saarlandes, 1996.
 * 
 * <dl>
 * <dt>Precondition:</dt>
 * <dd>A layered graph. The node ordering has taken place. The nodes on a layer that belong to the
 * same compound node are placed in an unbroken sequence in the layer.</dd>
 * <dt>Postcondition:</dt>
 * <dd>The nodes are ordered such that the subgraphs have the same relative position on all layers.
 * The nodes of one subgraph on one layer are still placed next to each other without other nodes
 * between them.</dd>
 * <dt>Slots:</dt>
 * <dd>After phase 3.</dd>
 * <dt>Same-slot dependencies:</dt>
 * <dd>none.</dd>
 * </dl>
 * 
 * @author ima
 */
public class SubgraphOrderingProcessor extends AbstractAlgorithm implements ILayoutProcessor {

    /**
     * {@inheritDoc}
     */
    public void process(final LayeredGraph layeredGraph) {
        getMonitor().begin(
                "Order subgraphs so that the relative position is the same on all layers", 1);
        // A subgraph ordering graph is used to find the correct subgraph ordering. The subgraph
        // ordering graph is disconnected - it consists of connected partial graphs for each depth
        // level of the nesting tree. Represent them as a list of layered graphs.
        LinkedList<LayeredGraph> levelOrderingGraphs = new LinkedList<LayeredGraph>();
        for (int j = 1; j <= layeredGraph.getProperty(Properties.MAX_DEPTH); j++) {
            LayeredGraph partialSubgraphOrderingGraph = new LayeredGraph();
            partialSubgraphOrderingGraph.setProperty(Properties.ORIGIN, j);
            levelOrderingGraphs.add(partialSubgraphOrderingGraph);
        }
        // Insert nodes and edges representing the relationship "is left of" into the subgraph
        // ordering graph parts.
        for (Layer layer : layeredGraph.getLayers()) {
            List<LNode> layerNodes = layer.getNodes();
            for (int i = 0; i < layerNodes.size(); i++) {
                LNode currentNode = layerNodes.get(i);
                LNode neighbour = layerNodes.get(i + 1);
            }
        }

        getMonitor().done();
    }
}