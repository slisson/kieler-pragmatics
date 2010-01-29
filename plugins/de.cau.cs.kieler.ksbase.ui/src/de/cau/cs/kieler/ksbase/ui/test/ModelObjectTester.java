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
 * 
 *****************************************************************************/
package de.cau.cs.kieler.ksbase.ui.test;

import java.util.List;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.PlatformUI;

import de.cau.cs.kieler.core.model.transformation.ITransformationFramework;
import de.cau.cs.kieler.core.model.util.ModelingUtil;
import de.cau.cs.kieler.ksbase.core.EditorTransformationSettings;
import de.cau.cs.kieler.ksbase.core.KSBasETransformation;
import de.cau.cs.kieler.ksbase.core.TransformationManager;

/**
 * A property tester which checks if a selected diagram object matches a given model element.
 * 
 * @author mim
 * 
 * @kieler.rating 2009-12-15 proposed yellow
 */
public class ModelObjectTester extends PropertyTester {

    /**
     * Test method called by the eclipse menu framework when checking for menu visibility. This is
     * kind of a hack, because we are ignoring the given items and using the PlatformUI class to get
     * the current selection object. This results in multiple calls for all selected object but is
     * the only good way to do this.
     * 
     * @param receiver
     *            The receiver object
     * @param property
     *            The property to check
     * @param args
     *            The arguments, in this case this has to be the array[Editor,Transformation]
     * @param expectedValue
     *            The expected value
     * @return True if all selected objects are matching to the current selection.
     */
    public boolean test(final Object receiver, final String property, final Object[] args,
            final Object expectedValue) {
        assert (args.length == 2);
        assert (args[0] instanceof String);
        assert (args[1] instanceof String);
        EditorTransformationSettings editor = TransformationManager.INSTANCE
                .getEditorById((String) args[0]);
        //First, check the editorID, so we do not execute transformations from other editors.
        if (!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor()
                .getEditorSite().getId().equals(editor.getEditorId())) {
            return false;
        }
        if (editor != null) {
            KSBasETransformation t = editor.getTransformationById((String) args[1]);
            if (t != null) {
                // Convert selection to model elements:
                List<EObject> modelElements = ModelingUtil.getModelElementsFromSelection();
                // First we will evaluate the validation transformation
                // This is a fast operation, test took between 0ms and 30ms (on context switch) , so
                // it can be assumed as 'fast enough'.
                // But the actual time depends on the transformation to be executed here, so better
                // use simple & fast ones :)
                String validation = t.getValidation();
                if (validation != null && validation.length() > 0) {
                    for (String valid : validation.split(",")) {
                        if (!evaluateTransformation(editor, valid, modelElements
                                .toArray(new Object[modelElements.size()]), true)) {
                            return false;
                        }
                    }
                }
                if (!evaluateTransformation(editor, t.getTransformation(), t.getParameters(), false)) {
                    // Could the transformation be executed?
                    return false;
                }

                return true;
            }
        }
        return false;
    }

    /**
     * Evaluates the given transformation.
     * 
     * @param editor
     *            The editor to use
     * @param transformation
     *            The transformation to validate
     * @param execute
     *            Should the transformation actually be executed?
     * @return True If the transformation could be initialized and, if allowed, the transformation
     *         returned true.
     */
    private boolean evaluateTransformation(final EditorTransformationSettings editor,
            final String transformation, final Object parameter, final boolean execute) {
        Boolean result = false;
        ITransformationFramework framework = editor.getFramework();
        if (parameter instanceof String[]) {
            if (!framework.setParameters((String[]) parameter)) {
                // Parameters could not be matched, so instantly return false
                return false;
            }
        } else if (parameter instanceof Object[]) {
            framework.setParameters((Object[]) parameter);
        }
        if (!framework.initializeTransformation(editor.getTransformationFile(), transformation,
                editor.getModelPackageClass())) {
            return false;
        }
        if (execute) {
            Object res = framework.executeTransformation();
            if (result instanceof Boolean) {
                result = (Boolean) res;
            } else {
                result = false;
            }
        } else {
            // we do not want to actually execute the transformation, so successfully initialized
            // the
            // transformation and we will return true here
            return true;
        }
        return result;
    }

}
