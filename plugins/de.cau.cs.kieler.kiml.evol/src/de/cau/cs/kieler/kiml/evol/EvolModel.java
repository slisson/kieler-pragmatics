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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.ui.IEditorPart;

import de.cau.cs.kieler.core.KielerException;
import de.cau.cs.kieler.core.ui.util.MonitoredOperation;
import de.cau.cs.kieler.kiml.LayoutProviderData;
import de.cau.cs.kieler.kiml.evol.alg.BasicEvolutionaryAlgorithm;
import de.cau.cs.kieler.kiml.evol.genetic.Genome;
import de.cau.cs.kieler.kiml.evol.genetic.Population;
import de.cau.cs.kieler.kiml.evol.ui.IEvolModelListener;
import de.cau.cs.kieler.kiml.evol.ui.IEvolModelListener.ModelChangeType;

/**
 * This class encapsulates the evolution model that is displayed in the
 * EvolView. The model basically consists of an evolutionary algorithm, a
 * population and a current individual. Additionally, it manages a population of
 * rating predictors that is evolved separately.
 *
 * @author bdu
 *
 */
public final class EvolModel {

    /**
     * Number of rating predictors.
     */
    private static final int NUM_RATING_PREDICTORS = 25;

    // private fields
    private BasicEvolutionaryAlgorithm evolAlg;
    private BasicEvolutionaryAlgorithm predictorsEvolAlg;
    private int position;
    private String layoutProviderId;

    private final List<IEvolModelListener> listeners = new LinkedList<IEvolModelListener>();

    /**
     * Adds a model listener.
     *
     * @param listener
     *            the {@link IEvolModelListener} to add
     */
    public void addListener(final IEvolModelListener listener) {
        if (listener != null) {
            this.listeners.add(listener);
        }
    }

    /**
     * Auto-rate all individuals in the appropriate editors.
     *
     * @param theMonitor
     *            a progress monitor; may be {@code null}
     */
    public void autoRateAll(final IProgressMonitor theMonitor) {

        final Population population = getPopulation();
        Assert.isNotNull(population);

        final Population predictors = getRatingPredictors();
        Assert.isNotNull(predictors);

        EvolUtil.autoRate(population, theMonitor, predictors);

        // Notify listeners.
        afterChange(ModelChangeType.AUTO_RATING);
    }

    /**
     * Changes the rating of the current individual.
     *
     * @param theDelta
     *            the amount of the change
     */
    public void changeCurrentRating(final double theDelta) {
        if (isValid()) {
            final Genome current = getCurrentIndividual();

            final Population pop = this.getPopulation();
            double rating = 0.0;
            // TODO: add preference to switch off compensation
            // TODO: Do evolution of rating predictors at lock step with
            // evolution of layout options. Use separate auto-rating and
            // user-rating fields for that purpose.
            final double compensation = -theDelta / ((pop.size() - 1));

            for (final Genome g : pop) {
                rating = (g.hasUserRating() ? g.getUserRating() : 0);
                if (g == current) {
                    g.setUserRating(Double.valueOf(rating + theDelta));
                } else {
                    // compensation for counterbalance
                    g.setUserRating(Double.valueOf(rating + compensation));
                }
            }

            // Punish predictors
            final String key = "proposedRating:" + current.getId();
            final double newRating = (current.hasUserRating() ? current.getUserRating() : 0.0);
            for (final Genome predictor : this.getRatingPredictors()) {
                final Map<String, Object> features = predictor.getFeatures();
                if ((features != null) && features.containsKey(key)) {
                    final Double prediction = (Double) features.get(key);
                    final double diff = newRating - prediction;
                    final double predictorRating =
                            (predictor.hasUserRating() ? predictor.getUserRating() : 0.0);
                    predictor.setUserRating(predictorRating - Math.abs(diff));
                }
            }

            this.predictorsEvolAlg.step();

            afterChange(ModelChangeType.CURRENT_RATING);
        }
    }

    /**
     * Performs a step of the evolutionary algorithm.
     *
     * @param theMonitor
     *            a progress monitor; may be {@code null}
     */
    public void evolve(final IProgressMonitor theMonitor) {

        final IProgressMonitor monitor;
        // Ensure there is a monitor of some sort.
        monitor = ((theMonitor != null) ? theMonitor : new NullProgressMonitor());

        final int stepWork = 1;
        final int afterStepWork = 1;
        final int autoRateWork = 1;
        final int listenersWork = 1;

        final int total = stepWork + afterStepWork + autoRateWork + listenersWork;
        final int scale = 100;

        try {
            monitor.beginTask("Performing evolutionary step.", total * scale);

            // Perform the step.
            this.evolAlg.step();
            monitor.worked(stepWork * scale);

            // The previously selected individual might not have survived in
            // evolution, so select a new one as current.
            selectInterestingIndividual();
            monitor.worked(afterStepWork * scale);

            // Calculate auto-rating for the yet unrated individuals.
            final Population unrated = getPopulation().select(Population.UNRATED_FILTER);
            Assert.isNotNull(unrated);

            final Population predictors = this.getRatingPredictors();
            Assert.isNotNull(predictors);
            Assert.isTrue(!predictors.isEmpty());

            final Runnable runnable = new Runnable() {
                public void run() {
                    EvolUtil.autoRate(unrated, new SubProgressMonitor(monitor, 1 * scale),
                            predictors);
                }
            };
            MonitoredOperation.runInUI(runnable, true);

            // reward predictors
            // XXX: this should be refactored. The predictors' fitness should
            // be calculated an a more sophisticated way.
            final double reward = 0.01;
            for (final Genome predictor : predictors) {
                Assert.isNotNull(predictor);
                final double oldRating =
                        (predictor.hasUserRating() ? predictor.getUserRating() : 0);
                predictor.setUserRating(oldRating + reward);
            }

            monitor.worked(autoRateWork * scale);

            // Notify listeners.
            afterChange(ModelChangeType.EVOLVE);
            monitor.worked(listenersWork * scale);

        } finally {
            monitor.done();
        }
    }

    /**
     * Returns the current individual.
     *
     * @return the current {@code Individual}, or {@code null} if none is
     *         selected.
     */
    public Genome getCurrentIndividual() {
        final Population pop = this.getPopulation();
        final int pos = this.getPosition();
        Assert.isTrue((pos >= 0) && (pos < pop.size()), "position out of range");

        // ensure that the position is within the valid range
        if ((pos >= pop.size()) || (pos < 0)) {
            return null;
        }

        return pop.get(pos);
    }

    /**
     *
     * @return the evolutionary algorithm.
     */
    public BasicEvolutionaryAlgorithm getEvolAlg() {
        return this.evolAlg;
    }

    /**
     * The layout provider id that the population was created for.
     *
     * @return the layout provider id
     */
    public String getLayoutProviderId() {
        return this.layoutProviderId;
    }

    /**
     *
     * @return the population
     */
    public Population getPopulation() {
        if (this.evolAlg == null) {
            return new Population();
        }
        return this.evolAlg.getPopulation();
    }

    /**
     * Returns the current position.
     *
     * @return the current position.
     */
    public int getPosition() {
        return this.position;
    }

    /**
     *
     * @return the population of rating predictors
     */
    public Population getRatingPredictors() {
        if (this.predictorsEvolAlg == null) {
            return new Population();
        }
        return this.predictorsEvolAlg.getPopulation();
    }

    /**
     * Checks if the model is in a valid state.
     *
     * @return {@code true} if the model is valid; otherwise {@code false}.
     *
     */
    public boolean isValid() {
        if (this.evolAlg == null) {
            EvolPlugin.logStatus("Algorithm is not set.");
            return false;
        }

        if (this.predictorsEvolAlg == null) {
            EvolPlugin.logStatus("Weights algorithm is not set.");
            return false;
        }

        if (this.predictorsEvolAlg.getPopulation() == null) {
            EvolPlugin.logStatus("Predictor population is not set.");
            return false;
        }

        final Population pop = this.getPopulation();
        // Population should be non-null in any case.
        Assert.isNotNull(pop, "Population is not set.");

        if (pop.isEmpty()) {
            EvolPlugin.logStatus("Population is empty.");
            return false;
        }

        final Genome currentIndividual = getCurrentIndividual();
        if (currentIndividual == null) {
            EvolPlugin.logStatus("No individual selected.");
            return false;
        }

        return true;
    }

    /**
     * Removes the specified model listener.
     *
     * @param listener
     *            the {@link IEvolModelListener} to remove
     */
    public void removeListener(final IEvolModelListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Reset the population and restart the algorithm.
     */
    public void reset() {
        final IEditorPart editor = EvolUtil.getCurrentEditor();
        final EditPart part = EvolUtil.getCurrentEditPart(editor);
        this.position = 0;
        this.evolAlg = null;
        this.predictorsEvolAlg = null;

        final LayoutProviderData providerData = EvolUtil.getLayoutProviderData(editor, part);

        if (providerData != null) {
            final String providerId = providerData.getId();
            this.layoutProviderId = providerId;

        } else {
            this.layoutProviderId = null;
        }

        // Create an initial population.
        final Set<IEditorPart> editors = EvolUtil.getEditors();
        Population sourcePopulation = null;
        try {
            sourcePopulation = EvolUtil.createPopulation(editors);
        } catch (final KielerException exception) {
            exception.printStackTrace();
            EvolPlugin.showError("A new population could not be created.", exception);
        }

        if ((sourcePopulation != null) && !sourcePopulation.isEmpty()) {

            // Add meta-evolution genes for the layout metrics.
            EvolPlugin.logStatus("Creating metric weights ...");
            final Set<String> metricIds = EvolutionServices.getInstance().getLayoutMetricsIds();

            final Population predictors = new Population();
            for (int i = 0; i < NUM_RATING_PREDICTORS; i++) {
                final Genome weightGenes = GenomeFactory.createWeightGenes(metricIds);
                Assert.isNotNull(weightGenes);
                predictors.add(weightGenes);
            }

            this.predictorsEvolAlg = new BasicEvolutionaryAlgorithm(predictors);
            this.predictorsEvolAlg.step();

            // Create and initialize the algorithm.
            final BasicEvolutionaryAlgorithm alg =
                    new BasicEvolutionaryAlgorithm(sourcePopulation);
            this.evolAlg = alg;
            alg.step();
        }

        // Notify listeners.
        afterChange(ModelChangeType.RESET);
    }

    /**
     * Sets the current position.
     *
     * @param thePosition
     *            the new position.
     */
    public void setPosition(final int thePosition) {
        Assert.isLegal(thePosition >= 0);
        Assert.isLegal((thePosition <= getPopulation().size()));

        final int oldPosition = this.position;

        if ((oldPosition != thePosition)) {
            this.position = thePosition;
            afterChange(ModelChangeType.SET_POSITION);
        }
    }

    /**
     * Notify listeners about a performed model change.
     *
     * @param cause
     */
    private void afterChange(final ModelChangeType cause) {
        for (final IEvolModelListener listener : this.listeners) {
            listener.afterChange(this, cause);
        }
    }

    /**
     * Return position of first unrated individual in the list.
     *
     * @return {@code -1} if no unrated individual exists.
     */
    private int firstUnrated() {
        final Population pop = this.getPopulation();
        Assert.isNotNull(pop);

        int result = -1;
        for (int i = 0; i < pop.size(); i++) {
            final Genome ind = pop.get(i);
            if (!ind.hasUserRating()) {
                result = i;
                break;
            }
        }
        return result;
    }

    /**
     * Selects an interesting individual.
     */
    private void selectInterestingIndividual() {

        final int firstUnrated = firstUnrated();
        if (firstUnrated > -1) {
            setPosition(firstUnrated);
        }

        updatePosition();
        Assert.isTrue(getPosition() >= 0);
    }

    /**
     * Make sure that the current position is not beyond the last individual.
     */
    private void updatePosition() {
        final int lim = getPopulation().size();
        if (getPosition() >= lim) {
            setPosition(lim - 1);
        }
    }
}
