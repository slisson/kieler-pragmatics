/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://rtsys.informatik.uni-kiel.de/kieler
 * 
 * Copyright 2015 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 */
package de.cau.cs.kieler.klay.layered.compaction.oned.algs;

import java.util.Queue;

import com.google.common.collect.Lists;

import de.cau.cs.kieler.klay.layered.compaction.oned.CGroup;
import de.cau.cs.kieler.klay.layered.compaction.oned.CNode;
import de.cau.cs.kieler.klay.layered.compaction.oned.OneDimensionalCompactor;

/**
 * Compacts a previously calculated constraint graph (
 * {@link de.cau.cs.kieler.klay.layered.compaction.oned.CGraph CGraph}) by using a technique similar
 * to a longest path layering.
 * 
 * The algorithm evaluates the {@link CGroup#reposition} flag.
 * 
 * @author uru
 */
public class LongestPathCompaction implements ICompactionAlgorithm {

    /**
     * {@inheritDoc}
     */
    @Override
    public void compact(final OneDimensionalCompactor compactor) {

        // calculating the left-most position of any element 
        // this will be our starting point for the compaction
        double minStartPos = Double.POSITIVE_INFINITY;
        for (CNode cNode : compactor.cGraph.cNodes) {
            minStartPos = Math.min(minStartPos, 
                                   cNode.cGroup.reference.hitbox.x + cNode.cGroupOffset.x);
        }
        
        // finding the sinks of the constraint graph
        Queue<CGroup> sinks = Lists.newLinkedList();
        for (CGroup group : compactor.cGraph.cGroups) {
            group.startPos = minStartPos;
            if (group.outDegree == 0) {
                sinks.add(group);
            }
        }
        // process sinks until every node in the constraint graph was handled
        while (!sinks.isEmpty()) {
            
            CGroup group = sinks.poll();
            
            // ------------------------------------------
            // #1 final positions for this group's nodes
            // ------------------------------------------
            for (CNode node : group.cNodes) {
                // CNodes can be locked in place to avoid pulling clusters apart
                double suggestedX = group.startPos + node.cGroupOffset.x;
                if (node.cGroup.reposition //node.reposition
                        // does the "fixed" position violate the constraints?
                        || (node.getPosition() < suggestedX)) {
                    node.startPos = suggestedX;
                } else {
                    // leave the node where it was!
                    node.startPos = node.hitbox.x;
                }
            }
            
            // ---------------------------------------------------
            // #2 propagate start positions to constrained groups
            // ---------------------------------------------------
            for (CNode node : group.cNodes) {
                for (CNode incNode : node.constraints) {
                    // determine the required spacing
                    double spacing;
                    if (compactor.direction.isHorizontal()) {
                        spacing = compactor.spacingsHandler.getHorizontalSpacing(node, incNode);
                    } else {
                        spacing = compactor.spacingsHandler.getVerticalSpacing(node, incNode);
                    }
                    
                    incNode.cGroup.startPos = Math.max(incNode.cGroup.startPos, 
                                                   node.startPos + node.hitbox.width + spacing 
                                                   // respect the other group's node's offset
                                                   - incNode.cGroupOffset.x);
                    
                    // whether the node's current position should be preserved
                    if (!incNode.reposition) {
                        incNode.cGroup.startPos =
                                Math.max(incNode.cGroup.startPos, incNode.getPosition()
                                        - incNode.cGroupOffset.x);
                    }
                    
                    incNode.outDegree--; // TODO i think group outdegree is enough
                    incNode.cGroup.outDegree--;
                    if (incNode.cGroup.outDegree == 0) {
                       sinks.add(incNode.cGroup); 
                    }
                }
            }
        }
        
        // ------------------------------------------------------
        // #3 setting hitbox positions to new starting positions
        // ------------------------------------------------------
        for (CNode cNode : compactor.cGraph.cNodes) {
            cNode.applyPosition();
        }
    }

}