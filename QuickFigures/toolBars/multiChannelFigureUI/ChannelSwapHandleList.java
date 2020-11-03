package multiChannelFigureUI;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;

import javax.swing.JPopupMenu;

import applicationAdapters.CanvasMouseEventWrapper;
import channelMerging.ChannelEntry;
import channelMerging.PanelStackDisplay;
import graphicalObjectHandles.SmartHandle;
import graphicalObjectHandles.SmartHandleList;
import graphicalObjects.CordinateConverter;
import graphicalObjects.ImagePanelGraphic;
import graphicalObjects_FigureSpecific.FigureOrganizingLayerPane;
import graphicalObjects_FigureSpecific.MultichannelDisplayLayer;
import menuUtil.SmartJMenu;
import menuUtil.SmartPopupJMenu;
import utilityClassesForObjects.LocatedObject2D;

public class ChannelSwapHandleList extends SmartHandleList {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FigureOrganizingLayerPane figure;
	private ArrayList<ChannelEntry> channels;
	LocatedObject2D anchorObject=null;
	
	int pressHandleIndex=0;
	private MultichannelDisplayLayer theDisplayLayer;
	
	
	public ChannelSwapHandleList(FigureOrganizingLayerPane f, ArrayList<ChannelEntry> chans, LocatedObject2D anchorObject) {
		this.figure=f;
		this.channels=chans;
		this.anchorObject=anchorObject;
		
		innitializeHandles();
	}



	public void innitializeHandles() {
		createHandles();
		updateLocations();
		updateColors();
	}
	

	
	int getPressChannel(CanvasMouseEventWrapper e) {
		SmartHandle h = this.getHandleForClickPoint(new Point2D.Double(e.getClickedXScreen(), e.getClickedYScreen()));
		if (h!=null)
			return h.getHandleNumber()-800;
		
		return -1;
	}
	
	private void createHandles() {
		for(ChannelEntry chan:channels) {
			add(new ChannelSwapHandle(chan.getOriginalChannelIndex()));
		}
		
	}
	
	public void updateList(ArrayList<ChannelEntry> hashChannel) {
		this.channels=hashChannel;
		this.clear();
		innitializeHandles();
	}

	void updateLocations() { 
		if (anchorObject!=null) {
			double x = anchorObject.getBounds().getMaxX()+8;
			double y = anchorObject.getBounds().getY();
			double step=anchorObject.getBounds().getHeight()/this.size();
			for(SmartHandle h: this) {
				h.setCordinateLocation(new Point2D.Double(x,y));
				y+=step;
			}
		}
	}
	
	void updateColors() {
		for(SmartHandle c: this) {
			if (c instanceof ChannelSwapHandle) 
			{
				ChannelSwapHandle handle =(ChannelSwapHandle) c;
				handle.setUpChannelColor();
			}
		}
	}
	
	public class ChannelSwapHandle extends SmartHandle {
		
		public void draw(Graphics2D g, CordinateConverter<?> cords) {
			updateLocations();
			updateColors();
			
			super.draw(g, cords);
			
		}
		
		int channelNumber=1;
		private ChannelEntry entry;

		public ChannelSwapHandle(int index) {
			super(0, 0);
			channelNumber=index;
			this.setHandleNumber(800+index);
			this.handlesize=6;
		}
		
		public ChannelSwapHandle(int x, int y) {
			super(x, y);
			
		}
		

		void setUpChannelColor() {
			for(ChannelEntry chan:channels) {
				if(chan.getOriginalChannelIndex()==channelNumber) {
					this.setHandleColor(chan.getColor());
				try {
					this.message=(
							theDisplayLayer.getMultichanalWrapper().getRealChannelName(channelNumber));
					this.entry=chan;
				} catch (Throwable t) {}
					
					this.messageColor=chan.getColor().darker();
				}
			}
		}
	
		
		public void handlePress(CanvasMouseEventWrapper canvasMouseEventWrapper) {
			pressHandleIndex=getPressChannel(canvasMouseEventWrapper);
		}
		
	public void handleRelease(CanvasMouseEventWrapper canvasMouseEventWrapper) {
		int relHandleIndex = getPressChannel(canvasMouseEventWrapper);
		if (pressHandleIndex==relHandleIndex)return;
		
		
		if (theDisplayLayer.getParentLayer() instanceof FigureOrganizingLayerPane) {
			FigureOrganizingLayerPane f=(FigureOrganizingLayerPane) theDisplayLayer.getParentLayer() ;
			for(PanelStackDisplay item: f.getMultiChannelDisplaysInOrder()) {
				item.getMultichanalWrapper().getChannelSwapper().swapChannelsOfImage(relHandleIndex, pressHandleIndex);
				item.updatePanels();
			}
		}
		else if(pressHandleIndex!=relHandleIndex) {
			theDisplayLayer.getMultichanalWrapper().getChannelSwapper().swapChannelsOfImage(relHandleIndex, pressHandleIndex);
			theDisplayLayer.updatePanelsAndLabelsFromSource();
		}
	
	}
	
	public JPopupMenu getJPopup() {
		if(entry!=null) {
			
			
			SmartPopupJMenu output = new SmartPopupJMenu ();
			
			ChannelSwapperToolBit2 out = new ChannelSwapperToolBit2((ImagePanelGraphic)anchorObject, entry);
			if (theDisplayLayer.getParentLayer() instanceof FigureOrganizingLayerPane) {
				figure=(FigureOrganizingLayerPane) theDisplayLayer.getParentLayer();
			}
			
			
			if (figure.getMultiChannelDisplaysInLayoutOrder().size()==1) {out.addChannelRelevantMenuItems(output);} 
			else {
				SmartJMenu jEveryImage=new SmartJMenu("For Each Image");
				out.addChannelRelevantMenuItems(jEveryImage);
				
				
				output.add(jEveryImage);
				
				SmartJMenu jOneImage=new SmartJMenu("Just This Image's Panels");
				jOneImage.setText("Only For "+theDisplayLayer.getTitle());
				
				out = new ChannelSwapperToolBit2((ImagePanelGraphic)anchorObject, entry);
				out.workOn=0;
				out.addChannelRelevantMenuItems(jOneImage);
				output.add(jOneImage);
			}
			
		return output;
		}
		return null;
	}
	
		private static final long serialVersionUID = 1L;}

	public void setDisplayLayer(MultichannelDisplayLayer display) {
		this.theDisplayLayer=display;
	}

	

}