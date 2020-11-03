package utilityClassesForObjects;

import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import utilityClasses1.NumberUse;


	public class PathPoint implements Serializable , Selectable{
		/**
		 * 
		 */

		private static final long serialVersionUID = 1L;
		private Point2D.Double anchor=new Point2D.Double();
		private Point2D.Double curveControl1=new Point2D.Double();
		private Point2D.Double curveControl2=new Point2D.Double();
		
		private boolean isClosePoint=false;//true if the path closes before this point, false otherwise
		private boolean selected;
		private transient boolean primarySelected=false;
		

		public PathPoint(double x, double y) {
			setAnchor(new Point2D.Double(x, y));
			setCurveControl1(new Point2D.Double(x, y));
			setCurveControl2(new Point2D.Double(x, y));
		}
		public PathPoint(Point2D p) {
			this(p.getX(), p.getY());
		}
		
		
		public PathPoint copy() {
			PathPoint p = new PathPoint(this.getAnchor());
			p.setCurveControl1(copyPoint(getCurveControl1()));
			p.setCurveControl2(copyPoint(getCurveControl2()));
			p.setClosePoint(isClosePoint());
			if (isSelected())p.select();
			return p;
		}
		
		
		
		static Point2D.Double copyPoint(Point2D p) {
			return new Point2D.Double(p.getX(),p.getY());
		}
		
		
		
		public void setCurveControl(Point2D p) {
			setCurveControl1(new Point2D.Double(p.getX(), p.getY()));
		}
		
		public void setAnchorPoint(Point2D p) {
			setAnchor(new Point2D.Double(p.getX(), p.getY()));
		}
		
		/**moves a pathPoint a given displacement. will move the curve control points as well as the anchor*/
		public void move(double dx, double dy) {
			getAnchor().x+=dx;
			getAnchor().y+=dy;
			
			moveCurveControl(dx, dy);
			moveCurveControl2(dx, dy);
		}
		public void moveCurveControl2(double dx, double dy) {
			getCurveControl2().x+=dx;
			getCurveControl2().y+=dy;
		}
		public void moveCurveControl(double dx, double dy) {
			getCurveControl1().x+=dx;
			getCurveControl1().y+=dy;
		}
		
		public void applyAffine(AffineTransform f) {
			//Point2D.Double in=new Point2D.Double();
			f.transform(getAnchor(), getAnchor());
			f.transform(getCurveControl1(), getCurveControl1());
			f.transform(getCurveControl2(), getCurveControl2());
		}
		
		static void applyCircletoRectDistort(Ellipse2D e, Rectangle2D r, Point2D p) {
			
		}
		
		static double getRelativeLocationRadians(Point2D origin, Point2D pointOfInterest) {
			return NumberUse .distanceFromCenterOfRotationtoAngle(origin, pointOfInterest);
		}
		
		
		/**returns radians, degress of the curve control point relative to Anchor point*/
		public double[] getCurveControl1LocationsRelativeToAnchor() {
			return NumberUse.getPointAsRadianDegree(getAnchor(), getCurveControl1());
		}
		
		public double[] getCurveControl2LocationsRelativeToAnchor() {
			return NumberUse.getPointAsRadianDegree(getAnchor(), getCurveControl2());
		}
		
	/**moves one of the curve control points to be in a line with the anchor and ther other.
	   set the boolean to true if you want the first point to remain fixed*/
		public void makePointAlongLine(boolean first) {
			double[] d1 = getCurveControl1LocationsRelativeToAnchor() ;
			double[] d2 = getCurveControl2LocationsRelativeToAnchor() ;

			
			if (first)	d2[1]=d1[1]+Math.PI; else d1[1]=d2[1]+Math.PI; 
			
			if (!first)
			setCurveControl1(NumberUse.getPointFromRadDeg(getAnchor(), d1[0],d1[1]));
			if (first)
			setCurveControl2(NumberUse.getPointFromRadDeg(getAnchor(), d2[0],d2[1]));
		
			
		}
		
		/**moves one of the curve control points to be in a line with the anchor and ther other.
		   set the boolean to true if you want the first point to remain fixed*/
			public void makePointEquidistantFromAnchor(boolean first) {
				double[] d1 = getCurveControl1LocationsRelativeToAnchor() ;
				double[] d2 = getCurveControl2LocationsRelativeToAnchor() ;

				
				if (first)	d2[0]=d1[0]; else d1[0]=d2[0]; 
				
				if (!first)
				setCurveControl1(NumberUse.getPointFromRadDeg(getAnchor(), d1[0],d1[1]));
				if (first)
				setCurveControl2(NumberUse.getPointFromRadDeg(getAnchor(), d2[0],d2[1]));
			
				
			}
		
		
		/**moves one of the curve control points to be in a line with the anchor and the other.
		   set the boolean to true if you want the first point to remain fixed*/
			public void makePointsOppositeLine(boolean first) {
				double[] d1 = getCurveControl1LocationsRelativeToAnchor() ;
				double[] d2 = getCurveControl2LocationsRelativeToAnchor() ;

				
				if (first)	d2[1]=d1[1]+Math.PI; else d1[1]=d2[1]+Math.PI; 
				
				if (!first)
				setCurveControl1(NumberUse.getPointFromRadDeg(getAnchor(), d2[0],d1[1]));
				if (first)
				setCurveControl2(NumberUse.getPointFromRadDeg(getAnchor(), d1[0],d2[1]));
			
				
			}
		
			/**While keeping each curve control point at the same distance
			 * from the anchor point, this sets the angle*/
		public void setAngleOfCurveControls(double angle) {
			double[] d1 = getCurveControl1LocationsRelativeToAnchor() ;
			double[] d2 = getCurveControl2LocationsRelativeToAnchor() ;
			setCurveControl1(NumberUse.getPointFromRadDeg(getAnchor(), d1[0],angle));
			setCurveControl2(NumberUse.getPointFromRadDeg(getAnchor(), d2[0],angle+Math.PI));
		}
		
		/**While keeping each curve control point at the same distance
		 * from the anchor point, this sets the angle. The weight parameter
		 * should be between 0 and 1 (1 is max effect)*/
	public void evenOutAngleOfCurveControls(double weight) {
		double[] d1 = getCurveControl1LocationsRelativeToAnchor() ;
		double[] d2 = getCurveControl2LocationsRelativeToAnchor() ;
		if (d1[0]<4 ||d2[0]<4) return;
		if (d2[1]<Math.PI)d2[1]+=2*Math.PI;
		double perfectangle1=(d1[1] +d2[1]-Math.PI)/2;//Calculates the angle needed to make them even
		double perfectangle2=perfectangle1+Math.PI;
		if (perfectangle2>2*Math.PI) perfectangle2-=2*Math.PI;
		if (d2[1]>2*Math.PI) d2[1]-=2*Math.PI;
		
		double newangle1=perfectangle1*weight+d1[1]*(1-weight);
		double newangle2=perfectangle2*weight+d2[1]*(1-weight);
		
		setCurveControl1(NumberUse.getPointFromRadDeg(getAnchor(), d1[0],newangle1));
		setCurveControl2(NumberUse.getPointFromRadDeg(getAnchor(), d2[0],newangle2));
	}
		
	

		public boolean isClosePoint() {
			return isClosePoint;
		}



		public void setClosePoint(boolean isClosePoint) {
			this.isClosePoint = isClosePoint;
		}
		public Point2D.Double getCurveControl2() {
			return curveControl2;
		}
		public void setCurveControl2(Point2D m2b) {
			this.curveControl2 = new Point2D.Double(m2b.getX(),m2b.getY()) ;
		}
		public Point2D.Double getAnchor() {
			return anchor;
		}
		public void setAnchor(Point2D.Double anchor) {
			this.anchor = anchor;
		}
		@Override
		public void select() {
			selected=true;
			
		}
		@Override
		public void deselect() {
			selected=false;
			primarySelected =false;
		}
		@Override
		public boolean isSelected() {
			// TODO Auto-generated method stub
			return selected;
		}
		public Point2D.Double getCurveControl1() {
			return curveControl1;
		}
		public void setCurveControl1(Point2D curveControl1) {
			this.curveControl1 = new Point2D.Double(curveControl1.getX(), curveControl1.getY());
		}
		@Override
		public boolean makePrimarySelectedItem(boolean isFirst) {
			// TODO Auto-generated method stub
			return false;
		}
		public boolean isPrimarySelected() {
			return primarySelected;
		}
		public void setPrimarySelected(boolean primarySelected) {
			if(primarySelected&&!isSelected()) this.select();
			
			this.primarySelected = primarySelected;
		}
		
	}