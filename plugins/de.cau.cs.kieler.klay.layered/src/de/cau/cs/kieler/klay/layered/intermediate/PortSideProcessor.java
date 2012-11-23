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
package de.cau.cs.kieler.klay.layered.intermediate;

import de.cau.cs.kieler.core.alg.IKielerProgressMonitor;
import de.cau.cs.kieler.kiml.options.LayoutOptions;
import de.cau.cs.kieler.kiml.options.PortConstraints;
import de.cau.cs.kieler.kiml.options.PortSide;
import de.cau.cs.kieler.klay.layered.ILayoutProcessor;
import de.cau.cs.kieler.klay.layered.graph.LNode;
import de.cau.cs.kieler.klay.layered.graph.LPort;
import de.cau.cs.kieler.klay.layered.graph.Layer;
import de.cau.cs.kieler.klay.layered.graph.LGraph;

/**
 * Makes sure ports have at least a fixed side. If they don't, input ports are assigned
 * to the left and output ports to the right side.
 * 
 * <dl>
 *   <dt>Precondition:</dt><dd>a layered graph.</dd>
 *   <dt>Postcondition:</dt><dd>all nodes have their ports distributed, with port constraints
 *     set to fixed sides at the least.</dd>
 *   <dt>Slots:</dt><dd>Before phase 3.</dd>
 *   <dt>Same-slot dependencies:</dt><dd>None.</dd>
 * </dl>
 * 
 * @author cds
 * @kieler.design 2012-08-10 chsch grh
 * @kieler.rating proposed yellow by msp
 */
public final class PortSideProcessor implements ILayoutProcessor {

    /**
     * {@inheritDoc}
     */
    public void process(final LGraph layeredGraph, final IKielerProgressMonitor monitor) {
        monitor.begin("Port side processing", 1);
        
        // Iterate through the nodes of all layers
        for (Layer layer : layeredGraph) {
            for (LNode node : layer) {
                if (node.getProperty(LayoutOptions.PORT_CONSTRAINTS).isSideFixed()) {
                    // Check whether there are ports with undefined side
                    for (LPort port : node.getPorts()) {
                        if (port.getSide() == PortSide.UNDEFINED) {
                            setPortSide(port);
                        }
                    }
                } else {
                    // Distribute all ports and change constraints to fixed side
                    for (LPort port : node.getPorts()) {
                        setPortSide(port);
                    }
                    node.setProperty(LayoutOptions.PORT_CONSTRAINTS, PortConstraints.FIXED_SIDE);
                }
            }
        }
        
        monitor.done();
    }
    
    /**
     * Places input ports on the left side and output ports on the right side of nodes.
     * 
     * @param port the port to set side and anchor position
     */
    private void setPortSide(final LPort port) {
        if (port.getNetFlow() < 0) {
            port.setSide(PortSide.EAST);
            // adapt the anchor so outgoing edges are attached right
            port.getAnchor().x = port.getSize().x;
        } else {
            port.setSide(PortSide.WEST);
            // adapt the anchor so incoming edges are attached left
            port.getAnchor().x = 0;
        }
    }

}
