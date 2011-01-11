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
package de.cau.cs.kieler.kiml.ui.util;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.graph.CompoundDirectedGraphLayout;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

import de.cau.cs.kieler.core.KielerException;
import de.cau.cs.kieler.core.alg.IKielerProgressMonitor;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KLabel;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.kiml.AbstractLayoutProvider;
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory;
import de.cau.cs.kieler.kiml.klayoutdata.KPoint;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;
import de.cau.cs.kieler.kiml.options.LayoutDirection;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.util.KimlUtil;

/**
 * Layout provider that uses the layout algorithm shipped with Draw2D. Either the
 * default version or the Compound version of the algorithm can be applied.
 * 
 * TODO implement compound graph layout using CompoundDirectedGraphLayout
 *
 * @kieler.rating 2009-12-11 proposed yellow msp
 * @author msp
 */
public class Draw2DLayoutProvider extends AbstractLayoutProvider {

    /** parameter value for the compound version of the layout algorithm. */
    public static final String PARAM_COMPOUND = "Compound";
    /** default value for minimal spacing. */
    private static final float DEF_MIN_SPACING = 16.0f;
    
    /** indicates whether the compound graph version of the algorithm shall be used. */
    private boolean compoundMode = false;
    
    /**
     * Initialize default options for the layout provider.
     */
    public Draw2DLayoutProvider() {
        setProperty(LayoutOptions.OBJ_SPACING, DEF_MIN_SPACING);
        setProperty(LayoutOptions.BORDER_SPACING, DEF_MIN_SPACING);
        setProperty(LayoutOptions.LAYOUT_DIRECTION, LayoutDirection.DOWN);
        setProperty(LayoutOptions.FIXED_SIZE, false);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(final String parameter) {
        compoundMode = PARAM_COMPOUND.equals(parameter);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void doLayout(final KNode layoutNode, final IKielerProgressMonitor progressMonitor)
            throws KielerException {
        progressMonitor.begin("Draw2D Directed Graph layout", 1);
        
        DirectedGraph graph = buildDraw2dGraph(layoutNode);
        DirectedGraphLayout draw2dLayout = compoundMode ? new CompoundDirectedGraphLayout()
                : new DirectedGraphLayout();
        draw2dLayout.visit(graph);
        applyLayout(layoutNode, graph);
        
        progressMonitor.done();
    }
    
    /**
     * Builds the graph which is used internally by the Draw2D layout algorithm.
     * 
     * @param layoutNode parent layout node
     * @return a graph that contains the children of the given parent
     */
    @SuppressWarnings("unchecked")
    private DirectedGraph buildDraw2dGraph(final KNode layoutNode) {
        DirectedGraph graph = new DirectedGraph();
        
        // set layout options for the graph
        KShapeLayout parentLayout = layoutNode.getData(KShapeLayout.class);
        float minSpacing = parentLayout.getProperty(LayoutOptions.OBJ_SPACING);
        if (minSpacing < 0) {
            minSpacing = DEF_MIN_SPACING;
        }
        graph.setDefaultPadding(new Insets((int) minSpacing));
        float borderSpacing = parentLayout.getProperty(LayoutOptions.BORDER_SPACING);
        if (borderSpacing < 0) {
            borderSpacing = DEF_MIN_SPACING;
        }
        graph.setMargin(new Insets((int) borderSpacing));
        LayoutDirection layoutDirection = parentLayout.getProperty(LayoutOptions.LAYOUT_DIRECTION);
        switch (layoutDirection) {
        case RIGHT:
        case LEFT:
            graph.setDirection(PositionConstants.EAST);
            break;
        default:
            graph.setDirection(PositionConstants.SOUTH);
            break;
        }
        
        // add nodes to the graph
        Map<KNode, Node> nodeMap = new HashMap<KNode, Node>();
        for (KNode knode : layoutNode.getChildren()) {
            Node draw2dNode = new Node(knode);
            KShapeLayout nodeLayout = knode.getData(KShapeLayout.class);
            if (!nodeLayout.getProperty(LayoutOptions.FIXED_SIZE)) {
                KimlUtil.resizeNode(knode);
            }
            draw2dNode.width = (int) nodeLayout.getWidth();
            draw2dNode.height = (int) nodeLayout.getHeight();
            nodeMap.put(knode, draw2dNode);
            graph.nodes.add(draw2dNode);
        }
        
        // add edges to the graph
        for (KNode source : layoutNode.getChildren()) {
            Node draw2dSource = nodeMap.get(source);
            for (KEdge kedge : source.getOutgoingEdges()) {
                KNode target = kedge.getTarget();
                Node draw2dTarget = nodeMap.get(target);
                if (draw2dTarget != null && draw2dTarget != draw2dSource) {
                    Edge draw2dEdge = new Edge(kedge, draw2dSource, draw2dTarget);
                    graph.edges.add(draw2dEdge);
                } else {
                    KEdgeLayout edgeLayout = kedge.getData(KEdgeLayout.class);
                    edgeLayout.setProperty(LayoutOptions.NO_LAYOUT, true);
                }
            }
        }
        
        return graph;
    }
    
    /**
     * Applies the layout determined by Draw2D to the original graph.
     * 
     * @param parentNode the parent layout node
     * @param graph the Draw2D graph
     */
    private void applyLayout(final KNode parentNode, final DirectedGraph graph) {
        // apply node layouts
        for (int i = 0; i < graph.nodes.size(); i++) {
            Node node = graph.nodes.getNode(i);
            if (node.data instanceof KNode) {
                KNode knode = (KNode) node.data;
                KShapeLayout nodeLayout = knode.getData(KShapeLayout.class);
                nodeLayout.setXpos(node.x);
                nodeLayout.setYpos(node.y);
            }
        }
        
        // apply edge layouts
        for (int i = 0; i < graph.edges.size(); i++) {
            Edge edge = graph.edges.getEdge(i);
            if (edge.data instanceof KEdge) {
                KEdge kedge = (KEdge) edge.data;
                KEdgeLayout edgeLayout = kedge.getData(KEdgeLayout.class);
                edgeLayout.getBendPoints().clear();
                PointList pointList = edge.getPoints();
                KPoint sourcekPoint = edgeLayout.getSourcePoint();
                Point sourcePoint = pointList.getFirstPoint();
                sourcekPoint.setX(sourcePoint.x);
                sourcekPoint.setY(sourcePoint.y);
                for (int j = 1; j < pointList.size() - 1; j++) {
                    Point point = pointList.getPoint(j);
                    KPoint kpoint = KLayoutDataFactory.eINSTANCE.createKPoint();
                    kpoint.setX(point.x);
                    kpoint.setY(point.y);
                    edgeLayout.getBendPoints().add(kpoint);
                }
                KPoint targetkPoint = edgeLayout.getTargetPoint();
                Point targetPoint = pointList.getLastPoint();
                targetkPoint.setX(targetPoint.x);
                targetkPoint.setY(targetPoint.y);
                
                // disable layout for the edge labels
                for (KLabel label : kedge.getLabels()) {
                    KShapeLayout labelLayout = label.getData(KShapeLayout.class);
                    labelLayout.setProperty(LayoutOptions.NO_LAYOUT, true);
                }
            }
        }
        
        // apply parent node layout
        KShapeLayout parentLayout = parentNode.getData(KShapeLayout.class);
        KInsets insets = parentLayout.getProperty(LayoutOptions.INSETS);
        Dimension layoutSize = graph.getLayoutSize();
        parentLayout.setWidth(insets.getLeft() + layoutSize.width + insets.getRight());
        parentLayout.setHeight(insets.getTop() + layoutSize.height + insets.getBottom());
    }

}
