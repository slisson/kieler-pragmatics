/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2011 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.core.annotations.ui.properties;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.statushandlers.StatusManager;

import de.cau.cs.kieler.core.annotations.Annotatable;
import de.cau.cs.kieler.core.annotations.Annotation;
import de.cau.cs.kieler.core.annotations.ui.AnnotationsUiPlugin;

/**
 * An action for creation of new annotations.
 *
 * @author msp
 */
public class AddAnnotationAction extends Action {

    /** enumeration of available action types. */
    public enum AddHow {
        /** add as top-level annotation. */
        TOP_LEVEL,
        /** add as sub-annotation. */
        SUB_ANNOT;
    }
    
    /** image for adding an annotation to the selected element. */
    private static final ImageDescriptor ADD_IMAGE = AnnotationsUiPlugin.getImageDescriptor(
            "icons/menu16/add_annot.gif");
    /** image for adding an annotation to the selected element. */
    private static final ImageDescriptor ADD_SUB_IMAGE = AnnotationsUiPlugin.getImageDescriptor(
            "icons/menu16/add_sub_annot.gif");
    
    /** how to add the new annotation. */
    private AddHow how;
    /** the property section for annotations. */
    private AnnotationsPropertySection propertySection;
    
    /**
     * Creates an action for adding annotations.
     * 
     * @param thepropertySection the property section for annotations
     * @param thehow whether to add annotations on top level or as sub-annotations
     */
    public AddAnnotationAction(final AnnotationsPropertySection thepropertySection,
            final AddHow thehow) {
        switch (thehow) {
        case TOP_LEVEL:
            setText("Add Top-Level Annotation");
            setImageDescriptor(ADD_SUB_IMAGE);
            break;
        case SUB_ANNOT:
            setText("Add Sub-Annotation");
            setImageDescriptor(ADD_IMAGE);
            break;
        }
        this.how = thehow;
        this.propertySection = thepropertySection;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Shell shell = propertySection.getPart().getSite().getShell();
        // offer a dialog to select an annotation type and a name
        NewAnnotationDialog annotationDialog = new NewAnnotationDialog(shell);
        if (annotationDialog.open() == NewAnnotationDialog.OK) {
            final Annotation annotation = annotationDialog.createAnnotation();
            TransactionalEditingDomain editingDomain = propertySection.getEditingDomain();
            if (editingDomain != null) {
                editingDomain.getCommandStack().execute(new RecordingCommand(editingDomain,
                        "Add Annotation") {
                    protected void doExecute() {
                        Annotatable annotatable = null;
                        switch (how) {
                        case TOP_LEVEL:
                            annotatable = propertySection.getAnnotatable();
                            break;
                        case SUB_ANNOT:
                            annotatable = propertySection.getTableSelection();
                            if (annotatable == null) {
                                annotatable = propertySection.getAnnotatable();
                            }
                            break;
                        }
                        if (annotatable != null) {
                            annotatable.getAnnotations().add(annotation);
                        }
                    }
                });
                propertySection.refresh();
            } else {
                IStatus status = new Status(IStatus.ERROR, AnnotationsUiPlugin.PLUGIN_ID,
                        "No transactional editing domain is availabe for the current selection.");
                StatusManager.getManager().handle(status, StatusManager.SHOW);
            }
        }
    }
    
}
