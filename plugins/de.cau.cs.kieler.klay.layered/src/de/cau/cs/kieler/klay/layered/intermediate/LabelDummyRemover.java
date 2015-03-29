/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2012 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klay.layered.intermediate;

import java.util.List;
import java.util.ListIterator;

import de.cau.cs.kieler.core.alg.IKielerProgressMonitor;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.options.PortSide;
import de.cau.cs.kieler.kiml.util.nodespacing.LabelSide;
import de.cau.cs.kieler.klay.layered.ILayoutProcessor;
import de.cau.cs.kieler.klay.layered.graph.LEdge;
import de.cau.cs.kieler.klay.layered.graph.LGraph;
import de.cau.cs.kieler.klay.layered.graph.LLabel;
import de.cau.cs.kieler.klay.layered.graph.LNode;
import de.cau.cs.kieler.klay.layered.graph.Layer;
import de.cau.cs.kieler.klay.layered.properties.InternalProperties;
import de.cau.cs.kieler.klay.layered.properties.NodeType;

/**
 * <p>Processor that removes the inserted center label dummies and places the labels on their
 * position.</p>
 * 
 * <dl>
 *   <dt>Precondition:</dt>
 *     <dd>a layered graph<dd>
 *     <dd>nodes are placed</dd>
 *     <dd>edges are routed</dd>
 *     <dd>center labels are represented by and attached to center label dummy nodes.</dd>
 *   <dt>Postcondition:</dt>
 *     <dd>labels are placed</dd>
 *     <dd>there are no dummy nodes of type
 *       {@link de.cau.cs.kieler.klay.layered.properties.NodeType#LABEL}.</dd>
 *     <dd>center labels are attached to their original edges again.</dd>
 *   <dt>Slots:</dt>
 *     <dd>After phase 5.</dd>
 *   <dt>Same-slot dependencies:</dt>
 *     <dd>{@link HierarchicalPortOrthogonalEdgeRouter}</dd>
 * </dl>
 *
 * @author jjc
 * @kieler.rating yellow proposed cds
 */
public final class LabelDummyRemover implements ILayoutProcessor {

    /**
     * {@inheritDoc}
     */
    public void process(final LGraph layeredGraph, final IKielerProgressMonitor monitor) {
        monitor.begin("Label dummy removal", 1);
        
        double labelSpacing = layeredGraph.getProperty(LayoutOptions.LABEL_SPACING);
        
        for (Layer layer : layeredGraph.getLayers()) {
            // An iterator is necessary for traversing nodes, since
            // dummy nodes might be removed
            ListIterator<LNode> nodeIterator = layer.getNodes().listIterator();
            
            while (nodeIterator.hasNext()) {
                LNode node = nodeIterator.next();
                
                if (node.getProperty(InternalProperties.NODE_TYPE) == NodeType.LABEL) {
                    // First, place labels on position of dummy node 
                    LEdge originEdge = (LEdge) node.getProperty(InternalProperties.ORIGIN);
                    double ypos = node.getPosition().y;
                    
                    if (node.getProperty(InternalProperties.LABEL_SIDE) == LabelSide.BELOW) {
                        ypos += originEdge.getProperty(LayoutOptions.THICKNESS);
                    }
                    
                    for (LLabel label : node.getProperty(InternalProperties.REPRESENTED_LABELS)) {
                        label.getPosition().x = node.getPosition().x
                                + (node.getSize().x - label.getSize().x) / 2;
                        label.getPosition().y = ypos;
                        ypos += label.getSize().y + labelSpacing;
                        
                        originEdge.getLabels().add(label);
                    }
                    
                    // We can assume that there are exactly one western and eastern port
                    // on each side of the node
                    List<LEdge> inputPortEdges =
                        node.getPorts(PortSide.WEST).iterator().next().getIncomingEdges();
                    List<LEdge> outputPortEdges =
                        node.getPorts(PortSide.EAST).iterator().next().getOutgoingEdges();
                    int edgeCount = inputPortEdges.size();
                    
                    // The following code assumes that edges with the same indices in the two
                    // lists originate from the same long edge, which is true for the current
                    // implementation of LabelDummyInserter
                    while (edgeCount-- > 0) {
                        // Get the two edges
                        LEdge survivingEdge = inputPortEdges.get(0);
                        LEdge droppedEdge = outputPortEdges.get(0);
                        
                        // Do some edgy stuff
                        survivingEdge.setTarget(droppedEdge.getTarget());
                        droppedEdge.setSource(null);
                        droppedEdge.setTarget(null);
                        
                        // Join their bend points
                        KVectorChain survivingBendPoints = survivingEdge.getBendPoints();
                        for (KVector bendPoint : droppedEdge.getBendPoints()) {
                            survivingBendPoints.add(new KVector(bendPoint));
                        }
                        
                        // Join their labels
                        List<LLabel> survivingLabels = survivingEdge.getLabels();
                        for (LLabel label2: droppedEdge.getLabels()) {
                            survivingLabels.add(label2);
                        }
                        
                        // Join their junction points
                        KVectorChain survivingJunctionPoints = survivingEdge.getProperty(
                                LayoutOptions.JUNCTION_POINTS);
                        KVectorChain droppedJunctionsPoints = droppedEdge.getProperty(
                                LayoutOptions.JUNCTION_POINTS);
                        if (droppedJunctionsPoints != null) {
                            if (survivingJunctionPoints == null) {
                                survivingJunctionPoints = new KVectorChain();
                                survivingEdge.setProperty(LayoutOptions.JUNCTION_POINTS,
                                        survivingJunctionPoints);
                            }
                            for (KVector jp : droppedJunctionsPoints) {
                                survivingJunctionPoints.add(new KVector(jp));
                            }
                        }
                    }
                    
                    // Remove the node
                    nodeIterator.remove();
                }
            }
        }
        monitor.done();
    }

}
