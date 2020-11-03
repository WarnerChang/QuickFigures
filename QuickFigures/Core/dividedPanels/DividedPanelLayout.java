package dividedPanels;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import genericMontageKit.PanelLayout;
import logging.IssueLog;
import plasticPanels.FundamentalPanelLayout;
import plasticPanels.PanelOperations;

public class DividedPanelLayout extends FundamentalPanelLayout implements Serializable, PanelLayout  {

	
	public layoutDividedArea mainArea=new layoutDividedArea();
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	double width=500;
	double height=500;
	int border=2;
	
	public DividedPanelLayout(Rectangle2D rect) {
		this.width=rect.getWidth();
		this.height=rect.getHeight();
		this.getReferenceLocation().setLocation(rect.getX(), rect.getY());
		mainArea.pushArea(rect);
	}
	
	
	
	@Override
	public Shape getBoundry() {
		// TODO Auto-generated method stub
		return new Rectangle2D.Double(this.getReferenceLocation().getX(), this.getReferenceLocation().getY(),width,height);
	}

	@Override
	public Rectangle getPanel(int index) {
		Rectangle[] panels = getPanels();
		if(index>panels.length) return panels[panels.length-1];
		return panels[index-1];
	}
	
	@Override
	public Rectangle[] getPanels() {
		ArrayList<Rectangle> list=getPanelsAsArray();
		
		 Rectangle[] output=new  Rectangle[list.size()] ;
		 for(int i=0; i<list.size(); i++) {output[i]=insetRect(border, list.get(i));}
		 return output;
	}
	
	Rectangle insetRect(int border, Rectangle r) {
		return new Rectangle(r.x+border, r.y+border, r.width-border*2, r.height-border*2);
	}
	
	
	/**returns the bare panels*/
	public ArrayList<Rectangle> getPanelsAsArray() {
		ArrayList<Rectangle> list=new ArrayList<Rectangle> ();
		mainArea.addToRectArray(list);
		return list;
	}
	

	
	/**returns a 1 based indexing*/
	@Override
	public int getNearestPanelIndex(double d, double e) {
		PanelOperations<Rectangle> pops = new PanelOperations<Rectangle> ();
		ArrayList<Rectangle> array = getPanelsAsArray() ;
		IssueLog.log("finding nearest panel to "+d+", "+e+" from list of "+array.size());
		IssueLog.log("1st is "+array.get(0));
		IssueLog.log("2nd is "+array.get(1));
		IssueLog.log("3rd is "+array.get(2));
		Rectangle panel = pops.getNearestPanel(array, new Rectangle2D.Double(d, e,1,1).getBounds());
		return array.indexOf(panel)+1;
	}

	@Override
	public void move(double x, double y) {
		double finX = getReferenceLocation().getX()+x;
		double finY = getReferenceLocation().getY()+y;
		getReferenceLocation().setLocation(finX, finY);
		mainArea.pushArea(new Rectangle2D.Double(finX, finY, width, height));
	}

	@Override
	public void setPanelWidth(int panel, double width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPanelHeight(int panel, double height) {
		

	}

	@Override
	public void resetPtsPanels() {
		setUpMainArea();
		
		Shape pa = this.allPanelArea();
		if (!this.getBoundry().contains(pa.getBounds())) {
			if(pa.getBounds().width>this.width) this.width=pa.getBounds().width+border;
			if(pa.getBounds().height>this.height) this.height=pa.getBounds().height+border;
			setUpMainArea();
		}

	}
	
	void setUpMainArea() {
		mainArea.pushArea(new Rectangle2D.Double(this.getReferenceLocation().getX(),
				this.getReferenceLocation().getY(),
				this.width,
				this.height
				
				));
	}

	@Override
	public double getStandardPanelWidth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getStandardPanelHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setStandardPanelWidth(double width) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setStandardPanelHeight(double height) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean doesPanelUseUniqueWidth(int panel) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean doesPanelUseUniqueHeight(int panel) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void nudgePanel(int panelnum, double dx, double dy) {
		ArrayList<layoutDividedArea> areas = getAllAreas();
		int i1 = panelnum;
		
		layoutDividedArea area1 = areas.get(i1-1);
		if (area1==null) return;
		layoutDivider hdtop = area1.getHorizontalDividerTop();
		layoutDivider hdbottom = area1.getHorizontalDivider();
		
		if (hdtop!=null)  {
			hdtop.nudgePosition(dy);
			
		}
		
		if (hdbottom!=null)  {
			 hdbottom.nudgePosition(dy);
			 if(hdbottom.isDependentOn(hdtop)&&hdbottom.parent!=hdtop.parent)  hdbottom.nudgePosition(-dy);
		}
		
	
		
		
		layoutDivider vdl=area1.getVerticalDividerLeft();
		layoutDivider vdr = area1.getVerticalDivider();
		
		if(vdl!=null) {
			vdl.nudgePosition(dx);
		} 
		
		if(vdr!=null) {
			vdr.nudgePosition(dx);
			if(vdr.isDependentOn(vdl)&&vdr.parent!=vdl.parent)  hdbottom.nudgePosition(-dx);
		} 
		
		
		
		resetPtsPanels() ;
		
	}
	
	ArrayList<layoutDividedArea> getAllAreas() {
		ArrayList<layoutDividedArea>  out=new ArrayList<layoutDividedArea>();
		this.mainArea.addToAreaArray(out);
		
		return out;
	}

	@Override
	public void nudgePanelDimensions(int panelnum, double dx, double dy) {
		ArrayList<layoutDividedArea> areas = this.getAllAreas();
			layoutDividedArea a1 = areas.get(panelnum-1);
			if(a1.getHorizontalDivider()!=null) {
				a1.getHorizontalDivider().nudgePosition(dy);
			} else
			{this.height+=dy;}
			
			if(a1.getVerticalDivider()!=null) {
				a1.getVerticalDivider().nudgePosition(dx);
			}  else
		 	{this.width+=dx;}
	}

	@Override
	public int nPanels() {
		int[] panelCount=new int[] {0};
		mainArea.addToPanelCount(panelCount);
		return panelCount[0];
	}
	
	public class layoutDividedArea extends Rectangle2D.Double{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;


		
		private layoutDivider definingDivider;//the divider responsible for separating out this area. it is to the right or below the area
	
		
		private boolean horizontal=true;
		ArrayList<layoutDivider> dividers= new ArrayList<layoutDivider>();
		private ArrayList< layoutDividedArea> subareas= new ArrayList<layoutDividedArea>();


		private layoutDividedArea parent;


		int areanum;



		private layoutDivider precedingDivider; 
		
		
		
		/**defines the boundries of the Area*/
		void pushArea(Rectangle2D rect) {
					this.x=rect.getX();
					this.y=rect.getY();
					this.width=rect.getWidth();
					this.height=rect.getHeight();
					setUpsubAreas();
		}
		
		public void validateDivider() {
			if (getDefiningDivider()==null) {
				IssueLog.log("Defining devider issue");
				return;
			}
			double p = this.getDefiningDivider().getPosition();
			if (p<0) {p=0;}
			if (this.isHorizontal()) {
				if(p>this.height) p=this.height;
				
			}
			else {if(p>this.width) p=this.width;}
			this.getDefiningDivider().setPosition(p);
			
		}

		/**Adds the number of panels in this area to the array value. precondition: the input array must have length over 0*/
		public void addToPanelCount(int[] panelCount) {
			if (isSubdivided()) {
				for(layoutDividedArea area: getSubareas()) {
					area.addToPanelCount(panelCount);
				}
				
			} else {panelCount[0]++;}
			
		}

		/**Adds a rectangular panel to the list*/
		void addToRectArray(ArrayList<Rectangle> r) {
				if (this.isSubdivided())  {
					for(layoutDividedArea area: getSubareas()) {
						area.addToRectArray(r);
					}
				}
				else {
					r.add(this.getBounds());
				}
		}
		
		/**Adds either this area, or subareas to the list*/
		void addToAreaArray(ArrayList<layoutDividedArea> r) {
				if (this.isSubdivided())  {
					for(layoutDividedArea area: getSubareas()) {
						area.addToAreaArray(r);
					}
				}
				else {
					r.add(this);
				}
		}
		
		public ArrayList<layoutDividedArea> getAllBottomLevelSubareas() {
			ArrayList<layoutDividedArea> aa = new ArrayList<layoutDividedArea>();
			addToAreaArray(aa);
			return aa;
		}
		
		
		/**Adds either this area, or subareas to the list*/
		void addToDividerArray(ArrayList<layoutDivider> r) {
				if (this.isSubdivided())  {
					r.addAll(dividers);
					for(layoutDividedArea area: getSubareas()) {
						area.addToDividerArray(r);
					}
				}
				
		}
		
		/**returns an array of all the layout dividers within this area*/
		ArrayList<layoutDivider> getHorizontalDividerArray( boolean horizontals) {
				ArrayList<layoutDivider> o1 = new ArrayList<layoutDivider>();
				this.addToDividerArray(o1);
				ArrayList<layoutDivider> o2 = new ArrayList<layoutDivider>();
				for(layoutDivider div: o1) {if (div.horizontal==horizontals)  o2.add(div);}
				return o2;
		}
		
		public void nudgeDividers(boolean hori, int nudge) {
			ArrayList<layoutDivider> arr = getHorizontalDividerArray(hori);
			for(layoutDivider a:arr) {a.nudgePosition(nudge);}
		}
		
		public void nudgeDividersIfIndependant(boolean hori, int nudge) {
			ArrayList<layoutDivider> arr = getHorizontalDividerArray(hori);
			for(layoutDivider a:arr) {
				if(!a.isDependentOnAnyOfSameOrientation())
				a.nudgePosition(nudge);
				}
		}
		
		boolean setUpsubAreas() {
			if(dividers.size()==0) return false;
			
				double w = 0;
				double h = 0;
				
				double txstart = this.getX();
				double tystart = this.getY();
				double tx = this.getX();
				double ty = this.getY();
			
				
				dividers=orderSmallToLarge(dividers);
				
			for(int i=0; i<dividers.size(); i++) {
				
				
				
				layoutDivider div = dividers.get(i);
				div.horizontal=this.horizontal;
				div.dividernum=i+1;
				
				layoutDividedArea area = getSubareas().get(i);
				area.setParent(this);
				area.setDefiningDivider(div);
				if(i>0) area.setPrecedingDivider(dividers.get(i-1)); else area.setPrecedingDivider(this.precedingDivider);
				area.areanum=i+1;
				/**Sets up the height or width. Divider starts */
					if(isHorizontal()) {
						w=this.getWidth();
						h=div.getPosition()-ty+tystart;
					} else {
						h=this.getHeight();
						w=div.getPosition()-tx+txstart;
					}
					
						Double newrect = new Rectangle2D.Double(tx, ty, w, h);
						area.pushArea(newrect);
					
						/**an area above the location of the dividers*/
							if (horizontal) div.rect=new Rectangle2D.Double(tx, ty+h, w, 1);
							else div.rect=new Rectangle2D.Double(tx+w, ty, 1, h);
					
					
				
				if(isHorizontal()) {
					
					ty+=h;
				} else {
					tx+=w;
				}
				
			}
			
			
			/**Adds the final area*/
			layoutDividedArea area = getSubareas().get(getSubareas().size()-1);
			area.setParent(this);
			if(isHorizontal()) {
				w=this.getWidth();
				h=this.getMaxY()-ty;
			} else {
				h=this.getHeight();
				w=this.getMaxX()-tx;
			}
			Double newrect = new Rectangle2D.Double(tx, ty, w, h);
			area.pushArea(newrect);
			area.areanum=subareas.size();
			
			
			area.setDefiningDivider(this.getDefiningDivider());
			area.setPrecedingDivider(dividers.get(dividers.size()-1));
			
			return true;
		}
		
		
		void setPrecedingDivider(layoutDivider layoutDivider) {
			 precedingDivider=layoutDivider;
			
		}

		/**adds a divider*/
		public boolean divide(double d) {
			
			if (dividers.size()==0 ){getSubareas().add(new layoutDividedArea());}//There is one more subarea then dividers
			
			layoutDivider div = new layoutDivider(d);
			dividers.add(div);
			div.parent=this;
			getSubareas().add(new layoutDividedArea());
			setUpsubAreas();
			return true;
		}
		
		public boolean removeDivider(layoutDivider div) {
			if(!this.dividers.contains(div)) {
				 
				IssueLog.log("could not remove divider");
				return false;
			}
			dividers.remove(div);
			for(layoutDividedArea p: subareas ) {
				if (p.getDefiningDivider()==div) {subareas.remove(p); break;}
			}
			if (dividers.size()==0) {
				subareas=new ArrayList<layoutDividedArea> ();
			}
			
			return true;
		}
		
		/**returns the horizontal divider responsible for the bottom border of this item*/
		layoutDivider getHorizontalDivider() {
			layoutDividedArea p = this;
			while(p!=null) {
				layoutDivider div = p.definingDivider;
				if (div!=null&&div.horizontal) return div; 
				p=p.getParent();
			}
			return null;
			
		}
		
		/**returns the horizontal divider responsible for the top border of this item*/
		layoutDivider getHorizontalDividerTop() {
			layoutDividedArea p = this;
			while(p!=null) {
				layoutDivider div = p.precedingDivider;
				if (div!=null&&div.horizontal) return div; 
				p=p.getParent();
			}
			return null;
			
		}
		
		/**returns the divider that is responsible for the right edge of this area*/
		layoutDivider getVerticalDivider() {
			layoutDividedArea p = this;
				
				while(p!=null) {
					layoutDivider div = p.definingDivider;
					if (div!=null&&!div.horizontal) return div; 
					p=p.getParent();
					
				}
				
				return null;
				
		}
		
		/**returns the divider that is responsible for the left edge of this area*/
		layoutDivider getVerticalDividerLeft() {
			layoutDividedArea p = this;
				
				while(p!=null) {
					layoutDivider div = p.precedingDivider;
					if (div!=null&&!div.horizontal) return div; 
					p=p.getParent();
					
				}
				
				return null;
				
		}
		
		boolean isSubdivided() {
			if (dividers.size()>0) return true;
			return false;
					
		}

		public ArrayList< layoutDividedArea> getSubareas() {
			return subareas;
		}

		public void setSubareas(ArrayList< layoutDividedArea> subareas) {
			this.subareas = subareas;
		}

		public boolean isHorizontal() {
			return horizontal;
		}

		public void setHorizontal(boolean horizontal) {
			this.horizontal = horizontal;
		}

		public layoutDividedArea getParent() {
			return parent;
		}

		public void setParent(layoutDividedArea parent) {
			this.parent = parent;
		}

		public layoutDivider getDefiningDivider() {
			return definingDivider;
		}

		public void setDefiningDivider(layoutDivider definingDivider) {
			this.definingDivider = definingDivider;
		}
		
	}
	
	
	
	
	
	class layoutDivider implements Serializable{
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		public int dividernum;
		public layoutDividedArea parent;
		public Rectangle2D.Double rect;
		public boolean horizontal;
		private double position=100;
		public layoutDivider(double spot) {
			setPosition(spot);
		}
		
		void nudgePosition(double dy) {
			if (getPosition()+dy<2) return;
			setPosition(getPosition() + dy);
		}
		
		/**returns true if a parent areas divider is necesary for this divider*/
		boolean isDependentOn(layoutDivider other) {
			layoutDividedArea pp = parent;
			while(pp!=null) {
				if(pp.dividers.contains(other)) return true;
				
				pp=pp.getParent();
			}
			
			return false;
		}
		
		/**returns true if any parent area has a divider of the same directional orientation as
		 * this one*/
		boolean isDependentOnAnyOfSameOrientation() {
			layoutDividedArea pp = parent.getParent();
			while(pp!=null) {
				if(pp.horizontal==this.horizontal&&pp.dividers.size()>0) return true;
				pp=pp.getParent();
			}
			return false;
			
		}

		public double getPosition() {
			return position;
		}

		public void setPosition(double position) {
			this.position = position;
		}
		
		
		
		
	}
	
	public layoutDivider findSmallest(ArrayList<layoutDivider> divs) {
		layoutDivider div = divs.get(0);
		for(layoutDivider div2: divs) {if(div2.getPosition()<div.getPosition()) div=div2;}
		return div;
	}
	
	
	ArrayList<layoutDivider> orderSmallToLarge(ArrayList<layoutDivider> divs) {
		ArrayList<layoutDivider> divs2=new ArrayList<layoutDivider> ();
		divs2.addAll(divs);
		ArrayList<layoutDivider> divs3=new ArrayList<layoutDivider> ();
		int len=divs2.size();
		for(int i=0; i<len; i++) {
			layoutDivider small = findSmallest(divs2);
			divs2.remove(small);
			divs3.add(small);
		}
		return divs3;
		
	}
	
	

}