/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.grana.text.serializer;

import com.google.inject.Inject;
import de.cau.cs.kieler.grana.text.services.GranaGrammarAccess;
import java.util.List;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.IGrammarAccess;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.AbstractElementAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.GroupAlias;
import org.eclipse.xtext.serializer.analysis.GrammarAlias.TokenAlias;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynNavigable;
import org.eclipse.xtext.serializer.analysis.ISyntacticSequencerPDAProvider.ISynTransition;
import org.eclipse.xtext.serializer.sequencer.AbstractSyntacticSequencer;

@SuppressWarnings("all")
public class GranaSyntacticSequencer extends AbstractSyntacticSequencer {

	protected GranaGrammarAccess grammarAccess;
	protected AbstractElementAlias match_ElkEdge___LeftCurlyBracketKeyword_7_0_RightCurlyBracketKeyword_7_4__q;
	protected AbstractElementAlias match_Grana_GlobalOutputsKeyword_1_0_q;
	protected AbstractElementAlias match_Grana_GlobalResourcesKeyword_0_0_q;
	
	@Inject
	protected void init(IGrammarAccess access) {
		grammarAccess = (GranaGrammarAccess) access;
		match_ElkEdge___LeftCurlyBracketKeyword_7_0_RightCurlyBracketKeyword_7_4__q = new GroupAlias(false, true, new TokenAlias(false, false, grammarAccess.getElkEdgeAccess().getLeftCurlyBracketKeyword_7_0()), new TokenAlias(false, false, grammarAccess.getElkEdgeAccess().getRightCurlyBracketKeyword_7_4()));
		match_Grana_GlobalOutputsKeyword_1_0_q = new TokenAlias(false, true, grammarAccess.getGranaAccess().getGlobalOutputsKeyword_1_0());
		match_Grana_GlobalResourcesKeyword_0_0_q = new TokenAlias(false, true, grammarAccess.getGranaAccess().getGlobalResourcesKeyword_0_0());
	}
	
	@Override
	protected String getUnassignedRuleCallToken(EObject semanticObject, RuleCall ruleCall, INode node) {
		return "";
	}
	
	
	@Override
	protected void emitUnassignedTokens(EObject semanticObject, ISynTransition transition, INode fromNode, INode toNode) {
		if (transition.getAmbiguousSyntaxes().isEmpty()) return;
		List<INode> transitionNodes = collectNodes(fromNode, toNode);
		for (AbstractElementAlias syntax : transition.getAmbiguousSyntaxes()) {
			List<INode> syntaxNodes = getNodesFor(transitionNodes, syntax);
			if (match_ElkEdge___LeftCurlyBracketKeyword_7_0_RightCurlyBracketKeyword_7_4__q.equals(syntax))
				emit_ElkEdge___LeftCurlyBracketKeyword_7_0_RightCurlyBracketKeyword_7_4__q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_Grana_GlobalOutputsKeyword_1_0_q.equals(syntax))
				emit_Grana_GlobalOutputsKeyword_1_0_q(semanticObject, getLastNavigableState(), syntaxNodes);
			else if (match_Grana_GlobalResourcesKeyword_0_0_q.equals(syntax))
				emit_Grana_GlobalResourcesKeyword_0_0_q(semanticObject, getLastNavigableState(), syntaxNodes);
			else acceptNodes(getLastNavigableState(), syntaxNodes);
		}
	}

	/**
	 * Ambiguous syntax:
	 *     ('{' '}')?
	 *
	 * This ambiguous syntax occurs at:
	 *     targets+=[ElkConnectableShape|QualifiedId] (ambiguity) (rule end)
	 */
	protected void emit_ElkEdge___LeftCurlyBracketKeyword_7_0_RightCurlyBracketKeyword_7_4__q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     'globalOutputs'?
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) 'globalResources'? (ambiguity) 'execute' execute+=[Job|ID]
	 *     (rule start) 'globalResources'? (ambiguity) 'execute' executeAll?='all'
	 *     (rule start) 'globalResources'? (ambiguity) 'execute' parallel?='parallel'
	 *     globalResources+=GlobalResourceRef (ambiguity) 'execute' execute+=[Job|ID]
	 *     globalResources+=GlobalResourceRef (ambiguity) 'execute' executeAll?='all'
	 *     globalResources+=GlobalResourceRef (ambiguity) 'execute' parallel?='parallel'
	 */
	protected void emit_Grana_GlobalOutputsKeyword_1_0_q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
	/**
	 * Ambiguous syntax:
	 *     'globalResources'?
	 *
	 * This ambiguous syntax occurs at:
	 *     (rule start) (ambiguity) 'globalOutputs' gloobalOutputs+=GlobalOutputRef
	 *     (rule start) (ambiguity) 'globalOutputs'? 'execute' execute+=[Job|ID]
	 *     (rule start) (ambiguity) 'globalOutputs'? 'execute' executeAll?='all'
	 *     (rule start) (ambiguity) 'globalOutputs'? 'execute' parallel?='parallel'
	 */
	protected void emit_Grana_GlobalResourcesKeyword_0_0_q(EObject semanticObject, ISynNavigable transition, List<INode> nodes) {
		acceptNodes(transition, nodes);
	}
	
}
