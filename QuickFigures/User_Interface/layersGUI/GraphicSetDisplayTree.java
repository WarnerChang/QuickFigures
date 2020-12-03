package layersGUI;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JTree;
import javax.swing.TransferHandler;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;

import graphicalObjects.FigureDisplayContainer;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_LayerTypes.ZoomableGraphicGroup;

/**The JTree for the tree UI*/
public class GraphicSetDisplayTree extends JTree {

	/**
	 * 
	 */
TreeUtil<ZoomableGraphic> tu=new TreeUtil<ZoomableGraphic>();
	

	public TreeUtil<ZoomableGraphic> branchOperation() {
		return tu;
	}
	
	private static final long serialVersionUID = 1L;
	private FigureDisplayContainer setDisplayContainer;

	 DefaultMutableTreeNode masternode;
	GraphicLayer baseLayer;
	
	/**Creates a J tree for the given container*/
	public GraphicSetDisplayTree(FigureDisplayContainer cont, DefaultMutableTreeNode masternode) {
		super(masternode, true);
	
		this.masternode=masternode;
		 baseLayer=cont.getGraphicLayerSet();
		 setDisplayContainer=cont;
		if (TreeMode.fancy)this.setRowHeight(0);
		setCellRenderer(new GraphicCellRenderer());
		
	
	}

	/***/
	public FigureDisplayContainer getSetDisplayContainer() {
		return setDisplayContainer;
	}
	
	public DefaultMutableTreeNode makeMasterNode(GraphicLayer baseLayer) {
		this.baseLayer=baseLayer;
		masternode = new DefaultMutableTreeNode(baseLayer, true);
		return masternode ;
	}
	

	/**when  given a tree path, this returns the last graphic*/
	public ZoomableGraphic getItemForTreePath(TreePath p) {
			DefaultMutableTreeNode tn= branchOperation().getNodeforPath( p);
			if (tn==null) return null;
			return getItemForTreeNode(tn);
}
	
	/**when  given a tree node, this returns the last graphic*/
	public ZoomableGraphic getItemForTreeNode(DefaultMutableTreeNode tn) {
		if (tn==null) return null;
		Object s2 = tn.getUserObject();
		if (s2 instanceof ZoomableGraphic) {
			ZoomableGraphic output = (ZoomableGraphic)s2;
			return output;
		}
		else return null;
	}
	
	
	/**when  given a tree path, this returns the last layer*/
	public GraphicLayer getContainerForTreePath(TreePath p) {
		Object o=p.getLastPathComponent();
		if (o instanceof DefaultMutableTreeNode) {
			
			DefaultMutableTreeNode tn = (DefaultMutableTreeNode) o;
			Object s2 = tn.getUserObject();
			if (s2 instanceof GraphicLayer) {
				GraphicLayer output = (GraphicLayer)s2;
				return output;
			}
			else if 
			 (s2 instanceof ZoomableGraphicGroup) {
				ZoomableGraphicGroup output = (ZoomableGraphicGroup)s2;
				return output.getTheLayer();
			}
			
			else {
				GraphicLayer gc2 = getContainerForTreePath(p.getParentPath());
				if (gc2!=null) return gc2; else return null;
			}
			
	} else return null;

}
	
	/**Returns the graphic that is depicted at the given point on the tree.
	 * needed to determine which object the user has clicked on in the tree ui*/
	public ZoomableGraphic getItemForPoint(Point p1) {
		TreePath tp = getPathForLocation(p1.x, p1.y);
		ZoomableGraphic item = getItemForTreePath(tp);
		return item;
	}
	
	/**Returns the layer than is either at the given point on the tree or contains the object at that point.
	 * needed to determine which object the user has clicked on in the tree ui*/
	public GraphicLayer getContainerForPoint(Point p1) {
		TreePath tp = getPathForLocation(p1.x, p1.y);
		GraphicLayer destination = this.getContainerForTreePath(tp);
		return destination;
	}
	
	/**returns the tree path at the click location*/
	public TreePath getPathForMouseEvent(MouseEvent arg0) {
		Point p1=new Point(arg0.getX(), arg0.getY());
		TreePath tp = getPathForLocation(p1.x, p1.y);
		return tp;
	}
	/**returns the item at the click location within the tree*/
	public ZoomableGraphic getItemForMouseEvent(MouseEvent arg0) {
		Point p1=new Point(arg0.getX(), arg0.getY());
		return  this.getItemForPoint(p1);
	}
	
	/**returns all selected graphics*/
	public ArrayList<ZoomableGraphic> getSelecteditems() {
		ArrayList<ZoomableGraphic> output = new ArrayList<ZoomableGraphic>();
	
		TreePath[] paths = getSelectionPaths();
		if (paths==null||paths.length==0) return output;
		for(TreePath path:paths) {
			output.add(this.getItemForTreePath(path));
		}
		return output ;
		
	}
	
	public String toString() {
		return "Display for "+getSetDisplayContainer();
	}
	
	/**generates a hashmap with the graphics as keys*/
	private HashMap<ZoomableGraphic, TreePath> getMap() {
		HashMap<ZoomableGraphic, TreePath> output=new HashMap<ZoomableGraphic, TreePath>();
		for(int i=0; i<getRowCount(); i++) {
			TreePath tp = this.getPathForRow(i);
			ZoomableGraphic item = this.getItemForTreePath(tp);
			output.put(item, tp);
		}
		
		
		return output;
	}
	
	
	/**gets the tree paths for the relevant user objects*/
	TreePath getPathForUserObject(Object z) {
		return getMap().get(z);
	}
	
	/**Adds the given used object to the selection*/
	public void addUserObjectsToSelection(ArrayList<ZoomableGraphic> addeds) {
		for(ZoomableGraphic added: addeds) {
			addUserObjectToSelection(added);
		}
	}
	
	/**Makes the given object selected in the JTree*/
	public void addUserObjectToSelection(ZoomableGraphic added) {
		
		TreePath path = getPathForUserObject(added);
		if (path==null) path=branchOperation().findPathWithUserObject(this.masternode, added);
	
		this.addSelectionPath(path);
		this.expandPath(path);
		
	}
	
	/**given a graphic, expands the tree to reveal its tree location*/
	public void expandPathForUserObject(ZoomableGraphic added) {
		TreePath  path=branchOperation().findPathWithUserObject(this.masternode, added);
		this.expandPath(path);
	}
	
	/**returns an array of tree paths for each object in the array*/
	TreePath[] getPathsForUserObjects(ArrayList<ZoomableGraphic> z) {
		HashMap<ZoomableGraphic, TreePath> map2 = getMap();
		TreePath[] output = new TreePath[z.size()];
		for(int i=0; i<z.size(); i++) {output[i]=map2.get(z.get(i));}
		return output;
	}
	
	

	
	
	
	boolean transferTest=true;
	private Rectangle indicatorRectangle;
	public TransferHandler getTransferHandler() {
		if (!transferTest) return super.getTransferHandler();
		return new GraphicTreeTransferHandler(super.getTransferHandler(), this);
	}
	
	public void setIndicatorRect(Rectangle pathBounds) {
		if (indicatorRectangle!=null&&!indicatorRectangle.equals(pathBounds)) repaint();
		if (indicatorRectangle==null &&pathBounds!=null) repaint();
		this.indicatorRectangle=pathBounds;
		
	}
	
	/**Paints a rectangle on the location that is specified by the field
	  I use this as an indicator when dragging a tree item around so the user can 
	  see where he is targeting*/
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		if (indicatorRectangle!=null) {
			g.drawRect(indicatorRectangle.x, indicatorRectangle.y, indicatorRectangle.width, indicatorRectangle.height);
			
		}
	}
	


}
