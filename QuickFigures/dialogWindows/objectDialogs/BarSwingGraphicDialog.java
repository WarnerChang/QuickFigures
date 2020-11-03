package objectDialogs;

import javax.swing.JTabbedPane;

import graphicalObjects_BasicShapes.BarGraphic;
import graphicalObjects_BasicShapes.TextGraphic;
import standardDialog.BooleanInputPanel;
import standardDialog.ColorComboboxPanel;
import standardDialog.ComboBoxPanel;
import standardDialog.NumberInputPanel;
import undo.CompoundEdit2;
import undo.Edit;

public class BarSwingGraphicDialog  extends GraphicItemOptionsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	BarGraphic rect;

	
	
	
	public BarSwingGraphicDialog() {}
	public BarSwingGraphicDialog(BarGraphic b) {
		//super(b);
		rect=b;
		addOptionsToDialog();
		super.undoableEdit=Edit.createGenericEditForItem(b);
	}


	@Override
	public void addOptionsToDialog() {
		addBarAttributesToDialog(rect);
		
		
	}
	
	public void addBarAttributesToDialog(BarGraphic rect) {
	super.addNameField(rect);
		//this.addStringField("Name ", rect.getName(), 30);
		super.addFixedEdgeToDialog(rect);
		
		NumberInputPanel nip = new NumberInputPanel("Width in "+rect.getScaleInfo().getUnits(), rect.getLengthInUnits());
		nip.setDecimalPlaces(2);
		this.add("uwidth", nip);
		
		
		this.add("barstroke", new NumberInputPanel("Thickness ", rect.getBarStroke(), true, true, 0,25));
		
		this.add("plen", new NumberInputPanel("Projection length", rect.getProjectionLength(), true, true, 0,50));
		this.add("ptype", new ComboBoxPanel("Projection Type", BarGraphic.projTypes, rect.getProjectionType()));
		this.addScaleInfoToDialog(rect.getScaleInfo());
		
		this.add("Angle",new NumberInputPanel("angle", rect.getAngle()*(180/Math.PI)));
		this.add("fill" ,new ColorComboboxPanel("Fill Color",null, rect.getFillColor()));
		this.add("showT" , new BooleanInputPanel("Show text ", rect.isShowText()));
		this.getMainPanel().moveGrid(2, -1);
		this.add("autoT" ,new BooleanInputPanel("Autolocate text ", rect.isSnapBarText()));
		this.getMainPanel().moveGrid(-2, 0);
		super.addSnappingBehviourToDialog(rect);
		
		TextGraphic t = rect.getBarText();
		TextGraphicSwingDialog tgsd = new TextGraphicSwingDialog(t);
		JTabbedPane mp = tgsd.removeOptionsTab();
		this.getOptionDisplayTabs().addTab("Bar Text", mp);
	}
	
	@Override
	public void setItemsToDiaog() {
		setItemsToDialog(rect);
		return ;
	}
	
	public void setItemsToDialog(BarGraphic rect) {
		super.setNameFieldToDialog(rect);
		//rect.setName(this.getNextString());
		//rect.setLocationType(this.getNextChoiceIndex());
		super.setFixedEdgeToDialog(rect);
		
		rect.setLengthInUnits(this.getNumber("uwidth"));
		rect.setLengthProjection((int)this.getNumber("plen"));
		rect.setBarStroke((this.getNumber("barstroke")));
		this.setScaleInfoToDialog(rect.getScaleInfo());
		rect.setProjectionType(this.getChoiceIndex("ptype"));
		
		double newangle=this.getNumber("Angle")/(180/Math.PI);
		rect.setAngle(newangle);
		rect.setShowText(this.getBoolean("showT"));
		rect.setSnapBarText(this.getBoolean("autoT"));
		rect.setFillColor(this.getColor("fill"));
		super.setObjectSnappingBehaviourToDialog(rect);
	}
	
	

}