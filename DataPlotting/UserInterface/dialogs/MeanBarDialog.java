package dialogs;

import java.util.ArrayList;

import javax.swing.JTabbedPane;

import objectDialogs.GraphicItemOptionsDialog;
import plotParts.DataShowingParts.DataBarShape;
import plotParts.DataShowingParts.PointModel;
import standardDialog.ColorComboboxPanel;
import standardDialog.ComboBoxPanel;
import standardDialog.DialogItemChangeEvent;
import standardDialog.NumberInputPanel;
import standardDialog.SwingDialogListener;

public class MeanBarDialog  extends GraphicItemOptionsDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	DataBarShape rect;
	ArrayList<DataBarShape> additionalBars=new ArrayList<DataBarShape>();

	private boolean bareBones;

	private PointOptionsDialog dia2;

	private boolean points;
	
	
	public MeanBarDialog(DataBarShape b, boolean bareBones) {
		this.bareBones=bareBones;
		rect=b;
		addOptionsToDialog();
	}
	
	public  MeanBarDialog(ArrayList<?> objects) {
		this.bareBones=true;
		
		for(Object o: objects) {
			if (o instanceof DataBarShape ) {
				if (rect==null) {
					rect=(DataBarShape ) o;
					addOptionsToDialog();
				}
				else {
					additionalBars.add((DataBarShape ) o);
					addToPointList((DataBarShape) o);
				}
			}
		}
	}
	
	public void addAdditionalBars(ArrayList<DataBarShape> bars) {
		additionalBars=bars;
		for(DataBarShape b: bars) {
			addToPointList(b);
		}
	}

	public void addToPointList(DataBarShape b) {
		if (dia2!=null&&points) dia2.addAdditionalPoint(b.getPointModel());
	}


	@Override
	public void addOptionsToDialog() {
		addBarAttributesToDialog(rect);
		
		
	}
	
	public void addBarAttributesToDialog(DataBarShape  rect) {
		if (!bareBones)
		{
			super.addNameField(rect);
			super.addStrokePanelToDialog(rect);
			ColorComboboxPanel filpanel = new ColorComboboxPanel("Fill Color", null, rect.getFillColor());
			this.add("FillColor", filpanel);
		}
		
		if (rect.showsAsPoint()) {
			points=true;
			PointModel m = rect.getPointModel();
			dia2 = new PointOptionsDialog(m, bareBones, new SwingDialogListener() {

				@Override
				public void itemChange(DialogItemChangeEvent event) {
					afterEachItemChange();
					
				}});
			JTabbedPane tab2 = dia2.removeOptionsTab();
			tab2.setName("Point Options");
			this.getOptionDisplayTabs().addTab("Edit Points", tab2);
		}
		
		addMeanBarSpecificOptions(rect);
		
	}


	protected void addMeanBarSpecificOptions(DataBarShape rect) {
		NumberInputPanel nip = new NumberInputPanel("Bar Width", rect.getBarWidth(), 0, 50);
		nip.setDecimalPlaces(2);
		this.add("width", nip);
		
		this.add("typ",
				new ComboBoxPanel("Show as", new String[] {"Line only", "Bar", "Hidden", "Point"}, rect.getBarType()));
	}
	
	@Override
	public void setItemsToDiaog() {
		setItemsToDialog(rect);
		for(DataBarShape bar: this.additionalBars) {setItemsToDialog(bar);}
		return ;
	}
	
	public void setItemsToDialog(DataBarShape  rect) {
		if (!bareBones)
		{super.setNameFieldToDialog(rect);
		rect.setFillColor(super.getColor("FillColor"));
		super.setStrokedItemtoPanel(rect);
		}
		
		setMeanBarSpecific(rect);
}


	protected void setMeanBarSpecific(DataBarShape rect) {
		if (rect==null) return;
		rect.setBarWidth((int)getNumber("width"));
		rect.setBarType(this.getChoiceIndex("typ"));
		
		rect.requestShapeUpdate();
		rect.updatePlotArea();
	}
	
	
}