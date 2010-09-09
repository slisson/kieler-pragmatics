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
package de.cau.cs.kieler.kiml.grana.views;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import de.cau.cs.kieler.kiml.grana.AbstractInfoAnalysis;
import de.cau.cs.kieler.kiml.grana.ui.HtmlResultGenerator;

/**
 * A view that is an alternative way to display analysis results.
 * 
 * @author mri
 */
public class AnalysisResultViewPart extends ViewPart {

    /** the view identifier. */
    public static final String VIEW_ID =
            "de.cau.cs.kieler.kiml.grana.views.analysisResults";

    /** the displayed html. */
    private String html;
    /** the html browser. */
    private Browser browser = null;

    /**
     * Finds the active analysis result view, if it exists.
     * 
     * @return the active analysis result view, or {@code null} if there is none
     */
    public static AnalysisResultViewPart findView() {
        IWorkbenchWindow activeWindow =
                PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        if (activeWindow != null) {
            IWorkbenchPage activePage = activeWindow.getActivePage();
            if (activePage != null) {
                return (AnalysisResultViewPart) activePage.findView(VIEW_ID);
            }
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public void createPartControl(final Composite parent) {
        try {
            browser = new Browser(parent, SWT.NONE);
            browser.setLayoutData(new GridData(GridData.FILL_BOTH));

        } catch (SWTError e) {
            System.out.println("Could not instantiate Browser: "
                    + e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setFocus() {
    }

    /**
     * Sets the analysis results.
     * 
     * @param analyses
     *            the analyses
     * @param results
     *            the results
     */
    public void setAnalysisResults(final List<AbstractInfoAnalysis> analyses,
            final Map<String, Object> results) {
        html = HtmlResultGenerator.generate(analyses, results);
        if (html != null && browser != null) {
            browser.setText(html);
        }
    }
}
