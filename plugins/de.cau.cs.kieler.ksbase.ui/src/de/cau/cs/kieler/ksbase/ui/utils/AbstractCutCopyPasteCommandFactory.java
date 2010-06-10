/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2009 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.ksbase.ui.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.common.core.command.ICommand;
import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.diagram.ui.parts.DiagramEditor;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramGraphicalViewer;
import org.eclipse.gmf.runtime.diagram.ui.parts.IDiagramWorkbenchPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.ui.IEditorPart;
import org.osgi.framework.Bundle;

import de.cau.cs.kieler.core.model.transformation.ITransformationFramework;
import de.cau.cs.kieler.core.model.transformation.xtend.XtendTransformationFramework;
import de.cau.cs.kieler.core.model.util.ModelingUtil;
import de.cau.cs.kieler.core.ui.handler.ICutCopyPasteCommandFactory;
import de.cau.cs.kieler.ksbase.ui.handler.TransformationCommand;

/**
 * Creates the cut, copy and paste commands from ksbase.
 * 
 * @author soh
 */
public abstract class AbstractCutCopyPasteCommandFactory implements
        ICutCopyPasteCommandFactory {

    /** The transformation FRAMEWORK. */
    private static final ITransformationFramework FRAMEWORK = new XtendTransformationFramework();

    /** The path of the transformation file. */
    private String filePath = null;

    /** The last selection. */
    private List<EObject> lastSelection;

    /** The instance of the job. */
    private static WorkerJob jobInstance = null;

    /**
     * Build a new copy command.
     * 
     * @param part
     *            the editor
     * @param selection
     *            the selection
     * @return the command
     */
    public ICommand buildCopyCommand(final IDiagramWorkbenchPart part,
            final List<EObject> selection) {
        return buildCommand(part, selection, "Copy");
    }

    /**
     * Build a new cut command.
     * 
     * @param part
     *            the editor
     * @param selection
     *            the selection
     * @return the command
     */
    public ICommand buildCutCommand(final IDiagramWorkbenchPart part,
            final List<EObject> selection) {
        return buildCommand(part, selection, "Cut");
    }

    /**
     * Build a new paste command.
     * 
     * @param part
     *            the editor
     * @param selection
     *            the selection
     * @return the command
     */
    public ICommand buildPasteCommand(final IDiagramWorkbenchPart part,
            final List<EObject> selection) {
        return buildCommand(part, selection, "Paste");
    }

    /**
     * Getter for the transformation file. e.g.: /transformations/feature.ext
     * 
     * @return the file
     */
    protected abstract String getFile();

    /**
     * Getter for the bundle of the plugin where the file is located.
     * 
     * @return the bundle
     */
    protected abstract Bundle getBundle();

    /**
     * Build a command.
     * 
     * @param part
     *            the editor
     * @param selection
     *            the selection
     * @param label
     *            the label and name of the transformation
     * @return the command
     */
    private ICommand buildCommand(final IDiagramWorkbenchPart part,
            final List<EObject> selection, final String label) {
        lastSelection = selection;

        Bundle bundle = getBundle();
        InputStream inStream = null;
        StringBuffer contentBuffer = new StringBuffer();
        try {
            if (bundle != null) {
                URL urlPath = bundle.getEntry(getFile());
                // Parse transformation file to read transformations and
                // parameters now:
                if (urlPath != null) {
                    inStream = urlPath.openStream();
                    while (inStream.available() > 0) {
                        contentBuffer.append((char) inStream.read());

                    }
                }
            }
            // Write transformation file to .metadata
            IPath path = ResourcesPlugin.getPlugin().getStateLocation();
            // Transformation file name:
            path = path.append("feature").addFileExtension("ext");

            File file = new File(path.toOSString());
            if (file != null) {
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                    if (out != null) {
                        if (!file.exists()) {
                            if (!file.createNewFile()) {
                                System.out.println("Can't create file.");
                            }
                        }

                        out.write(contentBuffer.toString().getBytes());
                        out.flush();
                        out.close();
                    }
                } catch (FileNotFoundException fne) {
                } catch (SecurityException sece) {
                } finally {
                    if (out != null) {
                        out.close();
                    }
                }
                // Set delete on exit flag, so the files will be cleaned when
                // exiting
                // eclipse
                filePath = file.getAbsolutePath();
                file.deleteOnExit();
            }
        } catch (IOException e0) {
            e0.printStackTrace();
        }
        TransformationCommandWithAutoLayout result = null;
        if (part instanceof DiagramEditor) {
            if (jobInstance != null) {
                jobInstance.cancel();
            }
            DiagramEditor editor = (DiagramEditor) part;
            TransactionalEditingDomain transDomain = editor.getEditingDomain();

            result = new TransformationCommandWithAutoLayout(transDomain, label);

            initializeTransformationCommand(result, editor, selection, label,
                    filePath, getModel(), FRAMEWORK);
        }
        return result;
    }

    /**
     * Initialize the transformation command.
     * 
     * The implementing method is responsible for calling result.initialize()
     * 
     * @param result
     *            the command
     * @param editor
     *            the editor
     * @param selection
     *            the current selection
     * @param label
     *            the command label
     * @param filePathParam
     *            the file path
     * @param modelParam
     *            the model
     * @param framework
     *            the framework
     */
    protected abstract void initializeTransformationCommand(
            final TransformationCommand result, final DiagramEditor editor,
            final List<EObject> selection, final String label,
            final String filePathParam, final String modelParam,
            final ITransformationFramework framework);

    /**
     * Get the path to the model package. e.g.:
     * de.cau.cs.kieler.synccharts.SyncchartsPackage
     * 
     * @return the model package
     */
    protected abstract String getModel();

    /**
     * Perform actions after the operation has finished.
     * 
     * @param monitor
     *            a progress monitor
     */
    protected abstract void performPostOperationActions(
            final IProgressMonitor monitor);

    /**
     * This transformation command performs an auto layout some time after the
     * last transformation.
     * 
     * @author soh
     */
    private class TransformationCommandWithAutoLayout extends
            TransformationCommand {

        /** The label. */
        private String label;

        /** The delay. */
        private static final int DELAY = 500;

        /**
         * Creates a new Transformation command.
         * 
         * @param domain
         * @param labelParam
         */
        public TransformationCommandWithAutoLayout(
                final TransactionalEditingDomain domain, final String labelParam) {
            super(domain, labelParam, null);
            this.label = labelParam;
        }

        @Override
        protected CommandResult doExecuteWithResult(
                final IProgressMonitor monitor, final IAdaptable info)
                throws ExecutionException {
            CommandResult res = super.doExecuteWithResult(monitor, info);
            if (label.equalsIgnoreCase("paste")) {
                WorkerJob job = new WorkerJob();
                job.schedule(DELAY);

            }
            return res;
        }
    }

    /**
     * Thread for triggering an autolayout after some time.
     * 
     * @author soh
     */
    private class WorkerJob extends Job {

        /**
         * Creates a new AbstractCutCopyPasteCommandFactory.java.
         * 
         * @param name
         */
        public WorkerJob() {
            super("Autolayout");
            jobInstance = this;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected IStatus run(final IProgressMonitor monitor) {
            performPostOperationActions(monitor);
            return new Status(IStatus.OK,
                    "de.cau.cs.kieler.synccharts.diagram.custom", "Layout done");
        }
    }

    /**
     * Refresh the edit policies.
     * 
     * @param editorPart
     *            the editor
     */
    protected void refreshEditPolicies(final IEditorPart editorPart) {
        if (editorPart instanceof IDiagramWorkbenchPart) {
            IDiagramWorkbenchPart part = (IDiagramWorkbenchPart) editorPart;
            if (lastSelection != null) {
                for (EObject sel : lastSelection) {
                    refreshPolicy(sel);
                }
                // commit changes to view
                IDiagramGraphicalViewer graphViewer = part
                        .getDiagramGraphicalViewer();
                graphViewer.flush();
            }
        }
        lastSelection = null;
    }

    /**
     * Refresh the edit policy on the given element.
     * 
     * @param sel
     *            the element
     */
    private void refreshPolicy(final EObject sel) {
        EditPart editPart = ModelingUtil.getEditPart(sel);
        // get all registered edit parts in order to get transitions as well
        Collection<?> parts = editPart.getViewer().getEditPartRegistry()
                .values();

        // results list
        List<CanonicalEditPolicy> policies = new LinkedList<CanonicalEditPolicy>();
        // iterate over all parts
        for (Iterator<?> iter = parts.iterator(); iter.hasNext();) {
            Object obj = iter.next();
            if (obj instanceof EditPart) {
                Object model = ((EditPart) obj).getModel();
                if (model instanceof View) {
                    EObject eObject = ((View) model).getElement();

                    // get policy for the semantic element
                    List<?> editPolicies = CanonicalEditPolicy
                            .getRegisteredEditPolicies(eObject);
                    for (Iterator<?> it = editPolicies.iterator(); it.hasNext();) {
                        CanonicalEditPolicy nextEditPolicy = (CanonicalEditPolicy) it
                                .next();
                        policies.add(nextEditPolicy);

                    }
                }
            }
        }

        // refresh all policies at once to avoid concurrent modification
        // exception
        for (CanonicalEditPolicy policy : policies) {
            policy.refresh();
        }
    }
}
