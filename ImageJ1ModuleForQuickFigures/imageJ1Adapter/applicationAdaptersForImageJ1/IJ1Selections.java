package applicationAdaptersForImageJ1;

import java.awt.BasicStroke;
import java.awt.Rectangle;

import genericMontageKit.SelectionManager;
import graphicalObjects_BasicShapes.BasicShapeGraphic;
import ij.ImagePlus;
import ij.gui.ShapeRoi;
import utilityClassesForObjects.LocatedObject2D;

public class IJ1Selections extends SelectionManager {
	private ImagePlus imagePlus;

	public IJ1Selections(ImagePlus imp) {
		this.setImagePlus(imp);
	}

	public ImagePlus getImagePlus() {
		return imagePlus;
	}

	public void setImagePlus(ImagePlus imagePlus) {
		this.imagePlus = imagePlus;
	}
public void removeSelections() {
	super.removeSelections();
	if (imagePlus!=null) {
		imagePlus.killRoi();
	}
}
public Rectangle getSelectionBounds1() {
	
	if (imagePlus==null||imagePlus.getRoi()==null)
		return new Rectangle();
	
	return imagePlus.getRoi().getBounds();
}

/**sets the slection*/
@Override
public void setSelection( LocatedObject2D lastRoi, int i) {
	if (imagePlus==null) {return;}
	if (lastRoi instanceof RoiWrapper) {
		RoiWrapper r=(RoiWrapper) lastRoi;
		//ImagePlusWrapper impw=(ImagePlusWrapper) imp;
		imagePlus.setRoi(r.roi);
	} else 
	if (lastRoi instanceof BasicShapeGraphic) {
		BasicShapeGraphic b=(BasicShapeGraphic) lastRoi;
		imagePlus.setRoi(new ShapeRoi(b.getShape()));
	} /**else
	if (imagePlus instanceof GraphicalImagePlus) {
	//	GraphicalImagePlus gimp=(GraphicalImagePlus) imagePlus;
		super.setSelection(lastRoi, i);
		
	}*/
	
	
}

public void movePrimarySelectionTo2nd() {
	if (imagePlus.getRoi()==null) return;
	RoiWrapper rw = new RoiWrapper(imagePlus.getRoi());
	BasicShapeGraphic bb = new BasicShapeGraphic(rw.getShape());
	selectionGraphic2=bb;
	if (rw.getStroke() instanceof BasicStroke) {
	BasicStroke basicStroke = (BasicStroke) rw.getStroke();
	bb.setStroke(basicStroke);
	}
	imagePlus.killRoi();
}

public boolean hasSelection1() {
	if (imagePlus.getRoi()==null) return false;
	return true;
}



}