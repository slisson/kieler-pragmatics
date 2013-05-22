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
package de.cau.cs.kieler.klay.tree.properties;

import java.util.Random;

import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.properties.IProperty;
import de.cau.cs.kieler.core.properties.Property;
import de.cau.cs.kieler.kiml.options.LayoutOptions;

/**
 * TODO: Document this class.
 * Property definitions for the T layouter.
 * 
 * @author sor
 * @author sgu
 */
public final class Properties {

    /** the original object from which a graph element was created. */
    public static final IProperty<Object> ORIGIN = new Property<Object>("origin");

    /**
     * Random number generator for the algorithm.
     */
    public static final IProperty<Random> RANDOM = new Property<Random>("random");

    /**
     * The longest path length of a node in the nesting tree of a compound graph.
     */
    public static final IProperty<Integer> DEPTH = new Property<Integer>("DEPTH", 0);

    /**
     * The maximal fan out of a node in the nesting tree of a compound graph.
     */
    public static final IProperty<Integer> FAN = new Property<Integer>("FAN", 0);
    
    /**
     * Is the node a root of a tree.
     */
    public static final IProperty<Boolean> ROOT = new Property<Boolean>("ROOT", false);

    /**
     * The level out of a node in the nesting tree of a compound graph.
     */
    public static final IProperty<Integer> LEVEL = new Property<Integer>("LEVEL", 0);

    /**
     * The direction from the origin of a node in the nesting tree of a compound graph.
     */
    public static final IProperty<Direction> DIRC = new Property<Direction>("DIRC", Direction.SOUTH);

    /** upper left corner of the graph's bounding box. */
    public static final IProperty<KVector> BB_UPLEFT = new Property<KVector>("boundingBox.upLeft");
    /** lower right corner of the graph's bounding box. */
    public static final IProperty<KVector> BB_LOWRIGHT = new Property<KVector>(
            "boundingBox.lowRight");

    // /////////////////////////////////////////////////////////////////////////////
    // USER INTERFACE OPTIONS

    /** minimal spacing between objects. */
    public static final Property<Float> SPACING = new Property<Float>(LayoutOptions.SPACING, 50.0f);

    /** the aspect ratio for packing connected components. */
    public static final Property<Float> ASPECT_RATIO = new Property<Float>(
            LayoutOptions.ASPECT_RATIO, 1.0f, 1.0f);

    /** priority of nodes or edges. */
    public static final Property<Integer> PRIORITY = new Property<Integer>(LayoutOptions.PRIORITY,
            1);

    /** label spacing property. */
    public static final Property<Float> LABEL_SPACING = new Property<Float>(
            LayoutOptions.LABEL_SPACING, 5.0f, 0.0f);

    // /////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTOR

    /**
     * Hidden default constructor.
     */
    private Properties() {
    }

}
