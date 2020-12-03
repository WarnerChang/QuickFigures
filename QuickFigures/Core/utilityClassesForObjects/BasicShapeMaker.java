package utilityClassesForObjects;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.io.Serializable;

import applicationAdapters.ToolbarTester;
import graphicalObjects_BasicShapes.PathGraphic;
import imageDisplayApp.GraphicContainingImage;
import imageDisplayApp.ImageWindowAndDisplaySet;
import standardDialog.DialogItemChangeEvent;
import standardDialog.SwingDialogListener;
import storedValueDialog.ReflectingFieldSettingDialog;

public abstract class BasicShapeMaker  implements ShapeMaker, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	
	public void addHorizontalFlippedIn(PathPointList path, double x) {
		
		PathPointList tobeAdded = path;
		tobeAdded=path.getTransformedCopy(createHFlip(x)).getOrderFlippedCopy();
		path.concatenate(tobeAdded);
	}
	
	
	public static AffineTransform createHFlip(double x) {
		AffineTransform out = AffineTransform.getTranslateInstance(x, 0);
		out.concatenate(AffineTransform.getScaleInstance(-1, 1));
		out.concatenate(AffineTransform.getTranslateInstance(-x, 0));
		return out;
	}
	
	public static AffineTransform createVFlip(double y) {
		AffineTransform out = AffineTransform.getTranslateInstance(0, y);
		out.concatenate(AffineTransform.getScaleInstance(1, -1));
		out.concatenate(AffineTransform.getTranslateInstance(0, -y));
		return out;
	}
	

	protected static void showShape(ShapeMaker  cpc) {
		GraphicContainingImage gl=new GraphicContainingImage();
		
		
		
		PathGraphic  pg=createDefaultCartoon(cpc);
		gl.addItemToImage(pg);
		gl.setWidth(900);
		gl.setHeight(700);
		 ImageWindowAndDisplaySet.show(gl);
		 ToolbarTester.showToolSet();
		 
		 createUpdatingDialog(pg, cpc);
			
	}
	
	public static PathGraphic createDefaultCartoon(ShapeMaker  cpc) {
		PathGraphic pg = new PathGraphic(cpc.getPathPointList() );
		
		pg.setStrokeColor(Color.gray);
		pg.setDashes(new float[]{0});
		pg.setAntialize(true);
		pg.setFillColor(Color.green);
		pg.setClosedShape(true);
		return pg;
	}
	
	public static void createUpdatingDialog(PathGraphic pg,ShapeMaker  cpc ) {
		 ReflectingFieldSettingDialog fsd = new ReflectingFieldSettingDialog(cpc);
			fsd.addDialogListener(new shapeUpdater(cpc, pg));
			fsd.showDialog();
	}
	
	static class shapeUpdater implements SwingDialogListener {

		private ShapeMaker sm;
		private PathGraphic pg;

		public  shapeUpdater(ShapeMaker sm, PathGraphic pg) {
			this.sm=sm;
			this.pg=pg;
		} 
		
		@Override
		public void itemChange(DialogItemChangeEvent event) {
			if (sm!=null&&pg!=null) {
				pg.setPoints(sm.getPathPointList());
				pg.updatePathFromPoints();
				pg.updateDisplay();
			}
			
		}
		
	}
	
	public void moveAllIntoView(PathPointList  p) {
		Rectangle bound = p.createPath(true).getBounds();
		double x=0;
		double y=0;
			 x=bound.x;
			y=bound.y;
			AffineTransform aft = AffineTransform.getTranslateInstance(-x, -y);
			p.applyAffine(aft);
	}



	

}
