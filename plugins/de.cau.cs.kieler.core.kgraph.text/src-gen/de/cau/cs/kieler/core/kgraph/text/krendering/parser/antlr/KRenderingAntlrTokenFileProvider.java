/*
* generated by Xtext
*/
package de.cau.cs.kieler.core.kgraph.text.krendering.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class KRenderingAntlrTokenFileProvider implements IAntlrTokenFileProvider {
	
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
    	return classLoader.getResourceAsStream("de/cau/cs/kieler/core/kgraph/text/krendering/parser/antlr/internal/InternalKRendering.tokens");
	}
}