/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package net.ogdf.ogml;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Transform Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.ogdf.ogml.OgmlPackage#getTransformType()
 * @model extendedMetaData="name='transform.type'"
 * @generated
 */
public enum TransformType implements Enumerator {
    /**
     * The '<em><b>Capitalize</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #CAPITALIZE_VALUE
     * @generated
     * @ordered
     */
    CAPITALIZE(0, "capitalize", "capitalize"),

    /**
     * The '<em><b>Uppercase</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #UPPERCASE_VALUE
     * @generated
     * @ordered
     */
    UPPERCASE(1, "uppercase", "uppercase"),

    /**
     * The '<em><b>Lowercase</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #LOWERCASE_VALUE
     * @generated
     * @ordered
     */
    LOWERCASE(2, "lowercase", "lowercase"),

    /**
     * The '<em><b>None</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NONE_VALUE
     * @generated
     * @ordered
     */
    NONE(3, "none", "none");

    /**
     * The '<em><b>Capitalize</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Capitalize</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #CAPITALIZE
     * @model name="capitalize"
     * @generated
     * @ordered
     */
    public static final int CAPITALIZE_VALUE = 0;

    /**
     * The '<em><b>Uppercase</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Uppercase</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #UPPERCASE
     * @model name="uppercase"
     * @generated
     * @ordered
     */
    public static final int UPPERCASE_VALUE = 1;

    /**
     * The '<em><b>Lowercase</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Lowercase</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #LOWERCASE
     * @model name="lowercase"
     * @generated
     * @ordered
     */
    public static final int LOWERCASE_VALUE = 2;

    /**
     * The '<em><b>None</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>None</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NONE
     * @model name="none"
     * @generated
     * @ordered
     */
    public static final int NONE_VALUE = 3;

    /**
     * An array of all the '<em><b>Transform Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final TransformType[] VALUES_ARRAY =
        new TransformType[] {
            CAPITALIZE,
            UPPERCASE,
            LOWERCASE,
            NONE,
        };

    /**
     * A public read-only list of all the '<em><b>Transform Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<TransformType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Transform Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TransformType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TransformType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Transform Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TransformType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            TransformType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Transform Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static TransformType get(int value) {
        switch (value) {
            case CAPITALIZE_VALUE: return CAPITALIZE;
            case UPPERCASE_VALUE: return UPPERCASE;
            case LOWERCASE_VALUE: return LOWERCASE;
            case NONE_VALUE: return NONE;
        }
        return null;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final int value;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String name;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private final String literal;

    /**
     * Only this class can construct instances.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private TransformType(int value, String name, String literal) {
        this.value = value;
        this.name = name;
        this.literal = literal;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public int getValue() {
      return value;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
      return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getLiteral() {
      return literal;
    }

    /**
     * Returns the literal value of the enumerator, which is its string representation.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        return literal;
    }
    
} //TransformType
