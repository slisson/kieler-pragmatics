/*
 * generated by Xtext
 */
package de.cau.cs.kieler.ksbase.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class FeatureDefinitionFormatter extends AbstractDeclarativeFormatter {
	
	@Override
	protected void configureFormatting(FormattingConfig c) {
		de.cau.cs.kieler.ksbase.services.FeatureDefinitionGrammarAccess f = (de.cau.cs.kieler.ksbase.services.FeatureDefinitionGrammarAccess) getGrammarAccess();

		//...
	}
}
