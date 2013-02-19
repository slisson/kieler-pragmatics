/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2008 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */

package de.cau.cs.kieler.kwebs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IContributor;
import org.eclipse.core.runtime.IExtension;

import de.cau.cs.kieler.kiml.service.ExtensionLayoutDataService;
import de.cau.cs.kieler.kwebs.servicedata.Category;
import de.cau.cs.kieler.kwebs.servicedata.KnownOption;
import de.cau.cs.kieler.kwebs.servicedata.LayoutAlgorithm;
import de.cau.cs.kieler.kwebs.servicedata.LayoutOption;
import de.cau.cs.kieler.kwebs.servicedata.LayoutType;
import de.cau.cs.kieler.kwebs.servicedata.RemoteEnum;
import de.cau.cs.kieler.kwebs.servicedata.SupportedDiagram;

/**
 * This class realizes the {@code IConfigurationElement} interface of the eclipse platform in order to
 * provide the meta data of a layout service in a form processable by {@code ExtensionLayoutDataService}.
 *
 * @author swe
 */
public class ServiceDataConfigurationElement implements IConfigurationElement {
   
    /** The name of the configuration element. */
    private String name;
    
    /** The value of the configuration element. */
    private String value;
    
    /** The map containing the attributes of the configuration element. */
    private Map<String, String> attributes
        = new HashMap<String, String>();
    
    /** The child elements of the configuration element. */
    private List<IConfigurationElement> children
        = new ArrayList<IConfigurationElement>();
    
    /** 
     *  The element containing this configuration parent. 
     *  Not necessarily an {@code IConfigurationElement}. 
     */
    private Object parent;

    /**
     * Create a configuration element with given name.
     * 
     * @param thename the name
     */
    public ServiceDataConfigurationElement(final String thename) {
        name = thename;
    }

    /**
     * Create a configuration element with given name and value.
     * 
     * @param thename the name
     * @param thevalue the value
     */
    public ServiceDataConfigurationElement(final String thename, final String thevalue) {
        name = thename;
        value = thevalue;
    }
    
    /**
     * Create a configuration element with given name, value, and content.
     * 
     * @param thename the name
     * @param thevalue the value
     * @param theattributes the attributes
     * @param thechildren the child elements
     */
    public ServiceDataConfigurationElement(final String thename, final String thevalue, 
        final Map<String, String> theattributes, final List<IConfigurationElement> thechildren) {
        name = thename;
        value = thevalue;
        attributes = theattributes;
        children = thechildren;
    }
    
    /**
     * Adds a child to this configuration element.
     * 
     * @param child
     *            the child element
     */
    public void addChild(final IConfigurationElement child) {
        if (child != null && !children.contains(child)) {
            children.add(child);
        }
    }
    
    /**
     * Adds an attribute to this configuration element.
     * 
     * @param thename
     *            the name of the attribute
     * @param thevalue
     *            the value of the attribute
     */
    public void addAttribute(final String thename, final String thevalue) {
        attributes.put(thename, thevalue);
    }
    
    /**
     * Returns a configuration element representing a category.
     * 
     * @param category
     *            the service data model element of type {@code Category}
     *            
     * @return a configuration element representing a category
     */
    public static ServiceDataConfigurationElement getCategoryElementFromModel(final Category category) {
        ServiceDataConfigurationElement element = new ServiceDataConfigurationElement(
            ExtensionLayoutDataService.ELEMENT_CATEGORY
        );
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_ID, category.getId());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_NAME, category.getName());
        return element;
    }
    
    /**
     * Returns a configuration element representing a layout type.
     * 
     * @param type
     *            the service data model element of type {@code LayoutType}
     *            
     * @return a configuration element representing a layout type
     */
    public static ServiceDataConfigurationElement getLayoutTypeElementFromModel(final LayoutType type) {
        ServiceDataConfigurationElement element = new ServiceDataConfigurationElement(
            ExtensionLayoutDataService.ELEMENT_LAYOUT_TYPE
        );
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_ID, type.getId());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_NAME, type.getName());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_DESCRIPTION, type.getDescription());
        return element;
    }

    /**
     * Returns a configuration element representing a layout option.
     * 
     * @param option
     *            the service data model element of type {@code LayoutOption}
     * 
     * @return a configuration element representing a layout option
     */
    public static ServiceDataConfigurationElement getLayoutOptionElementFromModel(
            final LayoutOption option) {
        ServiceDataConfigurationElement element = new ServiceDataConfigurationElement(
            ExtensionLayoutDataService.ELEMENT_LAYOUT_OPTION
        );
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_ID, option.getId());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_NAME, option.getName());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_TYPE, option.getType());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_DESCRIPTION, option.getDescription());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_DEFAULT, option.getDefault());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_IMPLEMENTATION,
                option.getImplementation());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_APPLIESTO, option.getAppliesTo());
        RemoteEnum remoteEnum = option.getRemoteEnum();
        if (remoteEnum != null) {
            StringBuilder enumValues = new StringBuilder();
            for (String enumValue : remoteEnum.getValues()) {
                if (enumValue.length() > 0) {
                    enumValues.append(" ").append(enumValue);
                }
            }
            element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_ENUMVALUES,
                    enumValues.toString());
        }
        return element;
    }
    
    /** Name of the previewImagePath attribute. */
    private static final String ATTRIBUTE_PREVIEWIMAGEPATH
        = "previewImagePath";

    /**
     * Returns a configuration element representing a layout algorithm.
     * 
     * @param algorithm
     *            the service data model element of type {@code LayoutAlgorithm}
     * 
     * @return a configuration element representing a layout algorithm
     */
    public static ServiceDataConfigurationElement getLayoutAlgorithmElementFromModel(
            final LayoutAlgorithm algorithm) {
        ServiceDataConfigurationElement element = new ServiceDataConfigurationElement(
            ExtensionLayoutDataService.ELEMENT_LAYOUT_ALGORITHM
        );
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_ID, algorithm.getId());
        element.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_NAME, algorithm.getName());
        String previewImagePath = algorithm.getPreviewImagePath();
        if (previewImagePath != null) {
            element.addAttribute(ATTRIBUTE_PREVIEWIMAGEPATH, previewImagePath);
        }
        LayoutType tmpType = algorithm.getType();
        if (tmpType != null) {
            element.addAttribute(
                ExtensionLayoutDataService.ATTRIBUTE_TYPE, tmpType.getId()
            );
        }
        element.addAttribute(
            ExtensionLayoutDataService.ATTRIBUTE_DESCRIPTION, algorithm.getDescription()
        );
        Category tmpCategory = algorithm.getCategory();
        if (tmpCategory != null) {
            element.addAttribute(
                ExtensionLayoutDataService.ATTRIBUTE_CATEGORY, tmpCategory.getId()
            );
        }
        for (KnownOption option : algorithm.getKnownOptions()) {
            ServiceDataConfigurationElement subElement
                = new ServiceDataConfigurationElement(ExtensionLayoutDataService.ELEMENT_KNOWN_OPTION);
            subElement.addAttribute(
                ExtensionLayoutDataService.ATTRIBUTE_OPTION, option.getOption().getId()
            );
            subElement.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_DEFAULT, option.getDefault());
            element.addChild(subElement);
        }
        for (SupportedDiagram diagram : algorithm.getSupportedDiagrams()) {
            ServiceDataConfigurationElement subElement = new ServiceDataConfigurationElement(
                ExtensionLayoutDataService.ELEMENT_SUPPORTED_DIAGRAM
            );
            subElement.addAttribute(ExtensionLayoutDataService.ATTRIBUTE_TYPE, diagram.getType());
            subElement.addAttribute(
                ExtensionLayoutDataService.ATTRIBUTE_PRIORITY, 
                Integer.toString(diagram.getPriority())
            );
            element.addChild(subElement);
        }
        return element;
    }
    
    /**
     * {@inheritDoc}
     */
    public Object createExecutableExtension(final String propertyName) throws CoreException {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getAttribute(final String thename) {
        return attributes.get(thename);
    }

    /**
     * {@inheritDoc}
     */
    public String getAttributeAsIs(final String thename) {
        return getAttribute(thename);
    }

    /**
     * {@inheritDoc}
     */
    public String[] getAttributeNames() {
        return attributes.keySet().toArray(new String[attributes.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public IConfigurationElement[] getChildren() {
        return children.toArray(new IConfigurationElement[children.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public IConfigurationElement[] getChildren(final String thename) {
        java.util.List<IConfigurationElement> result =
            new java.util.ArrayList<IConfigurationElement>(children.size());
        for (IConfigurationElement next : children) {
            if (next.getName().equals(thename)) {
                result.add(next);
            }
        }
        return result.toArray(new IConfigurationElement[result.size()]);
    }

    /**
     * {@inheritDoc}
     */
    public IExtension getDeclaringExtension() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    public String getValue() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public String getValueAsIs() {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuffer result = new StringBuffer(64); // SUPPRESS CHECKSTYLE MagicNumber

        result.append("ConfigurationElement[");
        result.append(getName());
        result.append(": ");
        result.append(attributes);
        result.append("]");
        return result.toString();
    }

    /**
     * {@inheritDoc}
     */
    public Object getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    public String getNamespace() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid() {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    public String getNamespaceIdentifier() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public IContributor getContributor() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getAttribute(final String attrName, final String locale) {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    public String getValue(final String locale) {
        return null;
    }
    
}
