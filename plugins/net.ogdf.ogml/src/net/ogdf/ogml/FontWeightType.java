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
 * A representation of the literals of the enumeration '<em><b>Font Weight Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see net.ogdf.ogml.OgmlPackage#getFontWeightType()
 * @model extendedMetaData="name='fontWeight.type'"
 * @generated
 */
public enum FontWeightType implements Enumerator {
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
     * The '<em><b>Bold</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BOLD_VALUE
     * @generated
     * @ordered
     */
    BOLD(1, "bold", "bold"),

    /**
     * The '<em><b>Bolder</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #BOLDER_VALUE
     * @generated
     * @ordered
     */
    BOLDER(2, "bolder", "bolder"),

    /**
     * The '<em><b>100</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_100_VALUE
     * @generated
     * @ordered
     */
    _100(3, "_100", "100"),

    /**
     * The '<em><b>200</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_200_VALUE
     * @generated
     * @ordered
     */
    _200(4, "_200", "200"),

    /**
     * The '<em><b>300</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_300_VALUE
     * @generated
     * @ordered
     */
    _300(5, "_300", "300"),

    /**
     * The '<em><b>400</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_400_VALUE
     * @generated
     * @ordered
     */
    _400(6, "_400", "400"),

    /**
     * The '<em><b>500</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_500_VALUE
     * @generated
     * @ordered
     */
    _500(7, "_500", "500"),

    /**
     * The '<em><b>600</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_600_VALUE
     * @generated
     * @ordered
     */
    _600(8, "_600", "600"),

    /**
     * The '<em><b>700</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_700_VALUE
     * @generated
     * @ordered
     */
    _700(9, "_700", "700"),

    /**
     * The '<em><b>800</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_800_VALUE
     * @generated
     * @ordered
     */
    _800(10, "_800", "800"),

    /**
     * The '<em><b>900</b></em>' literal object.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #_900_VALUE
     * @generated
     * @ordered
     */
    _900(11, "_900", "900");

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
     * The '<em><b>Bold</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Bold</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BOLD
     * @model name="bold"
     * @generated
     * @ordered
     */
    public static final int BOLD_VALUE = 1;

    /**
     * The '<em><b>Bolder</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>Bolder</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #BOLDER
     * @model name="bolder"
     * @generated
     * @ordered
     */
    public static final int BOLDER_VALUE = 2;

    /**
     * The '<em><b>100</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>100</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_100
     * @model literal="100"
     * @generated
     * @ordered
     */
    public static final int _100_VALUE = 3;

    /**
     * The '<em><b>200</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>200</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_200
     * @model literal="200"
     * @generated
     * @ordered
     */
    public static final int _200_VALUE = 4;

    /**
     * The '<em><b>300</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>300</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_300
     * @model literal="300"
     * @generated
     * @ordered
     */
    public static final int _300_VALUE = 5;

    /**
     * The '<em><b>400</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>400</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_400
     * @model literal="400"
     * @generated
     * @ordered
     */
    public static final int _400_VALUE = 6;

    /**
     * The '<em><b>500</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>500</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_500
     * @model literal="500"
     * @generated
     * @ordered
     */
    public static final int _500_VALUE = 7;

    /**
     * The '<em><b>600</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>600</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_600
     * @model literal="600"
     * @generated
     * @ordered
     */
    public static final int _600_VALUE = 8;

    /**
     * The '<em><b>700</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>700</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_700
     * @model literal="700"
     * @generated
     * @ordered
     */
    public static final int _700_VALUE = 9;

    /**
     * The '<em><b>800</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>800</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_800
     * @model literal="800"
     * @generated
     * @ordered
     */
    public static final int _800_VALUE = 10;

    /**
     * The '<em><b>900</b></em>' literal value.
     * <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>900</b></em>' literal object isn't clear,
     * there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * @see #_900
     * @model literal="900"
     * @generated
     * @ordered
     */
    public static final int _900_VALUE = 11;

    /**
     * An array of all the '<em><b>Font Weight Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    private static final FontWeightType[] VALUES_ARRAY =
        new FontWeightType[] {
            NORMAL,
            BOLD,
            BOLDER,
            _100,
            _200,
            _300,
            _400,
            _500,
            _600,
            _700,
            _800,
            _900,
        };

    /**
     * A public read-only list of all the '<em><b>Font Weight Type</b></em>' enumerators.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static final List<FontWeightType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Font Weight Type</b></em>' literal with the specified literal value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontWeightType get(String literal) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FontWeightType result = VALUES_ARRAY[i];
            if (result.toString().equals(literal)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Font Weight Type</b></em>' literal with the specified name.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontWeightType getByName(String name) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            FontWeightType result = VALUES_ARRAY[i];
            if (result.getName().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Font Weight Type</b></em>' literal with the specified integer value.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public static FontWeightType get(int value) {
        switch (value) {
            case NORMAL_VALUE: return NORMAL;
            case BOLD_VALUE: return BOLD;
            case BOLDER_VALUE: return BOLDER;
            case _100_VALUE: return _100;
            case _200_VALUE: return _200;
            case _300_VALUE: return _300;
            case _400_VALUE: return _400;
            case _500_VALUE: return _500;
            case _600_VALUE: return _600;
            case _700_VALUE: return _700;
            case _800_VALUE: return _800;
            case _900_VALUE: return _900;
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
    private FontWeightType(int value, String name, String literal) {
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
    
} //FontWeightType
