package sUnsortedDialogs;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.undo.UndoManager;

import standardDialog.NumberInputPanel;
import standardDialog.PointInputPanel;
import standardDialog.StandardDialog;
import undo.CompoundEdit2;
import undo.UndoScaling;
import utilityClassesForObjects.Scales;

public class ScaleAboutDialog extends StandardDialog{
	
	ArrayList<Scales > items=new ArrayList<Scales >();
	private double scaleLevel=1;
	double x=0;
	double y=0;
	private CompoundEdit2 undo;
	private UndoManager undoManager;
	
	
	public ScaleAboutDialog(double m, double x, double y) {
		super();
		this.scaleLevel = m;
		this.x = x;
		this.y = y;
		this.setModal(true);
		this.setWindowCentered(true);
		this.add("xy", new PointInputPanel("x y", new Point2D.Double(x,y)));
		this.add("m", new NumberInputPanel("scale",  m));
	}
	
	public ScaleAboutDialog() {
		this(1,0,0);
	}
	public ScaleAboutDialog(UndoManager undoManager) {
		this(1,0,0);
		this.undoManager=undoManager;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public void addItem(Scales i) {
		items.add(i);
		
	}
	
	public void addItems(Collection<Scales> i) {
		items.addAll(i);
		
	}
	public void addItemsScalable(Collection<?> i) {
		for(Object f: i) {
			if (f instanceof Scales) {
				addItem((Scales) f);
			}
		}
		
	}
	
	@Override
	public
	void showDialog() {
		super.showDialog();
		this.x=this.getPoint("xy").getX();
		this.y=this.getPoint("xy").getY();
		this.scaleLevel=this.getNumber("m");
		if (this.wasOKed())
			scaleItemstoDialog();
	}
	
	void scaleItemstoDialog() {
		CompoundEdit2 undo = new CompoundEdit2();
		for(Scales m:items ){
			UndoScaling edit = new UndoScaling(m);
			
			m.scaleAbout(getAbout(), this.scaleLevel);//does the scaling
			
			edit.establishFinalState();
			undo.addEditToList(edit);
		}
		this.undo=undo;
		if (undoManager!=null) undoManager.addEdit(undo);
	}
	
	public Point2D getAbout() {return new Point2D.Double(x, y);}

	public double getScaleLevel() {
		return scaleLevel;
	}

	public void setScaleLevel(double scaleLevel) {
		this.scaleLevel = scaleLevel;
	}

}
