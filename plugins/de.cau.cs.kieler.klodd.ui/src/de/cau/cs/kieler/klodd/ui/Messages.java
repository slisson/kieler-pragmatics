/******************************************************************************
 * KIELER - Kiel Integrated Environment for Layout for the Eclipse RCP
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2009 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klodd.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class for access to internationalized strings.
 * 
 * @author <a href="mailto:msp@informatik.uni-kiel.de">Miro Sp&ouml;nemann</a>
 */
public class Messages {

	private static final String BUNDLE_NAME = "de.cau.cs.kieler.klodd.ui.messages"; //$NON-NLS-1$
	private static ResourceBundle resourceBundle = null;

	/**
	 * Returns a string for the given key.
	 * 
	 * @param key key to lookup
	 * @return readable message string
	 */
	public static String getString(String key) {
		try {
			if (resourceBundle == null) {
				resourceBundle = ResourceBundle.getBundle(BUNDLE_NAME);
			}
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
