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
package de.cau.cs.kieler.kiml.options;

import java.util.EnumSet;
import java.util.Set;

/**
 * Things to take into account when determining the size of a node. Each item of this enumeration
 * corresponds to something a layout algorithm should pay attention to when calculating node sizes.
 * Usually, one will use a combination of these values in an {@code EnumSet} instance, with the
 * empty set meaning that node sizes are fixed.
 * 
 * <p><i>Note:</i> Layout algorithms may only support a subset of these options.</p>
 *
 * @author msp
 * @author cds
 */
public enum SizeConstraint {
    
    /**
     * The number of ports and their position should be taken into account when determining the
     * size of nodes. The sum of port widths and heights and the minimum spacing between ports
     * is a lower bound for the node size.
     */
    PORTS,
    
    /**
     * Ports labels are taken into account when determining the size of nodes. Depending on where
     * the labels are positioned the node will be made large enough to avoid overlaps and to try
     * to place labels in as unambiguous a way as possible. Setting this option doesn't make any
     * sense if the {@link #PORTS} option is not set as well.
     */
    PORT_LABELS,
    
    /**
     * A node's labels are taken into account.
     */
    NODE_LABELS,
    
    /**
     * If set, a node's size will be at least the minimum size set on it. If no minimum size is set,
     * the behaviour depends on whether the {@link #DEFAULT_MINIMUM_SIZE} constraint is set as well.
     */
    MINIMUM_SIZE,
    
    /**
     * If no minimum size is set on an element, the minimum size options are assumed to be some
     * default value determined by the particular layout algorithm.
     */
    DEFAULT_MINIMUM_SIZE,
    
    /**
     * If this option is set and insets are computed by the algorithm, the minimum size plus the
     * computed insets are a lower bound on the node size. If this option is not set, the minimum size
     * will be applied to the node's whole size regardless of any computed insets. Note that,
     * depending on the algorithm, this option may only apply to non-hierarchical nodes.
     */
    MINIMUM_SIZE_ACCOUNTS_FOR_INSETS,
    
    /**
     * With this option set, the insets of nodes will be computed and returned as part of the
     * algorithm's result. If port labels or node labels are placed, they may influence the size of
     * the insets. Note that, depending on the algorithm, this option may only apply to
     * non-hierarchical nodes.
     */
    COMPUTE_INSETS;
    
    
    public static boolean isSizeFixed(Set<SizeConstraint> constraints) {
        return !constraints.contains(PORTS) &&
                !constraints.contains(PORT_LABELS) &&
                !constraints.contains(NODE_LABELS) &&
                !constraints.contains(MINIMUM_SIZE);
    }
    
    
    /**
     * Returns the enumeration value related to the given ordinal.
     * 
     * @param i ordinal value
     * @return the related enumeration value
     */
    public static SizeConstraint valueOf(final int i) {
        return values()[i];
    }
    
    
    /**
     * Returns an empty enum set over this enumeration, which corresponds to fixed size constraints.
     * 
     * @return set over this enumeration representing fixed size constraints.
     */
    public static EnumSet<SizeConstraint> fixed() {
        return EnumSet.noneOf(SizeConstraint.class);
    }
    
    /**
     * Returns a set containing the common combination of {@link #MINIMUM_SIZE} and
     * {@link #DEFAULT_MINIMUM_SIZE}.
     * 
     * @return set with default minimum size constraint combination.
     */
    public static EnumSet<SizeConstraint> defaultMinimumSize() {
        return EnumSet.of(MINIMUM_SIZE, DEFAULT_MINIMUM_SIZE);
    }
    
    /**
     * Returns a set containing the common combination of {@link #MINIMUM_SIZE},
     * {@link #DEFAULT_MINIMUM_SIZE}, and {@link #PORTS}.
     * 
     * @return set with default minimum size constraint combination with ports.
     */
    public static EnumSet<SizeConstraint> defaultMinimumSizeWithPorts() {
        return EnumSet.of(PORTS, MINIMUM_SIZE, DEFAULT_MINIMUM_SIZE);
    }
    
    /**
     * Returns a set containing all options defined in this enumeration, effectively giving
     * the layout algorithm as much freedom as possible in determining the node size.
     * 
     * @return set with all available options.
     */
    public static EnumSet<SizeConstraint> free() {
        return EnumSet.allOf(SizeConstraint.class);
    }
}
