/*
* generated by Xtext
*/
package de.cau.cs.kieler.ksbase.parser.antlr;

import java.io.InputStream;
import org.eclipse.xtext.parser.antlr.IAntlrTokenFileProvider;

public class FeatureDefinitionAntlrTokenFileProvider implements IAntlrTokenFileProvider {
	
	public InputStream getAntlrTokenFile() {
		ClassLoader classLoader = getClass().getClassLoader();
    	return classLoader.getResourceAsStream("de/cau/cs/kieler/ksbase/parser/antlr/internal/InternalFeatureDefinition.tokens");
	}
}
