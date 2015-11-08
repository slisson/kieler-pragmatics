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
package de.cau.cs.kieler.klay.layered.compaction.components;

import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_EAST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_EAST_SOUTH;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_EAST_SOUTH_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_EAST_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_EAST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_EAST_SOUTH_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_EAST_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_SOUTH;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_SOUTH_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_NORTH_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_SOUTH;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_SOUTH_WEST;
import static de.cau.cs.kieler.kiml.options.PortSide.SIDES_WEST;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.kiml.options.Direction;
import de.cau.cs.kieler.kiml.options.PortSide;
import de.cau.cs.kieler.kiml.util.nodespacing.Rectangle;
import de.cau.cs.kieler.klay.layered.compaction.oned.CGraph;
import de.cau.cs.kieler.klay.layered.compaction.oned.CGroup;
import de.cau.cs.kieler.klay.layered.compaction.oned.CNode;
import de.cau.cs.kieler.klay.layered.compaction.oned.ICGraphTransformer;

/**
 * Transforms {@link IConnectedComponents} into a representing {@link CGraph} which can be passed to
 * a {@link de.cau.cs.kieler.klay.layered.compaction.oned.OneDimensionalCompactor
 * OneDimensionalCompactor} to obtain a more compact drawing.
 * 
 * @author uru
 * @param <N>
 *            the type of the contained nodes
 * @param <E>
 *            the type of the contained edges
 */
public class ComponentsToCGraphTransformer<N, E> implements
        ICGraphTransformer<IConnectedComponents<N, E>> {

    /** The {@link CGroup} resulting from this transformation. */
    private CGraph cGraph;
    
    private double spacing;

    // internal mappings 
    private Map<IComponent<N, E>, KVector> oldPosition = Maps.newHashMap();
    private Map<IComponent<N, E>, CRectNode> offsets = Maps.newHashMap();
    
    // a global offset and the new graph size after layout
    private KVector globalOffset;
    private KVector graphSize;

    
    private Multimap<CGroup, CNode> verticalExternalEdges = HashMultimap.create();
    private Multimap<CGroup, CNode> horizontalExternalEdges = HashMultimap.create();
    
    /**
     * @param spacing required spacing between connected IComponents.
     */
    public ComponentsToCGraphTransformer(final double spacing) {
        this.spacing = spacing;
    }

    /**
     * @param c
     *            a IComponent
     * @return the offset to c's original position after layout.
     */
    public KVector getOffset(final IComponent<N, E> c) {
        KVector cOffset = oldPosition.get(c).clone().sub(offsets.get(c).rect.getPosition());
        return cOffset;
    }
    
    /**
     * @return the globalOffset
     */
    public KVector getGlobalOffset() {
        return globalOffset;
    }
    
    /**
     * @return the graphSize
     */
    public KVector getGraphSize() {
        return graphSize;
    }
    
    /**
     * @return the horizontalExternalEdges
     */
    public Multimap<CGroup, CNode> getHorizontalExternalEdges() {
        return horizontalExternalEdges;
    }
    
    /**
     * @return the verticalExternalEdges
     */
    public Multimap<CGroup, CNode> getVerticalExternalEdges() {
        return verticalExternalEdges;
    }
    
    /* -----------------------------------------------------------
     *                    Graph Transformation
     * ----------------------------------------------------------- */
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CGraph transform(final IConnectedComponents<N, E> ccs) {

        cGraph = new CGraph(EnumSet.allOf(Direction.class));

        for (IComponent<N, E> comp : ccs.getComponents()) {

            CGroup group = new CGroup();
            cGraph.cGroups.add(group);

            // convert the hull of the graph's elements without external edges
            for (Rectangle rect : comp.getHull()) {

                CRectNode rectNode = new CRectNode(rect);
                setLock(rectNode, comp.getExternalPortSides());
                
                if (!oldPosition.containsKey(comp)) {
                    oldPosition.put(comp, new KVector(rect.x, rect.y));
                    offsets.put(comp, rectNode);
                }

                cGraph.cNodes.add(rectNode);
                group.addCNode(rectNode);
            }

            // now prepare the additional hull elements for external edges
            // they can be added to the CGraph on demand later on 
            comp.getExternalEdgeHulls().forEach((edge, rects) -> {
                rects.forEach(r ->  {
                    CRectNode rectNode = new CRectNode(r);
                    setLock(rectNode, comp.getExternalPortSides());
                    
                    if (edge.isVertical()) {
                        verticalExternalEdges.put(group, rectNode); 
                    } else {
                        horizontalExternalEdges.put(group, rectNode);
                    }
                });
            });            
        }

        return cGraph;
    }
    
    /**
     * The locks should assure that a component c that is connected to external ports on the sides
     * WEST and SOUTH stays in the bottom left corner. Otherwise edges may become unnecessarily
     * long.
     */
    private void setLock(final CNode cNode, final Set<PortSide> portSides) {
        
        if (portSides.isEmpty()) {
            cNode.lock.set(true, true, true, true);            
        }
        
        // regulars
        if (portSides.equals(SIDES_NORTH)) {
            cNode.lock.set(true, true, true, false);
        }
        if (portSides.equals(SIDES_EAST)) {
            cNode.lock.set(false, true, true, true);
        }
        if (portSides.equals(SIDES_SOUTH)) {
            cNode.lock.set(true, true, false, true);
        }
        if (portSides.equals(SIDES_WEST)) {
            cNode.lock.set(true, false, true, true);
        }

        // corners
        if (portSides.equals(SIDES_NORTH_EAST)) {
            cNode.lock.set(false, true, true, false);
        }
        if (portSides.equals(SIDES_EAST_SOUTH)) {
            cNode.lock.set(false, true, false, true);
        }
        if (portSides.equals(SIDES_SOUTH_WEST)) {
            cNode.lock.set(true, false, false, true);
        }
        if (portSides.equals(SIDES_NORTH_WEST)) {
            cNode.lock.set(true, false, true, false);
        }

        // opposite
        if (portSides.equals(SIDES_NORTH_SOUTH)) {
            cNode.lock.set(true, true, true, true);
        }
        if (portSides.equals(SIDES_EAST_WEST)) {
            cNode.lock.set(true, true, true, true);
        }

        // triumvirates
        if (portSides.equals(SIDES_NORTH_SOUTH)) {
            cNode.lock.set(true, true, true, true);
        }
        if (portSides.equals(SIDES_EAST_SOUTH_WEST)) {
            cNode.lock.set(true, true, true, true);
        }
        if (portSides.equals(SIDES_NORTH_SOUTH_WEST)) {
            cNode.lock.set(true, true, true, true);
        }
        if (portSides.equals(SIDES_NORTH_EAST_WEST)) {
            cNode.lock.set(true, true, true, true);
        }

        // all of them!
        if (portSides.equals(SIDES_NORTH_EAST_SOUTH_WEST)) {
            cNode.lock.set(true, true, true, true);
        }
    }

    /* -----------------------------------------------------------
     *                    Layout Application 
     * ----------------------------------------------------------- */
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void applyLayout() {

        for (CNode n : cGraph.cNodes) {
            n.applyElementPosition();
        }

        // calculating new graph size and offset
        KVector topLeft = new KVector(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        KVector bottomRight = new KVector(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        for (CNode cNode : cGraph.cNodes) {
            topLeft.x = Math.min(topLeft.x, cNode.hitbox.x);
            topLeft.y = Math.min(topLeft.y, cNode.hitbox.y);
            bottomRight.x = Math.max(bottomRight.x, cNode.hitbox.x + cNode.hitbox.width);
            bottomRight.y = Math.max(bottomRight.y, cNode.hitbox.y + cNode.hitbox.height);
        }
        
        globalOffset = topLeft.clone().negate();
        graphSize = bottomRight.clone().sub(topLeft);

        // resetting lists
        cGraph.cGroups.clear();
        cGraph.cNodes.clear();
    }

    /**
     * {@link CNode} implementation for {@link Rectangle}s representing 
     * parts of the rectangular convex hull of a connected IComponent.
     */
    private final class CRectNode extends CNode {

        private Rectangle rect;

        private CRectNode(final Rectangle rect) {
            this.rect = rect;
            hitbox = new Rectangle(rect.x, rect.y, rect.width, rect.height);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getHorizontalSpacing() {
            return spacing;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public double getVerticalSpacing() {
            return spacing;
        }
        
        /**
         * {@inheritDoc}
         */
        @Override
        public void applyElementPosition() {
            rect.x = hitbox.x;
            rect.y = hitbox.y;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public double getElementPosition() {
            return rect.x;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            // for debug output be quiet! :)
            return "";
            // return "CRectNode " + hitbox;
        }
    }
    
}
