/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.config.text.parser.antlr;

import com.google.inject.Inject;
import de.cau.cs.kieler.config.text.parser.antlr.internal.InternalLayoutConfigParser;
import de.cau.cs.kieler.config.text.services.LayoutConfigGrammarAccess;
import org.eclipse.xtext.parser.antlr.AbstractAntlrParser;
import org.eclipse.xtext.parser.antlr.XtextTokenStream;

public class LayoutConfigParser extends AbstractAntlrParser {

	@Inject
	private LayoutConfigGrammarAccess grammarAccess;

	@Override
	protected void setInitialHiddenTokens(XtextTokenStream tokenStream) {
		tokenStream.setInitialHiddenTokens("RULE_WS", "RULE_ML_COMMENT", "RULE_SL_COMMENT");
	}
	

	@Override
	protected InternalLayoutConfigParser createParser(XtextTokenStream stream) {
		return new InternalLayoutConfigParser(stream, getGrammarAccess());
	}

	@Override 
	protected String getDefaultRuleName() {
		return "RootNode";
	}

	public LayoutConfigGrammarAccess getGrammarAccess() {
		return this.grammarAccess;
	}

	public void setGrammarAccess(LayoutConfigGrammarAccess grammarAccess) {
		this.grammarAccess = grammarAccess;
	}
}
