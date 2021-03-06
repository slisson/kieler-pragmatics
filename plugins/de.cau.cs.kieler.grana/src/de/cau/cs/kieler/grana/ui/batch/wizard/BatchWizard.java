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
package de.cau.cs.kieler.grana.ui.batch.wizard;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.elk.core.LayoutConfigurator;
import org.eclipse.elk.core.util.Pair;
import org.eclipse.elk.graph.ElkGraphElement;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;

import de.cau.cs.kieler.grana.AnalysisData;
import de.cau.cs.kieler.grana.util.GranaUtil;

/**
 * The batch wizard lets the user select any number of diagram files and starts
 * a job which opens the diagrams, applies automatic layout, performs all
 * currently selected graph analyses and saves the results to a file.
 * 
 * @author mri
 * @author uru
 * @kieler.ignore (excluded from review process)
 */
public class BatchWizard extends Wizard {

    /** the wizard title. */
    private static final String TITLE = "Initialize a batch graph analysis";

    /** the preference store key for the selected analyses. */
    private static final String PREFERENCE_SELECTED_ANALYSES = "grana.batch.selectedAnalyses";
    /** the preference store key for the layout configuration. */
    private static final String PREFERENCE_LAYOUT_CONFIG = "grana.batch.layoutConfig";

    /** the initial selection. */
    private IStructuredSelection selection = null;
    /** the file selection page. */
    private BatchFileSelectionPage fileSelectionPage;
    /** the result file page. */
    private BatchResultFilePage resultFilePage;
    /** the analysis selection page. */
    private BatchAnalysisSelectionPage analysisSelectionPage;
    /** the layout configuration page. */
    private LayoutConfigurationPage layoutConfigurationPage;

    /** the selected files. */
    private List<IPath> selectedFiles;
    /** the 'layout before analysis' option. */
    private boolean layoutBeforeAnalysis;
    /** the result file. */
    private IPath resultFile;
    /** the selected analyses. */
    private List<AnalysisData> selectedAnalyses;
    /** the layout configurator. */
    private LayoutConfigurator layoutConfig;
    /** whether to record execution times. */
    private boolean performExecutionTimeAnalysis;

    /**
     * Constructs a BatchWizard without initial file selection.
     */
    public BatchWizard() {
        super();
        setWindowTitle(TITLE);
    }

    /**
     * Constructs a BatchWizard with initial file selection.
     * 
     * @param initialSelection
     *            the selection
     */
    public BatchWizard(final IStructuredSelection initialSelection) {
        super();
        setWindowTitle(TITLE);
        selection = initialSelection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addPages() {
        fileSelectionPage = new BatchFileSelectionPage(selection);
        resultFilePage = new BatchResultFilePage();
        selectedAnalyses = GranaUtil.getAnalysesSelection(PREFERENCE_SELECTED_ANALYSES);
        analysisSelectionPage = new BatchAnalysisSelectionPage(selectedAnalyses);
        layoutConfigurationPage = new LayoutConfigurationPage(GranaUtil.getConfiguration(
                PREFERENCE_LAYOUT_CONFIG));
        addPage(fileSelectionPage);
        addPage(resultFilePage);
        addPage(analysisSelectionPage);
        addPage(layoutConfigurationPage);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean performFinish() {
        fileSelectionPage.savePreferences();
        resultFilePage.savePreferences();
        selectedFiles = fileSelectionPage.getSelectedFiles();
        layoutBeforeAnalysis = fileSelectionPage.getLayoutBeforeAnalysis();
        performExecutionTimeAnalysis = fileSelectionPage.getExecutionTimeAnalysis();
        resultFile = resultFilePage.getResultFile();
        selectedAnalyses = analysisSelectionPage.getAnalyses();
        GranaUtil.setAnalysesSelection(PREFERENCE_SELECTED_ANALYSES, selectedAnalyses);
        
        LayoutConfigurator batchLayoutConfig = layoutConfigurationPage.getConfig();
        layoutConfig = batchLayoutConfig;
        if (batchLayoutConfig.getProperties(ElkGraphElement.class) != null) {
            GranaUtil.setConfiguration(PREFERENCE_LAYOUT_CONFIG, Pair.fromMap(
                    batchLayoutConfig.getProperties(ElkGraphElement.class).getAllProperties()));
        }
        
        return true;
    }

    /**
     * Returns the list of selected files.
     * 
     * @return the list of selected files
     */
    public List<IPath> getSelectedFiles() {
        return selectedFiles;
    }

    /**
     * Returns the value of the 'layout before analysis' option.
     * 
     * @return true if 'layout before analysis' has been activated
     */
    public boolean getLayoutBeforeAnalysis() {
        return layoutBeforeAnalysis;
    }
    
    /**
     * @return the performExecutionTimeAnalysis
     */
    public boolean getExecutionTimeAnalysis() {
        return performExecutionTimeAnalysis;
    }

    /**
     * Returns the selected result file.
     * 
     * @return the result file
     */
    public IPath getResultFile() {
        return resultFile;
    }

    /**
     * Returns the selected analyses.
     * 
     * @return the selected analyses
     */
    public List<AnalysisData> getAnalyses() {
        return selectedAnalyses;
    }
    
    /**
     * Return the selected layout configurator.
     * 
     * @return the layout configurator
     */
    public LayoutConfigurator getLayoutConfig() {
        return layoutConfig;
    }
    
}
