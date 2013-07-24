/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2013 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.ptolemy.klighd

import com.google.common.collect.ImmutableSet
import com.google.inject.Inject
import de.cau.cs.kieler.klighd.TransformationOption
import de.cau.cs.kieler.klighd.transformations.AbstractDiagramSynthesis
import de.cau.cs.kieler.ptolemy.klighd.transformation.Ptolemy2KGraphOptimization
import de.cau.cs.kieler.ptolemy.klighd.transformation.Ptolemy2KGraphTransformation
import de.cau.cs.kieler.ptolemy.klighd.transformation.Ptolemy2KGraphVisualization
import org.ptolemy.moml.DocumentRoot

import static de.cau.cs.kieler.ptolemy.klighd.PtolemyDiagramSynthesis.*

/**
 * Synthesis for turning Ptolemy models into KGraphs.
 * 
 * @author cds
 */
public class PtolemyDiagramSynthesis extends AbstractDiagramSynthesis<DocumentRoot> {
    
    // Our transformation options
    static val TransformationOption SHOW_COMMENTS = TransformationOption::createCheckOption(
        "Show comments", true)
    static val TransformationOption HIDE_RELATIONS = TransformationOption::createCheckOption(
        "Hide relations", true)
    
    // The parts of our transformation
    @Inject Ptolemy2KGraphTransformation transformation
    @Inject Ptolemy2KGraphOptimization optimization
    @Inject Ptolemy2KGraphVisualization visualization
    
    
    override transform(DocumentRoot model) {
        val kgraph = transformation.transform(model)
        optimization.optimize(kgraph, SHOW_COMMENTS.optionBooleanValue,
            HIDE_RELATIONS.optionBooleanValue)
        visualization.visualize(kgraph)
        
        return kgraph
    }
    
//    override getTransformationOptions() {
//        return ImmutableSet::of(SHOW_COMMENTS, HIDE_RELATIONS)
//    }
    
}