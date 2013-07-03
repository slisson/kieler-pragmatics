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
package de.cau.cs.kieler.klay.tree.graph;

import java.util.LinkedList;
import java.util.List;

import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.core.properties.MapPropertyHolder;

/**
 * A edge for the T graph.
 * 
 * @author sor
 * @author sgu
 */
public class TEdge extends MapPropertyHolder {

    /** the serial version UID. */
    private static final long serialVersionUID = 1L;

    /** the labels of the edge. */
    private List<TLabel> labels = new LinkedList<TLabel>();
    /** the source node of the edge. */
    private TNode source;
    /** the target node of the edge. */
    private TNode target;

    private KVectorChain bendPoints = new KVectorChain();

    /**
     * Constructor for setting up a TEdge object
     * 
     * @param source
     *            the source node of the edge
     * @param target
     *            the target node of the edge
     */
    public TEdge(TNode source, TNode target) {
        this.source = source;
        this.target = target;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (source != null && target != null) {
            return source.toString() + "->" + target.toString();
        } else {
            return "e_" + hashCode();
        }
    }

    /**
     * Returns the source node.
     * 
     * @return the source node
     */
    public TNode getSource() {
        return source;
    }

    /**
     * Sets the source node and update it's outgoing list accordingly.
     * 
     * @param theSource
     *            the source node set to
     */
    public final void setSource(final TNode theSource) {
        if (source != theSource) {
            if (source != null && source.getOutgoingEdges().contains(this)) {
                source.getOutgoingEdges().remove(this);
            }
            if (theSource != null && !theSource.getOutgoingEdges().contains(this)) {
                theSource.getOutgoingEdges().add(this);
            }
            source = theSource;
        }
    }

    /**
     * Returns the target node.
     * 
     * @return the target node
     */
    public TNode getTarget() {
        return target;
    }

    /**
     * Sets the target node and update it's incoming list accordingly.
     * 
     * @param theTarget
     *            the target node
     */
    public final void setTarget(final TNode theTarget) {
        if (target != theTarget) {
            if (target != null && target.getInComingEdges().contains(this)) {
                target.getInComingEdges().remove(this);
            }
            if (theTarget != null && !theTarget.getInComingEdges().contains(this)) {
                theTarget.getInComingEdges().add(this);
            }
            target = theTarget;
        }
    }

    /**
     * Returns the list of labels associated with this edge.
     * 
     * @return list of labels
     */
    public List<TLabel> getLabels() {
        return labels;
    }

    public KVectorChain getBendPoints() {
        return bendPoints;
    }

}
