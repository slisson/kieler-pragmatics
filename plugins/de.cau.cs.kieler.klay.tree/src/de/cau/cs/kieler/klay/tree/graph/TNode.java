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

/**
 * TODO: Document this class.
 * 
 * A node in the T graph.
 * 
 * @author sor
 * @author sgu
 */
public class TNode extends TShape {

    /** the serial version UID. */
    private static final long serialVersionUID = 1L;

    // CHECKSTYLEOFF VisibilityModifier
    /** the identifier number. */
    public int id;
    // CHECKSTYLEON VisibilityModifier

    /** the node label. */
    private String label;
    /** The parent node. */
    private TNode parent;

    private TNode leftChild;
    private TNode rightChild;
    
    private TNode leftNeighbour;
    private TNode rightNeighbour;
    /** List of child nodes. */
    private LinkedList<TNode> children;

    private LinkedList<TEdge> outgoingEdges;
    
    private LinkedList<TEdge> incomeingEdges;

    /**
     * Create a new node without parent and label.
     */
    public TNode(final int id, final TGraph graph) {
        super(id, graph);
        this.id = id;
        this.parent = null;
        this.rightChild = null;
        this.leftChild = null;
        this.leftNeighbour = null;
        this.rightNeighbour = null;
        this.children = new LinkedList<TNode>();
    }

    /**
     * Create a new node with given parent.
     */
    public TNode(final int id, final TGraph graph, final TNode parent) {
        super(id, graph);
        this.id = id;
        this.parent = parent;
        this.leftChild = null;
        this.rightChild = null;
        this.leftNeighbour = null;
        this.rightNeighbour = null;
        this.children = new LinkedList<TNode>();
    }

    /**
     * Create a new node with given label.
     * 
     * @param label
     *            the label text
     */
    public TNode(final int id, final TGraph graph, final String label) {
        super(id, graph);
        this.id = id;
        this.label = label;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.leftNeighbour = null;
        this.rightNeighbour = null;
        this.children = new LinkedList<TNode>();
    }

    /**
     * Create a new node with given parent node and label.
     * 
     * @param label
     *            the label text
     * @param parent
     *            the parent node
     */
    public TNode(final int id, final TGraph graph, final String label, final TNode parent) {
        super(id, graph);
        this.id = id;
        this.label = label;
        this.parent = parent;
        this.leftChild = null;
        this.rightChild = null;
        this.leftNeighbour = null;
        this.rightNeighbour = null;
        this.children = new LinkedList<TNode>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (label == null || label.length() == 0) {
            return "n_" + id;
        } else {
            return "n_" + label;
        }
    }

    /**
     * Returns whether this node is a compound node.
     * 
     * @return true if compound
     */
    public boolean isCompound() {
        return children != null && children.size() > 0;
    }

    /**
     * Returns the label text of this node.
     * 
     * @return the label
     */
    public String getLabel() {
        return label;
    }

    /**
     * Returns the parent node.
     * 
     * @return the parent
     */
    public TNode getParent() {
        return parent;
    }

    public TNode getLeftChild() {
        return leftChild;
    }

    public TNode getRightChild() {
        return rightChild;
    }
    
    public TNode getLeftNeighbour() {
        return leftNeighbour;
    }
    
    public TNode getRightNeighbour() {
        return rightNeighbour;
    }

    /**
     * Returns the list of children, creating it if necessary.
     * 
     * @return the children
     */
    public LinkedList<TNode> getChildren() {
        if (children == null) {
            children = new LinkedList<TNode>();
        }
        return children;
    }

    public List<TEdge> getOutgoingEdges() {
        if (outgoingEdges == null) {
            outgoingEdges = new LinkedList<TEdge>();
        }
        return outgoingEdges;
    }
    
    public List<TEdge> getInComingEdges() {
        if (incomeingEdges == null) {
            incomeingEdges = new LinkedList<TEdge>();
        }
        return incomeingEdges;
    }

    /**
     * Returns the depth of this node in the compound hierarchy.
     * 
     * @return the depth
     */
    public int getDepth() {
        int depth = 0;
        TNode node = parent;
        while (node != null) {
            node = node.getParent();
            depth++;
        }
        return depth;
    }

    // SETTERS

    /**
     * Set the label text of this node.
     * 
     */
    public void setLabel(String label) {
        this.label = label;
    }

    /**
     * Set the parent node.
     * 
     */
    public void setParent(TNode parent) {
        this.parent = parent;
    }

    public void setLeftChild(TNode leftChild) {
        this.leftChild = leftChild;
        children.add(leftChild);
    }

    public void setRightChild(TNode rightChild) {
        this.rightChild = rightChild;
        children.add(rightChild);
    }
    
    public void setLeftNeighbour(TNode tNode) {
        this.leftNeighbour = tNode;
    }
    
    public void setRightNeighbour(TNode tNode) {
        this.rightNeighbour = tNode;
    }

    /**
     * Set the list of children.
     * 
     */
    public void setChildren(LinkedList<TNode> children) {
        this.children = children;
    }

    /**
     * Add a node to the list of children.
     * 
     */
    public void addChild(TNode child) {
        this.children.add(child);
    }
}
