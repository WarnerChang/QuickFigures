package objectDialogs;

import graphicalObjects.BufferedImageGraphic;
import graphicalObjects.ImagePanelGraphic;
import standardDialog.AngleInputPanel;
import standardDialog.BooleanArrayInputPanel;
import standardDialog.BooleanInputPanel;
import standardDialog.ColorCheckbox;
import standardDialog.ColorComboboxPanel;
import standardDialog.ComboBoxPanel;
import standardDialog.InfoDisplayPanel;
import standardDialog.NumberInputPanel;
import undo.Edit;


public class ImageGraphicOptionsDialog extends GraphicItemOptionsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	ImagePanelGraphic   image;
	
	public ImageGraphicOptionsDialog() {}
	public ImageGraphicOptionsDialog(BufferedImageGraphic bg) {
		image=bg;
		addOptionsToDialog() ;
		super.undoableEdit=Edit.createGenericEditForItem(bg);
	}
	
	public ImageGraphicOptionsDialog(ImagePanelGraphic bg) {
		image=bg;
		addOptionsToDialog() ;
		super.undoableEdit=Edit.createGenericEditForItem(bg);
	}
	
	
	
	protected void addOptionsToDialog() {
		super.addNameField(image);
	
		super.addFixedEdgeToDialog(image);
		
		addCommonOptionsToDialog();
		this.addScaleInfoToDialog(image.getScaleInfo());
		
			this.add("embed", new BooleanInputPanel("Embed ", image.isEmbed()));
			if (image.isFilederived()) {
				this.moveGrid(2, -1);
				this.add("fileLoad", new BooleanInputPanel("Always Load From File", image.isLoadFromFile()));
				this.moveGrid(-2, 0);
			}
			
			this.add("Dimensions", new InfoDisplayPanel("Dimensions", image.getDimensionString()));
			this.add("Dimensions2", new InfoDisplayPanel("", image.getRealDimensionString()));
			
			this.add("PPI", 
					new InfoDisplayPanel("PPI ", image.getIllustratorPPI() /**+" ("+image.getScreenPPI()+" on screen)"*/));
			//this.add("PPI-ink", 
			//		new InfoDisplayPanel("PPI (Inkscape-like)", image.getInkscapePPI() /**+" ("+image.getScreenPPI()+" on screen)"*/));
			
			
		if (image instanceof BufferedImageGraphic) {
			BufferedImageGraphic image2=(BufferedImageGraphic) image;
	
		
		
		this.add("ExcludedChannels", new BooleanArrayInputPanel("Include Colors ", image2.getRemovedChannels(), ColorCheckbox.get4Channel()));
		this.add("Force Gray channel", new ComboBoxPanel("Force Gray channel ", new String[]{"None", "Red", "Green", "Blue"}, image2.getForceGrayChannel()));
		}
		
		AngleInputPanel aip = new AngleInputPanel("Angle", image.getAngle(), true);
		this.add("Angle", aip);
		
		this.add("locked in place", new BooleanInputPanel("Protect from mouse drags ", image.isUserLocked()==1));

		this.addSnappingBehviourToDialog(image);
	}
	
	public void addCommonOptionsToDialog() {
		this.add("scale", new NumberInputPanel("Relative Scale", image.getScale(), 2) );
		this.add("frame", new NumberInputPanel("Frame Width", image.getFrameWidthH(), 3) );
		this.add("frameC", new ColorComboboxPanel("Frame Color ", null, image.getFrameColor()));
	}
	
	protected void setItemsToDiaog() {
			setNameFieldToDialog(image);
			//IssueLog.log("setting image to options");
			
		//image.setName(this.getNextString());
		//image.setLocationType(this.getNextChoiceIndex());
				setFixedEdgeToDialog(image);
				setCommonOptionsToDialog(image);
				
				image.setEmbed(this.getBoolean("embed"));
				image.setUserLocked(this.getBoolean("locked in place")?1:0);
				if (image.isFilederived()) {
					
					image.setLoadFromFile(this.getBoolean("fileLoad"));
				}
				Strings.get("Dimensions").setContentText(image.getRealDimensionString());
				Strings.get("Dimensions2").setContentText(image.getDimensionString());
				Strings.get("PPI").setContentText(image.getIllustratorPPI() /**+" "+image.getScreenPPI()*/);
				//Strings.get("PPI-ink").setContentText(image.getInkscapePPI() /**+" "+image.getScreenPPI()*/);
				
				
				if (image instanceof BufferedImageGraphic) {
					BufferedImageGraphic	image2=(BufferedImageGraphic) image;
					
				image2.setRemovedChannels(this.getBooleanArray("ExcludedChannels"));
				image2.setForceGrayChannel((int)this.getChoiceIndex("Force Gray channel"));
				}
					this.setScaleInfoToDialog(image.getScaleInfo());
					this.setObjectSnappingBehaviourToDialog(image);
					image.setAngle(this.getNumber("Angle"));
	}
	
	public void setCommonOptionsToDialog(ImagePanelGraphic   image) {
		image.setScale(this.getNumber("scale"));
		image.setFrameWidthH((float)this.getNumber("frame"));
		image.setFrameColor(this.getColor("frameC"));
		image.notifyListenersOfUserSizeChange();
	}
}
