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
package de.cau.cs.kieler.klighd.microlayout;

import static de.cau.cs.kieler.core.krendering.KRenderingUtil.asGridPlacement;
import static de.cau.cs.kieler.core.krendering.KRenderingUtil.asGridPlacementData;
import static de.cau.cs.kieler.core.krendering.KRenderingUtil.getPlacementData;
import static de.cau.cs.kieler.klighd.microlayout.PlacementUtil.estimateSize;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;

import de.cau.cs.kieler.core.krendering.KContainerRendering;
import de.cau.cs.kieler.core.krendering.KGridPlacement;
import de.cau.cs.kieler.core.krendering.KGridPlacementData;
import de.cau.cs.kieler.core.krendering.KPosition;
import de.cau.cs.kieler.core.krendering.KRendering;
import de.cau.cs.kieler.core.krendering.KRenderingFactory;
import de.cau.cs.kieler.core.math.KielerMath;
import de.cau.cs.kieler.core.properties.IProperty;
import de.cau.cs.kieler.core.properties.Property;
import de.cau.cs.kieler.core.util.Pair;

/**
 * A utility class for evaluating the grid micro layout of KRenderings.
 * 
 * @author chsch, akoc
 */
public final class GridPlacementUtil {

    /**
     * Property definition allowing attach grid size assignments to {@link KRendering KRenderings}
     * with {@link KGridPlacement}. This is done in order to avoid unnecessary re-computations of
     * the grid's cell sizes while drawing the grid. This is especially important in case of nested
     * grids.
     */
    public static final IProperty<GridSizeAssignment> ESTIMATED_GRID_DATA
            = new Property<GridSizeAssignment>("klighd.grid.estimatedGridData");

    /**
     * Property definition allowing attach the determined position of the
     * {@link de.cau.cs.kieler.core.kgraph.KNode KNode's}
     * {@link de.cau.cs.kieler.core.krendering.KChildArea KChildArea} within the grid. This reduces
     * searching especially the child area is hidden in nested grids.
     */
    public static final IProperty<Pair<Integer, Integer>> CHILD_AREA_POSITION
            = new Property<Pair<Integer, Integer>>("klighd.grid.childAreaPosition");


    /**
     * Hidden standard constructor.
     */
    private GridPlacementUtil() {        
    }


    /**
     * A data holder class for the spacing of the grid calculated during the size estimation. this
     * class is intended to be used in the above defined IProperty ESTIMATED_GRID_DATA to be
     * evaluated during the actual evaluation of parentBounds to place childElements
     * 
     * @author akoc
     * @author chsch
     */
    public static class GridSizeAssignment {
        private float[] calculatedColumnWidths;
        private float[] calculatedRowHeights;

        /**
         * Copy constructor.
         * 
         * @param other
         *            the copied one
         */
        public GridSizeAssignment(final GridSizeAssignment other) {
            this.calculatedColumnWidths =
                    other == null ? null : other.calculatedColumnWidths.clone();
            this.calculatedRowHeights =
                    other == null ? null : other.calculatedRowHeights.clone();
        }

        /**
         * Constructor. Sets the attributes according to given parameters
         * 
         * @param cols
         *            the column widths calculated during the size estimation
         * @param rows
         *            the row heights calculated during the size estimation
         */
        GridSizeAssignment(final float[] cols, final float[] rows) {
            this.calculatedColumnWidths = cols;
            this.calculatedRowHeights = rows;
        }

        /**
         * Setter.
         *  
         * @param cols
         *            the column widths calculated during the size estimation
         * @param rows
         *            the row heights calculated during the size estimation
         */
        public void setCalculatedSizes(final float[] cols, final float[] rows) {
            this.calculatedColumnWidths = cols;
            this.calculatedRowHeights = rows;
        }
        /**
         * Getter.
         * 
         * @return the calculatedColumnWidths
         */
        public float[] getCalculatedColumnWidths() {
            return calculatedColumnWidths;
        }

        /**
         * Getter.
         * 
         * @return the calculatedRowHeights
         */
        public float[] getCalculatedRowHeights() {
            return calculatedRowHeights;
        }

        @Override
        public String toString() {
            return "(Widths = " + Arrays.toString(this.calculatedColumnWidths) + ", Height = "
                    + Arrays.toString(this.calculatedRowHeights) + ")";
        }
    }

    /**
     * Returns the bounds for a gridPlacement data in given parent bounds.
     * 
     * @param gridPlacement
     *            the definition of the parent gridPlacement defining the column number
     * @param children
     *            the children to be placed inside the grid with their placement data
     * @param parentBounds
     *            the parent bounds
     * @return the bounds
     */
    public static Bounds[] evaluateGridPlacement(final KGridPlacement gridPlacement,
            final List<KRendering> children, final Bounds parentBounds) {
        if (parentBounds.isEmpty()) {
            return null;
        }
        final GridPlacer placer = getGridPlacementObject(gridPlacement, children);
        return placer.evaluate(parentBounds);
    }


    /**
     * A helper class to calculate bounds for elements placed on a grid.
     */
    public static class GridPlacer {

        private KRendering parent;
        /** the associated grid placement data. */
        private List<KRendering> children;
        /** the number of columns. */
        private int numColumns;
        /** the number of rows. */
        private int numRows;
        
        private boolean[] flexibleCols;
        private boolean[] flexibleRows;
        
        private int countFlexibleCols = 0;
        private int countFlexibleRows = 0;
        
        /** the column widths. */
        private float[] columnMaxMinWidth;
        /** the row heights. */
        private float[] rowMaxMinHeight;

        private float[] calculatedRowHeight;

        private float[] calculatedColumnWidth;
        /** the total width hinted. */
        private float minTotalWidth = 0;
        /** the total height hinted. */
        private float minTotalHeight = 0;
        
        private KPosition topLeft = null;
        private KPosition bottomRight = null;
        
        private static final float TOLERANCE = 0.1f;

        /**
         * Evaluates the grid placement for the given parent bounds.
         * 
         * @param parentBounds
         *            the parent bounds
         * @return the bounds for the placed elements
         */
        public Bounds[] evaluate(final Bounds parentBounds) {
            if (children.size() == 0) {
                return new Bounds[0];
            }

            final Bounds[] bounds = new Bounds[children.size()];

            // in case there is actually no space - skip the costly computations
            if (parentBounds.width == 0f || parentBounds.height == 0f) {
                Arrays.fill(bounds, new Bounds(0, 0));
                return bounds;
            }

            GridSizeAssignment estimatedGrid = parent.getProperty(ESTIMATED_GRID_DATA);
            Pair<Integer, Integer> childAreaPosition = parent.getProperty(CHILD_AREA_POSITION);
            
            int width = 0, height = 0;
            float widthScale = 1, heightScale = 1;
            
            
            if (estimatedGrid != null) {
                //calculate the width that lead to the distribution before
                for (int i = 0; i < estimatedGrid.getCalculatedColumnWidths().length; i++) {
                    width += estimatedGrid.getCalculatedColumnWidths()[i];
                }
                for (int i = 0; i < estimatedGrid.getCalculatedRowHeights().length; i++) {
                    height += estimatedGrid.getCalculatedRowHeights()[i];
                }
            }

            final Bounds gridBounds = PlacementUtil.evaluateAreaPlacement(topLeft, bottomRight,
                    parentBounds);

            //if the calculated width differs from the width the parent has, we have to put 
            //that additional width to use
            if (childAreaPosition != null
                    && childAreaPosition.getFirst() > -1
                    && childAreaPosition.getSecond() > -1) {
                //check if there is a childArea and put that width into it's cell

                if (estimatedGrid != null) {
                    this.calculatedColumnWidth = estimatedGrid.getCalculatedColumnWidths().clone();
                    this.calculatedRowHeight = estimatedGrid.getCalculatedRowHeights().clone();

                    if (gridBounds.width - width > TOLERANCE
                            && childAreaPosition.getFirst() < numColumns) {
                        calculatedColumnWidth[childAreaPosition.getFirst()] 
                                += gridBounds.width - width;
                    }
                    if (gridBounds.height - height > TOLERANCE
                            && childAreaPosition.getSecond() < numRows) {
                        calculatedRowHeight[childAreaPosition.getSecond()]
                                += gridBounds.height - height;
                    }
                        
                } else {
                    final boolean[] tempFlexibleCols = new boolean[flexibleCols.length];
                    tempFlexibleCols[childAreaPosition.getFirst()] = true;
                    
                    final boolean[] tempFlexibleRows = new boolean[flexibleRows.length];
                    tempFlexibleRows[childAreaPosition.getSecond()] = true;
                    
                    this.calculatedColumnWidth = computeCellSizes(gridBounds.width, minTotalWidth,
                            columnMaxMinWidth, 1, tempFlexibleCols);
                    
                    this.calculatedRowHeight = computeCellSizes(gridBounds.height, minTotalHeight,
                            rowMaxMinHeight, 1, tempFlexibleRows);
                }
                
            } else {
                // we have to calculate the data for the single rows / cols

                this.calculatedColumnWidth = computeCellSizes(gridBounds.width, minTotalWidth,
                        columnMaxMinWidth, countFlexibleCols, flexibleCols);
                
                this.calculatedRowHeight = computeCellSizes(gridBounds.height, minTotalHeight,
                        rowMaxMinHeight, countFlexibleRows, flexibleRows);
            }
            
            // variables that are later on used to define the bounds of single objects
            final float startX = gridBounds.x;
            final float startY = gridBounds.y;
            
            float currentX = startX;
            float currentY = startY;
            
            for (int i = 0; i < children.size(); i++) {
                final KGridPlacementData gpd = asGridPlacementData(getPlacementData(children.get(i)));
                int column = i % numColumns;
                int row = i / numColumns;

                final Bounds cellBounds = Bounds.of(calculatedColumnWidth[column],
                        calculatedRowHeight[row]);

                final Bounds localBounds = gpd == null
                        ? cellBounds : PlacementUtil.evaluateAreaPlacement(
                            gpd.getTopLeft(), gpd.getBottomRight(), cellBounds);
                
                bounds[i] = localBounds.move(currentX, currentY);
                
                
                currentX += cellBounds.width * widthScale;

                // advance the current y-coordinate if necessary
                if (column == numColumns - 1) {
                    currentY += cellBounds.height * heightScale;
                    // new row => start from left.
                    currentX = startX;
                }
            }
            return bounds;
        }
        
        /**
         * Computes the widths (heights) of the cells of a grid based on the provided parameters
         * that characterize the grid wrt. the x (y) dimension.
         * 
         * @param availableSize
         *              the current width (height) of the grid's parent figure
         * @param requiredSize
         *              the width (height) required by the grid columns (rows)
         * @param requiredSizes
         *              the widths (heights) of the particular columns (rows)
         * @param countFlexibleSizes
         *              the number of flexible columns (rows)
         * @param flexibleSizes
         *              the particular flexibility flags wrt. the particular columns (rows)
         * @return an array of widths (heights) of the particular columns (rows)
         */
        private float[] computeCellSizes(final float availableSize, final float requiredSize,
                final float[] requiredSizes, final float countFlexibleSizes,
                final boolean[] flexibleSizes) {
            final float[] result = new float[requiredSizes.length];
            
            // 
            final float overSize = availableSize - requiredSize;
            final boolean overSized = overSize > 0;
            final float overSizeShare =
                    !overSized ? 0 : overSize / (countFlexibleSizes == 0 ? 1 : countFlexibleSizes);
            
            // the running variable
            float remainingSize = availableSize;
            
            for (int i = 0; i < requiredSizes.length; ++i) {
                if (remainingSize >= 0) {
                    result[i] = requiredSizes[i];
                    if (overSized
                            && (flexibleSizes[i] || countFlexibleSizes == 0
                                    && i + 1 == requiredSizes.length)) {
                        result[i] += overSizeShare;
                    }
                    remainingSize -= result[i];
                } else {
                    break;
                }
            }
            return result;
        }
    }


    /**
     * returns a gridPlacer to calculate bounds of single elements inside the grid.
     * 
     * @param gridPlacement
     *            the grid data
     * @param children
     *            the children to be placed inside the grid with their placementData
     * @return the gridPlacer
     */
    public static GridPlacer getGridPlacementObject(final KGridPlacement gridPlacement,
            final List<KRendering> children) {
        GridPlacer placer = new GridPlacer();
        placer.parent = (KRendering) gridPlacement.eContainer();
        placer.children = children;

        if (children.size() == 0) {
            // no elements to place
            return placer;
        }

        // the following prepares the placer

        // determine the required number of columns and rows
        int setColumns = gridPlacement.getNumColumns();
        placer.topLeft = gridPlacement.getTopLeft();
        placer.bottomRight = gridPlacement.getBottomRight();
        placer.numColumns = setColumns == -1 ? children.size() : setColumns < 1 ? 1 : gridPlacement
                .getNumColumns();
        placer.numRows = (children.size() - 1) / placer.numColumns + 1;
        
        
        placer.flexibleCols = new boolean[placer.numColumns];
        placer.flexibleRows = new boolean[placer.numRows];
        
        final GridSizeAssignment spacingProperty = placer.parent.getProperty(ESTIMATED_GRID_DATA);
        
        final boolean gridSizingAvailable = spacingProperty != null;
        
        final GridSizeAssignment estimatedSpacing =
                !gridSizingAvailable ? null : new GridSizeAssignment(spacingProperty);
        
        if (gridSizingAvailable) {
            //take the data already calculated during the size estimation
            placer.calculatedColumnWidth = estimatedSpacing.getCalculatedColumnWidths().clone();
            placer.calculatedRowHeight = estimatedSpacing.getCalculatedRowHeights().clone();
        }
        if (placer.columnMaxMinWidth == null) {
            //start from scratch
            placer.columnMaxMinWidth = new float[placer.numColumns];
            Arrays.fill(placer.columnMaxMinWidth, 0);
        }
        if (placer.rowMaxMinHeight == null) {
            placer.rowMaxMinHeight = new float[placer.numRows];
            Arrays.fill(placer.rowMaxMinHeight, 0);
        }
        
        Arrays.fill(placer.flexibleCols, true);
        Arrays.fill(placer.flexibleRows, true);

        for (int i = 0; i < children.size(); i++) {
            
            final KRendering r = children.get(i);
            final int column = i % placer.numColumns;
            final int row = i / placer.numColumns;
            
            KGridPlacementData gpd = asGridPlacementData(getPlacementData(r));
            if (gpd == null) {
                //no grid placement data present, create dummy to prevent null pointer exceptions
                gpd = KRenderingFactory.eINSTANCE.createKGridPlacementData();
            }
            
            //determine size of the rendering to be able to size the colum/row accordingly
            final Bounds childMinSize;
            if (gridSizingAvailable) {
                childMinSize = Bounds.of(estimatedSpacing.calculatedColumnWidths[column],
                        estimatedSpacing.calculatedRowHeights[row]);
            } else {
                childMinSize = estimateSize(r, new Bounds(0, 0));

                PlacementUtil.inverselyApplyBoundingBoxKPositions(
                        childMinSize, gpd.getTopLeft(), gpd.getBottomRight());
            }
            
            placer.flexibleCols[column] &= gpd.getFlexibleWidth();
            placer.flexibleRows[row] &= gpd.getFlexibleHeight();

            // determine the maximum of the minimum column widths and row heights
            // e.g. how big must a column be to fit all the minSizes
            
            // size for this element is size of child elements or minSize
            placer.columnMaxMinWidth[column] = KielerMath.maxf(
                    gpd.getMinCellWidth(), childMinSize.width, placer.columnMaxMinWidth[column]);
            placer.rowMaxMinHeight[row] = KielerMath.maxf(
                    gpd.getMinCellHeight(), childMinSize.height, placer.rowMaxMinHeight[row]);
        }

        // calculate the total width and height
        for (int i = 0; i < placer.numColumns; i++) {
            placer.minTotalWidth += placer.columnMaxMinWidth[i];
            placer.countFlexibleCols += placer.flexibleCols[i] ? 1 : 0;
        }
        for (int i = 0; i < placer.numRows; i++) {
            placer.minTotalHeight += placer.rowMaxMinHeight[i];
            placer.countFlexibleRows += placer.flexibleRows[i] ? 1 : 0;
        }
        return placer;
    }


    /**
     * Returns the minimal size of a {@link KContainerRendering} with
     * <code>childPlacement instanceof KGridPlacement</code> and updates the placement data of
     * internal KRenderings if necessary.<br>
     * <br>
     * {@link de.cau.cs.kieler.core.properties.IProperty IProperties} for delivering those data.
     * 
     * @param container
     *            the {@link KContainerRendering} to be evaluated
     * @param parentBounds
     *            the size that is currently assigned to 'container'
     * @return the minimal size
     */
    public static Bounds estimateGridSize(final KContainerRendering container,
            final Bounds parentBounds) {
        int numColumns = ((KGridPlacement) container.getChildPlacement()).getNumColumns();
        final List<KRendering> childRenderings = container.getChildren();

        final int numRows;
        if (numColumns == -1) {
            numColumns = childRenderings.size();
            numRows = 1;
        } else if (numColumns < 2) {
            // if the number of columns is not set or is 1 then in each row is one rendering,
            // so numRows = number of elements to place
            numColumns = 1;
            numRows = childRenderings.size();
        } else {
            // else calculate number of rows based on number of child elements and columns
            numRows = (childRenderings.size() + numColumns - 1) / numColumns;
        }

        // initialize the minimal size for each row and column with zero
        final float[] minColumnWidths = new float[numColumns];
        final float[] minRowHeights = new float[numRows];

        // variables to later store the information where an optional childArea might be placed
        int childAreaRowId = -1;
        int childAreaColId = -1;

        // first evaluate the grid to get position and size of each cell
        // to make the layout as compact as possible but have space to place all children,
        // evaluate the space needed for each child
        for (int k = 0; k < childRenderings.size(); k++) {
            final KRendering currentChild = childRenderings.get(k);

            final int row = k / numColumns;
            final int col = k - row * numColumns;

            final LinkedList<KRendering> path = Lists.newLinkedList();
            if (PlacementUtil.findChildArea(currentChild, path)) {
                // if a childArea is contained in the current child (or the child itself is a childarea)
                // remember the position of the area to be able to calculate
                // the size of the parentNode correctly
                // later this is used to calculate insets based on the cellPosition of the childArea
                childAreaColId = col;
                childAreaRowId = row;
            }

            final Bounds cellSize = estimateSize(currentChild, new Bounds(0, 0));
            
            // evaluate the "winning" KGridPlacementData
            final KGridPlacementData gridData = asGridPlacementData(getPlacementData(currentChild));

            if (gridData != null) {
                final KPosition topLeft = gridData.getTopLeft();
                final KPosition bottomRight = gridData.getBottomRight();

                // apply the cell-local "passe-partout" inversely on the cell size
                PlacementUtil.inverselyApplyBoundingBoxKPositions(cellSize, topLeft, bottomRight);
                
                // apply the minimal width & definitions
                Bounds.max(cellSize, Bounds.of(gridData.getMinCellWidth(), gridData.getMinCellHeight()));
            }
            
            // compare the width and height of the current rendering with the biggest width
            // and height of the corresponding row and column and update the values with the maximum
            minRowHeights[row] = Math.max(minRowHeights[row], cellSize.height);
            minColumnWidths[col] = Math.max(minColumnWidths[col], cellSize.width);
        }

        // store the information of the size of the cells in the KRendering containing the grid
        //  in order to be able to re-use that information later
        // the re-usage of already present property objects is done to reduce the notification flood
        //  and the result re-composition of the node figures in the Piccolo binding

        final boolean deliver = container.eDeliver();
        container.eSetDeliver(false);
        
        final GridSizeAssignment pSpacing = container.getProperty(ESTIMATED_GRID_DATA);
        if (pSpacing != null) {
            pSpacing.setCalculatedSizes(minColumnWidths, minRowHeights);
        } else {
            container.setProperty(ESTIMATED_GRID_DATA, new GridSizeAssignment(minColumnWidths,
                    minRowHeights));
        }
        
        final Pair<Integer, Integer> pCAPos = container.getProperty(CHILD_AREA_POSITION);
        if (pCAPos != null) {
            pCAPos.setFirst(childAreaColId);
            pCAPos.setSecond(childAreaRowId);
        } else {
            container.setProperty(CHILD_AREA_POSITION, Pair.of(childAreaColId, childAreaRowId));
        }

        container.eSetDeliver(deliver);

        // the minimum total bound is the sum of the single column widths and row heights
        final Bounds childBounds = new Bounds(0, 0);

        for (float width : minColumnWidths) {
            childBounds.width += width;
        }
        for (float height : minRowHeights) {
            childBounds.height += height;
        }

        // take insets of the grid itself into consideration
        final KGridPlacement placement = asGridPlacement(container.getChildPlacement());
        
        // increase size according to the space needed by the insets
        PlacementUtil.inverselyApplyBoundingBoxKPositions(childBounds, placement.getTopLeft(),
                placement.getBottomRight());
        
        return childBounds;
    }
}
