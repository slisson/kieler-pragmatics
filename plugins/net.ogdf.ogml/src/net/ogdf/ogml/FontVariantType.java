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
 * A representation of the literals of the enumeration '<em><b>Font Variant Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.ogdf.ogml.OgmlPackage#getFontVariantType()
 * @model extendedMetaData="name='fontVariant.type'"
 * @generated
 */
public enum FontVariantType implements Enumerator {
    /**
     * The '<em><b>Normal</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #NORMAL_VALUE
     * @generated
     * @ordered
     */
    NORMAL(0, "normal", "normal"),

    /**
     * The '<em><b>Small Cpaps</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #SMALL_CPAPS_VALUE
     * @generated
     * @ordered
     */
    SMALL_CPAPS(1, "smallCpaps", "small-cpaps");

    /**
     * The '<em><b>Normal</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Normal</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #NORMAL
     * @model name="normal"
     * @generated
     * @ordered
     */
    public static final int NORMAL_VALUE = 0;

    /**
     * The '<em><b>Small Cpaps</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Small Cpaps</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #SMALL_CPAPS
     * @model name="smallCpaps" literal="small-cpaps"
     * @generated
     * @ordered
     */
    public static final int SMALL_CPAPS_VALUE = 1;

    /**
     * An array of all the '<em><b>Font Variant Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FontVariantType[] VALUES_ARRAY =
        new FontVariantType[] {
            NORMAL,
            SMALL_CPAPS,
        };

    /**
     * A public read-only list of all the '<em><b>Font Variant Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FontVariantType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Font Variant Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontVariantType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FontVariantType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Font Variant Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontVariantType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FontVariantType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Font Variant Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontVariantType get(int value) {
        switch (value) {
            case NORMAL_VALUE: return NORMAL;
            case SMALL_CPAPS_VALUE: return SMALL_CPAPS;
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
    private FontVariantType(int value, String name, String literal) {
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
    
} //FontVariantType
