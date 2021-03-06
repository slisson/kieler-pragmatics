/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2012 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd;

import java.util.List;

import org.eclipse.elk.core.util.Pair;

/**
 * Represents a view synthesis option provided by implementations of
 * {@link de.cau.cs.kieler.klighd.syntheses.AbstractDiagramSynthesis}. By means of such
 * options the tool user can customize the diagram creation. It provides a type (on/off, choice of
 * settings) and a set of option values, if necessary.<br>
 * <br>
 * Hint: Declare {@link SynthesisOption SynthesisOptions} as static fields in the diagram synthesis
 * implementations as concrete settings in the tool are put in hash maps keyed by the corresponding
 * synthesis option instance.
 * 
 * @author chsch
 * 
 * @kieler.design proposed by chsch
 * @kieler.rating proposed yellow by chsch
 */
public final class SynthesisOption {
    
    
    /**
     * Static factory method providing a 'Separator' pseudo {@link SynthesisOption}.<br>
     * 
     * This option has no semantic meaning, it will result in a separator line within the options
     * view.
     * 
     * @return a 'Separator' {@link SynthesisOption}.
     */
    public static SynthesisOption createSeparator() {
        return new SynthesisOption("", TransformationOptionType.SEPARATOR, true);
    }
    
    /**
     * Static factory method providing a 'Category' pseudo {@link SynthesisOption}.<br>
     * 
     * This option has no semantic meaning, it will result in a collapsable section in the options
     * view, containing all other {@link SynthesisOption} configured with this category.<br>
     * 
     * The section will display the given label and the given initial expansion state.
     * 
     * @param label
     *            the label text of the category.
     * @param initiallyExpanded
     *            the initial expansion state
     * @return a 'Category' {@link SynthesisOption}.
     */
    public static SynthesisOption createCategory(final String label,
            final boolean initiallyExpanded) {
        return new SynthesisOption(label, TransformationOptionType.CATEGORY, initiallyExpanded);
    }

    /**
     * Static factory method providing a 'Category' pseudo {@link SynthesisOption}.<br>
     * 
     * This option has no semantic meaning, it will result in a collapsable section in the options
     * view, containing all other {@link SynthesisOption} configured with this category.<br>
     * 
     * The section will display the given label and will be initially expanded.
     * 
     * @param label
     *            the label text of the category.
     * @return a 'Category' {@link SynthesisOption}.
     */
    public static SynthesisOption createCategory(final String label) {
        return new SynthesisOption(label, TransformationOptionType.CATEGORY, true);
    }
    
    /**
     * Static factory method providing a 'Separator' pseudo {@link SynthesisOption} with a label
     * text. This can be used to partition transformation options into distinct, labeled groups.
     * 
     * @param label the label text of the separator. If {@code null} or empty, a separator without a
     *              label is created.
     * @return a 'Separator' {@link SynthesisOption}.
     */
    public static SynthesisOption createSeparator(final String label) {
        return new SynthesisOption(label, TransformationOptionType.SEPARATOR, true);
    }
    
    /**
     * Static factory method providing an 'OnOff' {@link SynthesisOption}.<br>
     * <br>
     * Hint: Declare {@link SynthesisOption TransformationOptions} by means of static fields if
     * the transformation is a re-initialized one (determined in the registration).
     * 
     * @param name
     *            the name of the option
     * @param initiallyChecked
     *            true is the option shall be set initially.
     * @return an 'OnOff' {@link SynthesisOption}
     */
    public static SynthesisOption createCheckOption(final String name,
            final Boolean initiallyChecked) {
        return new SynthesisOption(name, TransformationOptionType.CHECK, initiallyChecked);
    }
    
    /**
     * Static factory method providing a 'Choice' {@link SynthesisOption}.<br>
     * <br>
     * Hint: Declare {@link SynthesisOption TransformationOptions} by means of static fields if
     * the transformation is a re-initialized one (determined in the registration).
     * 
     * @param name
     *            the name of the option
     * @return the desired 'Choice' {@link SynthesisOption}
     */
    public static SynthesisOption createChoiceOption(final String name) {
        return new SynthesisOption(name, TransformationOptionType.CHOICE, null);
    }    

    /**
     * Static factory method providing a 'Choice' {@link SynthesisOption}.<br>
     * <br>
     * Hint: Declare {@link SynthesisOption TransformationOptions} by means of static fields if
     * the transformation is a re-initialized one (determined in the registration).
     * 
     * @param name
     *            the name of the option
     * @param values
     *            the available option values.
     * @param initialValue
     *            the initially selected option value.
     * @return the desired 'Choice' {@link SynthesisOption}
     */
    public static SynthesisOption createChoiceOption(final String name, final List<?> values,
            final Object initialValue) {
        final SynthesisOption option = new SynthesisOption(name,
                TransformationOptionType.CHOICE, initialValue);
        option.setValues(values);
        return option;
    }
    
    /**
     * Static factory method providing a 'Range' {@link SynthesisOption}.<br>
     * <br>
     * Hint: Declare {@link SynthesisOption TransformationOptions} by means of static fields if
     * the transformation is a re-initialized one (determined in the registration).<br>
     * <br>
     * <b>Note:</b> Use <<OPTION_NAME>>.<code>optionFloatValue</code> while testing the option value
     * if at least one of the parameters is a floating point value, and <<OPTION_NAME>>.
     * <code>optionIntValue</code> otherwise (in Xtend).
     * 
     * @param <T>
     *            concrete type of the range's end value
     * @param name
     *            the name of the option
     * @param lowerBound
     *            the range's lower bound
     * @param upperBound
     *            the range's upper bound
     * @param initialValue
     *            the initially selected option value.
     * @return the desired 'Range' {@link SynthesisOption}
     */
    public static <T extends Number> SynthesisOption createRangeOption(
            final String name, final T lowerBound, final T upperBound, final T initialValue) {
        final SynthesisOption option = new SynthesisOption(name,
                TransformationOptionType.RANGE, initialValue);
        option.setValues(Pair.of(lowerBound, upperBound));
        if (!lowerBound.equals(lowerBound.intValue())
                || !upperBound.equals(upperBound.intValue())
                || !initialValue.equals(initialValue.intValue())) {
            option.setStepSize(DEFAULT_STEP_SIZE_FLOAT);
        } else {
            option.setStepSize(DEFAULT_STEP_SIZE_INTEGER);
        }
        return option;
    }
    
    /**
     * The standard step size of floating-point-based 'range' options.
     */
    public static final float DEFAULT_STEP_SIZE_FLOAT = 10 ^ -1; // SUPPRESS CHECKSTYLE MagicNumber
    
    /**
     * The standard step size of integer-based 'range' options.
     */
    public static final float DEFAULT_STEP_SIZE_INTEGER = 1;
    
    /**
     * Static factory method providing a 'Range' {@link SynthesisOption}.<br>
     * <br>
     * Hint: Declare {@link SynthesisOption TransformationOptions} by means of static fields if
     * the transformation is a re-initialized one (determined in the registration).<br>
     * <br>
     * <b>Note:</b> Use <<OPTION_NAME>>.<code>optionFloatValue</code> while testing the option value
     * if at least one of the parameters is a floating point value, and <<OPTION_NAME>>.
     * <code>optionIntValue</code> otherwise (in Xtend).
     * 
     * 
     * @param <T>
     *            concrete type of the range's end value
     * @param name
     *            the name of the option
     * @param lowerBound
     *            the range's lower bound
     * @param upperBound
     *            the range's upper bound
     * @param stepSize
     *            the step size determining the option value granularity
     * @param initialValue
     *            the initially selected option value.
     * @return an 'Choice' {@link SynthesisOption}
     */
    public static <T extends Number> SynthesisOption createRangeOption(final String name,
            final T lowerBound, final T upperBound, final T stepSize, final T initialValue) {
        final SynthesisOption option = new SynthesisOption(name,
                TransformationOptionType.RANGE, initialValue);
        option.setValues(Pair.of(lowerBound, upperBound));
        if (!lowerBound.equals(lowerBound.intValue())
                || !upperBound.equals(upperBound.intValue())
                || !stepSize.equals(stepSize.intValue())
                || !initialValue.equals(initialValue.intValue())) {
            option.setStepSize(stepSize.floatValue());
        } else {
            option.setStepSize(Math.round(stepSize.floatValue()));
        }
        return option;
    }    

    /* -- the internal part -- */

    /**
     * Enumeration of types of {@link SynthesisOption}s.
     * 
     * @author chsch
     */
    private enum TransformationOptionType {
        
        /** Options of this type are just set or not set. */
        CHECK,
        /** Options of this type provide a set of possible disjoint values. */
        CHOICE,
        /** Options of this type provide a range of possible continuous values. */
        RANGE,
        /** Pseudo option representing a separator. */
        SEPARATOR,
        /** Pseudo option representing a container for other options. */
        CATEGORY;
    }
    
    private final String name;    
    private final TransformationOptionType type;
    private final Object initialValue;
    private List<?> values;
    private Pair<? extends Number, ? extends Number> range;
    private Number stepSize;
    private Boolean animateUpdate = null;
    private String updateStrategy = null;
    /** The action id of the optional actions handler. */
    private String updateAction = null;
    /** The optional category option. */
    private SynthesisOption category = null;
    
    /**
     * Constructor.
     */
    private SynthesisOption(final String theName, final TransformationOptionType theType,
            final Object theInitialValue) {
        this.name = theName;
        this.type = theType;
        this.initialValue = theInitialValue;
    }
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type
     */
    public boolean isCheckOption() {
        return type.equals(TransformationOptionType.CHECK);
    }

    /**
     * @return the type
     */
    public boolean isChoiceOption() {
        return type.equals(TransformationOptionType.CHOICE);
    }

    /**
     * @return the type
     */
    public boolean isRangeOption() {
        return type.equals(TransformationOptionType.RANGE);
    }
    
    /**
     * @return the type
     */
    public boolean isSeparator() {
        return type.equals(TransformationOptionType.SEPARATOR);
    }
    
    /**
     * @return the type
     */
    public boolean isCategory() {
        return type.equals(TransformationOptionType.CATEGORY);
    }

    /**
     * @return the optionValues
     */
    public List<?> getValues() {
        if (isChoiceOption()) {
            return values;
        } else {
            return null;
        }
    }

    /**
     * @return the optionValues
     */
    public Pair<? extends Number, ? extends Number> getRange() {
        if (isRangeOption()) {
            return range;
        } else {
            return null;
        }
    }

    /**
     * @return the option value step size
     */
    public Number getStepSize() {
        if (isRangeOption()) {
            return stepSize;
        } else {
            return null;
        }
    }

    /**
     * @return the optionValues
     */
    public Object getInitialValue() {
        return initialValue;
    }

    /**
     * @return {@link Boolean#TRUE} if animation shall be applied while updating the diagram,
     *         {@link Boolean#FALSE} if <i>no</i> animation shall be applied while updating the
     *         diagram, or <code>null</code> if no setting is configured (default).
     */
    public Boolean getAnimateUpdate() {
        return animateUpdate;
    }

    /**
     * @param animate
     *            <code>true</code> if animation shall be applied while updating the diagram,
     *            <code>false</code> if no animation shall be applied
     * @return <code>this</code> {@link SynthesisOption} for convenience
     */
    public SynthesisOption setAnimateUpdate(final boolean animate) {
        this.animateUpdate = Boolean.valueOf(animate);
        return this;
    }

    /**
     * @return the id of the {@link IUpdateStrategy} to apply while updating the view model, or
     *         <code>null</code> if no dedicated strategy is configured (default case)
     */
    public String getUpdateStrategy() {
        return this.updateStrategy;
    }

    /**
     * @param strategyId
     *            the id of the {@link IUpdateStrategy} to apply while updating the view model
     * @return <code>this</code> {@link SynthesisOption} for convenience
     */
    public SynthesisOption setUpdateStrategy(final String strategyId) {
        this.updateStrategy = strategyId;
        return this;
    }
    
    /**
     * Returns the update action id.
     * 
     * @return the id of the {@link IAction} which handles updating of the view model when this
     *         {@link SynthesisOption} changed, or <code>null</code> if no dedicated action is
     *         configured (default case)
     */
    public String getUpdateAction() {
        return this.updateAction;
    }

    /**
     * Sets the id of the update action handling model updates when this {@link SynthesisOption}
     * changed.
     * <p>
     * If set non <code>null</code> the action will replace the behavior of the related
     * {@link IUpdateStrategy}.
     * 
     * @param actionId
     *            the id of the {@link IAction} which handles updating of the view model when this
     *            {@link SynthesisOption} changed
     * @return <code>this</code> {@link SynthesisOption} for convenience
     */
    public SynthesisOption setUpdateAction(final String actionId) {
        this.updateAction = actionId;
        return this;
    }

    /**
     * @param values the optionValues to set
     */
    private void setValues(final List<?> values) {
        if (this.isChoiceOption()) { 
            this.values = values;
        } else {
            throw new UnsupportedOperationException(
                    "KLighD transformation registry: Option values are only allowed for"
                    + " 'Choice' options.");
        }
    }

    /**
     * @param values the optionValues to set
     */
    private void setValues(final Pair<? extends Number, ? extends Number> theRange) {
        if (this.isChoiceOption() || this.isRangeOption()) { 
            this.range = theRange;
        } else {
            throw new UnsupportedOperationException(
                    "KLighD transformation registry: Option values are only allowed for"
                    + " 'choice' and 'range' options.");
        }
    }
    
    /**
     * @param values the optionValues to set
     */
    private void setStepSize(final Number theStepSize) {
        if (this.isRangeOption()) { 
            this.stepSize = theStepSize;
        } else {
            throw new UnsupportedOperationException(
                    "KLighD transformation registry: The step size is only allowed for"
                    + " 'range' options.");
        }
    }

    /**
     * @return the category or <code>null</code> if no category is configured (default case)
     */
    public SynthesisOption getCategory() {
        return category;
    }

    /**
     * Sets the category for this option.
     * 
     * @param newCategory
     *            the new category for this option or <code>null</code> to unset the category.
     * @return <code>this</code> {@link SynthesisOption} for convenience
     */
    public SynthesisOption setCategory(final SynthesisOption newCategory) {
        if (newCategory != null && !newCategory.isCategory()) {
            throw new IllegalArgumentException("The given synthesis option is not a category");
        }
        this.category = newCategory;
        return this;
    }
}
