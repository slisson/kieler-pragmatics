/*
 * generated by Xtext
 */
package de.cau.cs.kieler.core.annotations.text.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import de.cau.cs.kieler.core.annotations.text.ui.internal.AnnotationsActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class AnnotationsExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return AnnotationsActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return AnnotationsActivator.getInstance().getInjector(AnnotationsActivator.DE_CAU_CS_KIELER_CORE_ANNOTATIONS_TEXT_ANNOTATIONS);
	}
	
}
