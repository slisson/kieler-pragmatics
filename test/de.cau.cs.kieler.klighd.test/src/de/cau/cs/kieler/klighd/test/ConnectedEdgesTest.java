/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 *
 * Copyright 2015 by
 * + Kiel University
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 *
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 */
package de.cau.cs.kieler.klighd.test;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.elk.core.klayoutdata.KIdentifier;
import org.eclipse.elk.core.klayoutdata.KLayoutData;
import org.eclipse.elk.core.util.ElkUtil;
import org.eclipse.elk.core.util.selection.DefaultSelectionIterator;
import org.eclipse.elk.core.util.selection.SelectionIterator;
import org.eclipse.elk.graph.KEdge;
import org.eclipse.elk.graph.KNode;
import org.eclipse.elk.graph.KPort;
import org.eclipse.elk.graph.PersistentEntry;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.base.Strings;
import com.google.common.collect.Iterators;

import de.cau.cs.kieler.kgraph.text.KGraphStandaloneSetup;
import de.cau.cs.kieler.klighd.util.Iterables2;
import de.cau.cs.kieler.pragmatics.test.common.runners.ModelCollectionTestRunner;
import de.cau.cs.kieler.pragmatics.test.common.runners.ModelCollectionTestRunner.BundleId;
import de.cau.cs.kieler.pragmatics.test.common.runners.ModelCollectionTestRunner.ModelFilter;
import de.cau.cs.kieler.pragmatics.test.common.runners.ModelCollectionTestRunner.ModelPath;
import de.cau.cs.kieler.pragmatics.test.common.runners.ModelCollectionTestRunner.StopOnFailure;

/**
 * @author mkr
 */
@RunWith(ModelCollectionTestRunner.class)
@BundleId("de.cau.cs.kieler.klighd.test")
@ModelPath("connectedEdgesTests/")
@ModelFilter("*.kgt")
public class ConnectedEdgesTest {
    /**
     * The model to be tested.
     */
    private KNode model;

    /**
     * Constructor to save the model.
     *
     * @param model
     *            - the test models to validate
     */
    public ConnectedEdgesTest(final KNode model) {
        this.model = model;
    }

    /**
     * Provides a {@link ResourceSet} in order to load the models properly.
     *
     * @return the required {@link ResourceSet}
     */
    @ModelCollectionTestRunner.ResourceSet
    public static ResourceSet getResourceSet() {
        return new KGraphStandaloneSetup().createInjectorAndDoEMFRegistration().getInstance(
                XtextResourceSet.class);
    }

    private static final String SOURCE_PORT = "sourcePort";
    private static final String EXPECTED_TARGET_PORT = "expectedTargetPort";
    private static final String EXPECTED_CONNECTED_EDGES = "expectedConnectedEdges";
    private static final int INVALID_EDGE_COUNT = -1;

    private List<KPort> sourcePorts = new ArrayList<KPort>();
    private List<KPort> expectedTargetPorts = new ArrayList<KPort>();
    private List<Integer> expectedConnectedEdges = new ArrayList<Integer>();

    /**
     * Load all test cases and get the relevant data. Therefore we need to get all
     * sourcePorts, expectedTargetPorts and the number of passed edges from source to
     * target port.
     */
    @Before
    public void loadModel() {
        final KLayoutData layoutData = model.getData(KLayoutData.class);
        int expConnectedEdges = INVALID_EDGE_COUNT;
        for (PersistentEntry pe : layoutData.getPersistentEntries()) {
            if (SOURCE_PORT.equals(pe.getKey())) {
                final String sourcePortName = pe.getValue();

                Assert.assertFalse(Strings.isNullOrEmpty(sourcePortName));

                final KIdentifier sourceId =
                        Iterators.find(Iterators.filter(model.eAllContents(), KIdentifier.class),
                                new Predicate<KIdentifier>() {

                            public boolean apply(final KIdentifier input) {
                                return input.getId().equals(sourcePortName);
                            }
                        }, null);

                Assert.assertThat(sourceId.eContainer(), IsInstanceOf.instanceOf(KPort.class));

                this.sourcePorts.add((KPort) sourceId.eContainer());
            } else if (EXPECTED_TARGET_PORT.equals(pe.getKey())) {
                final String expectedTargetPortName = pe.getValue();

                Assert.assertFalse(Strings.isNullOrEmpty(expectedTargetPortName));

                final KIdentifier targetId =
                        Iterators.find(Iterators.filter(model.eAllContents(), KIdentifier.class),
                                new Predicate<KIdentifier>() {

                            public boolean apply(final KIdentifier input) {
                                return input.getId().equals(expectedTargetPortName);
                            }
                        }, null);

                Assert.assertThat(targetId.eContainer(), IsInstanceOf.instanceOf(KPort.class));

                this.expectedTargetPorts.add((KPort) targetId.eContainer());

            } else if (EXPECTED_CONNECTED_EDGES.equals(pe.getKey())) {
                expConnectedEdges = Integer.parseInt(pe.getValue());

                Assert.assertFalse(expConnectedEdges == -1);

                this.expectedConnectedEdges.add(expConnectedEdges);
            }
        }
    }


    /**
     * This test checks if the before action runs correctly.
     * If there is an Failure, all the other tests are stopped,
     * else the test goes on.
     */
    @Test
    @StopOnFailure
    public void preTest() {

    }

    /**
     * Check the existence of the source and target port and that they are connected.
     */
    @Test
    public void test() {
        for (int i = 0; i < sourcePorts.size(); i++) {
            List<KEdge> edges = sourcePorts.get(i).getEdges();
            Iterator<KEdge> it =
                    Iterators.concat(Iterators.transform(edges.iterator(),
                            new Function<KEdge, Iterator<KEdge>>() {
                                @Override
                                public Iterator<KEdge> apply(KEdge input) {
                                    SelectionIterator sourceIter =
                                            new DefaultSelectionIterator(input, false, false);
                                    SelectionIterator targetIter =
                                            new DefaultSelectionIterator(input, false, true);
                                    return Iterators.filter(ElkUtil.getConnectedElements(input,
                                            sourceIter, targetIter), KEdge.class);
                                }
                            }));

            for (KEdge e : Iterables2.toIterable(it)) {
                if (e.getSourcePort() == this.sourcePorts.get(i)) {
                    if (findTarget(e.getSourcePort(), i)) {
//                        Assert.assertSame(expectedConnectedEdges.get(i), test);
                        return;
                    }
                }
            }
        }
        Assert.fail("At least one or both ports are not existing");
    }

//    private int test = 0;

    private boolean findTarget(final KPort p, final int index) {
        KPort port = p;
        List<KPort> ports = new ArrayList<KPort>();

        while (port != null) {
//            test++;
            Iterator<KEdge> it =
                    Iterators.concat(Iterators.transform(port.getEdges().iterator(),
                            new Function<KEdge, Iterator<KEdge>>() {
                                @Override
                                public Iterator<KEdge> apply(KEdge input) {
                                    SelectionIterator sourceIter =
                                            new DefaultSelectionIterator(input, false, false);
                                    SelectionIterator targetIter =
                                            new DefaultSelectionIterator(input, false, true);
                                    return Iterators.filter(ElkUtil.getConnectedElements(input,
                                            sourceIter, targetIter), KEdge.class);
                                }
                            }));
            for (KEdge e : Iterables2.toIterable(it)) {
                if (e.getTargetPort() == this.expectedTargetPorts.get(index)) {
                    return true;
                }

                if (e.getTargetPort() != this.sourcePorts.get(index)) {
                    ports.add(e.getTargetPort());
                }
            }

            if (!ports.isEmpty()) {
                port = ports.get(0);
                ports.remove(0);
            }
        }

        return true;
    }

}
