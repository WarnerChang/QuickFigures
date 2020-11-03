package popupMenusForComplexObjects;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import graphicalObjects_BasicShapes.PathGraphic;
import menuUtil.SmartPopupJMenu;
import objectDialogs.ArrowSwingDialog;
import pathGraphicToolFamily.AddRemoveAnchorPointTool;
import menuUtil.SmartJMenu;
import menuUtil.PopupMenuSupplier;
import sUnsortedDialogs.AffineTransformDialog;
import undo.AbstractUndoableEdit2;
import undo.CompoundEdit2;
import undo.Edit;
import undo.UndoAddItem;
import undo.UndoAbleEditForRemoveItem;
import undo.PathEditUndo;
import utilityClassesForObjects.BasicShapeMaker;
import utilityClassesForObjects.PathPoint;
import utilityClassesForObjects.PathPointList;

public class PathGraphicMenu extends SmartPopupJMenu implements ActionListener,
PopupMenuSupplier  {

	/**
	 * 
	 */
	
	static final String options="Options", moveEnd="Move Endpoint", rotate="Rotate", scale="Scale", vFlip="Flip Vertical", hFlip="Flip Horizontal", redoIt="Split Up Parts", switchHandleModes="Switch Handle Modes", break10Fold="Make Uncurved Copy";//, backGroundShap="Outline Shape";
	static final String crissCross="Show Crossing Path Lines", addNormIndicators="show vectors",
			cullMeaningLesspoints="Eliminate extra points", smooth="Smooth", addPoint="Add Point";
	;
	
	HashMap<String, SmartJMenu> subMenus=new HashMap<String, SmartJMenu> ();
	private SmartJMenu getSubMenu(String st) {
		SmartJMenu out = subMenus.get(st);
		if(out==null) {
			out=new SmartJMenu(st);
			subMenus.put(st, out);
			this.add(out);
		}
		
		return out;
	}
	
	PathGraphic pathForMenuG;
	private static String addArrowHead1="Arrow Head At Start", addArrowHead2="Arrow Head At End";
	public PathGraphicMenu(PathGraphic textG) {
		super();
		this.pathForMenuG = textG;
		this.addAllMenuItems(new ShapeGraphicMenu(textG).createMenuItems());
		
		 addItem(addPoint);
		
		
		 String tFormMenu = "Transform";
		 
		addItem(tFormMenu, rotate);
		 addItem(tFormMenu, scale);
		 
		 
		 addItem(tFormMenu, hFlip);
		 addItem(tFormMenu, vFlip);
		 
		
		
		// addItem(addNormIndicators);
		 addItem(switchHandleModes);
		String subMenuName = "expert options";
		
		 addItem(tFormMenu, break10Fold);
		addItem(tFormMenu,smooth);
		addItem(tFormMenu,redoIt);
		addItem(subMenuName, getAddArrowHead2MenuCommand());
		 addItem(subMenuName, getAddArrowHead1MenuCommand());
		 addItem(subMenuName, cullMeaningLesspoints);
		 addItem(subMenuName, moveEnd);
	}
	
	
	public void addItem(String subMenuName, String st) {
		this.getSubMenu(subMenuName).add(createItem(st));
	}
	
	public void addItem(String st) {
		add(createItem(st));
	}
	
	public JMenuItem createItem(String st) {
		JMenuItem o=new JMenuItem(st);
		o.addActionListener(this);
		o.setActionCommand(st);
		
		return o;
	}

	private static final long serialVersionUID = 1L;

	@Override
	public JPopupMenu getJPopup() {
		// TODO Auto-generated method stub
		return this;
	}


	@Override
	public void actionPerformed(ActionEvent arg0) {
		String com=arg0.getActionCommand();
		
		AbstractUndoableEdit2 undo = new PathEditUndo(pathForMenuG);
		
		if (com.equals(options)) {
			pathForMenuG.showOptionsDialog();
		}
		if (com.equals(moveEnd)) {
			pathForMenuG.getPoints().moveEnd();
		}
		if (com.equals(rotate)) {
			pathForMenuG.getPoints().applyAffine(AffineTransformDialog.showRotation(0, new Point(0,0)));
		}
		
		if (com.equals(scale)) {
			pathForMenuG.getPoints().applyAffine(AffineTransformDialog.showScale(new Point(1,1)));
		}
		

		
		if (com.equals(redoIt)) {
			PathIterator pi = pathForMenuG.getPath().getPathIterator(new AffineTransform());
			
		/**PathGraphic textG2 = (PathGraphic) textG.copy();
		textG.getParentLayer().add(textG2);*/
		
		CompoundEdit2 undo2 = new CompoundEdit2();
		
			ArrayList<PathPointList> arrayOfSec = PathPointList.createFromIterator(pi).createAtCloseSubsections();
			for(PathPointList arr: arrayOfSec) {
				if (arr==null ) continue;
				PathGraphic textG2 = (PathGraphic) pathForMenuG.copy();
				undo2.addEditToList(Edit.addItem(pathForMenuG.getParentLayer(), textG2));
				
				
				textG2.setPoints(arr);
				
			}
			undo2.addEditToList(new UndoAbleEditForRemoveItem(pathForMenuG.getParentLayer(), pathForMenuG));
			pathForMenuG.getParentLayer().remove(pathForMenuG);
			undo=undo2;
			
			;
		}
		
		if (com.equals(break10Fold)) {
			PathGraphic newpath = pathForMenuG.break10(20);
		
			newpath.select();
			undo=Edit.addItem(pathForMenuG.getParentLayer(), newpath);

		}
		
		if (com.equals(hFlip)) {
			pathForMenuG.getPoints().applyAffine(BasicShapeMaker.createHFlip(pathForMenuG.getPoints().createPath(true).getBounds().getCenterX()));
		}
		
		if (com.equals(vFlip)) {
			pathForMenuG.getPoints().applyAffine(BasicShapeMaker.createVFlip(pathForMenuG.getPoints().createPath(true).getBounds().getCenterY()));
		}
		if (com.equals(switchHandleModes)) {
			if (pathForMenuG.getHandleMode()!=PathGraphic.anchorHandleOnlyMode) {
				
				pathForMenuG.setHandleMode(PathGraphic.anchorHandleOnlyMode);
					} 
				else pathForMenuG.setHandleMode(PathGraphic.ThreeHandelMode);
		}
		
		if (com.equals(crissCross)) {pathForMenuG.setUseArea(!pathForMenuG.isUseArea());}
		
		if (com.equals(cullMeaningLesspoints)) {
			pathForMenuG.getPoints().cullUselessPoints(0.98, false, 2, true);
		}
		
		if (com.equals(smooth)) {
			for(PathPoint l:pathForMenuG.getPoints() ) {
				l.evenOutAngleOfCurveControls(0.5);
				pathForMenuG.updatePathFromPoints();
			}
			
		}
		
		if (com.equals(addNormIndicators)) {
			ArrayList<Point2D[]> vectors = pathForMenuG.getPoints().getTangentVectors();//.getDiffVectors();
			Point2D[] m = pathForMenuG.getPoints().getMidpionts(0.5);
			for(int it=0; it<vectors.size(); it++) {
				PathGraphic pt = PathGraphic.blackLine(vectors.get(it));pt.setStrokeWidth(2);
	
				pt.moveLocation(pathForMenuG.getLocation().getX(), pathForMenuG.getLocation().getY());
				pathForMenuG.getParentLayer().add(pt);
			}
		}
		
		if (com.equals(addPoint)) {
			new AddRemoveAnchorPointTool(false).addOrRemovePointAtLocation(pathForMenuG, false, super.getMemoryOfMouseEvent().getCordinatePoint());
		}
		
		if (com.equals(getAddArrowHead1MenuCommand())) {
			if (!pathForMenuG.hasArrowHead1())pathForMenuG.addArrowHeads(1);
			else if (pathForMenuG.getArrowHead1()!=null)
				new ArrowSwingDialog(pathForMenuG.getArrowHead1(), 1).showDialog();;
		}
				if (com.equals(getAddArrowHead2MenuCommand())) {
				if (!pathForMenuG.hasArrowHead2())pathForMenuG.addArrowHeads(2);
				else if (pathForMenuG.getArrowHead2()!=null)
					new ArrowSwingDialog(pathForMenuG.getArrowHead2(), 1).showDialog();;
					
		}
		
		pathForMenuG.updatePathFromPoints();
		undo.establishFinalState();
		if (getUndoManager()!=null) getUndoManager().addEdit(undo);
		pathForMenuG.updateDisplay();
	}


	public String getAddArrowHead1MenuCommand() {
		if(!pathForMenuG.hasArrowHead1()) return "Add Arrow Head To Start";
		return addArrowHead1;
	}


	public  String getAddArrowHead2MenuCommand() {
		if(!pathForMenuG.hasArrowHead2()) return "Add Arrow Head To End";
		return addArrowHead2;
	}
	
	
	
}
	


