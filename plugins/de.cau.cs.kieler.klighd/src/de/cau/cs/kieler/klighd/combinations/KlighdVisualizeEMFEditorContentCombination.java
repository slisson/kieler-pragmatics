/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse Rich Client
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
package de.cau.cs.kieler.klighd.combinations;

import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.edit.domain.IEditingDomainProvider;

import de.cau.cs.kieler.core.kivi.AbstractCombination;
import de.cau.cs.kieler.core.model.triggers.PartTrigger;
import de.cau.cs.kieler.core.model.triggers.PartTrigger.EditorState;
import de.cau.cs.kieler.core.model.triggers.PartTrigger.EventType;
import de.cau.cs.kieler.core.model.triggers.SelectionTrigger.SelectionState;
import de.cau.cs.kieler.klighd.effects.KlighdCloseDiagramEffect;
import de.cau.cs.kieler.klighd.effects.KlighdDiagramEffect;

/**
 * This combination in is charge of visualizing EMF tree editors' content graphically.
 * @author chsch
 */
public class KlighdVisualizeEMFEditorContentCombination extends AbstractCombination {
    
    /**
     * The execute method revealed and invoked by KIVi. <br>
     * <br>
     * This combination might cause some trouble with specialized ones addressing the view synthesis
     * of concrete model elements, such as the one in the Ecore visualization example.<br>
     * Due to this it is still registered in the plugin.xml, but its activity flag is set to false
     * at the moment.
     * 
     * @param es
     *            A {@link EditorState} carrying information on the last workbench part change.
     * @param ss
     *            A {@link SelectionState} carrying information on the last selection change.
     */
    public void execute(final EditorState es, final SelectionState ss) {
        if (es.getPart() == null || !(es.getPart() instanceof IEditingDomainProvider)) { 
            return;
        }
        
        IPath inputPath = es.getProperty(PartTrigger.EDITOR_INPUT_PATH);
        if (inputPath == null) { // Fix of KIELER-2135
            return;
        }
        
        String id = inputPath.toPortableString().replace(":", "");
        // the replacement is needed since the secondary view ids seem to be required
        //  to be free of ':', which will be violated on windows determining them this way. 

        if (this.latestState() == es) {            
            // in case the combination is fired by an editor change ...
            
            if (es.getEventType() == EventType.ACTIVE_EDITOR_CLOSED) {
                this.schedule(new KlighdCloseDiagramEffect(id));
                
            } else {            
                IEditingDomainProvider editor = (IEditingDomainProvider) es.getEditorPart();
                // null check is performed above!
                List<Resource> resources = editor.getEditingDomain().getResourceSet()
                        .getResources();
                if (!resources.isEmpty() && !resources.get(0).getContents().isEmpty()) {

                    this.schedule(new KlighdDiagramEffect(id, inputPath.lastSegment(), EcoreUtil
                            .getRootContainer(resources.get(0).getContents().get(0)), es
                            .getEditorPart()));
                }
            }
        } else {
            // otherwise the selection is examined ...            
            if (!ss.getSelectedObjects().isEmpty()
                    && (ss.getSelectedObjects().get(0) instanceof Resource || ss
                            .getSelectedObjects().get(0) instanceof EObject)) {

                Object selected = ss.getSelectedObjects().get(0);
                if (selected instanceof Resource && !((Resource) selected).getContents().isEmpty()) {
                    this.schedule(new KlighdDiagramEffect(id, inputPath.lastSegment(),
                            ((Resource) selected).getContents().get(0), es.getEditorPart()));
                    return;
                }
                if (selected instanceof EObject) {
                    this.schedule(
                            new KlighdDiagramEffect(
                            id, inputPath.lastSegment(), EcoreUtil
                            .getRootContainer((EObject) selected), es.getEditorPart()));
                    return;
                }
            }
        }
    }

}
