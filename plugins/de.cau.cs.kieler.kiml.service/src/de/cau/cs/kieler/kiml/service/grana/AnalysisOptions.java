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
package de.cau.cs.kieler.kiml.service.grana;

import de.cau.cs.kieler.core.properties.IProperty;
import de.cau.cs.kieler.core.properties.Property;

/**
 * Static definitions of options for graph analysis.
 *
 * @author msp
 */
public final class AnalysisOptions {
    
    /** analysis option identifier: process hierarchy recursively. */
    public static String ANALYZE_HIERARCHY_ID = "de.cau.cs.kieler.grana.analyzeHierarchy";
    /**
     * Whether the full hierarchy of a graph should be included in the analysis.
     */
    public static IProperty<Boolean> ANALYZE_HIERARCHY = new Property<Boolean>(
            ANALYZE_HIERARCHY_ID, true);

    /**
     * Hidden constructor to avoid instantiation.
     */
    private AnalysisOptions() {
    }

}
