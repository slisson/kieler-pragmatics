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
package de.cau.cs.kieler.klay.cola.graph;

import java.util.ArrayList;
import java.util.List;

import org.adaptagrams.Rectangle;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;

import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.options.PortSide;
import de.cau.cs.kieler.kiml.util.nodespacing.Spacing.Margins;
import de.cau.cs.kieler.klay.cola.properties.ColaProperties;

/**
 * @author uru
 */
public class CNode extends CShape {

    private static final long serialVersionUID = -8556727908262767615L;

    // CHECKSTYLEOFF VisibilityModifier
    // CHECKSTYLEOFF Javadoc

    /** The adaptagrams {@link Rectangle} representing this node. */
    public Rectangle rect;

    protected List<CNode> children;
    protected List<CEdge> outgoingEdges;
    protected List<CEdge> incomingEdges;
    protected List<CPort> ports;

    protected CGraphElement parent = null;

    /**
     * .
     */
    public CNode(final CGraph graph) {
        super(graph);

        // setup the internal lists
        children = new ArrayList<CNode>();
        outgoingEdges = new ArrayList<CEdge>();
        incomingEdges = new ArrayList<CEdge>();
        ports = new ArrayList<CPort>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // KShapeLayout layout = node.getData(KShapeLayout.class);

        // get margins
        Margins margin = this.getProperty(LayoutOptions.MARGINS);

        // x X y Y meaning x width y height
        // Rectangle r =
        // new Rectangle(layout.getXpos(), layout.getXpos() + layout.getWidth(),
        // layout.getYpos(), layout.getYpos() + layout.getHeight());

        // constrained layout considers previous positions, to make it independent from
        // any weird layout stuff used before we run it, use 0 as initial positions for all
        // rects
        rect =
                new Rectangle(0 - margin.left,
                // assure that the size is at least 1
                        Math.max(1, 0 + this.getSize().x + margin.right), 0 - margin.top, Math.max(
                                1, 0 + this.getSize().y + margin.bottom + 20) // same here
                );
        cIndex = graph.nodeIndex++;

        // register in graph
        graph.nodes.add(rect);

    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(final CGraphElement parent) {
        this.parent = parent;
    }

    /**
     * @return the parent
     */
    public CGraphElement getParent() {
        return parent;
    }

    /**
     * @return the outgoing and incoming edges
     */
    public Iterable<CEdge> getConnectedEdges() {
        return Iterables.concat(outgoingEdges, incomingEdges);
    }

    /**
     * @return the outgoingEdges
     */
    public List<CEdge> getOutgoingEdges() {
        return outgoingEdges;
    }

    /**
     * @return the incomingEdges
     */
    public List<CEdge> getIncomingEdges() {
        return incomingEdges;
    }

    /**
     * @return the ports
     */
    public List<CPort> getPorts() {
        return ports;
    }

    /**
     * @return the ports
     */
    public Iterable<CPort> getPorts(final PortSide side) {
        Iterable<CPort> filtered = Iterables.filter(ports, new Predicate<CPort>() {
            public boolean apply(final CPort p) {
                return p.side == side;
            }
        });
        return filtered;
    }

    // /**
    // * Returns the name of the node. The name is derived from the text of the first label, if any.
    // *
    // * @return the name, or {@code null}
    // */
    // public String getName() {
    // if (!origin.getLabels().isEmpty()) {
    // return origin.getLabels().get(0).getText();
    // }
    // return null;
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // public String toString() {
    // String name = getName();
    // if (name != null) {
    // return "n_" + getName();
    // }
    // return "n_" + cIndex;
    // }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        Object origin = getProperty(ColaProperties.ORIGIN);
        if (origin != null) {
            return origin.toString();
        }
        return super.toString();
    }
}
