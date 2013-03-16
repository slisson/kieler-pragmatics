/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2013 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.diff;

import org.eclipse.emf.compare.diff.engine.GenericDiffEngine;
import org.eclipse.emf.compare.diff.engine.check.AttributesCheck;

/**
 * Creates a diff model without checking specific attributes. Therefore, elements will be completely
 * replaced, in which unchecked attributes were modified.
 * 
 * @author sgu
 */
public class KgtDiffEngine extends GenericDiffEngine {

    /**
     * {@inheritDoc}
     */
    protected AttributesCheck getAttributesChecker() {
        return new KgtAttributesCheck(this.getMatchManager());
    }
}
