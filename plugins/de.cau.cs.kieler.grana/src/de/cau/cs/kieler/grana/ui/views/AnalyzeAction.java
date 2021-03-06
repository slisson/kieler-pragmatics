/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2013 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.grana.ui.views;

import java.util.List;
import java.util.Map;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;

import de.cau.cs.kieler.grana.AnalysisData;
import de.cau.cs.kieler.grana.GranaPlugin;
import de.cau.cs.kieler.grana.ui.visualization.VisualizationService;
import de.cau.cs.kieler.grana.util.DiagramAnalyzer;
import de.cau.cs.kieler.grana.util.GranaUtil;

/**
 * Action used for the {@link AnalysisResultViewPart} to analyze the current editor's diagram, if any.
 * 
 * @author cds
 */
public class AnalyzeAction extends Action {
    /**
     * Unique ID of this action.
     */
    public static final String ACTION_ID = "de.cau.cs.kieler.grana.configureAnalyses";
    
    
    /**
     * Creates a new action for the given view part.
     */
    public AnalyzeAction() {
        // Configure how the action is displayed
        setId(ACTION_ID);
        setToolTipText("Analyze Current Diagram");
        setImageDescriptor(GranaPlugin.imageDescriptorFromPlugin(
                GranaPlugin.PLUGIN_ID,
                "icons/menu16/analyzediagram.gif"));
    }
    
    
    @Override
    public void run() {
        // Run analyses, if possible
        if (VisualizationService.getInstance().findActiveMethod(true)) {
            
            // There is a visualization method; find the active editor, if any
            // Note: it would be nice to use this action for view parts as well 
            //       technically this is possible, however, as the action's button
            //       is part of the grana view, when the button is pressed, the active view
            //       is the grana view itself, not the view with the diagram in it
            final IEditorPart activeEditorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                            .getActivePage().getActiveEditor();
            
            if (activeEditorPart != null) {
                activeEditorPart.getSite().getShell().getDisplay().asyncExec(new Runnable() {
                    public void run() {
                        // Analyze the diagram and visualize the results
                        List<AnalysisData> analyses = GranaUtil.getLastAnalysesSelection();
                        Map<String, Object> results = DiagramAnalyzer.analyze(
                                activeEditorPart, analyses, false);
                        VisualizationService.getInstance().visualize(analyses, results, true);
                    }
                });
            }
        }
    }
}
