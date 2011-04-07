/*
 * generated by Xtext
 */
package de.cau.cs.kieler.kaom.text.formatting;

import org.eclipse.xtext.formatting.impl.FormattingConfig;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.util.Pair;

import de.cau.cs.kieler.core.annotations.formatting.AnnotationsFormatter;
import de.cau.cs.kieler.kaom.text.services.KaomGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 */
public class KaomFormatter extends AnnotationsFormatter {
	
    @Override
    protected void configureFormatting(FormattingConfig c) {
        KaomGrammarAccess f = (KaomGrammarAccess) getGrammarAccess();
        customConfigureFormatting(c, f);
    }

    /**
     * Method contains actual formatting instructions while GrammarAccess class maybe parameterized
     * allowing the reuse within other Formatters.
     * 
     * @param c
     *            FormattingConfig provided by caller
     * @param f
     *            GrammarAccess provided by caller
     */
    protected void customConfigureFormatting(FormattingConfig c, KaomGrammarAccess f) {
        super.customConfigureFormatting(c, f.getAnnotationsGrammarAccess());

		for(Pair<Keyword, Keyword> pair: f.findKeywordPairs("{", "}")) {
			c.setIndentation(pair.getFirst(), pair.getSecond());
			c.setLinewrap(1).after(pair.getFirst());
			c.setLinewrap(1).before(pair.getSecond());
			c.setLinewrap(1).after(pair.getSecond());
		}
		for(Keyword comma: f.findKeywords(",")) {
			c.setNoLinewrap().before(comma);
			c.setNoSpace().before(comma);
			c.setLinewrap().after(comma);
		}
//		c.setLinewrap(0, 1, 2).before(f.getSL_COMMENTRule());
//		c.setLinewrap(0, 1, 2).before(f.getML_COMMENTRule());
//		c.setLinewrap(0, 1, 1).after(f.getML_COMMENTRule());
	}
}
