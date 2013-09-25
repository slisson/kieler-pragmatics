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
package de.cau.cs.kieler.kiml.libavoid;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import org.adaptagrams.cola.libavoid.LibavoidServer;
import org.adaptagrams.cola.libavoid.LibavoidServer.Cleanup;
import org.adaptagrams.cola.libavoid.LibavoidServerException;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;

import de.cau.cs.kieler.core.WrappedException;
import de.cau.cs.kieler.core.alg.IKielerProgressMonitor;
import de.cau.cs.kieler.core.kgraph.KEdge;
import de.cau.cs.kieler.core.kgraph.KNode;
import de.cau.cs.kieler.core.math.KVector;
import de.cau.cs.kieler.core.math.KVectorChain;
import de.cau.cs.kieler.kiml.klayoutdata.KEdgeLayout;
import de.cau.cs.kieler.kiml.klayoutdata.KLayoutDataFactory;
import de.cau.cs.kieler.kiml.klayoutdata.KPoint;
import de.cau.cs.kieler.kiml.klayoutdata.KShapeLayout;

/**
 * @author uru
 */
public class LibavoidServerCommunicator {

    private BiMap<Integer, KNode> nodeIdMap = HashBiMap.create();
    private BiMap<Integer, KEdge> edgeIdMap = HashBiMap.create();

    private int nodeIdCounter = 0;
    private int edgeIdCounter = 0;
    private StringBuffer sb = new StringBuffer();

    public void reset() {
        nodeIdCounter = 0;
        nodeIdMap.clear();
        edgeIdCounter = 0;
        edgeIdMap.clear();
        sb = new StringBuffer();
    }

    public void requestLayout(final KNode layoutNode, final IKielerProgressMonitor progressMonitor,
            final LibavoidServer lvServer) {
        progressMonitor.begin("OGDF Layout", 4);
        // if the graph is empty there is no need to layout
        if (layoutNode.getChildren().isEmpty()) {
            progressMonitor.done();
            return;
        }

        // start the OGDF server process, or retrieve the previously used process
        lvServer.initialize("text");

        try {
            // retrieve the OGDF server input
            OutputStream outputStream = lvServer.input();
            // write the graph to the process
            writeTextGraph(layoutNode, outputStream); // , progressMonitor.subTask(SUBTASK_WORK),
                                                      // lvServer);
            // flush the stream
            outputStream.flush();

            // read the layout information
            Map<String, KVectorChain> layoutInformation =
                    readLayoutInformation(lvServer, progressMonitor.subTask(1));
            // apply the layout back to the KGraph
            applyLayout(layoutNode, layoutInformation, progressMonitor.subTask(1));
            // clean up the OGDF server process
            lvServer.cleanup(Cleanup.NORMAL);

        } catch (IOException exception) {
            lvServer.cleanup(Cleanup.ERROR);
            throw new WrappedException(exception, "Failed to communicate with the OGDF process.");
        } finally {
            progressMonitor.done();
            reset();
        }

    }

    /**
     * Applies the layout information back to the original graph.
     * 
     * @param parentNode
     *            the parent node of the layout graph
     * @param layoutInformation
     *            the layout information
     * @param progressMonitor
     *            the progress monitor
     */
    private void applyLayout(final KNode parentNode,
            final Map<String, KVectorChain> layoutInformation,
            final IKielerProgressMonitor progressMonitor) {
        progressMonitor.begin("Apply layout", 1);

        for(Entry<String, KVectorChain> entry : layoutInformation.entrySet()) {
            int edgeId = Integer.valueOf(entry.getKey().split(" ")[1]);
            
            KEdge e = edgeIdMap.get(edgeId);
            
            KEdgeLayout edgeLayout = e.getData(KEdgeLayout.class);
            edgeLayout.getBendPoints().clear();
            
            
           Iterator<KVector> it = entry.getValue().iterator();
           while(it.hasNext()) {
               KVector v = it.next();
               KPoint kpoint = KLayoutDataFactory.eINSTANCE.createKPoint();
               kpoint.setPos((float) v.x, (float) v.y);
               edgeLayout.getBendPoints().add(kpoint);
               
               System.out.println("Bend: " + kpoint);
           }
           
        }
        
        progressMonitor.done();
    }

    /**
     * Read layout information from the OGDF server process.
     * 
     * @param ogdfServer
     *            the OGDF server process interface
     * @param progressMonitor
     *            the progress monitor
     * @return a map of layout information
     */
    private Map<String, KVectorChain> readLayoutInformation(final LibavoidServer ogdfServer,
            final IKielerProgressMonitor progressMonitor) {
        progressMonitor.begin("Read output from OGDF", 1);
        Map<String, String> outputData = ogdfServer.readOutputData();
        if (outputData == null) {
            ogdfServer.cleanup(Cleanup.ERROR);
            throw new LibavoidServerException("No output from the OGDF process."
                    + " Try increasing the timeout value in the preferences"
                    + " (KIELER / Layout / OGDF).");
        }
        Map<String, KVectorChain> layoutInformation =
                Maps.newHashMapWithExpectedSize(outputData.size());
        for (Map.Entry<String, String> entry : outputData.entrySet()) {
            KVectorChain pointList = new KVectorChain();
            StringTokenizer tokenizer = new StringTokenizer(entry.getValue(), " ");
            // now the coordinates
            while (tokenizer.countTokens() >= 2) {
                double x = parseDouble(tokenizer.nextToken());
                double y = parseDouble(tokenizer.nextToken());
                pointList.add(new KVector(x, y));
            }
            layoutInformation.put(entry.getKey(), pointList);
        }
        progressMonitor.done();
        return layoutInformation;
    }

    private void writeTextGraph(KNode root, OutputStream stream) {

        reset();

        transformGraph(root);

        try {
            stream.write(sb.toString().getBytes());
            System.out.println(sb.toString());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void transformGraph(KNode root) {

        sb.append("GRAPH");
        sb.append("\n");

        // nodes
        for (KNode node : root.getChildren()) {
            transformNode(node);
        }

        // edges
        for (KNode node : root.getChildren()) {
            for (KEdge edge : node.getOutgoingEdges()) {
                transformEdge(edge);
            }
        }

        sb.append("GRAPHEND");
        sb.append("\n");
    }

    private void transformNode(KNode node) {
        // assign an id
        nodeIdMap.put(nodeIdCounter, node);

        // convert the bounds
        KShapeLayout shape = node.getData(KShapeLayout.class);
        // format: id topleft bottomright
        sb.append("NODE " + nodeIdCounter + " " + shape.getXpos() + " " + shape.getYpos() + " "
                + (shape.getXpos() + shape.getWidth()) + " "
                + (shape.getYpos() + shape.getHeight()));
        sb.append("\n");

        nodeIdCounter++;
    }

    private void transformEdge(KEdge edge) {
        // assign an id
        edgeIdMap.put(edgeIdCounter, edge);

        // convert the edge
        int srcId = nodeIdMap.inverse().get(edge.getSource());
        int tgtId = nodeIdMap.inverse().get(edge.getTarget());

        // format: edgeId srcId tgtId
        sb.append("EDGE " + edgeIdCounter + " " + srcId + " " + tgtId);
        sb.append("\n");

        edgeIdCounter++;
    }

    /**
     * Parse a double value ignoring illegal string values.
     * 
     * @param string
     *            a string value
     * @return the corresponding double, or NaN if the string is illegal
     */
    private static double parseDouble(final String string) {
        try {
            return Double.parseDouble(string);
        } catch (NumberFormatException exception) {
            // the vector chain could not be parsed - return NaN
            return Double.NaN;
        }
    }
}
