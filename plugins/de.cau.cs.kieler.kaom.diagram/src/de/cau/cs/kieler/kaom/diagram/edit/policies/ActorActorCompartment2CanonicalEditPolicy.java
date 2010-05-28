package de.cau.cs.kieler.kaom.diagram.edit.policies;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.eclipse.gmf.runtime.diagram.ui.editpolicies.CanonicalEditPolicy;
import org.eclipse.gmf.runtime.notation.View;

import de.cau.cs.kieler.kaom.KaomPackage;
import de.cau.cs.kieler.kaom.diagram.edit.parts.Actor2EditPart;
import de.cau.cs.kieler.kaom.diagram.edit.parts.Relation2EditPart;
import de.cau.cs.kieler.kaom.diagram.edit.parts.State3EditPart;
import de.cau.cs.kieler.kaom.diagram.part.KaomDiagramUpdater;
import de.cau.cs.kieler.kaom.diagram.part.KaomNodeDescriptor;
import de.cau.cs.kieler.kaom.diagram.part.KaomVisualIDRegistry;

/**
 * @generated
 */
public class ActorActorCompartment2CanonicalEditPolicy extends
		CanonicalEditPolicy {

	/**
	 * @generated
	 */
	Set myFeaturesToSynchronize;

	/**
	 * @generated
	 */
	protected List getSemanticChildrenList() {
		View viewObject = (View) getHost().getModel();
		List result = new LinkedList();
		for (Iterator it = KaomDiagramUpdater
				.getActorActorCompartment_7002SemanticChildren(viewObject)
				.iterator(); it.hasNext();) {
			result.add(((KaomNodeDescriptor) it.next()).getModelElement());
		}
		return result;
	}

	/**
	 * @generated
	 */
	protected boolean isOrphaned(Collection semanticChildren, final View view) {
		int visualID = KaomVisualIDRegistry.getVisualID(view);
		switch (visualID) {
		case Actor2EditPart.VISUAL_ID:
		case State3EditPart.VISUAL_ID:
		case Relation2EditPart.VISUAL_ID:
			if (!semanticChildren.contains(view.getElement())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @generated
	 */
	protected String getDefaultFactoryHint() {
		return null;
	}

	/**
	 * @generated
	 */
	protected Set getFeaturesToSynchronize() {
		if (myFeaturesToSynchronize == null) {
			myFeaturesToSynchronize = new HashSet();
			myFeaturesToSynchronize.add(KaomPackage.eINSTANCE
					.getEntity_ChildEntities());
			myFeaturesToSynchronize.add(KaomPackage.eINSTANCE
					.getEntity_ChildRelations());
		}
		return myFeaturesToSynchronize;
	}

}
