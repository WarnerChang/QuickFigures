/*******************************************************************************
 * Copyright (c) 2021 Gregory Mazo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
/**
 * Author: Greg Mazo
 * Date Modified: Jan 5, 2021
 * Version: 2021.1
 */
package genericTools;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.util.ArrayList;

import addObjectMenus.FileImageAdder;
import addObjectMenus.FigureAdder;
import appContext.CurrentAppContext;
import appContext.ImageDPIHandler;
import channelMerging.ImageDisplayLayer;
import externalToolBar.BasicDragHandler;
import figureOrganizer.FigureOrganizingLayerPane;
import figureOrganizer.MultichannelDisplayLayer;
import graphicActionToolbar.CurrentFigureSet;
import graphicalObjects.KnowsParentLayer;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_LayerTypes.GraphicLayerPane;
import graphicalObjects_LayoutObjects.DefaultLayoutGraphic;
import graphicalObjects_LayoutObjects.PanelLayoutGraphic;
import graphicalObjects_SpecialObjects.ImagePanelGraphic;
import imageDisplayApp.ImageWindowAndDisplaySet;
import layersGUI.GraphicSetDisplayTree;
import layersGUI.GraphicTreeTransferHandler;
import layout.basicFigure.BasicLayout;
import locatedObject.LocatedObject2D;
import locatedObject.Selectable;
import locatedObject.TakesAttachedItems;
import logging.IssueLog;
import messages.ShowMessage;
import selectedItemMenus.SVG_GraphicAdder2;
import ultilInputOutput.ForDragAndDrop;
import undo.AbstractUndoableEdit2;
import undo.CombinedEdit;
import undo.UndoAddItem;

/**A drag and drop handler for the tools on the toolbar.*/
public class NormalToolDragHandler extends BasicDragHandler {
	private BasicToolBit tool ;

	public NormalToolDragHandler(BasicToolBit roi_Mover) {
		this.tool=roi_Mover;
	}

	/**handles a drag and drop event */
	public void drop(ImageWindowAndDisplaySet displaySet, DropTargetDropEvent arg0) {
		AbstractUndoableEdit2 undo=null;
		ArrayList<File> file = ForDragAndDrop.dropedFiles(arg0);
		if (file!=null) undo=handleFileListDrop(displaySet, arg0.getLocation(), file);
		else
			handleLayersWindowDrop(displaySet, arg0);
		
		new CurrentFigureSet().addUndo(undo);;
	}

	/**
	 occasionally, a user might drag and item from one layers window into 
	 the canvas of another open figure. wrote this to handle that event*/
	public void handleLayersWindowDrop(ImageWindowAndDisplaySet displaySet, DropTargetDropEvent arg0) {
		try{
			
		Transferable t = arg0.getTransferable();
		GraphicSetDisplayTree tree=null;
		
		
		IssueLog.log("Warning: When moving items between trees, also move the linked items ");
		
		tree = (GraphicSetDisplayTree) t.getTransferData(GraphicTreeTransferHandler.selectedItemListFlavor);
	
		ArrayList<ZoomableGraphic> items = tree.getSelecteditems();
		
			if (items instanceof ArrayList<?> ) {
				ArrayList<?> items2=(ArrayList<?>) items;
				for(Object item: (ArrayList<?>)items) {
					if (item instanceof ZoomableGraphic) {
						moveItemToSet((ZoomableGraphic) item, displaySet);
					}
					
					if (item instanceof TakesAttachedItems) {
						TakesAttachedItems taker=(TakesAttachedItems) item;
						for(LocatedObject2D i: taker.getLockedItems()) {
							if (items2.contains(i)) continue;
							moveItemToSet((ZoomableGraphic) i, displaySet);
						}
					}
				}
				
				
			}
			
			if (tree!=null) {
				
				tree.getSetDisplayContainer().updateDisplay();;
				}
		
		}
		catch (Throwable t) {IssueLog.logT(t);
		
		}
	}
	
	/**selects an item. if that  item is a layer, selects everything inside the layer*/
	public void select(Object z) {
		if (z instanceof Selectable) ((Selectable) z).select();
		if (z instanceof GraphicLayer) {
			
			for(ZoomableGraphic l: ((GraphicLayer) z).getAllGraphics()) {
				select(l);
			};
		}
	}
	
	/**handles the transplant of a graphic to a new image*/
	void moveItemToSet(ZoomableGraphic z, ImageWindowAndDisplaySet displaySet) {
		select(z);
		if (displaySet.getImageAsWrapper().getTopLevelLayer().hasItem(z))
					return;
		
		
		GraphicLayer oldparent = z.getParentLayer();
		GraphicLayer newLayer = displaySet.getImageAsWrapper().getTopLevelLayer().getSelectedContainer();
		
		if (newLayer.canAccept(z)) {
			oldparent.remove(z);
			newLayer.add(z);
			
		}
		
	}
	
	/**Called when something is dragged over an image, draws a selection 
	 * over any empty layout panel at the position*/
	public void dragOver(ImageWindowAndDisplaySet displaySet, DropTargetDragEvent arg0) {
		super.dragOver(displaySet, arg0);
		
	
		
		LocatedObject2D roi2 = getObjectAtPoint(displaySet, position);
	
		
		if (roi2 instanceof PanelLayoutGraphic) {
			PanelLayoutGraphic p=(PanelLayoutGraphic) roi2;
			int i = p.getPanelLayout().getNearestPanelIndex(position.getX(),  position.getY());
			Rectangle2D panel = p.getPanelLayout().getPanel(i);
		
			
			displaySet.getImageAsWrapper().getOverlaySelectionManagger().setSelectionGraphic(p);
			displaySet.getImageAsWrapper().getOverlaySelectionManagger().select(panel, 1);
			
		}  else {
			displaySet.getImageAsWrapper().getOverlaySelectionManagger().removeObjectSelections();
			
		}
		displaySet.updateDisplay();
		
	}

	/**returns the object at the given location*/
	private LocatedObject2D getObjectAtPoint(ImageWindowAndDisplaySet displaySet, Point2D position) {
		return tool.getObjectAt(displaySet.getImageAsWrapper(), (int)position.getX(), (int) position.getY());
	}
	
	
	/**What to do if the user drops a file list on a certain canvas location.
	  If there is a readable multichannel. Creates the montage.
	  If there are single images. Adds them to the 
	 * @return */
	public CombinedEdit handleFileListDrop(ImageWindowAndDisplaySet imageAndDisplaySet, Point location, ArrayList<File> file) {
		Point2D location2 = imageAndDisplaySet.getConverter().unTransformP(location);
		LocatedObject2D roi2 = getObjectAtPoint(imageAndDisplaySet, location2);
		PanelLayoutGraphic layout=null;
	
		
		GraphicLayer layer = imageAndDisplaySet.getImageAsWrapper().getTopLevelLayer();
		ArrayList<ImagePanelGraphic> addedPanels=new ArrayList<ImagePanelGraphic>();
		
		if(roi2 instanceof KnowsParentLayer) {
			KnowsParentLayer km=(KnowsParentLayer) roi2;
			if(km.getParentLayer()!=null) layer=km.getParentLayer();
		}
		
		
		
		boolean multiChannelOpen=true;
		CombinedEdit undo = new CombinedEdit();
		for(File f: file) {
			
			
			if (ForDragAndDrop.getExtension(f).toLowerCase().equals("svg")) {
				new SVG_GraphicAdder2().addFromFile(f, layer);
				continue;
			}
			
			/**marks all non-tif files as non multichannels. */
			if (!ForDragAndDrop.getExtension(f).equals("tif")) {
				multiChannelOpen=false;
			}
			
			if (ForDragAndDrop.getExtension(f).equals("tiff")) {
				multiChannelOpen=true;
			}
			if (isMicroscopeFormat(f)) {
				multiChannelOpen=true;
			}
			else if (layer instanceof ImageDisplayLayer) {
				multiChannelOpen=true;
			}
			else if (layer instanceof FigureOrganizingLayerPane) {
				multiChannelOpen=true;
			}
			else if (multiChannelOpen&&!isMicroscopeFormat(f)) {
				
				MultichannelDisplayLayer testOpen = CurrentAppContext.getMultichannelContext().getMultichannelOpener().creatMultiChannelDisplayFromUserSelectedImage(true, f.getAbsolutePath());
				
				if(testOpen==null||testOpen.getMultiChannelImage()==null ) {
					multiChannelOpen=false;
				}
				else
				if (testOpen.getMultiChannelImage().getStackSize()>1 ||testOpen.getMultiChannelImage().getPixelWrapperForSlice(1,1,1).getBitsPerPixel()==16){} 
				else {multiChannelOpen=false;}
				if (testOpen!=null)testOpen.getSlot().hideImage();
					
			}
			
		
			if (multiChannelOpen) {
				AbstractUndoableEdit2 handleMultiChannelStackDrop = handleMultiChannelStackDrop(f,imageAndDisplaySet, layer, location2);
				
				undo.addEditToList(
						handleMultiChannelStackDrop
				);
				
						/**in case that the added figure is the first in a sequence, this sets up the fields so 
						 * that subsequent images are added*/
						if (layout==null&& handleMultiChannelStackDrop instanceof UndoAddItem) {
						
							ZoomableGraphic added = ((UndoAddItem) handleMultiChannelStackDrop).getAddedItem();
							if (added instanceof FigureOrganizingLayerPane ) {
							
								 FigureOrganizingLayerPane figure=(FigureOrganizingLayerPane) added;
								 layout=figure.getMontageLayoutGraphic();
								 roi2=layout;
								 layer=figure;
							}
						}
				
			} else 
			{
				ImagePanelGraphic imageadded = handleImageFileDrop(layer, f, location2, f.getAbsolutePath().equals("PNG"));
				if(imageadded==null) {
					MultichannelDisplayLayer item = CurrentAppContext.getMultichannelContext().createMultichannelDisplay().creatMultiChannelDisplayFromUserSelectedImage(true, f.getAbsolutePath());
					for(ZoomableGraphic i:item.getAllGraphics()) {
						if(i instanceof ImagePanelGraphic) imageadded=(ImagePanelGraphic) i;
					}
				}
				if (layout!=null &&imageadded!=null) {
					Rectangle2D rect = layout.getPanelLayout().getNearestPanel( location2.getX(),  location2.getY());
					imageadded.setLocation(new Point2D.Double(rect.getX(), rect.getY()));
				}
				if(imageadded!=null) addedPanels.add(imageadded);
				}
			
			imageAndDisplaySet.getImageAsWrapper().getOverlaySelectionManagger().removeObjectSelections();
		}
		
		
		/**If adding many raw single image panels, takes care of laying them out*/
		if ((addedPanels.size()>1|| ((roi2 instanceof DefaultLayoutGraphic)&&(addedPanels.size()>0))) &&(!multiChannelOpen)) {
			Rectangle rect = addedPanels.get(0).getBounds();
			boolean moveToNewLayer=false;
			GraphicLayer newLayer=null;
			int startingPanelindex = 1;
				BasicLayout ml;
				DefaultLayoutGraphic mlg;
				if (roi2 instanceof DefaultLayoutGraphic) {
					 mlg=(DefaultLayoutGraphic) roi2;
					 ml=mlg.getPanelLayout();
					 startingPanelindex = ml.getPanelIndex((int)location2.getX(), (int)location2.getY());
				} else {
					moveToNewLayer=true;
					newLayer=new GraphicLayerPane("new layer for panels");
					layer.add(newLayer);
					
					ml = new BasicLayout(addedPanels.size(),1,  rect.width, rect.height, 2,2, true);
					
					
					ml.setLabelSpaces(10, 5, 10, 5);
					mlg = new DefaultLayoutGraphic(ml);
					mlg.moveLocation(location2.getX(), location2.getY());
					//layer.add(mlg);
					//layer.moveItemToIndex(mlg, 0);
					newLayer.add(mlg);
					
					
				}
			
			for(int i=0; i<addedPanels.size(); i++) {
				
				double x = ml.getPanel(i+startingPanelindex).getMinX();
				double y = ml.getPanel(i+startingPanelindex).getMinY();
				ImagePanelGraphic z = addedPanels.get(i);
				z.setLocation(x, y);
				if(moveToNewLayer) {
					layer.removeItemFromLayer(z);
					newLayer.add(z);
				}
			} 
			mlg.resizeLayoutToFitContents();
		}
		
		tool.resizeCanvas(imageAndDisplaySet);
		imageAndDisplaySet.updateDisplay();
		
		return undo;
	}

	/**returns true if the file is in a microscopy format*/
	public static boolean isMicroscopeFormat(File f) {
		String extension = ForDragAndDrop.getExtension(f);
		String[] forms = new String[] {"zvi", "czi", "lif", "dv", "lei"};
		for(String form: forms) {
			if(extension.toLowerCase().equals(form))
				return true;
		}
		return false;
	}
	
	/**returns true if the file is in a raster image format*/
	public static boolean isImageFormat(File f) {
		String extension = ForDragAndDrop.getExtension(f);
		String[] forms = new String[] {"tif", "tiff", "jpeg", "png", "jpg"};
		for(String form: forms) {
			if(extension.toLowerCase().equals(form))
				return true;
		}
		return false;
	}
	
	/**Called when a multidimensional image file is dropped onto a location*/
	private AbstractUndoableEdit2 handleMultiChannelStackDrop(File f, ImageWindowAndDisplaySet imageAndDisplaySet, GraphicLayer layer , Point2D location2) {
		CombinedEdit undo=new CombinedEdit();
		layer = findValidLayer(layer);
		int startIndex=-1;
		if (layer instanceof FigureOrganizingLayerPane) {
			FigureOrganizingLayerPane figure=(FigureOrganizingLayerPane) layer;
			MultichannelDisplayLayer item = CurrentAppContext.getMultichannelContext().createMultichannelDisplay().creatMultiChannelDisplayFromUserSelectedImage(true, f.getAbsolutePath());
			BasicLayout ml = figure.getLayout();
			figure.getMontageLayoutGraphic().generateCurrentImageWrapper();
			startIndex=figure.getMontageLayout().getPanelIndex((int)location2.getX(), (int) location2.getY());
			if (item==null) return null;
			if (startIndex>0)
				{
				int numberOfEmptyNeeded = figure.getPrincipalMultiChannel().getPanelManager().getPanelList().getChannelUseInstructions().estimageNPanels(item.getMultiChannelImage());
				startIndex=figure.getMontageLayout().getEditor().indexOfFirstEmptyPanel(ml, numberOfEmptyNeeded, startIndex-1);
				}
		
			undo = figure.nextMultiChannel(item, startIndex);
			
			for(ZoomableGraphic g:item.getAllGraphics()) {
				select(g);
			}
			figure.getMontageLayoutGraphic().select();
			figure.getMontageLayoutGraphic().generateCurrentImageWrapper();
			if (figure.hasItem(item)) {
				showFileSizeWarning(item);
			}
			return undo;
		}
		
		
		FigureOrganizingLayerPane aa = new FigureAdder(true).add(layer, f.getAbsolutePath());
		aa.getMontageLayoutGraphic().moveLayoutAndContents(location2.getX(), location2.getY());
		
		imageAndDisplaySet.updateDisplay();
		aa.fixLabelSpaces();
		
		return new UndoAddItem(layer, aa);
		//return aa;
	}

	/**
	Tells the user if his file is to big
	 */
	protected void showFileSizeWarning(MultichannelDisplayLayer item) {
		int size = item.getSlot().getEstimatedSizeOriginal();
		if (size>16*1500*1000*4*2) {
			ShowMessage.showOptionalMessage("That is a large file!", false, "You added a very large file that takes a lot of memory",
					"The original is kept in case you want to change the scale or crop later", 
					"No figure needs to be this large",
					"Please use smaller version of the image in the future ");
		}
	}

	/**finds a layer that can accept a figure organizing layer panel*/
	private GraphicLayer findValidLayer(GraphicLayer layer) {
		FigureOrganizingLayerPane layer2 = FigureOrganizingLayerPane.findFigureOrganizer(layer);
		if (layer2==null) return layer;
		return layer2;
	}
	
	
	/**called when an image file is dropped onto a layer*/
	ImagePanelGraphic handleImageFileDrop(GraphicLayer layer, File f,Point2D location2, boolean hasAlpha) {
		ImagePanelGraphic image = new FileImageAdder(hasAlpha).getImage(f);
		if (image==null)return null;
		image.setRelativeScale(ImageDPIHandler.ratioForIdealDPI());
		layer.add(image);
		image.setLocation(location2);
		return image;
	}
	
	
	public void dragEnter(ImageWindowAndDisplaySet displaySet, DropTargetDragEvent arg0) {
		super.dragEnter(displaySet, arg0);
	}
}
