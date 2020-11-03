package graphicalObjects_FigureSpecific;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.undo.UndoableEdit;

import appContext.ImageDPIHandler;
import channelMerging.PanelStackDisplay;
import graphicalObjects.ImagePanelGraphic;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_LayoutObjects.PanelLayoutGraphic;
import logging.IssueLog;
import undo.CompoundEdit2;
import undo.PreprocessChangeUndo;
import undo.UndoScaling;
import utilityClassesForObjects.Scales;

public class FigureScaler {
	
	boolean alterBilinearScale=true;
	
	public  FigureScaler(boolean linScale) {
		this.alterBilinearScale=linScale;
	}

	
	public double getSlideSizeScale(PanelLayoutGraphic item) {
		if (item.getBounds().width>500) return 1;
		double factor=650.00/(item.getBounds().width+item.getBounds().x);
		
		double [] factors= new double []{ 300.0/200, 300.0/150, 300.0/100, 300.0/ImageDPIHandler.getStandardDPI(), 300.0/50};
		for (int i=0; i<factors.length; i++) {
		    if (factor<factors[i]&&i>0)  {
		    	factor=factors[i-1];
		    	break;}
		}
		
		return factor;
	}
	
	
	public CompoundEdit2 scaleFigure(PanelLayoutGraphic item, double factor, Point2D about ) {
		GraphicLayer parentLayer = item.getParentLayer();
		
		return scaleLayer(factor, about, parentLayer);
	}


	public CompoundEdit2 scaleLayer(double factor, Point2D about, GraphicLayer parentLayer) {
		CompoundEdit2 undo = new CompoundEdit2();
		
		ArrayList<ImagePanelGraphic> panelsInFigure = getAllPanelGraphics(parentLayer);
		
		ArrayList<ZoomableGraphic> ii = parentLayer.getAllGraphics();

		for(ZoomableGraphic xg: ii) {
			
			if (xg instanceof Scales  ) {
				Scales s=(Scales) xg;
				UndoScaling edit = new UndoScaling(s);
				
				s.scaleAbout(about, factor);
				edit.establishFinalState();
				undo.addEditToList(edit);
				
				/**undoes the scaling*/
				if (( alterBilinearScale&&(xg instanceof ImagePanelGraphic))&&panelsInFigure.contains(xg)) {
					ImagePanelGraphic image=(ImagePanelGraphic) xg;
					image.setScale(image.getScale()/factor);
					edit.establishFinalState();
				}
				
			}
		}
		
		if (alterBilinearScale) {
			undo.addEditToList(
					scaleDisplays(parentLayer, factor));
			updateDisplays(parentLayer);
			}
		else {
			panelLevelScaleDisplays(parentLayer, factor);
		}
		
		
		return undo;
	}
	
	/**Alters the bilinear scale factor for all the multichannel displays that use
	  this layout
	  */
	CompoundEdit2 scaleDisplays(GraphicLayer layer, double factor) {
		CompoundEdit2 undo = new CompoundEdit2();
		
		
		if (layer instanceof PanelStackDisplay) {
			undo.addEditToList(
			scaleDisplay((PanelStackDisplay) layer, factor));
			//if parent layer is a panel stack display, then only it needs scaling 
		} else 
		
		if (layer instanceof FigureOrganizingLayerPane) {
			for(PanelStackDisplay disp1: ((FigureOrganizingLayerPane) layer).getMultiChannelDisplays()) {
				undo.addEditToList(
				scaleDisplay(disp1, factor));
			}
		}else {
			//overhaul to scaling done on june 6 should make this part unneeded
			// if the parent layer is a normal layer, than it might be the layer for a set of inset definers
			/**ArrayList<PanelGraphicInsetDef> insets = PanelGraphicInsetDef.getInsetDefinersFromLayer(layer.getParentLayer());

			if (insets.size()>0) {
				for(PanelGraphicInsetDef ins: insets) {
					UndoInsetDefChange undoinset = new UndoInsetDefChange(ins);
					ins.setBilinearScale(ins.getBilinearScale()*factor);
					undoinset.establishFinalState();
					undo.addEditToList(undoinset);
					ins.updateDisplayPanelImages();
				}
			}
		*/
		}
		
		return undo;
		
	}
	
	/**Alters the bilinear scale factor for all the multichannel displays that use
	  this layout
	  */
	CompoundEdit2 panelLevelScaleDisplays(GraphicLayer layer, double factor) {
		CompoundEdit2 undo = new CompoundEdit2();
		
		
		if (layer instanceof PanelStackDisplay) {
			undo.addEditToList(
			panelLeveScaleDisplay((PanelStackDisplay) layer, factor));
			//if parent layer is a panel stack display, then only it needs scaling 
		} else 
		
		if (layer instanceof FigureOrganizingLayerPane) {
			for(PanelStackDisplay disp1: ((FigureOrganizingLayerPane) layer).getMultiChannelDisplays()) {
				undo.addEditToList(
						panelLeveScaleDisplay(disp1, factor));
			}
		}else {
		}
		
		return undo;
		
	}
	
	private UndoableEdit panelLeveScaleDisplay(PanelStackDisplay layer, double factor) {
		double s = layer.getPanelManager().getPanelLevelScale()*factor;
		 layer.getPanelManager().setPanelLevelScale(s);
		return new CompoundEdit2();
	}


	void updateDisplays(GraphicLayer layer) {

		if (layer instanceof PanelStackDisplay) {
			((PanelStackDisplay) layer).updatePanels();;
		}
		
		
		
		if (layer instanceof FigureOrganizingLayerPane) {
			for(PanelStackDisplay disp1: ((FigureOrganizingLayerPane) layer).getMultiChannelDisplays()) {
		
				disp1.updatePanels();
			}
		}
		
	}
	private PreprocessChangeUndo scaleDisplay(PanelStackDisplay layer, double factor) {
		PreprocessChangeUndo undoer = new PreprocessChangeUndo(layer);//scaleStack( layer.getStack(), factor);
		
		double nScale = layer.getPreprocessScale();
		layer.setPreprocessScale(nScale*factor);
		undoer.establishFinalLocations();
		return undoer;
	}
	
	
	/**
	private UndoStackEdit scaleDisplayOld(PanelStackDisplay layer, double factor) {
		UndoStackEdit undoer = scaleStack( layer.getStack(), factor);
		undoer.setDisplayLayer(layer);
		return undoer;
	}
	
	private UndoStackEdit scaleStackOld(PanelList stack, double factor) {
		UndoStackEdit edit = new UndoStackEdit(stack);
		double newBilinearScale = stack.getScaleBilinear()*factor;
		stack.setScaleBilinear(newBilinearScale);
		edit.establishFinalState();
		return edit;
	}*/

	/**Scales several figures at once
	private void scaleMultipleFigures(ArrayList<PanelLayoutGraphic> layouts, double factor) {
		Area area=new Area();
		for(PanelLayoutGraphic ob: layouts) {
			area.add(new Area( ob.getPanelLayout().getBoundry()));
		}
		
		Point loc = new Point(area.getBounds().x, area.getBounds().y);
		
		scaleMultipleFigures(layouts, loc, factor);
	}*/
	
	/**Scales several figures at once
	 * @return */
	public CompoundEdit2 scaleMultipleFigures(ArrayList<PanelLayoutGraphic> layouts, Point2D loc, double factor) {
	
		CompoundEdit2 undo = new CompoundEdit2();
		for(PanelLayoutGraphic ob: layouts) {
			undo.addEditToList(
					scaleFigure(ob, factor, loc)
					);
		}
		
		return undo;
	}
	
	
	ArrayList<ImagePanelGraphic>  getAllPanelGraphics(GraphicLayer gl) {
		ArrayList<ImagePanelGraphic> items= new 	ArrayList<ImagePanelGraphic>();
		ArrayList<PanelManager> managers = getPanelManagers(gl);
		for(PanelManager man: managers) {
			items.addAll(man.getStack().getPanelGraphics());
		}
		
		return items;
	}
	
	ArrayList<PanelManager> getPanelManagers(GraphicLayer gl) {
		GraphicLayer layer = gl;//the search layer
		ArrayList<PanelManager> output=new ArrayList<PanelManager>();
		
		/**If the starting layer is a sublayer of the figu*/
		while(layer!=null&& !(layer instanceof MultichannelDisplayLayer) &&!(layer instanceof FigureOrganizingLayerPane)) {
			layer=layer.getParentLayer();
		}
		
		if (layer==null) return output;
		for(ZoomableGraphic item: layer.getObjectsAndSubLayers()) {
			PanelManager pan = getPanelManagerForObject(item);
			if (pan!=null) output.add(pan);
		}
		
		return output;
	}
	
	PanelManager getPanelManagerForObject(Object item) {
		if (item instanceof PanelStackDisplay) {
			return ((PanelStackDisplay) item).getPanelManager();
		}
		if (item instanceof PanelGraphicInsetDef) {
			return ((PanelGraphicInsetDef) item).getPanelManager();
		}
		
		return null;
		
	}
	
	public static void scaleWarnings(GraphicLayer l) {
		showScaleWarnings(l.getAllGraphics());
	}
	
	public static void showScaleWarnings(ArrayList<?> objects) {
		HashMap<String, Object> allWarnings=new HashMap<String, Object>();
		for(Object o: objects) {
			if (o instanceof Scales) {
				Object w = ((Scales) o).getScaleWarning();
				if(w!=null && w instanceof String[]) 
					for(String s: (String[]) w) allWarnings.put(s, o);
					
			}
		}
		if(allWarnings.keySet().size()>0)
			IssueLog.showMessages(allWarnings.keySet());
		
	}
}
