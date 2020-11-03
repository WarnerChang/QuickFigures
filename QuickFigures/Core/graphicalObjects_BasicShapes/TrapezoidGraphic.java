package graphicalObjects_BasicShapes;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import graphicalObjectHandles.RectangleEdgeHandle;
import graphicalObjectHandles.SmartHandleList;
import illustratorScripts.ArtLayerRef;
import illustratorScripts.PathItemRef;
import utilityClassesForObjects.RectangleEdges;

public class TrapezoidGraphic extends RectangularGraphic {
	

	{name="Trapezoid";}
	/**
	 * 
	 */
	
	RectangleEdgeParameter parameter=new RectangleEdgeParameter(this, 0.5,  UPPER_LEFT, TOP);
	
	
	
	private static final long serialVersionUID = 1L;
	
	
	public RectangularGraphic blankShape(Rectangle r, Color c) {
		TrapezoidGraphic r1 = new TrapezoidGraphic(r, parameter.getRatioToMaxLength());
		r1.parameter.setRatioToMaxLength(this.parameter.getRatioToMaxLength());
		r1.setDashes(new float[]{100000,1});
		r1.setStrokeWidth(4);
		r1.setStrokeColor(c);
		return r1;
	}
	


	public TrapezoidGraphic copy() {
		TrapezoidGraphic output = new TrapezoidGraphic(this);
		output.parameter.setRatioToMaxLength(parameter.getRatioToMaxLength());
		return output;
	}
	
	public  TrapezoidGraphic(Rectangle rectangle) {
		super(rectangle);
	}
	public TrapezoidGraphic(Rectangle rectangle, double nV) {
		super(rectangle);
		this.parameter.setRatioToMaxLength(nV);
	}
	
	public TrapezoidGraphic(RectangularGraphic r) {
		super(r);
	}

	/**implements a formula to produce a trapezoid*/
	@Override
	public Shape getShape() {
		Path2D.Double path=new Path2D.Double();
		Rectangle2D r = this.getRectangle();
		double rx=getObjectWidth()/2;
		double ry=getObjectHeight()/2;
		
		Point2D startPoint = RectangleEdges.getLocation(LOWER_LEFT, r);
		path.moveTo( startPoint .getX(),startPoint .getY());
		
		Point2D p2 = RectangleEdges.getLocation(UPPER_LEFT, r);
		path.lineTo(p2.getX()+parameter.getRatioToMaxLength()*rx, p2.getY());
		
		Point2D p3 = RectangleEdges.getLocation(UPPER_RIGHT, r);
		if (parameter.getRatioToMaxLength()<1)path.lineTo(p3.getX()-parameter.getRatioToMaxLength()*rx, p3.getY());
		
		Point2D p4 = RectangleEdges.getLocation(LOWER_RIGHT, r);
		path.lineTo(p4.getX(), p4.getY());
	
		path.closePath();
		this.setClosedShape(true);
		
		return path;
		
	}


	
	/**returns the points that define the stroke' handles location and reference location.
	   Precondition: the distance between the two points should be about half the stroke*/
		public Point2D[] getStrokeHandlePoints() {
			PathIterator pi = getShape().getPathIterator(null);
			pi.next();
			if (this.parameter.getRatioToMaxLength()!=1)pi.next();
			double[] d=new double[6];pi.currentSegment(d);
			Point2D location2 =new Point2D.Double(d[0],d[1]);
			pi.next();d=new double[6];pi.currentSegment(d);
			Point2D location1 =new Point2D.Double(d[0],d[1]);
			this.getRotationTransform().transform(location2, location2);
			this.getRotationTransform().transform(location1, location1);
			return calculatePointsOnStrokeBetween(location1, location2);
		}
		
	
	RectangularGraphic rectForIcon() {
		return  blankShape(new Rectangle(0,0,12,10), Color.BLACK);//ArrowGraphic.createDefaltOutlineArrow(this.getFi
	}

	public void createShapeOnPathItem(ArtLayerRef aref, PathItemRef pi) {
		basicCreateShapeOnPathItem(	aref,pi);
	}

	
	
	protected SmartHandleList createSmartHandleList() {
		SmartHandleList list = super.createSmartHandleList();
		RectangleEdgeHandle handle = new RectangleEdgeHandle(this, parameter, Color.green, 20,1, 0.05);
		list.add(0,handle);
		return list;
	}
	
	
	public String getShapeName() {
		return "Trapezoid";
	}
	
	
	}
	
	
	
	
	
