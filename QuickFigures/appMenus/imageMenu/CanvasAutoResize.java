package imageMenu;

import java.awt.geom.Rectangle2D;

import javax.swing.Icon;

import appContext.ImageDPIHandler;
import applicationAdapters.DisplayedImageWrapper;
import applicationAdapters.ImageWrapper;
import basicMenusForApp.MenuItemForObj;
import genericMontageKit.BasicOverlayHandler;
import undo.CanvasResizeUndo;

public class CanvasAutoResize implements MenuItemForObj {

	public static int fitAll=0, page=1, slide=2;
	int mode=fitAll;
	
	
	public CanvasAutoResize() {}
	public CanvasAutoResize(int mode) {
		this.mode=mode;
	}
	
	@Override
	public void performActionDisplayedImageWrapper(DisplayedImageWrapper diw) {
		if(diw==null) return;
		CanvasResizeUndo undo = performUndoableAction(diw);
		if(undo.sizeSame()) return;//if no changes were made
		diw.getUndoManager().addEdit(undo);//adds the undo
	}
	public CanvasResizeUndo performUndoableAction(DisplayedImageWrapper diw) {
		CanvasResizeUndo undo = new CanvasResizeUndo(diw);//creates an undo
		ImageWrapper iw = diw.getImageAsWrapper();
		BasicOverlayHandler boh = new BasicOverlayHandler();
		
		if (mode==fitAll)boh.resizeCanvasToFitAllObjects(iw);
		if (mode!=fitAll) {
			Rectangle2D.Double r=new Rectangle2D.Double(0,0, ImageDPIHandler.getStandardDPI()*8.5, 490);
			
			if (mode==slide) {
				r=new Rectangle2D.Double(0,0,ImageDPIHandler.getStandardDPI()*10, ImageDPIHandler.getStandardDPI()*7.5);
				}
			iw.CanvasResizePixelsOnly( (int)r.width, (int)r.height, 0,0);
		}
		
		
		diw.updateDisplay();
		diw.updateWindowSize();
		
		undo.establishFinalState();
		return undo;
	}
	

	
	public void makeAllVisible(DisplayedImageWrapper diw) {
		performActionDisplayedImageWrapper(diw);
		diw.zoomOutToFitScreen();
	}

	@Override
	public String getCommand() {
		String output= "Canvas Resize";
		if (mode>fitAll) output+=mode;
		return output;
	}

	@Override
	public String getNameText() {
		if (mode==1) return "Make Page Size";
		if (mode==2) return "Make PowerPoint Slide Size";
		return "Expand Canvas To Fit All Objects";
	}

	@Override
	public String getMenuPath() {
		// TODO Auto-generated method stub
		return "Image<Canvas";
	}
	@Override
	public Icon getIcon() {
		// TODO Auto-generated method stub
		return null;
	}
	




}