/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2010 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.kiml.evol;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.ui.views.properties.IPropertyDescriptor;

import de.cau.cs.kieler.kiml.layout.LayoutOptionData;
import de.cau.cs.kieler.kiml.layout.LayoutServices;
import de.cau.cs.kieler.kiml.layout.LayoutOptionData.Type;
import de.cau.cs.kieler.kiml.layout.options.LayoutOptions;
import de.cau.cs.kieler.kiml.ui.views.GmfLayoutPropertySource;

/**
 * Utility methods for Evolutionary Meta Layout.
 * 
 * @author bdu
 * 
 */
public final class EvolUtil {
    /** Hidden constructor to avoid instantiation. **/
    private EvolUtil() {
        // nothing
    }
    
    /**
     * Create a population of the given size, taking initial values from the
     * given GmfLayoutPropertySource.
     * 
     * @param source
     *            where the initial data is taken from.
     * @param size
     *            determines the initial size of the population.
     * @return population
     */
    public static Population createPopulation(final GmfLayoutPropertySource source, final int size) {
        Population result = new Population();
        for (int i = 0; i < size; i++) {
            Genome genome = createGenome(source);
            Individual ind = new Individual(genome, 0);
            result.add(ind);
        }
        return result;
    }
    
    /**
     * Count the learnable properties of the given list of IPropertyDescriptor.
     * 
     * @return
     */
    private static int countLearnableProperties(final List<IPropertyDescriptor> propertyDescriptors) {
        int result = 0;
        // IPropertyDescriptor[] propertyDescriptors =
        // source.getPropertyDescriptors();
        for (IPropertyDescriptor p : propertyDescriptors) {
            String id = (String) p.getId();
            // check property descriptor id
            if (!LayoutOptions.LAYOUT_HINT.equals(id)) {
                final LayoutOptionData layoutOptionData = LayoutServices.getInstance()
                        .getLayoutOptionData(id);
                final Type type = layoutOptionData.getType();
                switch (type) {
                case BOOLEAN:
                case ENUM:
                case INT:
                case FLOAT:
                    // learnable --> count it
                    result++;
                    break;
                default:
                    // not learnable --> don't count
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * 
     * @param theId
     * @param theValue
     * @return
     */
    private static IGene<?> createGene(final String theId, final Object theValue,
            final double theMutationProbability) {
        IGene<?> result = null;
        final LayoutOptionData layoutOptionData = LayoutServices.getInstance().getLayoutOptionData(
                theId);
        final Type type = layoutOptionData.getType();
        final int intValue;
        final double var;
        switch (type) {
        case BOOLEAN:
            boolean booleanValue = (Integer.parseInt(theValue.toString()) == 1);
            result = new BooleanGene(theId, booleanValue, theMutationProbability);
            System.out.println(result);
            break;
        case ENUM:
            final int choicesCount = layoutOptionData.getChoices().length;
            Class<? extends Enum<?>> enumClass = LayoutOptions.getEnumClass(theId);
            Assert.isNotNull(enumClass);
            Assert.isTrue(enumClass.getEnumConstants().length == choicesCount);
            intValue = Integer.valueOf(theValue.toString());
            result = new EnumGene(theId, intValue, enumClass, theMutationProbability);
            System.out.println("Enum " + enumClass.getSimpleName() + "(" + choicesCount + "): "
                    + intValue);
            break;
        case INT:
            intValue = Integer.parseInt((String) theValue);
            var = MutationInfo.DEFAULT_VARIANCE;
            result = new IntegerGene(theId, intValue, theMutationProbability, var);
            System.out.println(result);
            break;
        case FLOAT:
            final float floatValue = (Float.parseFloat((String) theValue));
            // estimate desired variance
            final float verySmall = 1e-3f;
            final double scalingFactor = .1875;
            var = ((Math.abs(floatValue) < verySmall) ? MutationInfo.DEFAULT_VARIANCE : Math
                    .abs(floatValue)
                    * scalingFactor);
            if (floatValue > 0.0f) {
                // we presume we need a strictly positive float gene
                result = new StrictlyPositiveFloatGene(theId, floatValue, theMutationProbability,
                        var);
            } else {
                // we use a general float gene
                result = new FloatGene(theId, floatValue, theMutationProbability, var);
            }
            System.out.println(result);
            break;
        default:
            break;
        }
        return result;
    }
    
    /**
     * Create a genome from the given source.
     * 
     * @param source
     * @return
     */
    private static Genome createGenome(final GmfLayoutPropertySource source) {
        if (source == null) {
            return null;
        }
        Genome result = new Genome(null);
        IGene<?> gene = null;
        // get data from property descriptors
        IPropertyDescriptor[] propertyDescriptors = source.getPropertyDescriptors();
        // determine uniformly distributed mutation probability
        double uniformProb = 0.0;
        int learnableCount = countLearnableProperties(Arrays.asList(propertyDescriptors));
        if (learnableCount > 0) {
            uniformProb = 1.0 / learnableCount;
        }
        for (IPropertyDescriptor p : propertyDescriptors) {
            String id = (String) p.getId();
            Object value = source.getPropertyValue(id);
            // check property descriptor id
            if (LayoutOptions.LAYOUT_HINT.equals(id)) {
                // layout hint --> not learnable
                ILabelProvider labelProvider = p.getLabelProvider();
                String text;
                if (value != null && labelProvider != null) {
                    try {
                        text = labelProvider.getText(value);
                    } catch (ArrayIndexOutOfBoundsException e) {
                        text = "*** EXCEPTION";
                    }
                } else {
                    text = "???";
                }
                System.out.println("--- LAYOUT_HINT: " + value + "=" + text);
            } else {
                // learnable option
                gene = createGene(id, value, uniformProb);
                if (gene != null) {
                    result.add(gene);
                    gene = null;
                }
            }
        }
        Assert.isTrue(learnableCount == result.size());
        return result;
    }
}
