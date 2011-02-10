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
 */

package de.cau.cs.kieler.karma.ptolemy.figurecreation;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.Panel;
import org.eclipse.draw2d.PolygonShape;
import org.eclipse.draw2d.PolylineShape;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.draw2d.Shape;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PointList;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gmf.runtime.draw2d.ui.internal.figures.ImageFigureEx;
import org.eclipse.gmf.runtime.gef.ui.figures.NodeFigure;
import org.eclipse.gmf.runtime.gef.ui.internal.figures.CircleFigure;
import org.eclipse.gmf.runtime.gef.ui.internal.figures.OvalFigure;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Class for creating a draw2d figure out of an svg document.
 * 
 * @author ckru
 * 
 */
public final class FigureParser {

    /**
     * This is a utility class and thus the constructor is hidden.
     */
    private FigureParser() {
        
    }
    
    /**
     * Create an draw2d figure out of an svg document.
     * 
     * @param doc
     *            the svg document
     * @return the draw2d figure equivalent to the svg
     */
    public static IFigure createFigure(final Document doc) {
        Element svgElement = (Element) doc.getElementsByTagName("svg").item(0);
        IFigure rootFigure = new Panel();
        rootFigure.getBounds().setSize(
                new Dimension(Integer.parseInt(svgElement.getAttribute("width")), 
                        Integer.parseInt(svgElement.getAttribute("height"))));
        rootFigure = buildFigure(svgElement, rootFigure);
        return rootFigure;
    }

    /**
     * Parsing the svg and creating figures accordingly. Builds a hirachical structure.
     * 
     * @param root
     *            the top level svg element.
     * @param parentFigure
     *            an invisible figure as container for the actual figures
     * @return a hirachical figure representing the svg
     */
    @SuppressWarnings("restriction")
    private static IFigure buildFigure(final Element root, final IFigure parentFigure) {
        NodeList childList = root.getChildNodes();
        for (int i = 0; i < childList.getLength(); i++) {
            Node child = childList.item(i);
            if (child instanceof Element) {
                Element childElement = (Element) child;
                String tag = childElement.getTagName();
                if (tag.equals("rect")) {
                    RectangleFigure figure = new RectangleFigure();
                    Float x = Float.parseFloat(childElement.getAttribute("x"));
                    Float y = Float.parseFloat(childElement.getAttribute("y"));
                    int width = Integer.parseInt(childElement.getAttribute("width"));
                    int height = Integer.parseInt(childElement.getAttribute("height"));
                    String style = (String) childElement.getAttribute("style");
                    figure.setBounds(new Rectangle(x.intValue(), y.intValue(), width, height));
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                } else if (tag.equals("circle")) {
                    Float x = Float.parseFloat(childElement.getAttribute("cx"));
                    Float y = Float.parseFloat(childElement.getAttribute("cy"));
                    Float r = Float.parseFloat(childElement.getAttribute("r"));
                    String style = (String) childElement.getAttribute("style");
                    CircleFigure figure = new CircleFigure(r.intValue());
                    figure.getBounds().setLocation((x.intValue() + 1 - r.intValue()),
                            (x.intValue() + 1 - r.intValue()));
                    figure.getBounds().setSize((r.intValue() * 2), (r.intValue() * 2));
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                } else if (tag.equals("ellipse")) {
                    OvalFigure figure = new OvalFigure();
                    Float x = Float.parseFloat(childElement.getAttribute("cx"));
                    Float y = Float.parseFloat(childElement.getAttribute("cy"));
                    Float rx = Float.parseFloat(childElement.getAttribute("rx"));
                    Float ry = Float.parseFloat(childElement.getAttribute("ry"));
                    String style = (String) childElement.getAttribute("style");
                    figure.setBounds(new Rectangle(x.intValue(), y.intValue(), rx.intValue(), ry
                            .intValue()));
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                } else if (tag.equals("line")) {
                    float x1 = Float.parseFloat(childElement.getAttribute("x1"));
                    float y1 = Float.parseFloat(childElement.getAttribute("y1"));
                    float x2 = Float.parseFloat(childElement.getAttribute("x2"));
                    float y2 = Float.parseFloat(childElement.getAttribute("y2"));
                    String style = (String) childElement.getAttribute("style");
                    PolylineShape figure = new PolylineShape();
                    figure.setStart(new Point(x1, y1));
                    figure.setEnd(new Point(x2, y2));
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                    figure.getBounds().setSize(figure.getParent().getBounds().getSize().getCopy());
                } else if (tag.equals("polyline")) {
                    String allpoints = childElement.getAttribute("points");
                    String style = (String) childElement.getAttribute("style");
                    String[] pointsarray = allpoints.split(" +");
                    PointList pointList = new PointList();
                    for (String coords : pointsarray) {
                        String[] coordsarray = coords.split(",");
                        float x = Float.parseFloat(coordsarray[0]);
                        float y = Float.parseFloat(coordsarray[1]);
                        pointList.addPoint(new Point(x, y));
                    }
                    PolylineShape figure = new PolylineShape();
                    figure.setPoints(pointList);
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                    figure.getBounds().setSize(figure.getParent().getBounds().getSize().getCopy());
                } else if (tag.equals("polygon")) {
                    String allpoints = childElement.getAttribute("points");
                    String style = (String) childElement.getAttribute("style");
                    String[] pointsarray = allpoints.split(" +");
                    PointList pointList = new PointList();
                    for (String coords : pointsarray) {
                        String[] coordsarray = coords.split(",");
                        float x = Float.parseFloat(coordsarray[0]);
                        float y = Float.parseFloat(coordsarray[1]);
                        pointList.addPoint(new Point(x, y));
                    }
                    PolygonShape figure = new PolygonShape();
                    figure.setPoints(pointList);
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                    figure.getBounds().setSize(figure.getParent().getBounds().getSize().getCopy());
                } else if (tag.equals("text")) {
                    Float x = Float.parseFloat(childElement.getAttribute("x"));
                    Float y = Float.parseFloat(childElement.getAttribute("y"));
                    String style = (String) childElement.getAttribute("style");
                    String text = childElement.getTextContent();
                    text = text.replaceAll("\n", "");
                    text = text.trim();
                    Label figure = new Label(text);
                    figure.setLabelAlignment(PositionConstants.LEFT);
                    // figure.setTextPlacement(Label.WEST);
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));
                    figure.getBounds().setLocation(x.intValue(), y.intValue());
                    figure.getBounds().setSize(figure.getParent().getBounds().width, 8);
                    // figure.setTextPlacement(PositionConstants.EAST);
                    // figure.setTextAlignment(PositionConstants.TOP);
                } else if (tag.equals("image")) {
                    Float x = Float.parseFloat(childElement.getAttribute("x"));
                    Float y = Float.parseFloat(childElement.getAttribute("y"));
                    Float width = Float.parseFloat(childElement.getAttribute("width"));
                    Float height = Float.parseFloat(childElement.getAttribute("height"));
                    String link = (String) childElement.getAttribute("xlink:href");
                    String style = (String) childElement.getAttribute("style");
                    URL url = ClassLoader.getSystemResource(link);
                    if (url == null) {
                        try {
                            url = new URL(link);
                        } catch (MalformedURLException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                    Image img = null;
                    ImageFigureEx figure = null;
                    try {
                        img = new Image(null, url.openStream());
                        figure = new ImageFigureEx(img);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    figure.setBounds(new Rectangle(x.intValue(), y.intValue(), width.intValue(),
                            height.intValue()));
                    applyStyle(figure, style);
                    parentFigure.add(buildFigure(childElement, figure));

                }

            }
        }
        return parentFigure;
    }

    /**
     * Applys the style attribute of the svg element to the figure.
     * 
     * @param figure
     *            the figure whoose style to set
     * @param style
     *            the style as a string
     */
    private static void applyStyle(final IFigure figure, final String style) {
        if (style != null) {
            StringTokenizer t = new StringTokenizer(style, ";");

            while (t.hasMoreTokens()) {
                String string = t.nextToken().trim();
                int index = string.indexOf(":");
                String name = string.substring(0, index);
                String value = string.substring(index + 1);

                if (name.equals("fill")) {
                    figure.setBackgroundColor(lookupColor(value));
                } else if (name.equals("stroke")) {
                    figure.setForegroundColor(lookupColor(value));
                } else if (name.equals("stroke-width")) {
                    Float width = Float.parseFloat(value);
                    if (figure instanceof Shape) {
                        ((Shape) figure).setLineWidth(width.intValue());
                    } else if (figure instanceof NodeFigure) {
                        ((NodeFigure) figure).setLineWidth(width.intValue());
                    }

                }
            }
        }
    }

    /**
     * Make a draw2d color object out of a color name.
     * 
     * @param color
     *            string representation of a color
     * @return the color described by the string. black if not found.
     */
    private static Color lookupColor(final String color) {
        String s = color.toLowerCase();

        if (s.equals("black")) {
            return ColorConstants.black;
        } else if (s.equals("blue")) {
            return ColorConstants.blue;
        } else if (s.equals("cyan")) {
            return ColorConstants.cyan;
        } else if (s.equals("darkgray") || s.equals("darkgrey")) {
            return ColorConstants.darkGray;
        } else if (s.equals("lightgray") || s.equals("lightgrey")) {
            return ColorConstants.lightGray;
        } else if (s.equals("gray") || s.equals("grey")) {
            return ColorConstants.gray;
        } else if (s.equals("green")) {
            return ColorConstants.green;
        } else if (s.equals("orange")) {
            return ColorConstants.orange;
        } else if (s.equals("red")) {
            return ColorConstants.red;
        } else if (s.equals("white")) {
            return ColorConstants.white;
        } else if (s.equals("yellow")) {
            return ColorConstants.yellow;
        } else {
            Color c = ColorConstants.black;
            return c;
        }
    }

}
