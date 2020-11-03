package plotParts.DataShowingParts;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;

import graphicalObjects_BasicShapes.CrossGraphic;
import graphicalObjects_BasicShapes.CircularGraphic;
import graphicalObjects_BasicShapes.RectangularGraphic;
import graphicalObjects_BasicShapes.RegularPolygonGraphic;
import plotParts.DataShowingParts.ScatterPoints.plotPoint;
import utilityClassesForObjects.RectangleEdges;

public class PointModel implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private  RectangularGraphic modelShape=new  RegularPolygonGraphic(new Rectangle(-2,-2,3,3));
	private int pType;
	private double nSides;
	
	public int getPointType() {
		return pType;
	}



	public double getNSides() {
		if (getModelShape() instanceof RegularPolygonGraphic)
		return this.getModelShapeAsPolygon().getNvertex();
		
		return this.nSides;
	}
	
	public void setNVertex(int n) {
		nSides=n;
		if (n>2&&n<20) this.getModelShapeAsPolygon().setNvertex(n);
		if (n==0) setModelShape(new CircularGraphic(getModelShape(), 0));
		if (n==2) {
			setModelShape(new CrossGraphic(getModelShape()));
			
		}
		
	}
	
	public double getPointSize() {
		return this.getModelShape().getObjectWidth();
	}



	public void setPointType(int choiceIndex) {
		pType=choiceIndex;
	}
	public void setPointSize(double number) {
		this.getModelShape().setHeight(number);
		this.getModelShape().setWidth(number);
	}
	

	private RegularPolygonGraphic getModelShapeAsPolygon() {
		
		if (getModelShape()==null||!( getModelShape() instanceof RegularPolygonGraphic )) setModelShape(new  RegularPolygonGraphic(new Rectangle(-2,-2,6,6)));

		return (RegularPolygonGraphic) getModelShape();
	}
	
	/**creates a shape for the data point d. Numbers are 
	 * cordinates to draw, not the values*/
	public RectangularGraphic getShapeGraphicForCordinatePoint(plotPoint pt) {
		RectangularGraphic baseShape =getModelShape().copy();
		baseShape.setLocationType(RectangleEdges.CENTER);;
		baseShape.setLocation(0, 0);
		baseShape.moveLocation(pt.position.getX(), pt.position.getY());
		return baseShape;
		
		
	}
	
	
	/**creates a shape for the data point d. Numbers are 
	 * cordinates to draw, not the values*/
	public Shape getShapeForCordinatePoint(Point2D d) {
		
		Shape baseShape =getBasShape();
		
			AffineTransform tf = AffineTransform.getTranslateInstance(d.getX(), d.getY());
			return tf.createTransformedShape(baseShape);
		
		
	}
	
	private Shape getBasShape() {
		getModelShape().setLocationType(RectangleEdges.CENTER);;
		getModelShape().setLocation(0, 0);
		return getModelShape().getRotationTransformShape();
	}
	
	public RectangularGraphic createBasShapeCopy() {
		RectangularGraphic c = getModelShape().copy();
		c.setLocationType(RectangleEdges.CENTER);;
		c.setLocation(0, 0);
		return c;
	}




	public RectangularGraphic getModelShape() {
		return modelShape;
	}



	public void setModelShape(RectangularGraphic modelShape) {
		this.modelShape = modelShape;
	}

}