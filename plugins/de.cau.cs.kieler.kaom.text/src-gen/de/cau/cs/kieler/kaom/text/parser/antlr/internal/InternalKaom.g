/*
* generated by Xtext
*/
grammar InternalKaom;

options {
	superClass=AbstractInternalAntlrParser;
	backtrack=true;
	
}

@lexer::header {
package de.cau.cs.kieler.kaom.text.parser.antlr.internal;

// Hack: Use our own Lexer superclass by means of import. 
// Currently there is no other way to specify the superclass for the lexer.
import org.eclipse.xtext.parser.antlr.Lexer;
}

@parser::header {
package de.cau.cs.kieler.kaom.text.parser.antlr.internal; 

import org.eclipse.xtext.*;
import org.eclipse.xtext.parser.*;
import org.eclipse.xtext.parser.impl.*;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.parser.antlr.AbstractInternalAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;
import org.eclipse.xtext.parser.antlr.XtextTokenStream.HiddenTokens;
import org.eclipse.xtext.parser.antlr.AntlrDatatypeRuleToken;
import de.cau.cs.kieler.kaom.text.services.KaomGrammarAccess;

}

@parser::members {

/*
  This grammar contains a lot of empty actions to work around a bug in ANTLR.
  Otherwise the ANTLR tool will create synpreds that cannot be compiled in some rare cases.
*/
 
 	private KaomGrammarAccess grammarAccess;
 	
    public InternalKaomParser(TokenStream input, KaomGrammarAccess grammarAccess) {
        this(input);
        this.grammarAccess = grammarAccess;
        registerRules(grammarAccess.getGrammar());
    }
    
    @Override
    protected String getFirstRuleName() {
    	return "TopLevelEntity";	
   	}
   	
   	@Override
   	protected KaomGrammarAccess getGrammarAccess() {
   		return grammarAccess;
   	}
}

@rulecatch { 
    catch (RecognitionException re) { 
        recover(input,re); 
        appendSkippedTokens();
    } 
}




// Entry rule entryRuleTopLevelEntity
entryRuleTopLevelEntity returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTopLevelEntityRule()); }
	 iv_ruleTopLevelEntity=ruleTopLevelEntity 
	 { $current=$iv_ruleTopLevelEntity.current; } 
	 EOF 
;

// Rule TopLevelEntity
ruleTopLevelEntity returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
	{ 
	  /* */ 
	}
    {
        $current = forceCreateModelElement(
            grammarAccess.getTopLevelEntityAccess().getEntityAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getAnnotationsImportAnnotationParserRuleCall_1_0()); 
	    }
		lv_annotations_1_0=ruleImportAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_1_0, 
        		"ImportAnnotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*((
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getAnnotationsAnnotationParserRuleCall_2_0_0()); 
	    }
		lv_annotations_2_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_2_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_3='entity' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getTopLevelEntityAccess().getEntityKeyword_2_1());
    }
(
(
		lv_id_4_0=RULE_ID
		{
			newLeafNode(lv_id_4_0, grammarAccess.getTopLevelEntityAccess().getIdIDTerminalRuleCall_2_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTopLevelEntityRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"id",
        		lv_id_4_0, 
        		"ID");
	    }

)
)(
(
		lv_name_5_0=RULE_STRING
		{
			newLeafNode(lv_name_5_0, grammarAccess.getTopLevelEntityAccess().getNameSTRINGTerminalRuleCall_2_3_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getTopLevelEntityRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_5_0, 
        		"STRING");
	    }

)
)?((	otherlv_6='{' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getTopLevelEntityAccess().getLeftCurlyBracketKeyword_2_4_0_0());
    }
((
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getChildEntitiesEntityParserRuleCall_2_4_0_1_0_0()); 
	    }
		lv_childEntities_7_0=ruleEntity		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"childEntities",
        		lv_childEntities_7_0, 
        		"Entity");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getChildLinksLinkParserRuleCall_2_4_0_1_1_0()); 
	    }
		lv_childLinks_8_0=ruleLink		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"childLinks",
        		lv_childLinks_8_0, 
        		"Link");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getChildPortsPortParserRuleCall_2_4_0_1_2_0()); 
	    }
		lv_childPorts_9_0=rulePort		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"childPorts",
        		lv_childPorts_9_0, 
        		"Port");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getTopLevelEntityAccess().getChildRelationsRelationParserRuleCall_2_4_0_1_3_0()); 
	    }
		lv_childRelations_10_0=ruleRelation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTopLevelEntityRule());
	        }
       		add(
       			$current, 
       			"childRelations",
        		lv_childRelations_10_0, 
        		"Relation");
	        afterParserOrEnumRuleCall();
	    }

)
))*	otherlv_11='}' 
    {
    	newLeafNode(otherlv_11, grammarAccess.getTopLevelEntityAccess().getRightCurlyBracketKeyword_2_4_0_2());
    }
)
    |	otherlv_12=';' 
    {
    	newLeafNode(otherlv_12, grammarAccess.getTopLevelEntityAccess().getSemicolonKeyword_2_4_1());
    }
))?)
;





// Entry rule entryRuleEntity
entryRuleEntity returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getEntityRule()); }
	 iv_ruleEntity=ruleEntity 
	 { $current=$iv_ruleEntity.current; } 
	 EOF 
;

// Rule Entity
ruleEntity returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
	{ 
	  /* */ 
	}
    {
        $current = forceCreateModelElement(
            grammarAccess.getEntityAccess().getEntityAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getEntityAccess().getAnnotationsAnnotationParserRuleCall_1_0()); 
	    }
		lv_annotations_1_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEntityRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_1_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_2='entity' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getEntityAccess().getEntityKeyword_2());
    }
(
(
		lv_id_3_0=RULE_ID
		{
			newLeafNode(lv_id_3_0, grammarAccess.getEntityAccess().getIdIDTerminalRuleCall_3_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getEntityRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"id",
        		lv_id_3_0, 
        		"ID");
	    }

)
)(
(
		lv_name_4_0=RULE_STRING
		{
			newLeafNode(lv_name_4_0, grammarAccess.getEntityAccess().getNameSTRINGTerminalRuleCall_4_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getEntityRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_4_0, 
        		"STRING");
	    }

)
)?((	otherlv_5='{' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getEntityAccess().getLeftCurlyBracketKeyword_5_0_0());
    }
((
(
		{ 
	        newCompositeNode(grammarAccess.getEntityAccess().getChildEntitiesEntityParserRuleCall_5_0_1_0_0()); 
	    }
		lv_childEntities_6_0=ruleEntity		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEntityRule());
	        }
       		add(
       			$current, 
       			"childEntities",
        		lv_childEntities_6_0, 
        		"Entity");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getEntityAccess().getChildLinksLinkParserRuleCall_5_0_1_1_0()); 
	    }
		lv_childLinks_7_0=ruleLink		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEntityRule());
	        }
       		add(
       			$current, 
       			"childLinks",
        		lv_childLinks_7_0, 
        		"Link");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getEntityAccess().getChildPortsPortParserRuleCall_5_0_1_2_0()); 
	    }
		lv_childPorts_8_0=rulePort		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEntityRule());
	        }
       		add(
       			$current, 
       			"childPorts",
        		lv_childPorts_8_0, 
        		"Port");
	        afterParserOrEnumRuleCall();
	    }

)
)
    |(
(
		{ 
	        newCompositeNode(grammarAccess.getEntityAccess().getChildRelationsRelationParserRuleCall_5_0_1_3_0()); 
	    }
		lv_childRelations_9_0=ruleRelation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getEntityRule());
	        }
       		add(
       			$current, 
       			"childRelations",
        		lv_childRelations_9_0, 
        		"Relation");
	        afterParserOrEnumRuleCall();
	    }

)
))*	otherlv_10='}' 
    {
    	newLeafNode(otherlv_10, grammarAccess.getEntityAccess().getRightCurlyBracketKeyword_5_0_2());
    }
)
    |	otherlv_11=';' 
    {
    	newLeafNode(otherlv_11, grammarAccess.getEntityAccess().getSemicolonKeyword_5_1());
    }
))
;







// Entry rule entryRuleLink
entryRuleLink returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getLinkRule()); }
	 iv_ruleLink=ruleLink 
	 { $current=$iv_ruleLink.current; } 
	 EOF 
;

// Rule Link
ruleLink returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
(
		{ 
	        newCompositeNode(grammarAccess.getLinkAccess().getAnnotationsAnnotationParserRuleCall_0_0()); 
	    }
		lv_annotations_0_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getLinkRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_0_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_1='link' 
    {
    	newLeafNode(otherlv_1, grammarAccess.getLinkAccess().getLinkKeyword_1());
    }
(
(
		lv_name_2_0=RULE_STRING
		{
			newLeafNode(lv_name_2_0, grammarAccess.getLinkAccess().getNameSTRINGTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getLinkRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_2_0, 
        		"STRING");
	    }

)
)?(
(
		{ 
		  /* */ 
		}
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getLinkRule());
	        }
        }
	otherlv_3=RULE_ID
	{
		newLeafNode(otherlv_3, grammarAccess.getLinkAccess().getSourceLinkableCrossReference_3_0()); 
	}

)
)	otherlv_4='to' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getLinkAccess().getToKeyword_4());
    }
(
(
		{ 
		  /* */ 
		}
		{
			if ($current==null) {
	            $current = createModelElement(grammarAccess.getLinkRule());
	        }
        }
	otherlv_5=RULE_ID
	{
		newLeafNode(otherlv_5, grammarAccess.getLinkAccess().getTargetLinkableCrossReference_5_0()); 
	}

)
)	otherlv_6=';' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getLinkAccess().getSemicolonKeyword_6());
    }
)
;





// Entry rule entryRulePort
entryRulePort returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getPortRule()); }
	 iv_rulePort=rulePort 
	 { $current=$iv_rulePort.current; } 
	 EOF 
;

// Rule Port
rulePort returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
	{ 
	  /* */ 
	}
    {
        $current = forceCreateModelElement(
            grammarAccess.getPortAccess().getPortAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getPortAccess().getAnnotationsAnnotationParserRuleCall_1_0()); 
	    }
		lv_annotations_1_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getPortRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_1_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_2='port' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getPortAccess().getPortKeyword_2());
    }
(
(
		lv_id_3_0=RULE_ID
		{
			newLeafNode(lv_id_3_0, grammarAccess.getPortAccess().getIdIDTerminalRuleCall_3_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getPortRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"id",
        		lv_id_3_0, 
        		"ID");
	    }

)
)(
(
		lv_name_4_0=RULE_STRING
		{
			newLeafNode(lv_name_4_0, grammarAccess.getPortAccess().getNameSTRINGTerminalRuleCall_4_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getPortRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_4_0, 
        		"STRING");
	    }

)
)?	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getPortAccess().getSemicolonKeyword_5());
    }
)
;





// Entry rule entryRuleRelation
entryRuleRelation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getRelationRule()); }
	 iv_ruleRelation=ruleRelation 
	 { $current=$iv_ruleRelation.current; } 
	 EOF 
;

// Rule Relation
ruleRelation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
((
	{ 
	  /* */ 
	}
    {
        $current = forceCreateModelElement(
            grammarAccess.getRelationAccess().getRelationAction_0(),
            $current);
    }
)(
(
		{ 
	        newCompositeNode(grammarAccess.getRelationAccess().getAnnotationsAnnotationParserRuleCall_1_0()); 
	    }
		lv_annotations_1_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getRelationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_1_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_2='relation' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getRelationAccess().getRelationKeyword_2());
    }
(
(
		lv_id_3_0=RULE_ID
		{
			newLeafNode(lv_id_3_0, grammarAccess.getRelationAccess().getIdIDTerminalRuleCall_3_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getRelationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"id",
        		lv_id_3_0, 
        		"ID");
	    }

)
)(
(
		lv_name_4_0=RULE_STRING
		{
			newLeafNode(lv_name_4_0, grammarAccess.getRelationAccess().getNameSTRINGTerminalRuleCall_4_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getRelationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"name",
        		lv_name_4_0, 
        		"STRING");
	    }

)
)?	otherlv_5=';' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getRelationAccess().getSemicolonKeyword_5());
    }
)
;





// Entry rule entryRuleAnnotation
entryRuleAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getAnnotationRule()); }
	 iv_ruleAnnotation=ruleAnnotation 
	 { $current=$iv_ruleAnnotation.current; } 
	 EOF 
;

// Rule Annotation
ruleAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getCommentAnnotationParserRuleCall_0()); 
    }
    this_CommentAnnotation_0=ruleCommentAnnotation
    { 
        $current = $this_CommentAnnotation_0.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getTagAnnotationParserRuleCall_1()); 
    }
    this_TagAnnotation_1=ruleTagAnnotation
    { 
        $current = $this_TagAnnotation_1.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getKeyStringValueAnnotationParserRuleCall_2()); 
    }
    this_KeyStringValueAnnotation_2=ruleKeyStringValueAnnotation
    { 
        $current = $this_KeyStringValueAnnotation_2.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getTypedKeyStringValueAnnotationParserRuleCall_3()); 
    }
    this_TypedKeyStringValueAnnotation_3=ruleTypedKeyStringValueAnnotation
    { 
        $current = $this_TypedKeyStringValueAnnotation_3.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getKeyBooleanValueAnnotationParserRuleCall_4()); 
    }
    this_KeyBooleanValueAnnotation_4=ruleKeyBooleanValueAnnotation
    { 
        $current = $this_KeyBooleanValueAnnotation_4.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getKeyIntValueAnnotationParserRuleCall_5()); 
    }
    this_KeyIntValueAnnotation_5=ruleKeyIntValueAnnotation
    { 
        $current = $this_KeyIntValueAnnotation_5.current; 
        afterParserOrEnumRuleCall();
    }

    |
	{ 
	  /* */ 
	}
    { 
        newCompositeNode(grammarAccess.getAnnotationAccess().getKeyFloatValueAnnotationParserRuleCall_6()); 
    }
    this_KeyFloatValueAnnotation_6=ruleKeyFloatValueAnnotation
    { 
        $current = $this_KeyFloatValueAnnotation_6.current; 
        afterParserOrEnumRuleCall();
    }
)
;







// Entry rule entryRuleCommentAnnotation
entryRuleCommentAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getCommentAnnotationRule()); }
	 iv_ruleCommentAnnotation=ruleCommentAnnotation 
	 { $current=$iv_ruleCommentAnnotation.current; } 
	 EOF 
;

// Rule CommentAnnotation
ruleCommentAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(
(
		lv_value_0_0=RULE_COMMENT_ANNOTATION
		{
			newLeafNode(lv_value_0_0, grammarAccess.getCommentAnnotationAccess().getValueCOMMENT_ANNOTATIONTerminalRuleCall_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getCommentAnnotationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_0_0, 
        		"COMMENT_ANNOTATION");
	    }

)
)
;





// Entry rule entryRuleTagAnnotation
entryRuleTagAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTagAnnotationRule()); }
	 iv_ruleTagAnnotation=ruleTagAnnotation 
	 { $current=$iv_ruleTagAnnotation.current; } 
	 EOF 
;

// Rule TagAnnotation
ruleTagAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getTagAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTagAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTagAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_2='(' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getTagAnnotationAccess().getLeftParenthesisKeyword_2_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTagAnnotationAccess().getAnnotationsAnnotationParserRuleCall_2_1_0()); 
	    }
		lv_annotations_3_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTagAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_3_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_4=')' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getTagAnnotationAccess().getRightParenthesisKeyword_2_2());
    }
)?)
;





// Entry rule entryRuleKeyStringValueAnnotation
entryRuleKeyStringValueAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getKeyStringValueAnnotationRule()); }
	 iv_ruleKeyStringValueAnnotation=ruleKeyStringValueAnnotation 
	 { $current=$iv_ruleKeyStringValueAnnotation.current; } 
	 EOF 
;

// Rule KeyStringValueAnnotation
ruleKeyStringValueAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getKeyStringValueAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyStringValueAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyStringValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyStringValueAnnotationAccess().getValueEStringParserRuleCall_2_0()); 
	    }
		lv_value_2_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyStringValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_3='(' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getKeyStringValueAnnotationAccess().getLeftParenthesisKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyStringValueAnnotationAccess().getAnnotationsAnnotationParserRuleCall_3_1_0()); 
	    }
		lv_annotations_4_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyStringValueAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_4_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getKeyStringValueAnnotationAccess().getRightParenthesisKeyword_3_2());
    }
)?)
;





// Entry rule entryRuleTypedKeyStringValueAnnotation
entryRuleTypedKeyStringValueAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getTypedKeyStringValueAnnotationRule()); }
	 iv_ruleTypedKeyStringValueAnnotation=ruleTypedKeyStringValueAnnotation 
	 { $current=$iv_ruleTypedKeyStringValueAnnotation.current; } 
	 EOF 
;

// Rule TypedKeyStringValueAnnotation
ruleTypedKeyStringValueAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getTypedKeyStringValueAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedKeyStringValueAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedKeyStringValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_2='[' 
    {
    	newLeafNode(otherlv_2, grammarAccess.getTypedKeyStringValueAnnotationAccess().getLeftSquareBracketKeyword_2());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedKeyStringValueAnnotationAccess().getTypeExtendedIDParserRuleCall_3_0()); 
	    }
		lv_type_3_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedKeyStringValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"type",
        		lv_type_3_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)	otherlv_4=']' 
    {
    	newLeafNode(otherlv_4, grammarAccess.getTypedKeyStringValueAnnotationAccess().getRightSquareBracketKeyword_4());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedKeyStringValueAnnotationAccess().getValueEStringParserRuleCall_5_0()); 
	    }
		lv_value_5_0=ruleEString		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedKeyStringValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"value",
        		lv_value_5_0, 
        		"EString");
	        afterParserOrEnumRuleCall();
	    }

)
)(	otherlv_6='(' 
    {
    	newLeafNode(otherlv_6, grammarAccess.getTypedKeyStringValueAnnotationAccess().getLeftParenthesisKeyword_6_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getTypedKeyStringValueAnnotationAccess().getAnnotationsAnnotationParserRuleCall_6_1_0()); 
	    }
		lv_annotations_7_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getTypedKeyStringValueAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_7_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_8=')' 
    {
    	newLeafNode(otherlv_8, grammarAccess.getTypedKeyStringValueAnnotationAccess().getRightParenthesisKeyword_6_2());
    }
)?)
;





// Entry rule entryRuleKeyBooleanValueAnnotation
entryRuleKeyBooleanValueAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getKeyBooleanValueAnnotationRule()); }
	 iv_ruleKeyBooleanValueAnnotation=ruleKeyBooleanValueAnnotation 
	 { $current=$iv_ruleKeyBooleanValueAnnotation.current; } 
	 EOF 
;

// Rule KeyBooleanValueAnnotation
ruleKeyBooleanValueAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getKeyBooleanValueAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyBooleanValueAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyBooleanValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_value_2_0=RULE_BOOLEAN
		{
			newLeafNode(lv_value_2_0, grammarAccess.getKeyBooleanValueAnnotationAccess().getValueBOOLEANTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getKeyBooleanValueAnnotationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"BOOLEAN");
	    }

)
)(	otherlv_3='(' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getKeyBooleanValueAnnotationAccess().getLeftParenthesisKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyBooleanValueAnnotationAccess().getAnnotationsAnnotationParserRuleCall_3_1_0()); 
	    }
		lv_annotations_4_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyBooleanValueAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_4_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getKeyBooleanValueAnnotationAccess().getRightParenthesisKeyword_3_2());
    }
)?)
;





// Entry rule entryRuleKeyIntValueAnnotation
entryRuleKeyIntValueAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getKeyIntValueAnnotationRule()); }
	 iv_ruleKeyIntValueAnnotation=ruleKeyIntValueAnnotation 
	 { $current=$iv_ruleKeyIntValueAnnotation.current; } 
	 EOF 
;

// Rule KeyIntValueAnnotation
ruleKeyIntValueAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getKeyIntValueAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyIntValueAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyIntValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_value_2_0=RULE_INT
		{
			newLeafNode(lv_value_2_0, grammarAccess.getKeyIntValueAnnotationAccess().getValueINTTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getKeyIntValueAnnotationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"INT");
	    }

)
)(	otherlv_3='(' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getKeyIntValueAnnotationAccess().getLeftParenthesisKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyIntValueAnnotationAccess().getAnnotationsAnnotationParserRuleCall_3_1_0()); 
	    }
		lv_annotations_4_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyIntValueAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_4_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getKeyIntValueAnnotationAccess().getRightParenthesisKeyword_3_2());
    }
)?)
;





// Entry rule entryRuleKeyFloatValueAnnotation
entryRuleKeyFloatValueAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getKeyFloatValueAnnotationRule()); }
	 iv_ruleKeyFloatValueAnnotation=ruleKeyFloatValueAnnotation 
	 { $current=$iv_ruleKeyFloatValueAnnotation.current; } 
	 EOF 
;

// Rule KeyFloatValueAnnotation
ruleKeyFloatValueAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='@' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getKeyFloatValueAnnotationAccess().getCommercialAtKeyword_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyFloatValueAnnotationAccess().getNameExtendedIDParserRuleCall_1_0()); 
	    }
		lv_name_1_0=ruleExtendedID		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyFloatValueAnnotationRule());
	        }
       		set(
       			$current, 
       			"name",
        		lv_name_1_0, 
        		"ExtendedID");
	        afterParserOrEnumRuleCall();
	    }

)
)(
(
		lv_value_2_0=RULE_FLOAT
		{
			newLeafNode(lv_value_2_0, grammarAccess.getKeyFloatValueAnnotationAccess().getValueFLOATTerminalRuleCall_2_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getKeyFloatValueAnnotationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"value",
        		lv_value_2_0, 
        		"FLOAT");
	    }

)
)(	otherlv_3='(' 
    {
    	newLeafNode(otherlv_3, grammarAccess.getKeyFloatValueAnnotationAccess().getLeftParenthesisKeyword_3_0());
    }
(
(
		{ 
	        newCompositeNode(grammarAccess.getKeyFloatValueAnnotationAccess().getAnnotationsAnnotationParserRuleCall_3_1_0()); 
	    }
		lv_annotations_4_0=ruleAnnotation		{
	        if ($current==null) {
	            $current = createModelElementForParent(grammarAccess.getKeyFloatValueAnnotationRule());
	        }
       		add(
       			$current, 
       			"annotations",
        		lv_annotations_4_0, 
        		"Annotation");
	        afterParserOrEnumRuleCall();
	    }

)
)*	otherlv_5=')' 
    {
    	newLeafNode(otherlv_5, grammarAccess.getKeyFloatValueAnnotationAccess().getRightParenthesisKeyword_3_2());
    }
)?)
;





// Entry rule entryRuleImportAnnotation
entryRuleImportAnnotation returns [EObject current=null] 
	:
	{ newCompositeNode(grammarAccess.getImportAnnotationRule()); }
	 iv_ruleImportAnnotation=ruleImportAnnotation 
	 { $current=$iv_ruleImportAnnotation.current; } 
	 EOF 
;

// Rule ImportAnnotation
ruleImportAnnotation returns [EObject current=null] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(	otherlv_0='import' 
    {
    	newLeafNode(otherlv_0, grammarAccess.getImportAnnotationAccess().getImportKeyword_0());
    }
(
(
		lv_importURI_1_0=RULE_STRING
		{
			newLeafNode(lv_importURI_1_0, grammarAccess.getImportAnnotationAccess().getImportURISTRINGTerminalRuleCall_1_0()); 
		}
		{
	        if ($current==null) {
	            $current = createModelElement(grammarAccess.getImportAnnotationRule());
	        }
       		setWithLastConsumed(
       			$current, 
       			"importURI",
        		lv_importURI_1_0, 
        		"STRING");
	    }

)
))
;





// Entry rule entryRuleEString
entryRuleEString returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getEStringRule()); } 
	 iv_ruleEString=ruleEString 
	 { $current=$iv_ruleEString.current.getText(); }  
	 EOF 
;

// Rule EString
ruleEString returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(    this_STRING_0=RULE_STRING    {
		$current.merge(this_STRING_0);
    }

    { 
    newLeafNode(this_STRING_0, grammarAccess.getEStringAccess().getSTRINGTerminalRuleCall_0()); 
    }

    |    this_ID_1=RULE_ID    {
		$current.merge(this_ID_1);
    }

    { 
    newLeafNode(this_ID_1, grammarAccess.getEStringAccess().getIDTerminalRuleCall_1()); 
    }
)
    ;





// Entry rule entryRuleExtendedID
entryRuleExtendedID returns [String current=null] 
	:
	{ newCompositeNode(grammarAccess.getExtendedIDRule()); } 
	 iv_ruleExtendedID=ruleExtendedID 
	 { $current=$iv_ruleExtendedID.current.getText(); }  
	 EOF 
;

// Rule ExtendedID
ruleExtendedID returns [AntlrDatatypeRuleToken current=new AntlrDatatypeRuleToken()] 
    @init { enterRule(); 
    }
    @after { leaveRule(); }:
(    this_ID_0=RULE_ID    {
		$current.merge(this_ID_0);
    }

    { 
    newLeafNode(this_ID_0, grammarAccess.getExtendedIDAccess().getIDTerminalRuleCall_0()); 
    }
(
	kw='.' 
    {
        $current.merge(kw);
        newLeafNode(kw, grammarAccess.getExtendedIDAccess().getFullStopKeyword_1_0()); 
    }
    this_ID_2=RULE_ID    {
		$current.merge(this_ID_2);
    }

    { 
    newLeafNode(this_ID_2, grammarAccess.getExtendedIDAccess().getIDTerminalRuleCall_1_1()); 
    }
)*)
    ;





RULE_COMMENT_ANNOTATION : '/**' ( options {greedy=false;} : . )*'*/';

RULE_ML_COMMENT : '/*' ~('*') ( options {greedy=false;} : . )*'*/';

RULE_INT : '-'? ('0'..'9')+;

RULE_FLOAT : ('-'? ('0'..'9')+ '.' ('0'..'9')* (('e'|'E') ('+'|'-')? ('0'..'9')+)? 'f'?|'-'? ('0'..'9')+ 'f');

RULE_BOOLEAN : ('true'|'false');

RULE_STRING : '"' ('\\' ('b'|'t'|'n'|'f'|'r'|'"'|'\''|'\\')|~(('\\'|'"')))* '"';

RULE_ID : '^'? ('a'..'z'|'A'..'Z'|'_') ('a'..'z'|'A'..'Z'|'_'|'0'..'9')*;

RULE_SL_COMMENT : '//' ~(('\n'|'\r'))* ('\r'? '\n')?;

RULE_WS : (' '|'\t'|'\r'|'\n')+;

RULE_ANY_OTHER : .;


