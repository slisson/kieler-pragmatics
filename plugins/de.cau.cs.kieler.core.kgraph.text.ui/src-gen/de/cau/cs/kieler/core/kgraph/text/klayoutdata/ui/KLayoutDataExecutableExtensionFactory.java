/*
 * generated by Xtext
 */
package de.cau.cs.kieler.core.kgraph.text.klayoutdata.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.cau.cs.kieler.core.kgraph.text.klayoutdata.ui.internal.KLayoutDataActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class KLayoutDataExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return KLayoutDataActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return KLayoutDataActivator.getInstance().getInjector(KLayoutDataActivator.DE_CAU_CS_KIELER_CORE_KGRAPH_TEXT_KLAYOUTDATA_KLAYOUTDATA);
	}
	
}