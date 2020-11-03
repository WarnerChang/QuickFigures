package includedToolbars;


import java.awt.Point;
import java.awt.dnd.DropTarget;
import java.util.ArrayList;

import applicationAdapters.DisplayedImageWrapper;
import basicMenusForApp.OpeningFileDropHandler;
import externalToolBar.InterfaceExternalTool;
import externalToolBar.ToolBarManager;
import genericMontageLayoutToolKit.BorderAdjusterTool;
import genericMontageLayoutToolKit.LayoutMover;
import genericMontageLayoutToolKit.LayoutScalerTool;
import genericMontageLayoutToolKit.MontageLayoutRowColNumberTool;
import genericMontageLayoutToolKit.MontageMoverTool;
import genericMontageLayoutToolKit.NonMontageSpaceAdjusterTool;
import genericMontageLayoutToolKit.PanelSizeAdjusterTool;
import genericMontageLayoutToolKit.Panel_Selector2;
import genericMontageLayoutToolKit.PannelGrabberTool;
import genericMontageLayoutToolKit.RowColSwapperTool2;
import genericMontageLayoutToolKit.RowLabelIntroducerTool;
import genericMontageUIKit.GeneralTool;
import genericMontageUIKit.ToolBit;
import gridLayout.MontageSpaces;

public class LayoutToolSet extends QuickFiguresToolBar {
	
	public static LayoutToolSet currentToolset;

	public void start() {
		
	}
	//AdapterKit<DisplayedImageWrapper> ak=new AdapterKit <DisplayedImageWrapper>(new ToolAdapterG());
	
	public void setCurrentTool(InterfaceExternalTool<DisplayedImageWrapper> currentTool) {
		super.setCurrentTool(currentTool);
		ToolBarManager.setCurrentTool(currentTool);
	}
	

	
	public void graphicTools() {
		//Roi_Mover mover = new Roi_Mover();
		//mover.setExcludedClass(PanelLayoutGraphic.class);
		
		
		for(ToolBit b: getMinimumLayoutToolBits()) {
			
			addToolBit(b);
		}
		
		
		
	for(ToolBit b: getStandardLayoutToolBits()) {
			
			addToolBit(b);
		}
	

	
	addTool(
				new GeneralTool(getOptionalToolBits()));
	
	
	
for(ToolBit b: getLayoutLabelBits3()) {
			
			addToolBit(b);
		}
		
	
		setCurrentTool(this.tools.get(1));
		
	}
	
	/**returns the tool bits for the montage layout editor tools*/
	public static ArrayList<ToolBit> getOptionalToolBits() {
		ArrayList<ToolBit> output=new ArrayList<ToolBit>();
		ToolBit[] a=getBeyondStandardToolBits();
		for(ToolBit i: a) {output.add(i);};
		return output;
	}
	public static ToolBit[] getBeyondStandardToolBits() {
		return new ToolBit[] {new MontageLayoutRowColNumberTool(),new PanelSizeAdjusterTool()
		,new LayoutScalerTool(),
				new PannelGrabberTool(1),
				new PannelGrabberTool(2),
				new PannelGrabberTool(0),
				new Panel_Selector2()};
	} 
	
	/**returns the tool bits for the montage layout editor tools*/
	public static ArrayList<ToolBit> getMinimumLayoutToolBits() {
		ArrayList<ToolBit> output=new ArrayList<ToolBit>();
		output.add(new LayoutMover());
		output.add(new BorderAdjusterTool());
		return output;
	} 
	
	/**returns the tool bits for the montage layout editor tools*/
	public static ArrayList<ToolBit> getStandardLayoutToolBits() {
		ArrayList<ToolBit> output=new ArrayList<ToolBit>();
		
		output.add(new RowColSwapperTool2(1));
		output.add(new RowColSwapperTool2(2));
		output.add(new RowColSwapperTool2(0));
		
		output.add(new MontageMoverTool());
		output.add(new NonMontageSpaceAdjusterTool());
		
		
		return output;
	} 
	
	
	/**returns the tool bits for the montage layout editor tools*/
	public static ArrayList<ToolBit> getLayoutLabelBits3() {
		ArrayList<ToolBit> output=new ArrayList<ToolBit>();
		
		
		
	
		output.add(new  RowLabelIntroducerTool(MontageSpaces.ROW_OF_PANELS));
		output.add(new  RowLabelIntroducerTool(MontageSpaces.COLUMN_OF_PANELS));
		output.add(new  RowLabelIntroducerTool(MontageSpaces.PANELS));
	
		
		
		return output;
	} 
	
public void run(String s) {
		
		
		if (currentToolset!=null&&currentToolset!=this) currentToolset.getframe().setVisible(false);
		super.maxGridx=16;
		graphicTools();
	

		
		showFrame();
		
		currentToolset=this;
		this.getframe().setLocation(new Point(5, 40));
	
	}
	
	public void showFrame() {
		super.showFrame();
		getframe().setTitle("Layout Tools");
		addToolKeyListeners();
		new DropTarget(getframe(), new OpeningFileDropHandler());
	}
}