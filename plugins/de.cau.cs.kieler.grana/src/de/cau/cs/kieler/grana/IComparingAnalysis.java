/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://rtsys.informatik.uni-kiel.de/kieler
 * 
 * Copyright 2016 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 */
package de.cau.cs.kieler.grana;

import java.util.Map;

import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.properties.IProperty;
import org.eclipse.elk.graph.properties.Property;
import org.eclipse.emf.ecore.EObject;

/**
 * @author uru
 */
public interface IComparingAnalysis extends IAnalysis {

    /**
     * Holds the mapping between a source graph's elements and its copy's elements.
     */
    IProperty<Map<EObject, EObject>> ELEMENT_MAPPING = new Property<>(
            "de.cau.cs.kieler.grana.compare.mapping");

    /**
     * {@inheritDoc}
     */
    default Object doAnalysis(final ElkNode parentNode, final AnalysisContext context,
            final IElkProgressMonitor progressMonitor) {

        assert parentNode.getChildren().size() == 2;

        ElkNode first = parentNode.getChildren().get(0);
        ElkNode second = parentNode.getChildren().get(1);

        Map<EObject, EObject> elementMapping = parentNode.getProperty(ELEMENT_MAPPING);

        return doAnalysis(first, second, elementMapping, context, progressMonitor);
    }

    /**
     * Perform the comparing analysis.
     * 
     * @param first
     *            the first graph
     * @param second
     *            the second graph
     * @param mapping
     *            the mapping of {@code first}'s elements to {@code second}'s elements
     * @param context
     *            analysis context
     * @param progressMonitor
     *            a progress monitor
     * @return the result of
     *         {@link #doAnalysis(ElkNode, ElkNode, AnalysisContext, IKielerProgressMonitor)}
     */
    Object doAnalysis(ElkNode first, ElkNode second, Map<EObject, EObject> mapping,
            AnalysisContext context, IElkProgressMonitor progressMonitor);

}
