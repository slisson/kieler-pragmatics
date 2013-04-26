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
package de.cau.cs.kieler.klighd.piccolo;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.LineAttributes;
import org.eclipse.swt.graphics.Pattern;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.TextLayout;

import de.cau.cs.kieler.klighd.KlighdConstants;
import de.cau.cs.kieler.klighd.piccolo.util.RGBGradient;
import edu.umd.cs.piccolo.util.PAffineTransform;
import edu.umd.cs.piccolox.swt.SWTGraphics2D;
import edu.umd.cs.piccolox.swt.SWTShapeManager;

/**
 * Standard implementation of {@link KlighdSWTGraphics} based on Piccolo2D's {@link SWTGraphics2D}.
 * It's aim is to rewind the supplements added to the Piccolo implementation.
 * 
 * @author chsch
 */
public class KlighdSWTGraphicsImpl extends SWTGraphics2D implements KlighdSWTGraphics {

    /**
     * Constructor for SWTGraphics2D.
     * 
     * @param gc
     *            The Eclipse Graphics Context onto which all Graphics2D operations are
     *            delegating
     * @param device
     *            Device onto which ultimately all gc operations are drawn onto
     */
    public KlighdSWTGraphicsImpl(final GC gc, final Device device) {
        super(gc, device);
    }

    /**
     * Constructor for SWTGraphics2D.
     * 
     * @param gc
     *            The Eclipse Graphics Context onto which all Graphics2D operations are
     *            delegating
     * @param tl
     *            The Eclipse Graphics Context onto which all Graphics2D operations are
     *            delegating
     * @param device
     *            Device onto which ultimately all {@link GC} operations are drawn onto
     */
    public KlighdSWTGraphicsImpl(final GC gc, final TextLayout tl, final Device device) {
        super(gc, tl, device);
    }

    private static final Rectangle2D.Float TEMP_RECT = new Rectangle2D.Float();

    /**
     * {@inheritDoc}
     */
    public void setDevice(final Device theDevice) {
        this.device = theDevice;
    }

    /**
     * {@inheritDoc}
     */
    public void setGC(final GC theGc) {
        this.gc = theGc;
        this.gc.setAntialias(SWT.ON);
    }

    /**
     * {@inheritDoc}
     */
    public void setAlpha(final int alpha) {
        super.setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void setColor(final RGB foregroundColor) {
        super.setColor(foregroundColor);
    }

    /**
     * {@inheritDoc}
     */
    public LineAttributes getLineAttributes() {
        return gc.getLineAttributes();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setLineAttributes(final LineAttributes attributes) {
        gc.setLineAttributes(attributes);
        // the following line keeps the lineWidth in mind in order to
        //  be able to adjust it according to the zoom factor
        this.setLineWidth(attributes.width);
    }

    /**
     * {@inheritDoc}
     */
    public float getLineWidth() {
        return super.getLineWidth();
    }
    
    /**
     * {@inheritDoc}
     */
    public void setPattern(final RGBGradient gradient, final Rectangle2D bounds) {
        TEMP_RECT.setRect(bounds);
        SWTShapeManager.transform(TEMP_RECT, this.getTransform());

        Point2D[] points = computePatternPoints(TEMP_RECT, Math.toRadians(gradient.getAngle()));

        final float curAlpha = (float) this.getAlpha();
        final int alpha1 = (int) (gradient.getAlpha1() * (curAlpha / KlighdConstants.ALPHA_FULL_OPAQUE));
        final int alpha2 = (int) (gradient.getAlpha2() * (curAlpha / KlighdConstants.ALPHA_FULL_OPAQUE));

        this.setPattern(new Pattern(this.getGraphicsContext().getDevice(), (float) points[0]
                .getX(), (float) points[0].getY(), (float) points[1].getX(), (float) points[1]
                .getY(), this.getColor(gradient.getColor1()), alpha1, this.getColor(gradient
                .getColor2()), alpha2));
    }

    /**
     * {@inheritDoc}
     */
    public void setBackgroundPattern(final RGBGradient gradient, final Rectangle2D bounds) {
        TEMP_RECT.setRect(bounds);
        SWTShapeManager.transform(TEMP_RECT, this.getTransform());

        Point2D[] points = computePatternPoints(TEMP_RECT, Math.toRadians(gradient.getAngle()));

        final float curAlpha = (float) this.getAlpha();
        final int alpha1 = (int) (gradient.getAlpha1() * (curAlpha / KlighdConstants.ALPHA_FULL_OPAQUE));
        final int alpha2 = (int) (gradient.getAlpha2() * (curAlpha / KlighdConstants.ALPHA_FULL_OPAQUE));

        this.setBackgoundPattern(new Pattern(this.getGraphicsContext().getDevice(),
                (float) points[0].getX(), (float) points[0].getY(), (float) points[1].getX(),
                (float) points[1].getY(), this.getColor(gradient.getColor1()), alpha1, this
                        .getColor(gradient.getColor2()), alpha2));
    }


    /*----------------------------------------------------*/
    /* overrides of draw methods to realize anti-aliasing */
    /*----------------------------------------------------*/
    
    /**
     * {@inheritDoc}
     */
    public void drawRect(final double x, final double y, final double width, final double height) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawRect(x, y, width, height);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawRoundRect(final double x, final double y, final double width,
            final double height, final double arcWidth, final double arcHeight) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawRoundRect(x, y, width, height, arcWidth, arcHeight);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawOval(final double x, final double y, final double width, final double height) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawOval(x, y, width, height);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawPolygon(final double[] points) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawPolygon(points);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawArc(final double x, final double y, final double width, final double height,
            final double startAngle, final double extent) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawArc(x, y, width, height, startAngle, extent);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawPolyline(final double[] points) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawPolyline(points);
        
        setAlpha(alpha);
    }
    
    /**
     * {@inheritDoc}
     */
    public void drawGeneralPath(final GeneralPath gp) {
        int alpha = getAlpha();
        antiAliase();
        updateCustomLineStyle(); 
        
        super.drawGeneralPath(gp);
        
        setAlpha(alpha);
    }
    
    /*-----------------------------*/
    /* overrides of legacy methods */
    /*-----------------------------*/
    
    /**
     * {@inheritDoc}
     * @deprecated see {@link KlighdSWTGraphics#draw(Shape)}.
     */
    public void draw(final Shape shape) {
        final String msg = "KLighD: Invocation of KlighdSWTGraphics#draw(Shape) is not supported as"
                + " the position and size data cannot be scaled properly for general shapes.";
        throw new UnsupportedOperationException(msg); 
    }
    
    /**
     * {@inheritDoc}
     * @deprecated see {@link KlighdSWTGraphics#fill(Shape)}.
     */
    public void fill(final Shape shape) {
        final String msg = "KLighD: Invocation of KlighdSWTGraphics#fill(Shape) is not supported as"
                + " the position and size data cannot be scaled properly for general shapes.";
        throw new UnsupportedOperationException(msg); 
    }
    
    /*-------------------------*/
    /* internal helper methods */
    /*-------------------------*/

    /**
     * This method realizes a poor man anti aliasing if the adjusted line width drawn on the screen
     * is less than two.
     */
    protected void antiAliase() {
        final int alpha = getAlpha();
        final float lineWidth = this.getTransformedLineWidthFloat();
        final int factor = 100;
        
        if (lineWidth < 2) {
            double adjustedAlpha = 
                    alpha * (KlighdConstants.ALPHA_FULL_OPAQUE - (2 - lineWidth) * factor)
                            / KlighdConstants.ALPHA_FULL_OPAQUE;
            this.setAlpha((int) adjustedAlpha);
        }
    }
    
    /** A rectangle object used to adjust custom line dash configurations wrt. the current transform. */
    private static final Rectangle2D.Float TEMP_DASH_RECT = new Rectangle2D.Float(); 

    /**
     * A helper function that adjusts custom dash patterns and dash offset according the given transform,
     * i.e. the zoom factor
     * 
     */
    private void updateCustomLineStyle() {
        if (this.gc.getGCData().lineStyle == SWT.LINE_CUSTOM) {
            
            // adjust the pattern
            float[] dashPattern = this.gc.getGCData().lineDashes;
            for (int i = 0; i < dashPattern.length; i++) {
                TEMP_DASH_RECT.setRect(0, 0, dashPattern[i], dashPattern[i]);
                SWTShapeManager.transform(TEMP_DASH_RECT, transform);
                dashPattern[i] = TEMP_DASH_RECT.width;
            }
            
            // adjust the offset
            float dashOffset = this.gc.getGCData().lineDashesOffset;
            TEMP_DASH_RECT.setRect(0, 0, dashOffset, dashOffset);
            SWTShapeManager.transform(TEMP_DASH_RECT, transform);
            this.gc.getGCData().lineDashesOffset = TEMP_DASH_RECT.width;
        }
    }
    
    private Point2D[] computePatternPoints(final Rectangle2D bounds, final double angle) {

        PAffineTransform t = new PAffineTransform();
        t.rotate(-angle, bounds.getCenterX(), bounds.getCenterY());
        t.transform(bounds, bounds);

        Rectangle2D.Float incBounds = (Rectangle2D.Float) bounds.getBounds2D();
        Point2D p1 = new Point2D.Double(incBounds.getMinX(), incBounds.getCenterY());
        Point2D p2 = new Point2D.Double(incBounds.getMaxX(), incBounds.getCenterY());

        t.inverseTransform(p1, p1);
        t.inverseTransform(p2, p2);

        return new Point2D[] { p1, p2 };
    }
}