/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2017 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 */
package de.cau.cs.kieler.graphs.hierarchicalViews.radial;

import org.eclipse.elk.alg.radial.InternalProperties;
import org.eclipse.elk.alg.radial.RadialLayoutProvider;
import org.eclipse.elk.alg.radial.options.CompactionStrategy;
import org.eclipse.elk.alg.radial.options.RadialOptions;
import org.eclipse.elk.alg.radial.options.RadialTranslationStrategy;
import org.eclipse.elk.alg.radial.options.SortingStrategy;
import org.eclipse.elk.core.AbstractLayoutProvider;
import org.eclipse.elk.core.options.CoreOptions;
import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.graph.ElkNode;

/**
 * The hierarchical radial layout provider uses the elk radial layout provider
 * and arguments it with a polar coordinate sorter,explosion line edge routing
 * and radial compaction.
 */
public class HierarchicalRadialLayoutProvider extends AbstractLayoutProvider {

	@Override
	public void layout(ElkNode layoutGraph, IElkProgressMonitor progressMonitor) {
		RadialLayoutProvider radialLayouter = new RadialLayoutProvider();
		layoutGraph.setProperty(RadialOptions.COMPACTOR, CompactionStrategy.RADIAL_COMPACTION);
		layoutGraph.setProperty(RadialOptions.SORTER, SortingStrategy.POLAR_COORDINATE);
		layoutGraph.setProperty(RadialOptions.OPTIMIZATION_CRITERIA,
				RadialTranslationStrategy.EDGE_LENGTH_BY_POSITION);
		layoutGraph.setProperty(CoreOptions.SPACING_NODE_NODE, 30.0);
		radialLayouter.layout(layoutGraph, progressMonitor);

		ExplosionLineRouter linerouter = new ExplosionLineRouter();
		ElkNode node = layoutGraph.getProperty(InternalProperties.ROOT_NODE);
		linerouter.bendEdgesToHierarchicalEdges(node);
	}

}
