package imageDisplayApp;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;

import javax.swing.JPanel;

import applicationAdapters.CanvasMouseEventWrapper;
import basicAppAdapters.GMouseEvent;
import basicMenusForApp.MenuItemForObj;
import graphicalObjectHandles.ActionButtonHandleList;
import graphicalObjectHandles.IconHandle;
import graphicalObjectHandles.SmartHandle;
import graphicalObjectHandles.HasMiniToolBarHandles;
import graphicalObjects.BasicCoordinateConverter;
import graphicalObjects.CordinateConverter;
import imageMenu.ZoomFit;

/**A components that displays a side panel within each image window or potentially within*/
class MiniToolBarPanel extends JPanel implements MouseListener {
	
	boolean inWindow;

	ArrayList<MenuItemForObj> permanentObjects=new ArrayList<MenuItemForObj>();
	private ImageWindowAndDisplaySet displaySet;
	private ActionButtonHandleList buttonList;
	private ActionButtonHandleList alternateList;
	private Object lastItem;
	boolean vertical=true;
	
	{this.setForeground(Color.orange);}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public MiniToolBarPanel(ImageWindowAndDisplaySet display, boolean vertical) {
		this.vertical=vertical;
		this.displaySet=display;
		addAction(new ZoomFit("In"));
		addAction(new ZoomFit("Out"));
		addAction(new ZoomFit(ZoomFit.SCREEN_FIT));
		
		
		buttonList=createActionList();
		this.addMouseListener(this);
		if (display!=null)
			display.setSidePanel(this);
	}

	public MiniToolBarPanel(ImageWindowAndDisplaySet display) {
		this(display, true);
	}

	public Dimension getPreferredSize() {
		if (!vertical)new Dimension(330, 50);
		return new Dimension(30, 325);
	}
	
	public void addAction(MenuItemForObj object) {
		if (object.getIcon()!=null) {
			permanentObjects.add(object);
		}
	}
	
	public void paintComponent(Graphics g) {
		
		if (g instanceof Graphics2D)
		{
			Graphics2D g2 = (Graphics2D) g;
			
		
			Rectangle r = new Rectangle(0,0, this.getWidth(), this.getHeight());
			g.setColor(Color.white);
			g2.fill(r);
			
			g.setColor(Color.black);
			g2.setStroke(new BasicStroke(2));
			g2.draw(r);
			this.getStableButtonList().draw(g2, new BasicCoordinateConverter());
			ActionButtonHandleList bl = this.getChangingButtonList();
			if (bl!=null) {
				bl.draw(g2, new BasicCoordinateConverter());
			}
		}
		
	}

	/**
	returns the currently used action button handle list
	 */
	public ActionButtonHandleList getStableButtonList() {
		
		return buttonList;
	}
	
	/**
	returns the currently used action button handle list
	 */
	public ActionButtonHandleList getChangingButtonList() {
		ImageWindowAndDisplaySet displaySet2 = getDisplaySet();
		if(displaySet2==null) return null;
		Object selectedItem = displaySet2.getImageAsWrapper().getSelectionObject();
		if (selectedItem!=lastItem) {
			updateAlternateList(selectedItem);
			lastItem=selectedItem;
		}
		if(alternateList!=null) return alternateList;
		
		return null;
	}

	/**
	 * @return
	 */
	public ImageWindowAndDisplaySet getDisplaySet() {
		return displaySet;
	}

	/**
	 updates the alternate mini toolbar to account for the newly selected item
	 */
	public void updateAlternateList(Object selectedItem) {
		if(selectedItem instanceof  HasMiniToolBarHandles) {
			HasMiniToolBarHandles s = (HasMiniToolBarHandles) selectedItem;
			alternateList = s.createActionHandleList();
			
			if(alternateList!=null)
				{
				alternateList.setVertical(vertical);
				alternateList.setLocation(getToolLocations2());
				
				}
		}
		else alternateList=null;
	}
	
	/**this class provides a means for the menu bar items to appear as components of a miniToolbar*/
	public class MenuBarIconHandle extends IconHandle {

		private MenuItemForObj item;
		public MenuBarIconHandle(MenuItemForObj i, Point2D p) {
			super(i.getIcon(), p);
			item=i;
		}

		public void handlePress(CanvasMouseEventWrapper canvasMouseEventWrapper) {
			if( getDisplaySet()==null) return;
			item.performActionDisplayedImageWrapper( getDisplaySet());
			 getDisplaySet().updateDisplay();
		}
		private static final long serialVersionUID = 1L;
		}
	
	/**creates a set of action handles*/
	public ActionButtonHandleList createActionList() {
		ActionButtonHandleList output = new ActionButtonHandleList(vertical);
		for(MenuItemForObj ob:permanentObjects)output.add(new  MenuBarIconHandle(ob, new Point()));
		output.setLocation(getToolLocations());
		return output;
	}

	/**
	 returns the location of the first toolbar
	 */
	public Double getToolLocations() {
		if (!vertical)new Dimension(15, 40);
		return new Point2D.Double(15,5);
	}
	
	/**
	 returns the location of the second mini toolbar
	 */
	public Double getToolLocations2() {
		if (!vertical)  new Point2D.Double(90,40);
		return new Point2D.Double(15,85);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	/**generates a mouse press event for the handle*/
	@Override
	public void mousePressed(MouseEvent e) {
		
		GMouseEvent cew = new LocalMouseEvent(getDisplaySet(), e);
		mousePressOnHandleList(cew, getStableButtonList());
		this.mousePressOnHandleList(cew, this.getChangingButtonList());
	}

	/**
 	determines if the user mouse press was within the handle list
   and calls the handlePress method for the listwhen a mouse press on the panel occurs
	 */
	public void mousePressOnHandleList(GMouseEvent cew, ActionButtonHandleList bList) {
		if(bList==null) return;
		SmartHandle handle = bList.getHandleForClickPoint(cew.getCoordinatePoint());
		if (handle!=null) handle.handlePress(cew);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		this.repaint();
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	public class LocalMouseEvent extends GMouseEvent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LocalMouseEvent(ImageWindowAndDisplaySet displaySet, MouseEvent e) {
			super(displaySet, e);
		}

		public CordinateConverter<?> getUsedConverter() {
			return new BasicCoordinateConverter();
		}
	}
	
}