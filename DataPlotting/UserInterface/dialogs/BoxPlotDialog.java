package dialogs;

import java.util.ArrayList;

import objectDialogs.GraphicItemOptionsDialog;
import plotParts.DataShowingParts.Boxplot;
import standardDialog.ComboBoxPanel;
import standardDialog.NumberInputPanel;

public class BoxPlotDialog  extends GraphicItemOptionsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	Boxplot mainBox;
	ArrayList<Boxplot> additionalBoxes=new ArrayList<Boxplot>();

	private boolean bareBones;
	
	
	public BoxPlotDialog(ArrayList<?> objects) {
		this.bareBones=true;
		
		for(Object o: objects) {
			if (o instanceof Boxplot) {
				if (mainBox==null) {
					mainBox=(Boxplot) o;
					addOptionsToDialog();
				}
				else additionalBoxes.add((Boxplot) o);
			}
		}
	}
	
	public BoxPlotDialog(Boxplot b, boolean bareBones) {
		this.bareBones=bareBones;
		mainBox=b;
		addOptionsToDialog();
	}
	public void addAdditionalBars(ArrayList<Boxplot> bars) {
		additionalBoxes=bars;
	}


	@Override
	public void addOptionsToDialog() {
		addBarAttributesToDialog(mainBox);
		
		
	}
	
	public void addBarAttributesToDialog(Boxplot  rect) {
		if (!bareBones)
		{
			super.addNameField(rect);
			super.addStrokePanelToDialog(rect);

		}
		
		addMeanBarSpecificOptions(rect);
		
	}


	protected void addMeanBarSpecificOptions(Boxplot rect) {
		NumberInputPanel nip = new NumberInputPanel("Box Width", rect.getBarWidth(), 4, 40);
		nip.setDecimalPlaces(2);
		this.add("width", nip);
		
		NumberInputPanel nip2 = new NumberInputPanel("Cap Width", rect.getCapSize(), 4);
	
		this.add("width2", nip2);
		
		this.add("typ",
				new ComboBoxPanel("Show ends as", new String[] {"Min/Max", "Min/Max excluding outliers"}, rect.getWhiskerType()));
	}
	
	@Override
	public void setItemsToDiaog() {
		setItemsToDialog(mainBox);
		for(Boxplot bar: this.additionalBoxes) {setItemsToDialog(bar);}
		return ;
	}
	
	public void setItemsToDialog(Boxplot  rect) {
		if (!bareBones)
		{super.setNameFieldToDialog(rect);
		super.setStrokedItemtoPanel(rect);
		}
		
		setBoxBarSpecific(rect);
}


	protected void setBoxBarSpecific(Boxplot rect) {
		rect.setBarWidth((int)getNumber("width"));
		rect.setWhiskerType(this.getChoiceIndex("typ"));
		rect.setCapSize(getNumber("width2"));
		rect.requestShapeUpdate();
		rect.updatePlotArea();
	}
	
	
}