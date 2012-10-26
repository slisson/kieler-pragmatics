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
package de.cau.cs.kieler.klay.test.utils;

import java.io.File;

import de.cau.cs.kieler.core.kgraph.KNode;

/**
 * This Object represents a Graph file and the graph contained therein.
 * 
 * @author wah
 */
public class GraphTestObject {

    /* The Graph file */
    private File file;
    /* The graph contained */
    private KNode knode;

    /**
     * The constructor of the class with two parameters to initialize the graph file and the graph
     * contained therein
     * 
     * @param file
     *            the Graph file
     * @param knode
     *            the graph contained
     */
    public GraphTestObject(File file, KNode knode) {
        this.file = file;
        this.knode = knode;
    }

    /**
     * Return the graph file
     * 
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * Return the KNode
     * 
     * @return the knode
     */
    public KNode getKnode() {
        return knode;
    }

    /**
     * Set the graph file
     * 
     * @param file
     *            the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * Set the the graph contained(KNode)
     * 
     * @param knode
     *            the knode to set
     */
    public void setKnode(KNode knode) {
        this.knode = knode;
    }
}
