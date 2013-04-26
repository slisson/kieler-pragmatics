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
package de.cau.cs.kieler.klighd.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import com.google.common.collect.Iterators;

/**
 * A collection of some modeling related convenience functions.
 * 
 * @author chsch
 */
public final class ModelingUtil {
    
    private ModelingUtil() {
    }
    

    /**
     * Returns an {@link Iterable} containing {@code value} itself and all recursively contained
     * elements.
     * 
     * @param value
     *            the value
     * @return the required {@link Iterable}
     */
    public static Iterable<? extends EObject> selfAndEAllContents(final EObject value) {
        return Iterables2.toIterable(
                Iterators.concat(
                        Iterators.singletonIterator(value),
                        value.eAllContents()
                )
        );
    }

    
    /**
     * Returns an {@link Iterable} containing {@code value} itself and all recursively contained
     * elements of {@code value}'s type.
     * 
     * @param <T>
     *            the generic type of {@code value}
     * @param value
     *            the value
     * @return the required {@link Iterable}
     */
    public static <T extends EObject> Iterable<T> selfAndEAllContentsOfSameType(final T value) {
        @SuppressWarnings("unchecked")
        Class<T> clazz = (Class<T>) value.getClass();
        return Iterables2.toIterable(
                Iterators.concat(
                        Iterators.singletonIterator(value),
                        Iterators.filter(value.eAllContents(), clazz)
                )
        );
    }

    
    /**
     * Returns the first element of type <code>eClass</code> in <code>eObject</code>'s containment
     * hierarchy.
     * 
     * @param eObject
     *            the element reveal the required parent
     * @param eClass
     *            the type of the required parent
     * @return the required parent
     */
    public static EObject eContainerOfType(final EObject eObject, final EClass eClass) {
        EObject eo = eObject;
        while (eo != null && eo.eContainer() != null) {
            if (eo.eContainer().eClass().equals(eClass)) {
                return eo.eContainer();
            } else {
                eo = eo.eContainer();
            }
        }
        return null;
    }

    /**
     * Returns the first element of type <code>eClass</code> in <code>eObject</code>'s containment
     * hierarchy including <code>eObject</code> itself.
     * 
     * @param eObject
     *            the element reveal the required parent
     * @param eClass
     *            the type of the required parent
     * @return the required parent
     */
    public static EObject eContainerOrSelfOfType(final EObject eObject, final EClass eClass) {
        if (eObject.eClass().equals(eClass)) {
            return eObject;
        } else {
            return eContainerOfType(eObject, eClass);
        }
    }

    /**
     * Returns the first element of type <code>eClass</code> in <code>eObject</code>'s containment
     * hierarchy.
     * 
     * @param <T>
     *            the type of the required parent
     * @param eObject
     *            the element reveal the required parent
     * @param clazz
     *            the type of the required parent
     * @return the required parent
     */
    public static <T> T eContainerOfType(final EObject eObject, final Class<T> clazz) {
        EObject eo = eObject;
        while (eo != null && eo.eContainer() != null) {
            if (clazz.isInstance(eo.eContainer())) {
                @SuppressWarnings("unchecked")
                final T res = ((T) eo.eContainer());
                return res;
            } else {
                eo = eo.eContainer();
            }
        }
        return null;
    }

    /**
     * Returns the first element of type <code>eClass</code> in <code>eObject</code>'s containment
     * hierarchy including <code>eObject</code> itself.
     * 
     * @param <T>
     *            the type of the required parent
     * @param eObject
     *            the element reveal the required parent
     * @param clazz
     *            the type of the required parent
     * @return the required parent
     */
    public static <T> T eContainerOrSelfOfType(final EObject eObject, final Class<T> clazz) {
        if (clazz.isInstance(eObject)) {
            @SuppressWarnings("unchecked")
            final T res = ((T) eObject);
            return res;
        } else {
            return eContainerOfType(eObject, clazz);
        }
    }

}