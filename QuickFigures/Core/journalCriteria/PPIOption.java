package journalCriteria;

import java.util.ArrayList;

import javax.swing.Icon;

import graphicalObjects.ImagePanelGraphic;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_FigureSpecific.PanelManager;
import iconGraphicalObjects.IconUtil;
import logging.IssueLog;
import multiChannelFigureUI.ChannelSwapperToolBit2;
import selectedItemMenus.BasicMultiSelectionOperator;
import standardDialog.GraphicDisplayComponent;
import standardDialog.NumberInputPanel;
import standardDialog.StandardDialog;
import undo.CompoundEdit2;

public class PPIOption extends BasicMultiSelectionOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	@Override
	public String getMenuCommand() {
		return "Select PPI";
	}




	@Override
	public String getMenuPath() {
		return null;
	}




	@Override
	public void run() {
		ArrayList<ZoomableGraphic> array1 = this.getAllArray();
		
		ArrayList<PanelManager> panels=new ArrayList<PanelManager>();
		
		for(ZoomableGraphic z: array1) {
			if(z instanceof ImagePanelGraphic) try {
				ChannelSwapperToolBit2 z2 = new ChannelSwapperToolBit2((ImagePanelGraphic) z);
				PanelManager pm = z2.getPressedPanelManager();
				if(pm!=null&&!panels.contains(pm)) panels.add(pm);
			} catch (Throwable t) {}
			
		}
		
		if (panels.size()<1) {
			IssueLog.showMessage("Select an image panel first");
			return;}
		
		
		
		String st="Set PPI";
		double starting=300;
		CompoundEdit2 undo = showPPIChangeDialog(panels, st, starting);
		super.getUndoManager().addEdit(undo);
	}




	protected CompoundEdit2 showPPIChangeDialog(ArrayList<PanelManager> panels, String st, double starting) {
		CompoundEdit2 output = new CompoundEdit2();
		StandardDialog sd = new StandardDialog(st);
		sd.setModal(true);
		sd.setWindowCentered(true);
		
		sd.add(st, new NumberInputPanel(st, starting, 4));
		
		sd.showDialog();
		
		if(!sd.wasOKed()) {
			return null;
		}
		double n = sd.getNumber(st);
		for(PanelManager p: panels) {
			output.addEditToList(
					p.changePPI(n)
					)	;
			
		}
		return output;
	}


	public Icon getIcon() {
		return new GraphicDisplayComponent(IconUtil.createAllIcon("ppi")  );
	}
	
	

}