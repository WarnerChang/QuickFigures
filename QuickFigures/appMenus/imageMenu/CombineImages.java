package imageMenu;

import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;

import javax.swing.Icon;

import applicationAdapters.DisplayedImageWrapper;
import basicMenusForApp.MenuItemForObj;
import genericMontageKit.BasicOverlayHandler;
import graphicActionToombar.CurrentSetInformerBasic;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import imageDisplayApp.GraphicSet;
import imageDisplayApp.ImageAndDisplaySet;
import sUnsortedDialogs.ObjectListChoice;
import utilityClassesForObjects.LocatedObject2D;

public class CombineImages implements MenuItemForObj {

	@Override
	public void performActionDisplayedImageWrapper(DisplayedImageWrapper diw) {
		ArrayList<DisplayedImageWrapper> choices = getChoices();
		ImageAndDisplaySet figure =  ImageAndDisplaySet.createAndShowNew("new set", 0,0);;
		
		
			for(DisplayedImageWrapper figure2: choices) {
				CombineImages.combineInto(figure, figure2, false);
			BasicOverlayHandler boh = new BasicOverlayHandler();
		
		 boh.resizeCanvasToFitAllObjects(figure.getImageAsWrapper());
		 figure.updateDisplay();
		}
	figure.autoZoom();
	}
	

public static ArrayList<DisplayedImageWrapper> getChoices() {
	 
		 ArrayList<DisplayedImageWrapper> alldisp = new CurrentSetInformerBasic().getVisibleDisplays();
		ArrayList<DisplayedImageWrapper> foruse = new ObjectListChoice<DisplayedImageWrapper>("").selectMany("Chose Which to combine", alldisp, 4);
		 
		 return foruse;
	 
	 
}

public static DisplayedImageWrapper getChoice(String prompt) {
	 
	 ArrayList<DisplayedImageWrapper> allDisp = new CurrentSetInformerBasic().getVisibleDisplays();
	DisplayedImageWrapper foruse = new ObjectListChoice<DisplayedImageWrapper>("").select(prompt, allDisp);
	 
	 return foruse;


}

	@Override
	public String getCommand() {
		return "Combine two images";
	}

	@Override
	public String getNameText() {
		return "Combine Figure Displays";
	}

	@Override
	public String getMenuPath() {
		return "Image";
	}

	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	
	

	/**Combines two displays by adding one of them to the other*/
	public static void combineInto(ImageAndDisplaySet recipient, DisplayedImageWrapper figure2, boolean horizontal) {
		if (recipient==null) return;
		if (figure2==null) return;
		int h = recipient.getTheSet().getHeight();
		int w = recipient.getTheSet().getWidth();
			Dimension dims = getCombinedSize(recipient, figure2, horizontal);
	if (horizontal)
			combineInto(recipient, figure2, new Point(w,0)); 
	else
			combineInto(recipient, figure2, new Point(0,h));
	
	
	recipient.getTheSet().setHeight(dims.height);
	recipient.getTheSet().setWidth(dims.width);
	}
	
	static Dimension getCombinedSize(ImageAndDisplaySet recipient, DisplayedImageWrapper figure2, boolean horizontal) {
		
		int w=0;
		int h=0;
		if (horizontal)
			w=recipient.getTheSet().getWidth()+ figure2.getImageAsWrapper().width();
		else
			w=Math.max(recipient.getTheSet().getWidth(), figure2.getImageAsWrapper().width());
		
		if (!horizontal)
			h=recipient.getTheSet().getHeight()+ figure2.getImageAsWrapper().height();
		else
			h=Math.max(recipient.getTheSet().height(), figure2.getImageAsWrapper().height());
		
		return new Dimension(w,h);
	}
	
	
	
	
	/**Combines two displays by adding one of them to the other*/
	public static void combineInto(ImageAndDisplaySet recipient, DisplayedImageWrapper addition, Point XYDisplace) {
		GraphicLayer layer = addition.getImageAsWrapper().getGraphicLayerSet();
		for(ZoomableGraphic ob1: layer.getAllGraphics()) {
			if (ob1 instanceof LocatedObject2D) {
				LocatedObject2D ob2=(LocatedObject2D) ob1;
				ob2.moveLocation(XYDisplace.getX(), XYDisplace.getY());
			}
		}
		recipient.updateDisplay();
		addition.updateDisplay();
		GraphicSet set = recipient.getTheSet();
		
		set.getGraphicLayerSet().add(layer);
		
		
		
		set.setWidth((int) (set.getWidth()+XYDisplace.getX()));
		set.setHeight((int) (set.getHeight()+XYDisplace.getY()));
		
		addition.closeWindowButKeepObjects();//.getTheWindow().closeGroupWithoutObjectDeath();
		recipient.updateDisplay();
	}
	

}