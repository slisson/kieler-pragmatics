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
package de.cau.cs.kieler.core.annotations.ui;

import de.cau.cs.kieler.core.annotations.Annotation;
import de.cau.cs.kieler.core.annotations.AnnotationsFactory;
import de.cau.cs.kieler.core.annotations.AnnotationsPackage;

/**
 * Enumeration of annotation types and utility methods for conversion.
 *
 * @author msp
 */
public enum AnnotationType {
    
    /** non-typed annotation. */
    NONE,
    /** string annotation. */
    STRING,
    /** integer annotation. */
    INT,
    /** floating point annotation. */
    FLOAT,
    /** boolean annotation. */
    BOOLEAN,
    /** typed string annotation. */
    TYPED_STRING,
    /** reference annotation. */
    REFERENCE,
    /** containment annotation. */
    CONTAINMENT;
    
    /**
     * Determine the type of the given annotation.
     * 
     * @param annotation an annotation
     * @return the type of the annotation
     */
    public static AnnotationType of(final Annotation annotation) {
        switch (annotation.eClass().getClassifierID()) {
        case AnnotationsPackage.STRING_ANNOTATION:
            return STRING;
        case AnnotationsPackage.INT_ANNOTATION:
            return INT;
        case AnnotationsPackage.FLOAT_ANNOTATION:
            return FLOAT;
        case AnnotationsPackage.BOOLEAN_ANNOTATION:
            return BOOLEAN;
        case AnnotationsPackage.TYPED_STRING_ANNOTATION:
            return TYPED_STRING;
        case AnnotationsPackage.REFERENCE_ANNOTATION:
            return REFERENCE;
        case AnnotationsPackage.CONTAINMENT_ANNOTATION:
            return CONTAINMENT;
        default:
            return NONE;
        }
    }
    
    /**
     * Create an annotation that corresponds to this annotation type.
     * 
     * @return a new annotation
     */
    public Annotation create() {
        switch (this) {
        case STRING:
            return AnnotationsFactory.eINSTANCE.createStringAnnotation();
        case INT:
            return AnnotationsFactory.eINSTANCE.createIntAnnotation();
        case FLOAT:
            return AnnotationsFactory.eINSTANCE.createFloatAnnotation();
        case BOOLEAN:
            return AnnotationsFactory.eINSTANCE.createBooleanAnnotation();
        case TYPED_STRING:
            return AnnotationsFactory.eINSTANCE.createTypedStringAnnotation();
        case REFERENCE:
            return AnnotationsFactory.eINSTANCE.createReferenceAnnotation();
        case CONTAINMENT:
            return AnnotationsFactory.eINSTANCE.createContainmentAnnotation();
        default:
            return AnnotationsFactory.eINSTANCE.createAnnotation();
        }
    }

}
