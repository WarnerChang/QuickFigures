package graphicalObjectHandles;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Point2D;

import actionToolbarItems.EditAndColorizeMultipleItems;
import actionToolbarItems.EditScaleBars;
import graphicalObjectHandles.ActionButtonHandleList.GeneralActionHandle;
import graphicalObjectHandles.ActionButtonHandleList.GeneralActionListHandle;
import graphicalObjectHandles.ShapeActionButtonHandleList2.ColoringButton;
import graphicalObjectHandles.TextActionButtonHandleList.GeneralActionHandleForText;
import graphicalObjects.CordinateConverter;
import graphicalObjects.ImagePanelGraphic;
import graphicalObjects_BasicShapes.BarGraphic;
import graphicalObjects_BasicShapes.ShapeGraphic;
import journalCriteria.PPIOption;
import logging.IssueLog;
import multiChannelFigureUI.ImagePropertiesButton;
import objectDialogs.DialogIcon;
import selectedItemMenus.BarOptionsSyncer;
import selectedItemMenus.ImageGraphicOptionsSyncer;
import selectedItemMenus.SelectAllButton;
import selectedItemMenus.SnappingSyncer;
import selectedItemMenus.TextOptionsSyncer;
import utilityClassesForObjects.LocatedObject2D;

public class ScaleBarActionHandleList extends ActionButtonHandleList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private BarGraphic theBar;
	
	public ScaleBarActionHandleList(BarGraphic t) {
	
		this.theBar=t;
		EditAndColorizeMultipleItems itemForIcon2 = new EditAndColorizeMultipleItems(false, t.getFillColor());
		itemForIcon2.setModelItem(t);
		GeneralActionListHandle hf = addOperationList(itemForIcon2, new EditAndColorizeMultipleItems[] {});
		hf.setAlternativePopup(new ColoringButton(itemForIcon2, 78341));
			
		
	
		
		EditScaleBars itemForIcon;
		addProjectionButton(t);
		
		
		itemForIcon = new EditScaleBars(EditScaleBars.TYPE_WIDTH, 4);
		itemForIcon.setModelItem(t);
		addOperationList(itemForIcon, EditScaleBars.getUnitLengthList(t.getScaleInfo().getUnits()));
		
		add(new BarSyncHandle(1120));
		createGeneralButton(new SelectAllButton(t));
		add(new GeneralActionHandle(new SnappingSyncer(true, theBar), 741905));
		
	}



	protected void addProjectionButton(BarGraphic t) {
		EditScaleBars itemForIcon = new EditScaleBars(EditScaleBars.TYPE_PROJ, 0);
		itemForIcon.setModelItem(t);
		addOperationList(itemForIcon, EditScaleBars.getProjectionList());
	}


	
	public void updateLocation() {
		
		Rectangle bounds = theBar.getOutline().getBounds();
		super.setLocation(new Point2D.Double(bounds.getX()+5, bounds.getMaxY()+20));
	
	}
	public void updateHandleLocations(double magnify) {
		 
		super.updateHandleLocations(magnify);
	}

	public void draw(Graphics2D g, CordinateConverter<?> cords) {
		
		
		super.draw(g, cords);
	}
	
	
	public class BarSyncHandle extends GeneralActionHandle {

		public  BarSyncHandle( int num) {
			super(new BarOptionsSyncer(), num);
		}
		
		public void updateIcon() {
			super.setIcon(DialogIcon.getIcon());
		}
		
		@Override
		public boolean isHidden() {
			
			return false;
		}

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;}
	
	
}