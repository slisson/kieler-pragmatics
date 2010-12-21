/**
 * 
 */
package de.cau.cs.kieler.rail.editor.features;

import javax.swing.JOptionPane;

import org.eclipse.graphiti.features.IDirectEditingInfo;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.impl.AbstractAddFeature;
import org.eclipse.graphiti.mm.GraphicsAlgorithmContainer;
import org.eclipse.graphiti.mm.algorithms.Ellipse;
import org.eclipse.graphiti.mm.algorithms.Polygon;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.services.IPeCreateService;
import org.eclipse.graphiti.util.ColorConstant;
import org.eclipse.graphiti.util.IColorConstant;

import de.cau.cs.kieler.core.model.graphiti.IStyleProvider;

import de.cau.cs.kieler.rail.Topologie.SpecializedVertices.EOrientation;
import de.cau.cs.kieler.rail.Topologie.SpecializedVertices.Einbruchsknoten;
import de.cau.cs.kieler.rail.Topologie.SpecializedVertices.Stumpfgleisknoten;
import de.cau.cs.kieler.rail.Topologie.SpecializedVertices.Weichenknoten;
import de.cau.cs.kieler.rail.editor.StyleProvider;

/**
 * @author hdw
 * @param <TypeFeatures>
 *
 */
public class AddFeature extends AbstractAddFeature {

	/** the style provider. */ 
    protected IStyleProvider styleProvider;
    
    protected static final IColorConstant CLASS_TEXT_FOREGROUND =
        new ColorConstant(51, 51, 153);
 
    protected static final IColorConstant CLASS_FOREGROUND =
        new ColorConstant(255, 102, 0);
 
    protected static final IColorConstant CLASS_BACKGROUND =
        new ColorConstant(255, 204, 153);
    
    TypeFeatures type;
    
	public AddFeature(IFeatureProvider fp, final IStyleProvider thestyleProvider, TypeFeatures type) {
		super(fp);
		this.styleProvider = thestyleProvider;
		this.type = type;
	}


	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#canAdd(org.eclipse.graphiti.features.context.IAddContext)
	 */
	//public abstract boolean canAdd(IAddContext context);
    public boolean canAdd(IAddContext context) {
        // check if user wants to add a Einbruchsknoten
        if (isInstanceof(context.getNewObject())) {
            // check if user wants to add to a diagram
            if (context.getTargetContainer() instanceof Diagram) {
                return true;
            }
        }
        return false;
    }

	/* (non-Javadoc)
	 * @see org.eclipse.graphiti.func.IAdd#add(org.eclipse.graphiti.features.context.IAddContext)
	 */
	public  PictogramElement add(IAddContext context){
		PictogramElement pe=null;
		switch (type){
		case BREANCH:
			pe = addBreach(context);
			break;
		case DEADENDVERTEX:
			pe = addDeadEndVertex(context);
			break;
		case SWITCHVERTEX_LEFT:
			pe = addSwitchVertex(context,EOrientation.LINKS);
			break;
		case SWITCHVERTEX_RIGHT:
			pe = addSwitchVertex(context,EOrientation.RECHTS);
			break;
		}
		layoutPictogramElement(pe);
		return pe;
	}

	public boolean isInstanceof(Object object){
		switch (type){
		case BREANCH:
			return object instanceof Einbruchsknoten;
		case DEADENDVERTEX:
			return object instanceof Stumpfgleisknoten;
		case SWITCHVERTEX_LEFT:
		case SWITCHVERTEX_RIGHT:
			return object instanceof Weichenknoten;
		}
		return false;
	}
	
	private PictogramElement addBreach(IAddContext context){
		Einbruchsknoten addedClass = (Einbruchsknoten) context.getNewObject();
        Diagram targetDiagram = (Diagram) context.getTargetContainer();
 
        // CONTAINER SHAPE WITH CIRCLE
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        ContainerShape containerShape =
             peCreateService.createContainerShape(targetDiagram, true);
 
        
        
        // define a default size for the shape
        int width = 50;
        int height = 50; 
        IGaService gaService = Graphiti.getGaService();
 
        System.out.println(context.getHeight());
        System.out.println(context.getWidth());
        
        {
        	//Create Ellipse
            Ellipse ellipse = gaService.createEllipse( containerShape);
            ellipse.setLineWidth(3);
            ellipse.setFilled(false);
            ellipse.setForeground(manageColor(0,0,0));
            ellipse.setBackground(manageColor(255,255,255));
            
            gaService.setLocationAndSize(ellipse,
                    context.getX(), context.getY()+10, width, height-10);
            
            //ellipse.setStyle(styleProvider.getStyle("1"));
            
            
 
            // if added Class has no resource we add it to the resource 
            // of the diagram
            // in a real scenario the business model would have its own resource
            if (addedClass.eResource() == null) {
                     getDiagram().eResource().getContents().add(addedClass);
            }
            // create link and wire it
            link(containerShape, addedClass);
        }
 
        // SHAPE WITH TEXT
        {
            // create shape for text
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set text graphics algorithm
            //Compromise only
            String ans;
            ans = JOptionPane.showInputDialog(null, "Enter Label");
            addedClass.setName(ans);  //TODO ???
            Text text = gaService.createDefaultText(shape, addedClass.getName()); //addedClass.getName()
            text.setForeground(manageColor(CLASS_TEXT_FOREGROUND));
            text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
            text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
            text.getFont().setBold(true);
            gaService.setLocationAndSize(text, 0, 0, width, 20);
 
            // create link and wire it
            link(shape, addedClass);
        }
        
        // add a chopbox anchor to the shape
        peCreateService.createChopboxAnchor(containerShape);
  
        // call the layout feature
        layoutPictogramElement(containerShape);
        
        return containerShape;
	}
	private PictogramElement addDeadEndVertex(IAddContext context){
		Stumpfgleisknoten addedClass = (Stumpfgleisknoten) context.getNewObject();
        Diagram targetDiagram = (Diagram) context.getTargetContainer();
 
        // CONTAINER SHAPE WITH ROUNDED RECTANGLE
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        ContainerShape containerShape =peCreateService.createContainerShape(targetDiagram, true);
        peCreateService.createChopboxAnchor(containerShape);
        
        
     // define a default size for the shape
        //TODO make constants (50)
        int width = context.getWidth() != 50 ? 50 : context.getWidth();
        int height = context.getHeight() != 50 ? 50 : context.getHeight();
        IGaService gaService = Graphiti.getGaService();
 
        Rectangle portContainer = gaService.createInvisibleRectangle(containerShape);
        
        gaService.setLocationAndSize(portContainer,
                context.getX() ,
                context.getY() ,
                width ,
                height) ;
        {
        	
        	Rectangle rectangleShape = gaService.createRectangle(portContainer);
            
 
            // if added Class has no resource we add it to the resource 
            // of the diagram
            // in a real scenario the business model would have its own resource
            if (addedClass.eResource() == null) {
                     getDiagram().eResource().getContents().add(addedClass);
            }
            // create link and wire it
            //link(containerShape, addedClass);
        }
 
       
        // SHAPE WITH LINE
        {
            // create shape for line
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set graphics algorithm
            Polyline polyline =
                gaService.createPolyline(shape, new int[] { width/2, 0, width/2, height });
            polyline.setStyle(styleProvider.getStyle(StyleProvider.DEFAULT_STYLE));
            //gaService.setLocationAndSize(polyline,width/2, 0, width/2, height);
        }
        
 
        // SHAPE WITH TEXT
        {
            // create shape for text
            Shape shapeLabel = peCreateService.createShape(containerShape, false);
 
            // create and set text graphics algorithm
            addedClass.setName("Test");
            Text text = gaService.createDefaultText(shapeLabel, addedClass.getName());
            text.setForeground(manageColor(CLASS_TEXT_FOREGROUND));
            text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
            text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
            text.getFont().setBold(true);
            gaService.setLocationAndSize(text, 0, 0, width, 20);
 
            // create link and wire it
            link(shapeLabel, addedClass);
            
            /*


            //TODO ???? don't know if this is necessary 
         // set container shape for direct editing after object creation
            IDirectEditingInfo directEditingInfo = getFeatureProvider().getDirectEditingInfo();
            directEditingInfo.setMainPictogramElement(containerShape);
            // set shape and graphics algorithm where the editor for
            // direct editing shall be opened after object creation
            directEditingInfo.setPictogramElement(shapeLabel);
            directEditingInfo.setGraphicsAlgorithm(text);
            
            */
            
            
        }
        
        //TODO maybe to delete (at the other places maybe too)
        // add a chopbox anchor to the shape
        peCreateService.createChopboxAnchor(containerShape);
  
        //TODO maybe kick out
        // call the layout feature
        layoutPictogramElement(containerShape);
        
 
        return containerShape;
    }
	
	private PictogramElement addSwitchVertex(IAddContext context, EOrientation orientatin){
		//create Switch from source
		Weichenknoten switchVertex = (Weichenknoten) context.getNewObject();
		switchVertex.setAbzweigendeLage(orientatin);
		
		Diagram targetDiagram = (Diagram) context.getTargetContainer();
		
		// CONTAINER SHAPE
		IPeCreateService peCreateService = Graphiti.getPeCreateService();
		ContainerShape containerShape =
            peCreateService.createContainerShape(targetDiagram, true);
		
		IGaService gaService = Graphiti.getGaService();
		
		//virtual Rectangle
		Rectangle R = gaService.createRectangle(containerShape);
		R.setStyle(styleProvider.getStyle(StyleProvider.DEFAULT_STYLE));
		R.setForeground(manageColor(0, 0, 0));
		
		//Line (straight line)
		Shape shapep=peCreateService.createShape(containerShape, false);
		Polyline polyline = gaService.createPolyline(shapep,new int[]{0,25,25,25});
		
		
		//Line (30°)
		Shape shapep30=peCreateService.createShape(containerShape, false);
		Polyline polyline30 = gaService.createPolyline(shapep30,new int[]{20,25,0,(int) (25*0.577350269)});
		
		//TODO draw small triangle
		//Polygon 
		/*
		Shape shapePG = peCreateService.createShape(containerShape, false);
		Polygon polygon = gaService.createPolygon(shapePG,new int[]{20,25,10,(int) (30*0.577350269),50,25});
		polygon.setStyle(styleProvider.getStyle(StyleProvider.DEFAULT_STYLE));
		polygon.setForeground(manageColor(255, 0, 0));
		polygon.setFilled(true);
		*/
		
		//link(shapePG, switchVertex);
		//0.577350269
		//Text
		Shape shape = peCreateService.createShape(containerShape, false);
		
		switchVertex.setName("Bla");
		Text text = gaService.createDefaultText(shape, switchVertex.getName());
		text.setStyle(styleProvider.getStyle(StyleProvider.DEFAULT_STYLE));
		text.setX(context.getX());
		text.setY(context.getY());
		
		link(containerShape, switchVertex);
		
		return containerShape;
	}
	/*
	private PictogramElement addSwitchVertex(IAddContext context, EOrientation orientatin) {
		Weichenknoten addedClass = (Weichenknoten) context.getNewObject();
		addedClass.setAbzweigendeLage(orientatin);
        Diagram targetDiagram = (Diagram) context.getTargetContainer();
 
     // CONTAINER SHAPE WITH ROUNDED RECTANGLE
        IPeCreateService peCreateService = Graphiti.getPeCreateService();
        ContainerShape containerShape =
             peCreateService.createContainerShape(targetDiagram, true);
 
     // define a default size for the shape
        int width = context.getWidth() <= 0 ? 50 : context.getWidth();
        int height = context.getHeight() <= 0 ? 50 : context.getHeight();
        IGaService gaService = Graphiti.getGaService();
        
        System.out.println(context.getHeight());
        System.out.println(context.getWidth());
        
        {
			Rectangle R = gaService.createRectangle(containerShape);
			R.setBackground(manageColor(255,255,255));
			R.setForeground(manageColor(255,255,255));
			R.setHeight(height);
            R.setWidth(width);
            R.setX(context.getX());
            R.setY(context.getY());
			
            // if added Clas has no resource we add it to the resource 
            // of the diagram
            // in a real scenario the business model would have its own resource
            if (addedClass.eResource() == null) {
                     getDiagram().eResource().getContents().add(addedClass);
            }
            // create link and wire it
            link(containerShape, addedClass);
        }
 
        
        
        
        // SHAPE WITH LINE
        {
            // create shape for line
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set graphics algorithm
            Polyline polyline =
                gaService.createPolyline(shape, new int[] { context.getX()+width/2, context.getY(),context.getX()+ width/2,context.getY()+ height });
            polyline.setForeground(manageColor(100,0,100));
            polyline.setLineWidth(2);
            polyline.setHeight(height);
            polyline.setWidth(width);
            polyline.setX(context.getX());
            polyline.setY(context.getY());
            gaService.setLocationAndSize(polyline,width/2, 0, width/2, height);
        }
        
        
     // triangle through points: top-middle, bottom-right, bottom-left
        //50, 0, 100, 100, 0, 100
        {
        int xy[] = new int[] { context.getX(), context.getY() + height/2,context.getX()+ width,context.getY()+ height /2};
		//IGaService gaService = Graphiti.getGaService();
        Polyline p = gaService.createPolyline(containerShape, xy);
        Polyline p2 = gaService.createPolyline(containerShape, new int[] { context.getX(), context.getY()+height/2,context.getX()+ width, context.getY()+(int)(0.57735026919*height /2)});
        
        gaService.setLocationAndSize(p,context.getX(), context.getY() ,width,height );
        gaService.setLocationAndSize(p2,context.getX(), context.getY() ,width,height );
        }
 
        // SHAPE WITH TEXT
        {
            // create shape for text
            Shape shape = peCreateService.createShape(containerShape, false);
 
            // create and set text graphics algorithm
            addedClass.setName("Test");
            Text text = gaService.createDefaultText(shape, addedClass.getName());
            text.setForeground(manageColor(CLASS_TEXT_FOREGROUND));
            text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
            text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
            text.getFont().setBold(true);
            gaService.setLocationAndSize(text, context.getX(), context.getY(), width, height);
 
            // create link and wire it
            link(shape, addedClass);
        }
        

        
        
        // add a chopbox anchor to the shape
        peCreateService.createChopboxAnchor(containerShape);
  
        // call the layout feature
        layoutPictogramElement(containerShape);
        
 
        return containerShape;
	}*/
}
