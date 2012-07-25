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
package de.cau.cs.kieler.klay.layered.properties;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import de.cau.cs.kieler.core.kgraph.KGraphElement;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.core.properties.IProperty;
import de.cau.cs.kieler.core.properties.Property;
import de.cau.cs.kieler.kiml.klayoutdata.KInsets;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.options.PortSide;
import de.cau.cs.kieler.klay.layered.graph.LGraphElement;
import de.cau.cs.kieler.klay.layered.graph.LNode;
import de.cau.cs.kieler.klay.layered.graph.LPort;
import de.cau.cs.kieler.klay.layered.p1cycles.CycleBreakingStrategy;
import de.cau.cs.kieler.klay.layered.p2layers.LayeringStrategy;
import de.cau.cs.kieler.klay.layered.p3order.CrossingMinimizationStrategy;
import de.cau.cs.kieler.klay.layered.p3order.NodeGroup;
import de.cau.cs.kieler.klay.layered.p4nodes.NodePlacementStrategy;

/**
 * Container for property definitions.
 * 
 * @author msp
 * @author cds
 * @author ima
 * @kieler.design proposed by msp
 * @kieler.rating proposed yellow by msp
 */
public final class Properties {

    /**
     * The original object from which a graph element was created.
     */
    public static final IProperty<Object> ORIGIN = new Property<Object>("origin");

    /**
     * Node type.
     */
    public static final IProperty<NodeType> NODE_TYPE = new Property<NodeType>("nodeType",
            NodeType.NORMAL);

    /**
     * Offset of port position to the node border. An offset of 0 means that the port touches its
     * parent node on the outside, positive offsets move the port away from the node, and negative
     * offset move the port towards the inside.
     */
    public static final IProperty<Float> OFFSET = new Property<Float>(LayoutOptions.OFFSET, 0.0f);

    /**
     * The original bend points.
     */
    public static final IProperty<KVectorChain> ORIGINAL_BENDPOINTS = new Property<KVectorChain>(
            "originalBendpoints");

    /**
     * Edge type.
     */
    public static final IProperty<EdgeType> EDGE_TYPE = new Property<EdgeType>("edgeType",
            EdgeType.NORMAL);

    /**
     * Flag for reversed edges.
     */
    public static final IProperty<Boolean> REVERSED = new Property<Boolean>("reversed", false);

    /**
     * Random number generator for the algorithm.
     */
    public static final IProperty<Random> RANDOM = new Property<Random>("random");

    /**
     * Width and height ratio by which a node was resized prior to importing.
     */
    public static final IProperty<KVector> RESIZE_RATIO = new Property<KVector>("resizeRatio");

    /**
     * The source port of a long edge before it was broken into multiple segments.
     */
    public static final IProperty<LPort> LONG_EDGE_SOURCE = new Property<LPort>("longEdgeSource",
            null);
    /**
     * The target port of a long edge before it was broken into multiple segments.
     */
    public static final IProperty<LPort> LONG_EDGE_TARGET = new Property<LPort>("longEdgeTarget",
            null);

    /**
     * Edge constraints for nodes.
     */
    public static final IProperty<EdgeConstraint> EDGE_CONSTRAINT = new Property<EdgeConstraint>(
            "edgeConstraint", EdgeConstraint.NONE);

    /**
     * The layout unit a node belongs to. This property only makes sense for nodes. A layout unit is
     * a set of nodes between which no nodes belonging to other layout units may be placed. Nodes
     * not belonging to any layout unit may be placed arbitrarily between nodes of a layout unit.
     * Layer layout units are identified through one of their nodes.
     */
    public static final IProperty<LNode> IN_LAYER_LAYOUT_UNIT = new Property<LNode>(
            "inLayerLayoutUnit");

    /**
     * The in-layer constraint placed on a node. This indicates whether this node should be handled
     * like any other node, or if it must be placed at the top or bottom of a layer. This is
     * important for external port dummy nodes. Crossing minimizers are not required to respect this
     * constraint. If they don't, however, they must include a dependency on
     * {@link de.cau.cs.kieler.klay.layered.intermediate.InLayerConstraintProcessor}.
     */
    public static final IProperty<InLayerConstraint> IN_LAYER_CONSTRAINT 
           = new Property<InLayerConstraint>(
            "inLayerConstraint", InLayerConstraint.NONE);

    /**
     * Indicates that a node {@code x} may only appear inside a layer before the node {@code y} the
     * property is set to. That is, having {@code x} appear after {@code y} would violate this
     * constraint. This property only makes sense for nodes.
     */
    public static final IProperty<LNode> IN_LAYER_SUCCESSOR_CONSTRAINT = new Property<LNode>(
            "inLayerSuccessorConstraint");

    /**
     * The node group of an LNode as used in the crossing minimization phase.
     */
    public static final IProperty<NodeGroup> NODE_GROUP = new Property<NodeGroup>("nodeGroup");

    /**
     * Crossing hint used for in-layer cross counting with northern and southern port dummies.
     */
    public static final IProperty<Integer> CROSSING_HINT = new Property<Integer>("crossingHint", 0);

    /**
     * Flags indicating the properties of a graph.
     */
    public static final IProperty<Set<GraphProperties>> GRAPH_PROPERTIES 
    = new Property<Set<GraphProperties>>(
            "graphProperties", EnumSet.allOf(GraphProperties.class));

    /**
     * The side of an external port a dummy node was created for.
     */
    public static final IProperty<PortSide> EXT_PORT_SIDE = new Property<PortSide>(
            "externalPortSide", PortSide.UNDEFINED);

    /**
     * Original size of the external port a dummy node was created for.
     */
    public static final IProperty<KVector> EXT_PORT_SIZE = new Property<KVector>(
            "externalPortSize", new KVector());

    /**
     * The original position or position-to-node-size ratio of an external port. This is a property
     * usually used for external port dummy nodes. There is only one coordinate since the side of
     * the port determines the other coordinate. (for eastern and western ports, the x coordinate is
     * determined automatically; for northern and southern ports, the y coordinate is determined
     * automatically)
     */
    public static final IProperty<Double> EXT_PORT_RATIO_OR_POSITION = new Property<Double>(
            "externalPortRatioOrPosition", 0.0);

    /**
     * External port dummies that represent northern or southern external ports are replaced by new
     * dummy nodes during layout. In these cases, this property is set to the original dummy node.
     */
    public static final IProperty<LNode> EXT_PORT_REPLACED_DUMMY = new Property<LNode>(
            "externalPortReplacedDummy");

    /**
     * The port sides of external ports a connected component connects to. This property is set on
     * the layered graph that represents a connected component and defaults to no connections. If a
     * connected component connects to an external port on the EAST side and to another external
     * port on the NORTH side, this enumeration will list both sides.
     */
    public static final IProperty<Set<PortSide>> EXT_PORT_CONNECTIONS = new Property<Set<PortSide>>(
            "externalPortConnections", EnumSet.noneOf(PortSide.class));

    /**
     * A list of nodes whose barycenters should go into the barycenter calculation of the node this
     * property is set on. Nodes in this list are expected to be in the same layer as the node the
     * property is set on. This is primarily used when edges are rerouted from a node to dummy
     * nodes.
     * <p>
     * This property is currently not declared as one of the layout options offered by KLay Layered
     * and should be considered highly experimental.
     */
    public static final IProperty<List<LNode>> BARYCENTER_ASSOCIATES = new Property<List<LNode>>(
            "barycenterAssociates");

    /**
     * KNode that contained the origin of this node in the KGraph.
     */
    public static final IProperty<KNode> K_PARENT = new Property<KNode>("k_Parent");

    /**
     * LNode that is representative of the node that contains the property holder. Property for an
     * LNode.
     */
    public static final IProperty<LGraphElement> PARENT = new Property<LGraphElement>("parent");

    /**
     * List of nodes that are children (direct descendants) of the node that is represented by the
     * property holder. Property for an LNode.
     */
    public static final IProperty<LinkedList<LNode>> CHILDREN = new Property<LinkedList<LNode>>(
            "children", null);

    /**
     * LNode that is the left border node for the compound node the side dummy guards the side of.
     */
    public static final IProperty<LNode> SIDE_OWNER = new Property<LNode>("sideOwner");

    /**
     * Flag indicating whether an LPort is set to a leave node in the inclusion tree to enable
     * connections with dummy edges for layering.
     */
    public static final IProperty<Boolean> LEAVE_DUMMY_PORT = new Property<Boolean>(
            "leaveDummyPort", false);

    /**
     * UPPER_BORDER_DUMMY node determining the compound node this dummy node belongs to.
     */
    public static final IProperty<LNode> COMPOUND_NODE = new Property<LNode>("CompoundNode");

    /**
     * KInsets of the KNode a upper border dummy node is representing.
     */
    public static final IProperty<KInsets> ORIGINAL_INSETS = new Property<KInsets>("OriginalInsets");

    /**
     * Map between KGraph nodes/ports/edges and LGraph nodes/ports/edges.
     */
    public static final IProperty<HashMap<KGraphElement, LGraphElement>> ELEMENT_MAP 
        = new Property<HashMap<KGraphElement, LGraphElement>>(
            "ElementMap");

    /**
     * List of comment boxes that are placed on top of a node.
     */
    public static final IProperty<List<LNode>> TOP_COMMENTS = new Property<List<LNode>>(
            "TopSideComments");

    /**
     * List of comment boxes that are placed in the bottom of of a node.
     */
    public static final IProperty<List<LNode>> BOTTOM_COMMENTS = new Property<List<LNode>>(
            "BottomSideComments");

    /**
     * The port of a node that originally connected a comment box with that node.
     */
    public static final IProperty<LPort> COMMENT_CONN_PORT = new Property<LPort>(
            "CommentConnectionPort");

    /**
     * The maximum depth of a leave node in the original graph a layered graph is representing.
     */
    public static final IProperty<Integer> MAX_DEPTH = new Property<Integer>("MaxDepth", 0);

    /**
     * The depth of a node in the nesting tree of a compound graph.
     */
    public static final IProperty<Integer> DEPTH = new Property<Integer>("Depth", 0);

    /**
     * Difference of Positions for an UPPER_BORDER_DUMMY_NODE before and after the
     * CompoundGraphRestorer.
     */
    public static final IProperty<KVector> POSITION_DIFFERENCE = new Property<KVector>(
            "PositionDifference", null);

    /**
     * Whether a port is used to collect all incoming edges of a node.
     */
    public static final IProperty<Boolean> INPUT_COLLECT = new Property<Boolean>("inputCollect",
            false);
    /**
     * Whether a port is used to collect all outgoing edges of a node.
     */
    public static final IProperty<Boolean> OUTPUT_COLLECT = new Property<Boolean>("outputCollect",
            false);
    /**
     * Property of a LayeredGraph. Whether the graph has been processed by the cycle breaker and the
     * cycle breaker has detected cycles and reverted edges.
     */
    public static final IProperty<Boolean> CYCLIC = new Property<Boolean>("cyclic", false);

    /**
     * The offset to the port position where connections shall be attached.
     */
    public static final IProperty<KVector> PORT_ANCHOR = new Property<KVector>(
            "de.cau.cs.kieler.klay.layered.portAnchor");

    // /////////////////////////////////////////////////////////////////////////////
    // USER INTERFACE OPTIONS

    /** minimal spacing between objects. */
    public static final Property<Float> OBJ_SPACING = new Property<Float>(LayoutOptions.SPACING,
            20.0f, 0.0f);
    /** spacing to the border of the drawing. */
    public static final Property<Float> BORDER_SPACING = new Property<Float>(
            LayoutOptions.BORDER_SPACING, 20.0f, 0.0f);

    /** factor for minimal spacing between edges. */
    public static final Property<Float> EDGE_SPACING_FACTOR = new Property<Float>(
            "de.cau.cs.kieler.klay.layered.edgeSpacingFactor", 0.5f);

    /** priority of elements. controls how much single edges are emphasized. */
    public static final Property<Integer> PRIORITY = new Property<Integer>(LayoutOptions.PRIORITY,
            0);

    /** the aspect ratio for packing connected components. */
    public static final Property<Float> ASPECT_RATIO = new Property<Float>(
            LayoutOptions.ASPECT_RATIO, 1.6f, 0.0f);

    /** defines the minimal angle a short edge may have. */
    public static final IProperty<Float> MIN_EDGE_ANGLE = new Property<Float>(
            "de.cau.cs.kieler.klay.layered.minimalAngle", 0.0f);

    /** whether nodes shall be distributed during layer assignment. */
    public static final IProperty<Boolean> DISTRIBUTE_NODES = new Property<Boolean>(
            "de.cau.cs.kieler.klay.layered.distributeNodes", false);

    /** property to choose a cycle breaking strategy. */
    public static final IProperty<CycleBreakingStrategy> CYCLE_BREAKING 
        = new Property<CycleBreakingStrategy>(
            "de.cau.cs.kieler.klay.layered.cycleBreaking", CycleBreakingStrategy.GREEDY);

    /** property to choose a node layering strategy. */
    public static final IProperty<LayeringStrategy> NODE_LAYERING = new Property<LayeringStrategy>(
            "de.cau.cs.kieler.klay.layered.nodeLayering", LayeringStrategy.NETWORK_SIMPLEX);

    /** property to choose a crossing minimization strategy. */
    public static final IProperty<CrossingMinimizationStrategy> CROSSMIN 
        = new Property<CrossingMinimizationStrategy>(
            "de.cau.cs.kieler.klay.layered.crossMin", CrossingMinimizationStrategy.LAYER_SWEEP);

    /** property to choose a node placement strategy. */
    public static final IProperty<NodePlacementStrategy> NODEPLACE = new Property<NodePlacementStrategy>(
            "de.cau.cs.kieler.klay.layered.nodePlace", NodePlacementStrategy.LINEAR_SEGMENTS);

    /** property to choose a node placement strategy. */
    public static final IProperty<Boolean> EDGE_BENDS = new Property<Boolean>(
            "de.cau.cs.kieler.klay.layered.edgeBends", true);

    /** property to switch debug mode on or off. */
    public static final IProperty<Boolean> DEBUG_MODE = new Property<Boolean>(
            "de.cau.cs.kieler.debugMode", false);

    /** property that determines how much effort should be spent. */
    public static final IProperty<Integer> THOROUGHNESS = new Property<Integer>(
            "de.cau.cs.kieler.klay.layered.thoroughness", 5, 1);

    /** property to set constraints on the node layering. */
    public static final IProperty<LayerConstraint> LAYER_CONSTRAINT = new Property<LayerConstraint>(
            "de.cau.cs.kieler.klay.layered.layerConstraint", LayerConstraint.NONE);

    /**
     * Property to enable or disable port merging. Merging ports is only interesting for edges
     * directly connected to nodes instead of ports. When this option is disabled, one port is
     * created for each edge directly connected to a node. When it is enabled, all such incoming
     * edges share an input port, and all outgoing edges share an output port.
     */
    public static final IProperty<Boolean> MERGE_PORTS = new Property<Boolean>(
            "de.cau.cs.kieler.klay.layered.mergePorts", false);

    /** property that determines which point in a node determines the result of interactive phases. */
    public static final IProperty<InteractiveReferencePoint> INTERACTIVE_REFERENCE_POINT 
        = new Property<InteractiveReferencePoint>(
            "de.cau.cs.kieler.klay.layered.interactiveReferencePoint",
            InteractiveReferencePoint.CENTER);

    // /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR

    /**
     * Hidden default constructor.
     */
    private Properties() {
    }

}
