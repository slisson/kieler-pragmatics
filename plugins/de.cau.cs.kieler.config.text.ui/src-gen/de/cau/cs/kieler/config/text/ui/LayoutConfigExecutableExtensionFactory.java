/*
 * generated by Xtext 2.12.0
 */
package de.cau.cs.kieler.config.text.ui;

import com.google.inject.Injector;
import de.cau.cs.kieler.config.text.ui.internal.TextActivator;
import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class LayoutConfigExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return TextActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return TextActivator.getInstance().getInjector(TextActivator.DE_CAU_CS_KIELER_CONFIG_TEXT_LAYOUTCONFIG);
	}
	
}
