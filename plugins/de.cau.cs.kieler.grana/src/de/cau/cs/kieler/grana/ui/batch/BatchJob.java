/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2010 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.grana.ui.batch;

import java.util.List;
import java.util.Map;

import org.eclipse.elk.core.IGraphLayoutEngine;
import org.eclipse.elk.core.LayoutConfigurator;
import org.eclipse.elk.core.RecursiveGraphLayoutEngine;
import org.eclipse.elk.core.util.ElkUtil;
import org.eclipse.elk.core.util.IElkProgressMonitor;
import org.eclipse.elk.core.util.Pair;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.elk.graph.ElkNode;
import org.eclipse.elk.graph.properties.MapPropertyHolder;
import org.eclipse.elk.graph.util.ElkGraphUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Lists;

import de.cau.cs.kieler.grana.AnalysisContext;
import de.cau.cs.kieler.grana.AnalysisData;
import de.cau.cs.kieler.grana.AnalysisService;
import de.cau.cs.kieler.grana.IComparingAnalysis;

/**
 * The class which represents an analysis batch job.
 * 
 * @author mri
 * @kieler.ignore (excluded from review process)
 * @param <T>
 *            the type of the parameter
 */
public abstract class BatchJob<T> implements IBatchJob<T> {

    // SUPPRESS CHECKSTYLE NEXT 20 VisibilityModifier
    
    /** overall work carried out by a job. */
    protected static final int WORK = 3;
    /** part of the work is to load the graph. */
    protected static final int WORK_KGRAPH = 1;
    /** the rest is spent analyzing. */
    protected static final int WORK_ANALYSIS = 2;
    
    /** the parameter for this batch job. */
    protected T parameter;
    /** the KGraph provider for this batch run. */
    protected IElkGraphProvider<T> graphProvider;
    /** the containing batch for reference, if any. */
    protected Batch batch = null;

    /**
     * Constructs an AnalysisBatchJob.
     * 
     * @param param
     *            the parameter
     * @param provider
     *            the KGraph provider
     */
    public BatchJob(final T param, final IElkGraphProvider<T> provider) {
        parameter = param;
        graphProvider = provider;
    }

    /**
     * @param batch the batch to set
     */
    @Override
    public void setBatch(final Batch batch) {
        this.batch = batch;
    }
    
    /**
     * Returns the parameter of the job.
     * 
     * @return the parameter
     */
    public T getParameter() {
        return parameter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BatchJobResult execute(final List<AnalysisData> analyses,
            final IElkProgressMonitor monitor) throws Exception {
        
        monitor.begin("Executing analysis batch job: " + parameter, WORK);
        ElkNode graph = graphProvider.getElkGraph(parameter, monitor.subTask(WORK_KGRAPH));

        BatchJobResult batchJobResult = localExecute(graph, analyses, monitor);
        // only present if actual execution time is measured
        batchJobResult.setFastestExection(graph.getProperty(BatchHandler.EXECUTION_TIME_RESULTS));

        monitor.done();
        return batchJobResult;
    }

    /**
     * Execute the job locally, i.e. perform the given analyses.
     * 
     * @param graph
     *            the graph to analyze
     * @param analyses
     *            basic analyses to execute
     * @param monitor
     *            the monitor
     * @return the result
     */
    protected abstract BatchJobResult localExecute(ElkNode graph,
            List<AnalysisData> analyses, IElkProgressMonitor monitor);

    // -------------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - SIMPLE - - - - - - - - - - - - - - - - - -
    // -------------------------------------------------------------------------------

    // SUPPRESS CHECKSTYLE NEXT 1 Javadoc
    public static class Simple<T> extends BatchJob<T> {
        /**
         * Constructs an AnalysisBatchJob.
         * 
         * @param param
         *            the parameter
         * @param provider
         *            the KGraph provider
         */
        public Simple(final T param, final IElkGraphProvider<T> provider) {
            super(param, provider);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected BatchJobResult localExecute(final ElkNode graph,
                final List<AnalysisData> analyses, final IElkProgressMonitor monitor) {
            AnalysisContext context = AnalysisService.getInstance().analyze(graph, analyses,
                    monitor.subTask(WORK_ANALYSIS));
            return new BatchJobResult.Simple(this, context.getResults());
        }
    }
    
    // ------------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - RANGE - - - - - - - - - - - - - - - - - -
    // ------------------------------------------------------------------------------

    // SUPPRESS CHECKSTYLE NEXT 1 Javadoc
    public static class Range<T> extends BatchJob<T> {

        /** the containing batch for reference. */
        // private Batch batch;
        
        private static IGraphLayoutEngine layoutEngine;

        private LinkedListMultimap<MapPropertyHolder, Map<String, Object>> rangeResults =
                LinkedListMultimap.create();

        private LayoutConfigurator commonConfig;
        private List<MapPropertyHolder> rangeConfigurations;

        /**
         * Constructs an AnalysisBatchJob.
         * 
         * @param param
         *            the parameter
         * @param provider
         *            the KGraph provider
         * @param commonCfg
         *            the config applied during every layout run
         * @param cfgs
         *            different layout configurations to be applied (a.k.a. range options)
         */
        public Range(final T param, final IElkGraphProvider<T> provider,
                final LayoutConfigurator commonCfg,
                final List<MapPropertyHolder> cfgs) {
            super(param, provider);
            this.commonConfig = commonCfg;
            this.rangeConfigurations = cfgs;
        }

        /**
         * @return the rangeResults
         */
        public LinkedListMultimap<MapPropertyHolder, Map<String, Object>> getRangeResults() {
            return rangeResults;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected BatchJobResult localExecute(final ElkNode graph,
                final List<AnalysisData> analyses, final IElkProgressMonitor monitor) {

            AnalysisContext context = AnalysisService.getInstance().analyze(graph, analyses,
                    monitor.subTask(WORK_ANALYSIS));

            // get the range analyses
            if (batch == null) {
                throw new IllegalStateException(
                        "During execution, a " + this.getClass().getSimpleName() + " must know the "
                                + Batch.class.getSimpleName() + " collection it is part of.");
            }
            
            Batch.Range rangeBatch = (Batch.Range) batch;
            List<AnalysisData> rangeAnalyses = rangeBatch.getRangeAnalyses();
            if (rangeAnalyses == null || rangeAnalyses.isEmpty()) {
                rangeAnalyses = Lists.newArrayList();
                rangeAnalyses.add(rangeBatch.getRangeAnalysis());
            }
            
            for (MapPropertyHolder options : rangeConfigurations) {

                // now layout with the range or whatever it is layout option
                LayoutConfigurator lc = new LayoutConfigurator();
                lc.overrideWith(commonConfig);
                
                // FIXME the following two lines are problematic, see ELK bug #153
                lc.configure(ElkGraphElement.class).copyProperties(options);
                lc.configure(ElkNode.class).copyProperties(options);
                
                // #1 create a copy of the graph
                // following three lines copied from EcoreUtils#copy
                Copier copier = new Copier();
                final ElkNode graphCopy = (ElkNode) copier.copy(graph);
                copier.copyReferences();

                ElkUtil.applyVisitors(graphCopy, lc);
                if (layoutEngine == null) {
                    layoutEngine = new RecursiveGraphLayoutEngine();
                }
                layoutEngine.layout(graphCopy, monitor.subTask(1));

                AnalysisContext rangeContext = AnalysisService.getInstance().analyze(
                        graphCopy, rangeAnalyses, monitor.subTask(1));

                rangeResults.put(options, rangeContext.getResults());
            }

            BatchJobResult batchJobResult =
                    new BatchJobResult.Range(this, context.getResults(), rangeResults);
            monitor.done();

            return batchJobResult;
        }

    }

    // --------------------------------------------------------------------------------
    // - - - - - - - - - - - - - - - - - - COMPARE - - - - - - - - - - - - - - - - - -
    // --------------------------------------------------------------------------------
    
    // SUPPRESS CHECKSTYLE NEXT 1 Javadoc
    public static class Compare<T> extends BatchJob<T> {

        /** the layout configs for the two layout runs to be compared. */
        private Pair<LayoutConfigurator, LayoutConfigurator> layoutConfigurators;
        
        private static IGraphLayoutEngine layoutEngine;

        /**
         * Constructs an AnalysisBatchJob.
         * 
         * @param param
         *            the parameter
         * @param provider
         *            the KGraph provider
         * @param layoutConfigs
         *            the layout configs for the two layout runs to be compared.
         * 
         */
        public Compare(final T param, final IElkGraphProvider<T> provider,
                final Pair<LayoutConfigurator, LayoutConfigurator> layoutConfigs) {
            super(param, provider);
            layoutConfigurators = layoutConfigs; 
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected BatchJobResult localExecute(final ElkNode graph,
                final List<AnalysisData> analyses, final IElkProgressMonitor monitor) {
            
            ElkNode first = graph;
            
            // #1 create a copy of the graph
            // following three lines copied from EcoreUtils#copy
            Copier copier = new Copier();
            final ElkNode second = (ElkNode) copier.copy(first);
            copier.copyReferences();
            Map<EObject, EObject> objectMapping = copier;
            
            // #2
            // layout both graphs (make sure the graph has been copied beforehand!)
            if (layoutEngine == null) {
                layoutEngine = new RecursiveGraphLayoutEngine();
            }
            ElkUtil.applyVisitors(first, layoutConfigurators.getFirst());
            layoutEngine.layout(first, monitor.subTask(1));
            ElkUtil.applyVisitors(second, layoutConfigurators.getSecond());
            layoutEngine.layout(second, monitor.subTask(1));
            
            // #3 create a wrapper node to be passed to the analysis service
            ElkNode wrapper = ElkGraphUtil.createGraph();
            wrapper.setProperty(IComparingAnalysis.ELEMENT_MAPPING, objectMapping);
            wrapper.getChildren().add(first);
            wrapper.getChildren().add(second);
            
            // TODO 
            // a) we should check whether all analyses implement ICompareAnalysis
            // b) implement a dedicated BatchJobResult.Compare that can hold both regular and comparing results
            
            AnalysisContext context = AnalysisService.getInstance().analyze(wrapper, analyses,
                    monitor.subTask(WORK_ANALYSIS));
            BatchJobResult batchJobResult = new BatchJobResult.Simple(this, context.getResults());
            
            monitor.done();
            return batchJobResult;
        }
    }
    
}