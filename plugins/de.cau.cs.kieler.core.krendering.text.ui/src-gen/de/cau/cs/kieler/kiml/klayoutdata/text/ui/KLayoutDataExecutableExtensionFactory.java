/*
 * generated by Xtext
 */
package de.cau.cs.kieler.kiml.klayoutdata.text.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class KLayoutDataExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return de.cau.cs.kieler.kiml.klayoutdata.text.ui.internal.KLayoutDataActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return de.cau.cs.kieler.kiml.klayoutdata.text.ui.internal.KLayoutDataActivator.getInstance().getInjector("de.cau.cs.kieler.kiml.klayoutdata.text.KLayoutData");
	}
	
}
