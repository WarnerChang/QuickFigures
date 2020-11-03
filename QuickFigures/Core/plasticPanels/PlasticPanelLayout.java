package plasticPanels;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import logging.IssueLog;
import utilityClassesForObjects.SnappingPosition;

public class PlasticPanelLayout extends BasicSpacedPanelLayout implements SpacedPanelLayout{
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;



	public PlasticPanelLayout() {
		
	}
	
public PlasticPanelLayout(int size) {
		for(int i=0; i<size; i++) {
			addNewPanel();
		}
		
	}


	ArrayList<PlasticPanel> panels=new ArrayList<PlasticPanel>();
	
	private int standardPanelWidth=100;
	private int standardPanelHeight=100;
	

	
	SnappingPosition sb=SnappingPosition.partnerExternal();

	
	
	@Override
	public PlasticPanel[] getPanels() {
		PlasticPanel[] p = new PlasticPanel[nPanels()] ;
		for(int i=0; i<nPanels(); i++) {
			//panel=this.getPanel(i+1);
			p[i]=getPanel(i+1);
		}
		return p;
	}

	public void sortLeftToRightAboveToBottom() {
		PlasticPanel[] panels2 = getPanels();
		 this.getneighborFinder().getLeftAndAboveSorted(panels2);
		ArrayList<PlasticPanel> output = new ArrayList<PlasticPanel>();
		for(PlasticPanel pan:panels2) {
			if (pan==null) continue;
			output.add(pan);
		}
		this.panels=output;
	}
	
	@Override
	public PlasticPanel getPanel(int index) {
		while(index>nPanels()) {addNewPanel();}
		if (index<1) {
			return null;
		}
		PlasticPanel r=panels.get(index-1);
		if (r==null) {
			r= makeNewPanel();
			panels.set(index-1, r);
		}
		
		return r;
	}
	
	public void addNewPanel() {
		PlasticPanel panelLast = getPanel(nPanels());
		panels.add(makeNewPanel());
		if (panelLast!=null) sb.snapRects(getPanel(nPanels()), panelLast);
	}
	
	public void removePanel(int i) {
	
		panels.remove(getPanel(i));
		
	}
	
	PlasticPanel makeNewPanel() {
		return new PlasticPanel(0,0,this.getStandardPanelWidth(), this.getStandardPanelHeight());
	}

	@Override
	public int getNearestPanelIndex(double d, double e) {
		Rectangle2D panel = this.getNearestPanel(d, e);
		return panels.indexOf(panel)+1;
	}

	@Override
	public void move(double x, double y) {
		location.x+=x;
		location.y+=y;
		for(PlasticPanel p: panels) {
			if (p==null) continue;
			p.x+=x; p.y+=y;
		}
		
	}
	




	@Override
	public void setPanelWidth(int panel, double width) {
		this.getPanel(panel).width=(int)width;
		
	}

	@Override
	public void setPanelHeight(int panel, double height) {
		this.getPanel(panel).height=(int)height;
		
	}

	@Override
	public void resetPtsPanels() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public boolean doesPanelUseUniqueWidth(int panel) {
		// TODO Auto-generated method stub
		return this.getPanel(panel).width==this.getStandardPanelWidth();
	}

	@Override
	public boolean doesPanelUseUniqueHeight(int panel) {
		// TODO Auto-generated method stub
		return this.getPanel(panel).height==this.getStandardPanelHeight();
	}

	@Override
	public void nudgePanel(int panelnum, double dx, double dy) {
		PlasticPanel panel = this.getPanel(panelnum);
		panel.x+=dx;
		panel.y+=dy;
	}

	@Override
	public void nudgePanelDimensions(int panelnum, double dx, double dy) {
		PlasticPanel panel = this.getPanel(panelnum);
		panel.width+=dx;
		panel.height+=dy;
		IssueLog.log("nudging panel");
	}

	public double getStandardPanelWidth() {
		return standardPanelWidth;
	}

	public void setStandardPanelWidth(double standardPanelWidth) {
		this.standardPanelWidth = (int)standardPanelWidth;
	}

	public double getStandardPanelHeight() {
		return standardPanelHeight;
	}

	public void setStandardPanelHeight(double standardPanelHeight) {
		this.standardPanelHeight = (int) standardPanelHeight;
	}

	@Override
	public int nPanels() {
		// TODO Auto-generated method stub
		return panels.size();
	}

	


	

	
	
	

	
	
	
	
	
	


	

}