package uiForAnimations;

import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Area;

import javax.swing.Icon;
import animations.KeyFrameCompatible;
import graphicActionToombar.CurrentSetInformerBasic;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_BasicShapes.ArrowGraphic;
import graphicalObjects_BasicShapes.BasicShapeGraphic;
import graphicalObjects_BasicShapes.ShapeGraphic;
import standardDialog.GraphicDisplayComponent;

public class KeyFrameAssign extends BasicTimeLineOperator {

	
	
	
	boolean update=false;
		
	public KeyFrameAssign(boolean update) {this.update=update;}

	@Override
	public void run() {
		for(ZoomableGraphic item: array) {
			if (item==null) continue;
			actioinOnSelected(item);
			
		}
		
	}
	
	@Override
	public String getMenuCommand() {
		if (update)return "Update Key Frame";
		return "Make Key Frame";
	}
	
	/**adds a key frame*/
	public void actioinOnSelected(ZoomableGraphic selectedItem) {
		
		
		
		
		
		if (selectedItem instanceof KeyFrameCompatible ) {
			KeyFrameCompatible  m=(KeyFrameCompatible ) selectedItem;
			int frame = new CurrentSetInformerBasic().getCurrentlyActiveDisplay().getCurrentFrame();
			
			if (update)m.getOrCreateAnimation().updateKeyFrame(frame); else
			m.getOrCreateAnimation().recordKeyFrame(frame);
		}
		
		
		
		
	}
	

	static ShapeGraphic createCartoonX(boolean selected) {
		Point p1=new Point(2,-3);
		Point p2=new Point(17,15);
		
			ArrowGraphic ag1 =ArrowGraphic.createDefaltOutlineArrow(Color.black, Color.black);
			ag1.setStrokeWidth(4);
			if (selected) ag1.getBackGroundShape().setFillColor(Color.red);
			ag1.setPoints(p1, p2);
			ag1.setHeadnumber(0);
			
			ArrowGraphic ag2 = ag1.copy();
			p1=new Point(17,2);
			p2=new Point(10,10);
			ag2.setPoints(p1, p2);
			
			ArrowGraphic ag3 = ag2.copy();
			ag3.moveLocation(2, 8); ag3.moveLocation(3, -3);
			
			Area s= ag2.getOutline();
			s.add(ag1.getOutline()); s.add(ag3.getOutline());
			BasicShapeGraphic output = new BasicShapeGraphic(s);
			output.copyAttributesFrom(ag2);
			output.setStrokeColor(Color.black);
			output.setFillColor(Color.yellow);
			output.setStrokeWidth(2);
			output.setFilled(true);
			output.setAntialize(true);
			return output;
	}
	
	public GraphicDisplayComponent getDeleteIcon(boolean selected) {
		 GraphicDisplayComponent output = new GraphicDisplayComponent(createCartoonX( selected));
		 
		 output.setRelocatedForIcon(false);
		 //output.setSelected(selected);
		 return output;
	}
	
	
	public Icon getIcon() {
		return  getDeleteIcon(true);
	}

}