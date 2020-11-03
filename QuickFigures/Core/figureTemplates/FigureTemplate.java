package figureTemplates;

import java.io.Serializable;
import java.util.ArrayList;

import channelMerging.PanelStackDisplay;
import genericMontageKit.PanelList;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_BasicShapes.BarGraphic;
import graphicalObjects_BasicShapes.TextGraphic;
import graphicalObjects_BasicShapes.BasicGraphicalObject;
import graphicalObjects_FigureSpecific.FigureOrganizingLayerPane;
import graphicalObjects_FigureSpecific.MultichannelDisplayLayer;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_LayoutObjects.MontageLayoutGraphic;
import gridLayout.MontageSpaces;
import logging.IssueLog;
import utilityClassesForObjects.LocatedObject2D;
import utilityClassesForObjects.TakesLockedItems;
import appContext.CurrentAppContext;
import applicationAdapters.ImageWrapper;
import channelLabels.ChannelLabelProperties;
import channelLabels.ChannelLabelTextGraphic;

public class FigureTemplate implements MontageSpaces, Serializable{
	
	
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RowLabelPicker rowLabelPicker=new RowLabelPicker(new TextGraphic() , ROW_OF_PANELS);
	private RowLabelPicker colLabelPicker=new RowLabelPicker(new TextGraphic() , COLUMN_OF_PANELS);
	private RowLabelPicker titleLabelPicker=new RowLabelPicker(new TextGraphic() , MontageSpaces.ALL_MONTAGE_SPACE);
	private RowLabelPicker panelLabelPicker=new RowLabelPicker(new TextGraphic() , MontageSpaces.PANELS);
	
	private RowLabelPicker channelLabelPicker=new ChannelLabelPicker(new ChannelLabelTextGraphic(new ChannelLabelProperties()));
	private ScaleBarPicker scaleBar=new ScaleBarPicker(new BarGraphic());
	MultichannelDisplayPicker	mdp=new MultichannelDisplayPicker();
	GridLayoutPicker layoutpicker =new GridLayoutPicker(new MontageLayoutGraphic());
	

	
	private GraphicalItemPicker<?>[] pickers=new GraphicalItemPicker[] {layoutpicker,  rowLabelPicker, colLabelPicker,  titleLabelPicker, panelLabelPicker, getChannelLabelPicker(),scaleBar };
	public ItemPicker<?>[] pickersReg=new ItemPicker[] {mdp};
	public boolean awaitingReset;
	
	public FigureTemplate() {
		
	}
	public FigureTemplate(MultichannelDisplayLayer chan ) {
		this();
		
		autoGeneratePickersForDisplay(chan);
	}
	
	
	
	public void applyProperties(ImageWrapper iw) {
		this.applyProperties(iw.getGraphicLayerSet());
		
		
	}
	
	/**the standard template for new quickfugure creations*/
	public static FigureTemplate createStandardTemplate() {
		FigureTemplate ft = new FigureTemplate();
		ft.panelLabelPicker=null;
		ft.colLabelPicker=null;
		ft.rowLabelPicker=null;
		return ft;
	}
	
	public void applyProperties(GraphicLayer iw) {
		try{
		ArrayList<GraphicLayer> l = new ArrayList<GraphicLayer>();
		l.add(iw);
		l.addAll(iw.getSubLayers());
		this.applyProperties(iw.getAllGraphics());
		this.applyProperties(l);
		}
		
			catch (Throwable t) {
			t.printStackTrace();
		}
	}
	
	
	public void applyProperties(ArrayList<?> list) {
		for(GraphicalItemPicker<?> pik:getPickers()) {
			pik.applyPropertiesToList(list);
		}
		for(ItemPicker<?> pik:pickersReg) {
			pik.applyPropertiesToList(list);
			
		}
	}
	
	public void summarizeContent() {
		for(GraphicalItemPicker<?> pik:getPickers()) {
			String st="picker "+pik.getClass().getName();
		}
		for(ItemPicker<?> pik:pickersReg) {
			String st="picker "+pik.getClass().getName();
		}
	}
	

	
	public ArrayList<ItemPicker<?>> getStartupLabelPcikerList() {
		ArrayList<ItemPicker<?>> output=new ArrayList<ItemPicker<?>>();
		//output.add(mdp);
		//output.add(layoutpicker);
			output.add(rowLabelPicker);
			output.add(colLabelPicker);
			output.add(titleLabelPicker);
			output.add(panelLabelPicker);
			//output.add(scaleBar);
		return output;
	}
	
	public RowLabelPicker getRowLabelPicker() {
		return rowLabelPicker;
	}
	public void setRowLabelPicker(RowLabelPicker rowLabelPicker) {
		this.rowLabelPicker = rowLabelPicker;
	}
	public RowLabelPicker getColLabelPicker() {
		return colLabelPicker;
	}
	public void setColLabelPicker(RowLabelPicker colLabelPicker) {
		this.colLabelPicker = colLabelPicker;
	}
	public RowLabelPicker getTitleLabelPicker() {
		return titleLabelPicker;
	}
	public void setTitleLabelPicker(RowLabelPicker titleLabelPicker) {
		this.titleLabelPicker = titleLabelPicker;
	}
	public ScaleBarPicker getScaleBar() {
		return scaleBar;
	}
	public void setScaleBar(ScaleBarPicker scaleBar) {
		this.scaleBar = scaleBar;
	}

	public RowLabelPicker getChannelLabelPicker() {
		return channelLabelPicker;
	}

	public void setChannelLabelPicker(RowLabelPicker channelLabelPicker) {
		this.channelLabelPicker = channelLabelPicker;
	}
	
	
	/**creates a set of starter labels and scale bars from the list of model objects
	  Does not create channel labels as it is another 
	  */
	public void createDefaultLabelsObjectsFromTemplate( GraphicLayer l22, MultichannelDisplayLayer display, MontageLayoutGraphic p) {
		
		if (this.awaitingReset) {
			autoGeneratePickersForDisplay(display);
		}
		
		/**recreates the panels and channel labels and applies the template to them*/
	
		if (l22==null) return;
		applyProperties(l22);
	
		/**iterates through the pikers, letting them each add their model
		  to the layer. Locks them all to the Layout*/
		for(ItemPicker<?> item : getStartupLabelPcikerList()) {
			if (item==null||item.getModelItem()==null||item instanceof ChannelLabelPicker) continue;
			
			if (item.getModelItem() instanceof BasicGraphicalObject&&item.getModelItem() !=null) {
				 
				/**The model item is copied to produce the new item*/
				BasicGraphicalObject b=(BasicGraphicalObject) item.getModelItem();
				 LocatedObject2D cop = b.copy();
				 l22.add((ZoomableGraphic)cop);
				 p.addLockedItem(cop);
				 item.applyProperties(cop);
				 p.snapLockedItems();
				
				
				 /**If the item is a row, panel or column picker, this alters the montage label space
				    to include its area. Since the label starts out locked to the layout, the space is
				    not essensial to keep it in place*/
				 if (item instanceof RowLabelPicker) {
						RowLabelPicker rowLabp=(RowLabelPicker) item;
						if (rowLabp.isInRowOrColumn()) {
							
							p.generateCurrentImageWrapper();
							p.getEditor().expandSpacesToInclude(p.getPanelLayout(), cop.getBounds());
						}
					}
				 
				 
				 
			}
			
			
			
		};
		
		
		
		applyProperties(l22);
		
	}
	
	/**If there is a model scale bar selected it will give it to the merge panel of the principal graphic layer*/
	public void createScaleBarOffTemplate(FigureOrganizingLayerPane p) {
		PanelStackDisplay display=p.getPrincipalMultiChannel();
		
		if (this.awaitingReset) {
			autoGeneratePickersForDisplay((MultichannelDisplayLayer) display);
		}
		
		BarGraphic oldsbar = getScaleBar().getModelItem();
		
		if (oldsbar!=null) {
			PanelList stack = display.getStack();
			int i =stack.getSize();
			if (i<=0) {IssueLog.log("Cannot create scale bar from template without a panel to put it");return;}
			Object displayob = stack.getPanels().get(i-1).getImageDisplayObject();
			if (displayob instanceof TakesLockedItems) {
				BarGraphic newbar = new BarGraphic();
				newbar.copyAttributesButNotScale(oldsbar);
				newbar.setSnappingBehaviour(oldsbar.getSnappingBehaviour());
				p.add(newbar);
				
				TakesLockedItems t= (TakesLockedItems) displayob ;
				t.addLockedItem(newbar);
				getScaleBar().applyProperties(newbar);
				
				/**optimize the bar length. */
				if (displayob instanceof LocatedObject2D) {
					LocatedObject2D panel = (LocatedObject2D)displayob;
					BarGraphic.optimizeBarLengths(newbar, panel);
					
				}
				
				
			}
		
			
			}
	}
	
	
	public void addDisplayToFigure(FigureOrganizingLayerPane currentFigureOrganizer, MultichannelDisplayLayer display ) {
		/**must apply before creating the layout so the minimum number of columns can be created*/
		//IssueLog.log("awaiting reset "+awaitingReset);
		if (awaitingReset) {
			autoGeneratePickersForDisplay(display);
		}
		
		applyProperties(display);
		currentFigureOrganizer.addNovelMultiChannel(display, -1);
		applyProperties(currentFigureOrganizer);
	}
	

	
	public void autoGeneratePickersForDisplay(MultichannelDisplayLayer chan ) {
		IssueLog.log("Figure Template Reset, generating standard from multichannel" );
		
		for(GraphicalItemPicker<?> pik: getPickers())try  {
			IssueLog.log("Figure Template Reset, generating standard for "+pik.getOptionName());
			pik.setToStandardFor(chan);
		} catch (Throwable t) {
			t.printStackTrace();
		}
		
	}
	public Iterable<GraphicalItemPicker<?>> getPickers() {
		ArrayList<GraphicalItemPicker<?>> outputpickers = new ArrayList<GraphicalItemPicker<?>>();
		for(GraphicalItemPicker<?> pi:pickers ) {
			outputpickers.add(pi);
		}
				return outputpickers;
	}
	
	/**changes the properties of this multichannel picker to be the default for merge only
	  displays*/
	public void makeMergeOnly() {
		if (mdp==null) IssueLog.log("no example image display (innitial template fail)");
		if (mdp.getModelItem()==null) 
			{IssueLog.log("no example image display");
			MultichannelDisplayLayer mid = CurrentAppContext.getMultichannelContext().getMultichannelOpener().creatMultiChannelDisplayFromOpenImage();
			if (mid!=null)
			mdp.setModelItem(mid);
			}
		
		
		
		
		mdp.getModelItem().getStack().getChannelUseInstructions().MergeHandleing=3;//3 is only merge
		mdp.getModelItem().getChannelLabelProp().setMergeLabelType(ChannelLabelProperties.Multiline_Labels);
		//mdp.getModelItem().getSetter().setInsertionType(PanelSetter.rowSourceInsertion);
	}
	
	
	/**two methods for fixing the label spaces */
	public void fixupLabelSpaces(GraphicLayer graphicLayer) {
		ArrayList<ZoomableGraphic> items = graphicLayer.getAllGraphics();
		
		fixupLabelSpaces(items);
		
	}
	public void fixupLabelSpaces( ArrayList<ZoomableGraphic> items) {
		for (ZoomableGraphic z: items) {
			
			if (z instanceof MontageLayoutGraphic) {
				MontageLayoutGraphic m=(MontageLayoutGraphic) z;
				m.snapLockedItems();
				m.updateDisplay();
				//selector.getGraphicDisplayContainer().updateDisplay();
				m.getEditor().fitLabelSpacesToContents(m.getPanelLayout());
				
			}
			if (z instanceof GraphicLayer) {
				fixupLabelSpaces( ((GraphicLayer) z).getAllGraphics());
			}
		}
	}
	
	public MultichannelDisplayPicker getMultiChannelPicker() {
		return mdp;
	}
}