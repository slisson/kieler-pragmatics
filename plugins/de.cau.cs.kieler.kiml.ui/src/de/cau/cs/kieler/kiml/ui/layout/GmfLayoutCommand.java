/*
 * KIELER - Kiel Integrated Environment for Layout Eclipse RichClient
 *
 * http://www.informatik.uni-kiel.de/rtsys/kieler/
 * 
 * Copyright 2009 by
 * + Christian-Albrechts-University of Kiel
 *   + Department of Computer Science
 *     + Real-Time and Embedded Systems Group
 * 
 * This code is provided under the terms of the Eclipse Public License (EPL).
 * See the file epl-v10.html for the license text.
 * 
 *****************************************************************************/
package de.cau.cs.kieler.kiml.ui.layout;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.common.core.command.CommandResult;
import org.eclipse.gmf.runtime.diagram.core.util.ViewUtil;
import org.eclipse.gmf.runtime.emf.commands.core.command.AbstractTransactionalCommand;
import org.eclipse.gmf.runtime.notation.Edge;
import org.eclipse.gmf.runtime.notation.IdentityAnchor;
import org.eclipse.gmf.runtime.notation.NotationFactory;
import org.eclipse.gmf.runtime.notation.NotationPackage;
import org.eclipse.gmf.runtime.notation.RelativeBendpoints;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.gmf.runtime.notation.datatype.RelativeBendpoint;


/**
 * Command used to apply layout.
 * 
 * @author <a href="mailto:msp@informatik.uni-kiel.de">Miro Sp&ouml;nemann</a>
 * @see org.eclipse.gmf.runtime.diagram.ui.commands.SetBoundsCommand
 * @see org.eclipse.gmf.runtime.diagram.ui.internal.commands.SetConnectionBendpointsCommand
 * @see org.eclipse.gmf.runtime.diagram.core.commands.SetConnectionAnchorsCommand
 */
@SuppressWarnings("restriction")
public class GmfLayoutCommand extends AbstractTransactionalCommand {

    /** layout data for node shapes. */
    private static final class ShapeLayoutData {
        private View view;
        private Point location;
        private Dimension size;
        
        private ShapeLayoutData() {
        }
    }
    
    /** layout data for edges. */
    private static final class EdgeLayoutData {
        private Edge edge;
        private PointList bends;
        private String sourceTerminal;
        private String targetTerminal;
        
        private EdgeLayoutData() {
        }
    }
    
    /** adapter for the view of the base diagram. */
    private IAdaptable diagramViewAdapter;
    /** list of shape layouts to be applied to nodes. */
    private List<ShapeLayoutData> shapeLayouts = new LinkedList<ShapeLayoutData>();
    /** list of edge layouts to be applied to edges. */
    private List<EdgeLayoutData> edgeLayouts = new LinkedList<EdgeLayoutData>();
    
    
    /**
     * Creates a command to apply layout.
     * 
     * @param domain the editing domain through which model changes are made
     * @param label the command label
     * @param adapter an adapter to the {@code View} of the base diagram
     */
    public GmfLayoutCommand(final TransactionalEditingDomain domain, final String label,
            final IAdaptable adapter) {
        super(domain, label, null);
        this.diagramViewAdapter = adapter;
    }
    
    /**
     * Adds the given shape layout data to this command.
     * 
     * @param view view from the GMF notation model
     * @param location new location for the view, or {@code null} if the
     *     location shall not be changed
     * @param size new size for the view, or {@code null} if the size
     *     shall not be changed
     */
    public void addShapeLayout(final View view, final Point location, final Dimension size) {
        assert view != null;
        ShapeLayoutData layout = new ShapeLayoutData();
        layout.view = view;
        layout.location = location;
        layout.size = size;
        shapeLayouts.add(layout);
    }
    
    /**
     * Adds the given edge layout data to this command.
     * 
     * @param edge edge from the GMF notation model
     * @param bends list of bend points for the edge, or {@code null}
     *     if the bend points shall not be changed
     * @param sourceTerminal new source terminal, encoded as string, or
     *     {@code null} if the source terminal shall not be changed
     * @param targetTerminal new target terminal, encoded as string, or
     *     {@code null} if the target terminal shall not be changed
     */
    public void addEdgeLayout(final Edge edge, final PointList bends, final String sourceTerminal,
            final String targetTerminal) {
        assert edge != null;
        EdgeLayoutData layout = new EdgeLayoutData();
        layout.edge = edge;
        layout.bends = bends;
        layout.sourceTerminal = sourceTerminal;
        layout.targetTerminal = targetTerminal;
        edgeLayouts.add(layout);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CommandResult doExecuteWithResult(final IProgressMonitor monitor,
            final IAdaptable info) throws ExecutionException {
        monitor.beginTask(getLabel(), 1);
        // process shape layout data
        for (ShapeLayoutData shapeLayout : shapeLayouts) {
            // set new location of the element
            if (shapeLayout.location != null) {
                ViewUtil.setStructuralFeatureValue(shapeLayout.view, NotationPackage.eINSTANCE
                        .getLocation_X(), Integer.valueOf(shapeLayout.location.x));
                ViewUtil.setStructuralFeatureValue(shapeLayout.view, NotationPackage.eINSTANCE
                        .getLocation_Y(), Integer.valueOf(shapeLayout.location.y));
            }
            // set new size of the element
            if (shapeLayout.size != null) {
                ViewUtil.setStructuralFeatureValue(shapeLayout.view, NotationPackage.eINSTANCE
                        .getSize_Width(), Integer.valueOf(shapeLayout.size.width));
                ViewUtil.setStructuralFeatureValue(shapeLayout.view, NotationPackage.eINSTANCE
                        .getSize_Height(), Integer.valueOf(shapeLayout.size.height));
            }
        }
        shapeLayouts.clear();
        
        // process edge layout data
        for (EdgeLayoutData edgeLayout : edgeLayouts) {
            // set new bend points of the edge
            if (edgeLayout.bends != null) {
                List<RelativeBendpoint> newBendpoints = new ArrayList<RelativeBendpoint>(
                        edgeLayout.bends.size());
                Point sourcePoint = edgeLayout.bends.getFirstPoint();
                Point targetPoint = edgeLayout.bends.getLastPoint();
                for (int i = 0; i < edgeLayout.bends.size(); i++) {
                    Point bend = edgeLayout.bends.getPoint(i);
                    newBendpoints.add(new RelativeBendpoint(
                            bend.x - sourcePoint.x,
                            bend.y - sourcePoint.y,
                            bend.x - targetPoint.x,
                            bend.y - targetPoint.y));
                }
                RelativeBendpoints points = (RelativeBendpoints)edgeLayout.edge.getBendpoints();
                points.setPoints(newBendpoints);
            }
            // set new source anchor point of the edge
            if (edgeLayout.sourceTerminal != null) {
                IdentityAnchor anchor = (IdentityAnchor)edgeLayout.edge.getSourceAnchor();
                if (anchor == null) {
                    anchor = NotationFactory.eINSTANCE.createIdentityAnchor();
                    edgeLayout.edge.setSourceAnchor(anchor);
                }
                anchor.setId(edgeLayout.sourceTerminal);
            }
            // set new target anchor point of the edge
            if (edgeLayout.targetTerminal != null) {
                IdentityAnchor anchor = (IdentityAnchor)edgeLayout.edge.getTargetAnchor();
                if (anchor == null) {
                    anchor = NotationFactory.eINSTANCE.createIdentityAnchor();
                    edgeLayout.edge.setTargetAnchor(anchor);
                }
                anchor.setId(edgeLayout.targetTerminal);
            }
        }
        edgeLayouts.clear();
        
        monitor.done();
        return CommandResult.newOKCommandResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<?> getAffectedFiles() {
        if (diagramViewAdapter != null) {
            View view = (View)diagramViewAdapter.getAdapter(View.class);
            if (view != null) {
                return getWorkspaceFiles(view);
            }
        }
        return super.getAffectedFiles();
    }

}
