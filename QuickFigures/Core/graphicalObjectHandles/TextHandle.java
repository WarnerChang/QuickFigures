package graphicalObjectHandles;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;

import applicationAdapters.CanvasMouseEventWrapper;
import graphicalObjects_BasicShapes.TextGraphic;

public class TextHandle extends SmartHandle {

	public static final int ROTATION_HANDLE = 0, TEXT_FONT_SIZE_HANDLE = 1;
	
	private static final long serialVersionUID = 1L;

	private TextGraphic textItem;

	private Point2D baseLineStart;

	public TextHandle(TextGraphic textGraphic, int rotationHandle) {
		super(0,0);
		this.textItem=textGraphic;
		this.setHandleNumber(rotationHandle);
	}
	
	public Point2D getCordinateLocation() {
		if (this.getHandleNumber()==TEXT_FONT_SIZE_HANDLE) {
				 return textItem.getUpperLeftCornerOfBounds();
			
		}
		
		if (this.getHandleNumber()==ROTATION_HANDLE) {
			 	 return textItem.getBaseLineEnd();
			
		}
		
		
		return super.getCordinateLocation();
	}
	
	public Color getHandleColor() {
		if (textItem.isEditMode()) return Color.red;
		return Color.white;
	}
	
	public int handleSize() {
		if (textItem.isEditMode()) return 1;
		return handlesize;
	}
	
	public void handlePress(CanvasMouseEventWrapper canvasMouseEventWrapper) {
		baseLineStart = textItem.getBaseLineStart();
		
	}

	
	public void handleDrag(CanvasMouseEventWrapper lastDragOrRelMouseEvent) {
		if (this.getHandleNumber()==ROTATION_HANDLE) {
			double angle=TextGraphic.distanceFromCenterOfRotationtoAngle(textItem.getCenterOfRotation(), lastDragOrRelMouseEvent.getCordinatePoint());

				textItem.setAngle(angle);
				
		}
		Point p2 = lastDragOrRelMouseEvent.getCordinatePoint();
		
		if (getHandleNumber()==TEXT_FONT_SIZE_HANDLE ) {
			
			
			Point2D handleLoc=p2;
			
			java.awt.geom.Point2D.Double rotStart = new Point2D.Double();
			java.awt.geom.Point2D.Double rotLeft = new Point2D.Double();
			try {
				/**Sets the font size to match the distance between the drag point and the baseline*/
				textItem.getRotationTransform().createInverse().transform(baseLineStart, rotStart);
				textItem.getRotationTransform().createInverse().transform(handleLoc, rotLeft);
				double newsize =textItem.getFont().getSize();
				double d = Math.abs(rotLeft.y-rotStart.y);
				if(d>3) newsize=d;
				userSetNewSize(newsize);
				//IssueLog.log("distance between pts "+d);
				return;
			
			} catch (NoninvertibleTransformException e) {
				
			}
			/**Sets the font size the old way. not as natural for rotated text*/
			
			double distance2=p2.distance(baseLineStart);
			double distX=p2.getX()-baseLineStart.getX();
			double distY=-p2.getY()+baseLineStart.getY();
			double cos = Math.cos(textItem.getAngle());
			double newsize =textItem.getFont().getSize();
			if (cos!=0)
				newsize=distY/cos;//+distX*Math.sin(getAngle());
			if (newsize>50)newsize =textItem.getFont().getSize();
			
			userSetNewSize(newsize);
		}
		
	}
	public void userSetNewSize(double newsize) {
		if ((int)newsize!=textItem.getFont().getSize()
				&& newsize>1 
				&& newsize<300) {
			Font font = textItem.getFont().deriveFont((float)(newsize));
			textItem.setFont(font);
				}
	}
}