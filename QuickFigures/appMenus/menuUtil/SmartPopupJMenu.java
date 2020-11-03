package menuUtil;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.undo.AbstractUndoableEdit;

import applicationAdapters.CanvasMouseEventWrapper;
import logging.IssueLog;
import undo.UndoManagerPlus;

public class SmartPopupJMenu extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public SmartPopupJMenu() {
		super();
	}
	PopupCloser closer;
	
	private UndoManagerPlus undoManager;

	private transient CanvasMouseEventWrapper mEvent;
	
	public void setLastMouseEvent(CanvasMouseEventWrapper e) {
		this.mEvent=e;
		for(MenuElement e2 : this.getSubElements()) {
			if (e2 instanceof SmartJMenu) {
				((SmartJMenu) e2).setLastMouseEvent(e);
			}
		}
		
	}
	
	public void performUndoable(AbstractUndoableEdit... edits) {
		if(undoManager!=null) undoManager.addEdits(edits);
		else if(mEvent!=null) mEvent.addUndo(edits);
		else {
			IssueLog.log("failed to add undo");
		}
	}
	
	public void showForMouseEvent(CanvasMouseEventWrapper w) {
		this.setLastMouseEvent(w);
		this.show(w.getComponent(), w.getClickedXScreen(), w.getClickedYScreen());
	}
	
	public void showForMouseEvent(CanvasMouseEventWrapper w, int dx, int dy) {
		this.setLastMouseEvent(w);
		this.show(w.getComponent(), w.getClickedXScreen()+dx, w.getClickedYScreen()+dy);
	}
	
	public JMenu getSubmenuOfName(String st) {
		for(MenuElement e2 : this.getSubElements()) {
			if (e2 instanceof JMenu)
				{
				JMenu jMenu = (JMenu) e2;
				
				if (jMenu.getText().equals(st)) return jMenu;
				}
			
		}
		
		
		
		SmartJMenu menuItem = new SmartJMenu(st);
		this.add(menuItem);
		return menuItem;
	}
	
	protected CanvasMouseEventWrapper getMemoryOfMouseEvent() {return mEvent;};
	/**
	 * 
	

	private static final long serialVersionUID = 1L;
	{
		closer=new PopupCloser(this);
		;closer.removeAfterDone=false;
} */
	/**
	 * 
	 */
/**
	public JMenu.WinListener createWinListener(JPopupMenu p) {
 		WinListener output = super.createWinListener(p);
 		IssueLog.log("win listener created");
 		closer = new PopupCloser(p);
 		//closer.removeAfterDone=false;
 		this.popupListener=output;
 		return output;
 	}*/
	
	public void setVisible(boolean b) {
		
		
		super.setVisible(b);
		if (b==true) {new PopupCloser(this);new PopupCloser(this);
		}
	}

public UndoManagerPlus getUndoManager() {
	return undoManager;
}

public void setUndoManager(UndoManagerPlus undoManager) {
	this.undoManager = undoManager;
	for(MenuElement e : this.getSubElements()) {
		if (e instanceof SmartJMenu) {
			((SmartJMenu) e).setUndoManager(getUndoManager());
		}
	}
}

public JMenu extractToMenu(String label) {
	JMenu output=new SmartJMenu(label);
	MenuElement[] elements = this.getSubElements();
	this.removeAll();
	for(MenuElement e : elements) {
		if (e instanceof JMenuItem) output.add((JMenuItem) e);
	}
	return output;
}

protected void addAllMenuItems(Iterable<? extends JMenuItem> i) {
	for(JMenuItem j:i) {add(j);}
}

}