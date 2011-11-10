/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2011 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.transformations;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IExecutableExtensionFactory;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.spi.RegistryContributor;
import org.osgi.framework.Bundle;

import com.google.common.base.Strings;

import de.cau.cs.kieler.core.WrappedException;
import de.cau.cs.kieler.klighd.KlighdPlugin;

/**
 * A generic factory initializing Xtend2 based model transformation classes
 * using Google Guice. It is inspired by the ...ExecutableExtensionFactory classes
 * generated by Xtext.
 * 
 * @author chsch
 *
 */
public class GuiceBasedTransformationFactory implements IExecutableExtension,
        IExecutableExtensionFactory {

    private String contributingBundleId;
    private String contributingBundleName;
    private String transformationClassName;

    
    /**
     * {@inheritDoc}
     */
    public void setInitializationData(final IConfigurationElement config,
            final String propertyName, final Object data) throws CoreException {
        // implementation inspired by org.eclipse.core.internal.registry.ConfigurationElement
        if (propertyName.equals("class") && data instanceof String) {
            String string = (String) data;
            int index = string.indexOf('/');
            if (index == -1) {
                this.contributingBundleId = ((RegistryContributor) config.getContributor()).getId();
                this.transformationClassName = string;
            } else {
                /* for experimental use I want to allow to regisiter transformations that are
                 * deposited in another plugin;
                 * in order to use this specify a the class name preceded by a '/' preceded by
                 * the contributing bundles symbolic name (id) */
                this.contributingBundleId = string.substring(0, index).trim();
                this.transformationClassName = string.substring(index + 1).trim();
            }
        }
    }

    
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Object create() throws CoreException {
        try {
            Class<?> clazz = null;
            if (Strings.isNullOrEmpty(this.contributingBundleName)) {
                Bundle contributingBundle = KlighdPlugin.getDefault().getBundle()
                        .getBundleContext().getBundle(Long.parseLong(this.contributingBundleId));
                clazz = contributingBundle.loadClass(transformationClassName);
            } else {
                clazz = Platform.getBundle(contributingBundleName).loadClass(
                        transformationClassName);
            }
            
            return new ReinitializingTransformationProxy(clazz);
        } catch (ClassNotFoundException e) {
            throw new WrappedException(e,
                    "KLighD: Registered transformation class could not be loaded properly via the"
                    + "GuiceBasedTransformationFactory. Did you miss to provide the related bundle"
                    + "id in the extension (plugin.xml)?");
        }
    }
}
