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
 * Date Modified: Jan 6, 2021
 * Version: 2021.1
 */
package popupMenusForComplexObjects;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import channelMerging.CSFLocation;
import channelMerging.ChannelEntry;
import channelMerging.MultiChannelImage;
import channelMerging.ImageDisplayLayer;
import channelMerging.PreProcessInformation;
import fLexibleUIKit.MenuItemExecuter;
import fLexibleUIKit.MenuItemMethod;
import figureEditDialogs.WindowLevelDialog;
import figureFormat.TemplateUserMenuAction;
import figureOrganizer.FigureOrganizingLayerPane;
import figureOrganizer.LabelCreationOptions;
import figureOrganizer.MultichannelDisplayLayer;
import graphicActionToolbar.CurrentFigureSet;
import graphicalObjects_LayoutObjects.DefaultLayoutGraphic;
import graphicalObjects_SpecialObjects.ComplexTextGraphic;
import graphicalObjects_SpecialObjects.ImagePanelGraphic;
import graphicalObjects_SpecialObjects.TextGraphic;
import iconGraphicalObjects.ChannelUseIcon;
import iconGraphicalObjects.CropIconGraphic;
import iconGraphicalObjects.IconUtil;
import icons.SourceImageTreeIcon;
import icons.ToolIconWithText;
import imageDisplayApp.CanvasOptions;
import layout.basicFigure.BasicLayout;
import layout.basicFigure.LayoutSpaces;
import logging.IssueLog;
import menuUtil.SmartJMenu;
import menuUtil.SmartPopupJMenu;
import menuUtil.BasicSmartMenuItem;
import menuUtil.PopupMenuSupplier;
import multiChannelFigureUI.ChannelPanelEditingMenu;
import objectDialogs.CroppingDialog;
import standardDialog.StandardDialog;
import storedValueDialog.StoredValueDilaog;
import undo.AbstractUndoableEdit2;
import undo.CanvasResizeUndo;
import undo.ChannelDisplayUndo;
import undo.CombinedEdit;
import undo.PreprocessChangeUndo;
import undo.UndoLayoutEdit;

/**A menu for a figure organizing layer*/
public class FigureOrganizingSuplierForPopup implements PopupMenuSupplier, LayoutSpaces, ActionListener {
	FigureOrganizingLayerPane figureOrganizingLayerPane;
	JMenuItem addImageFromFileButton;
	private JMenuItem addOpenImageFromList;
	private JMenuItem rowLabelButton;
	private JMenuItem columnLabelButton;
	private JMenuItem recreatePanelsButton;
	private JMenuItem minMaxButton5;
	private JMenuItem windowLevelButton;
	private JMenuItem channelUseOptionsButton;
	private JMenuItem panelLabelButton;
	private JMenuItem recropPanelsButton;
	private JMenuItem reScalePanelsButton;
	private JMenuItem rePPIPanelsButton;
	
	
	public FigureOrganizingSuplierForPopup(FigureOrganizingLayerPane figureOrganizingLayerPane) {
		this.figureOrganizingLayerPane=figureOrganizingLayerPane;
	}



	@Override
	public JPopupMenu getJPopup() {
		SmartPopupJMenu jj = new SmartPopupJMenu();
		 addMenus(jj);
		return jj;
	}


	/**Adds the menu items from this popup to an arbitrary container*/
	protected void addMenus(Container jj) {
		JMenu imagesMenu = new SmartJMenu("Images", new SourceImageTreeIcon());	
		
		JMenu addImage=new SmartJMenu("Add Image",new SourceImageTreeIcon());
		
		jj.add(addImage);
		addImageFromFileButton = new BasicSmartMenuItem("Image From File");
		addImage.add(addImageFromFileButton);
		addImageFromFileButton.addActionListener(this);
		
		addOpenImageFromList = new BasicSmartMenuItem("Currently Open Image");
		addImage.add(addOpenImageFromList);
		addOpenImageFromList.addActionListener(this);
		
	

		JMenu labelMenu = new SmartJMenu("Add Labels", ComplexTextGraphic.createImageIcon());
		
			 rowLabelButton = new BasicSmartMenuItem("Generate Row Labels", new ToolIconWithText(0, ROW_OF_PANELS).getMenuVersion());
			 labelMenu.add(rowLabelButton);
				rowLabelButton.addActionListener(this);
				
				 columnLabelButton = new BasicSmartMenuItem("Generate Column Labels", new ToolIconWithText(0, COLUMN_OF_PANELS).getMenuVersion());
				 labelMenu.add(columnLabelButton);
					columnLabelButton.addActionListener(this);
					
					panelLabelButton = new BasicSmartMenuItem("Generate Panel Labels", new ToolIconWithText(0, PANELS).getMenuVersion());
					 labelMenu.add(panelLabelButton);
						panelLabelButton.addActionListener(this);
					
						new MenuItemExecuter(this).addToJMenu(labelMenu);
					jj.add(labelMenu);
					
					
					
					
				recropPanelsButton= new BasicSmartMenuItem("Re-Crop All Images");
				recropPanelsButton.addActionListener(this);
				recropPanelsButton.setIcon( CropIconGraphic.createsCropIcon());
				imagesMenu.add(recropPanelsButton);
				
				reScalePanelsButton= new BasicSmartMenuItem("Re-Set Scale for All Images");
				reScalePanelsButton.addActionListener(this);
				imagesMenu.add(reScalePanelsButton);
				
				recreatePanelsButton = new BasicSmartMenuItem("Recreate All Panels");
				jj.add(recreatePanelsButton);
							recreatePanelsButton.addActionListener(this);
				jj.add(imagesMenu);
				rePPIPanelsButton=new BasicSmartMenuItem("Re-Set Pixel Density for All Images");
				rePPIPanelsButton.addActionListener(this);
				imagesMenu.add(rePPIPanelsButton);
				
				
							
				JMenu chanMen=new SmartJMenu("All Channels");
					
					
					
					
					 channelUseOptionsButton = new BasicSmartMenuItem("Channel Use", new ChannelUseIcon());
					
					 chanMen.add(channelUseOptionsButton);
						channelUseOptionsButton.addActionListener(this);
						
						
					
						
					 minMaxButton5 = new BasicSmartMenuItem("Min/Max");
					 minMaxButton5.setIcon(IconUtil.createBrightnessIcon());
					 chanMen.add(minMaxButton5);
						minMaxButton5.addActionListener(this);
						
						 windowLevelButton = new BasicSmartMenuItem("Window/Level");
						 chanMen.add(windowLevelButton);
						 windowLevelButton.setIcon(IconUtil.createBrightnessIcon());
							windowLevelButton.addActionListener(this);
							try {addRecolorMenu(chanMen);} catch (Throwable t) {IssueLog.logT(t);};
							jj.add(chanMen);
							SmartJMenu excluders = this.getMenuContext().createChannelMergeMenu(ChannelPanelEditingMenu.EXCLUDED_CHANNEL_AND_DONT_MERGE);
							excluders.setIcon(new ChannelUseIcon());
							jj.add(excluders);
							
							jj.add(TemplateUserMenuAction.createFormatMenu(figureOrganizingLayerPane));
							
							if (figureOrganizingLayerPane.getMontageLayoutGraphic()!=null) {
								FigureScalerMenu figureScaler = new FigureScalerMenu(figureOrganizingLayerPane.getMontageLayoutGraphic());
								imagesMenu.add(figureScaler.createRescaleMenuItem());
								jj.add(figureScaler);
							}
	}
	
	/**Adds the recolor channels menu*/
	public void addRecolorMenu(JMenu j) {
		
		MultiChannelImage mw = getPrimaryMultichannelWrapper();
		ArrayList<ChannelEntry> iFin = mw.getChannelEntriesInOrder();
		
		ChannelPanelEditingMenu bit = new ChannelPanelEditingMenu(figureOrganizingLayerPane, iFin.get(0).getOriginalChannelIndex());
		
		
		bit.addChenEntryColorMenus( j, iFin);
	}



	public MultiChannelImage getPrimaryMultichannelWrapper() {
		return figureOrganizingLayerPane.getPrincipalMultiChannel().getMultiChannelImage();
	}





	@Override
	public void actionPerformed(ActionEvent arg0) {
		Object source = arg0.getSource();
		CombinedEdit undo=null ;
		if (source==addImageFromFileButton) {
			IssueLog.log("about to open image ");
			undo=figureOrganizingLayerPane.nextMultiChannel(true);
		}
		if (source==addOpenImageFromList) {
			undo=figureOrganizingLayerPane.nextMultiChannel(false);
		}
		
		if (source==rowLabelButton||source==columnLabelButton||source==panelLabelButton) {
			int type=BasicLayout.ROWS;
			if(source==columnLabelButton)  type=BasicLayout.COLS;
			if(source==panelLabelButton)  type=BasicLayout.PANELS;
			
			CombinedEdit many = figureOrganizingLayerPane.addRowOrColLabel(type);
			
			/**Adds to the undo manager*/
			figureOrganizingLayerPane.getUndoManager().addEdit(many);
		}
		
		
		
		if (source==recreatePanelsButton) {
            
			figureOrganizingLayerPane.recreateFigurePanels();
        }
		
		if (source ==recropPanelsButton) {
			
			undo= recropAll();
		}
		
		if (source ==reScalePanelsButton) {
			undo=showReScaleAll();
		}
		
		if (source ==rePPIPanelsButton) {
			undo=showRePPIAll();
		}
		
		ChannelPanelEditingMenu bit = getMenuContext();
		if (source==minMaxButton5) {
			CombinedEdit undoMinMax = ChannelDisplayUndo.createMany(figureOrganizingLayerPane.getAllSourceImages(), bit);
			undo=undoMinMax;
			WindowLevelDialog.showWLDialogs(getPrimaryMultichannelWrapper().getChannelEntriesInOrder(), getPrimaryMultichannelWrapper(), bit, WindowLevelDialog.MIN_MAX, undoMinMax);
			
		}
		if (source==windowLevelButton) {
			CombinedEdit undoMinMax = ChannelDisplayUndo.createMany(figureOrganizingLayerPane.getAllSourceImages(), bit);
			undo=undoMinMax;
			WindowLevelDialog.showWLDialogs(getPrimaryMultichannelWrapper().getChannelEntriesInOrder(), getPrimaryMultichannelWrapper(), bit,  WindowLevelDialog.WINDOW_LEVEL, undoMinMax);
			
		}
		
		if (source==channelUseOptionsButton){
			figureOrganizingLayerPane.showChannelUseOptions();
			
		}
		
		figureOrganizingLayerPane.getUndoManager().addEdit(undo);
	}



	/**
	generates a channel panel editing menu context for this popup menu
	 */
	public ChannelPanelEditingMenu getMenuContext() {
		return new ChannelPanelEditingMenu( figureOrganizingLayerPane, ChannelPanelEditingMenu.ALL_IMAGES_IN_CLICKED_FIGURE);
	}

	

	/**returns a label editor for the given text item*/
	public EditLabels getLabelEditorMenuItemFor(TextGraphic t) {
		int gridSnap = t.getAttachmentPosition().getGridSpaceCode();
		EditLabels output = new EditLabels(gridSnap, figureOrganizingLayerPane.getMontageLayoutGraphic(), t);
	
		if(output.getLabels(t).size()==0) {
			
			return null;
		}
		return output;
	}

/**Opens a dialog to recrop all the panels
 * @return */
	public CombinedEdit recropAll() {
		MultichannelDisplayLayer crop1 = (MultichannelDisplayLayer) figureOrganizingLayerPane.getPrincipalMultiChannel();
		ArrayList<ImageDisplayLayer> all = figureOrganizingLayerPane.getMultiChannelDisplays();
		
		return recropManyImages(crop1, all);
	}


	/**shows a dialog for changing the drop area for many multichannel images within the figure*/
public static CombinedEdit recropManyImages(MultichannelDisplayLayer crop1, ArrayList<? extends ImageDisplayLayer> all) {
	CombinedEdit output = new CombinedEdit();
	output.addEditToList(
			showRecropDisplayDialog( crop1, null)
			);
	PreProcessInformation modifications = crop1.getSlot().getModifications();
	Rectangle r1=null;
	if (modifications!=null)
		r1= modifications.getRectangle();
	Dimension d1;
	if (r1==null) {
		d1=crop1.getMultiChannelImage().getDimensions();
	}else d1=new Dimension(r1.width, r1.height);
	
	
	for(ImageDisplayLayer crop2: all) {
		if(crop2==crop1) continue;
		output.addEditToList(
				showRecropDisplayDialog( (MultichannelDisplayLayer) crop2, d1)
		);
	}
	if (CanvasOptions.current.resizeCanvasAfterEdit)
		output.addEditToList(
				CurrentFigureSet.canvasResizeUndoable()
				);
	return output;
}

	/**shows a cropping dialog*/
	public static CombinedEdit showRecropDisplayDialog(MultichannelDisplayLayer display, Dimension dim) {
		PreProcessInformation original = display.getSlot().getModifications();
		display.getPanelManager().setupViewLocation();
		PreprocessChangeUndo undo1 = new PreprocessChangeUndo(display);
		CSFLocation csfInitial = display.getSlot().getDisplaySlice().duplicate();
		
		CroppingDialog.showCropDialogOfSize(display.getSlot(), dim);
		
		onViewLocationChange(display, csfInitial, display.getSlot().getDisplaySlice());
		
		if (display.getSlot().getModifications()!=null&&display.getSlot().getModifications().isSame(original)
				) {
			return null;
		}
		undo1.establishFinalLocations();
		
		
		return new CombinedEdit(undo1, updateRowColSizesOf(display));
		
	}


	/**Called if the user switches slices or channels*/
	private static void onViewLocationChange(MultichannelDisplayLayer display, CSFLocation i,
			CSFLocation f) {
		
		if (!display.getPanelManager().selectsSlicesOrFrames()) return;
		
		if (f.changesT(i)  )  {
			display.getPanelManager().performReplaceOfIndex(
					CSFLocation.frameLocation(i.frame), CSFLocation.frameLocation(f.frame)
					);
			
		}
			if (f.changesZ(i)  )  {
				display.getPanelManager().performReplaceOfIndex(
						CSFLocation.sliceLocation(i.slice), CSFLocation.sliceLocation(f.slice)
						);
				}	
	}


	/**called to resize the layout in order to match the dimensions of object within the layout*/
	public static UndoLayoutEdit updateRowColSizesOf(MultichannelDisplayLayer display) {
		
		if (display.getParentLayer() instanceof FigureOrganizingLayerPane) {
			FigureOrganizingLayerPane f=(FigureOrganizingLayerPane) display.getParentLayer();
			DefaultLayoutGraphic l = f.getMontageLayoutGraphic();
			l.generateCurrentImageWrapper();
			UndoLayoutEdit undo = new UndoLayoutEdit(l);
			l.getEditor().alterPanelWidthAndHeightToFitContents(l.getPanelLayout());
			undo.establishFinalLocations();
			return undo;
		}
		return null;
	}
	
	/**shows a dialog for changing the scale factor of many multichannel images within the figure*/
	CombinedEdit showReScaleAll() {
		CombinedEdit output     = showReScaleAllDisplayDialog((MultichannelDisplayLayer) figureOrganizingLayerPane.getPrincipalMultiChannel());
		CanvasResizeUndo output2 = CurrentFigureSet.canvasResizeUndoable();
		return new CombinedEdit(output, output2);
	}




	/**shows a dialog for changing the scale factor of many multichannel images within the figure*/
	 CombinedEdit showReScaleAllDisplayDialog(MultichannelDisplayLayer display) {
		 CombinedEdit output = new CombinedEdit();
		double newScale = showRescaleDialogSingleFor(display);
		
		output.addEditToList(
				applyNewScaleTo(display, newScale)
				);
		
		ArrayList<ImageDisplayLayer> all = figureOrganizingLayerPane.getMultiChannelDisplays();
		
		for(ImageDisplayLayer crop2: all) {
			if(crop2==display) continue;
			
				output.addEditToList(
							applyNewScaleTo((MultichannelDisplayLayer) crop2, newScale));
		}
		
		return output;
	}
	 
	 /**shows a dialog for the user to input a pixel density for all the images*/
	 private CombinedEdit showRePPIAll() {
		 CombinedEdit output = new CombinedEdit();
		 double newPPI = showPPISingleImage(figureOrganizingLayerPane.getPrincipalMultiChannel());
		 ArrayList<ImageDisplayLayer> all = figureOrganizingLayerPane.getMultiChannelDisplays();
		 for(ImageDisplayLayer crop2: all) {
			 output.addEditToList(
				((MultichannelDisplayLayer) crop2).getPanelManager().changePPI(newPPI)
				);
			}
		 return output;
		}



	 /**shows a dialog for the user to input a pixel density for an image*/
	private double showPPISingleImage(ImageDisplayLayer principalMultiChannel) {
		ImagePanelGraphic panel = principalMultiChannel.getPanelManager().getPanelList().getPanels().get(0).getPanelGraphic();
		double ppi = panel.getQuickfiguresPPI();
		double newppi=StandardDialog.getNumberFromUser("Input Pixels per inch ", ppi);
		return newppi;
	}


	/**shows a dialog for changing the scale factor one image*/
	public static double showSingleImageRescale(MultichannelDisplayLayer display) {
		double newScale = showRescaleDialogSingleFor(display);
		
		applyNewScaleTo(display, newScale);
		return newScale;
	}



	protected static double showRescaleDialogSingleFor(MultichannelDisplayLayer display) {
		PreProcessInformation original = display.getSlot().getModifications();
		
		double oldScale =1;
		if (original!=null)
				oldScale= original.getScale();
		
		double newScale = FigureScalerMenu.getScaleFromDialog("Change Image Scale", "Scaling with Bilinear Interpolation is done", oldScale);
		return newScale;
	}


/**Sets a new preprocess scale for the image, panels will be resized by this change and
  layout rows and columns will also be resized*/
	public static AbstractUndoableEdit2 applyNewScaleTo(MultichannelDisplayLayer display, double newScale) {
		if (display.getPreprocessScale()==newScale)
			return null;
		PreprocessChangeUndo output1 = new PreprocessChangeUndo(display);
		display.setPreprocessScale(newScale);
		output1.establishFinalLocations();
		UndoLayoutEdit output2 = updateRowColSizesOf(display);
		
		return new CombinedEdit(output1,output2 );
	}
	
	/**shows a labeling options dialog*/
	@MenuItemMethod(menuActionCommand = "Label Creation Options", menuText = "Label Creation Options")
	public void changeLabelProperties() {
		new StoredValueDilaog(LabelCreationOptions.current).showDialog();;
	}
	
	
}