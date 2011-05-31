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
package de.cau.cs.kieler.klay.force.model;

import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.klay.force.graph.FGraph;
import de.cau.cs.kieler.klay.force.graph.FNode;
import de.cau.cs.kieler.klay.force.graph.FParticle;
import de.cau.cs.kieler.klay.force.properties.Properties;

/**
 * A force model after the Fruchterman-Reingold approach.
 *
 * @author msp
 * @author tmn
 * @author fhol
 */
public class FruchtermanReingoldModel extends AbstractForceModel {

    private static final double SPACING_FACTOR = 0.01;
    
    private double temperature = Properties.DEF_TEMPERATURE;
    private double threshold;
    private double k;
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void initialize(final FGraph graph) {
        super.initialize(graph);
        temperature = graph.getProperty(Properties.TEMPERATURE);
        threshold = temperature / graph.getProperty(Properties.ITERATIONS);
        
        // calculate an appropriate value for K
        int n = graph.getNodes().size();
        double totalWidth = 0;
        double totalHeight = 0;
        for (FNode v : graph.getNodes()) {
            totalWidth += v.getSize().x;
            totalHeight += v.getSize().y;
        }
        double area = totalWidth * totalHeight;
        double c = graph.getProperty(Properties.SPACING) * SPACING_FACTOR;
        k = Math.sqrt(area / (2 * n)) * c;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean moreIterations(final int count) {
        return temperature > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected KVector calcDisplacement(final FParticle forcer, final FParticle forcee) {
        avoidSamePosition(getRandom(), forcer, forcee);

        // compute distance (z in the original algorithm)
        KVector displacement = forcee.getPosition().differenceCreate(forcer.getPosition());
        double d = displacement.getLength();
        
        // calculate repulsive force, independent of adjacency
        double force = repulsive(d, k) * forcer.getProperty(Properties.PRIORITY);
        
        // calculate attractive force, depending of adjacency
        int connection = getGraph().getConnection(forcer, forcee);
        if (connection > 0) {
            force -= attractive(d, k) * connection;
        }

        // scale distance vector to the amount of repulsive forces
        displacement.scale(force * temperature / d);

        return displacement;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void iterationDone() {
        super.iterationDone();
        temperature -= threshold;
    }
    
    /**
     * Calculates the amount of repulsive force along a distance.
     * 
     * @param d the distance over which the force exerts
     * @param k the space parameter, depending on the available area
     * @return the amount of the repulsive force
     */
    private static double repulsive(final double d, final double k) {
        return k * k / d;
    }
    
    /**
     * Calculates the amount of attracting force along a distance.
     * 
     * @param d the distance over which the force exerts
     * @param k the space-parameter, depending on the available area
     * @return the amount of the attracting force
     */
    public static double attractive(final double d, final double k) {
        return d * d / k;
    }

}