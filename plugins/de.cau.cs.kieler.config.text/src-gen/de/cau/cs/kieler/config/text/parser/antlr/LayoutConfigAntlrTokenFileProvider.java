/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.config.text.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class LayoutConfigAntlrTokenFileProvider implements IAntlrTokenFileProvider {

	@Override
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
		return classLoader.getResourceAsStream("de/cau/cs/kieler/config/text/parser/antlr/internal/InternalLayoutConfig.tokens");
	}
}
