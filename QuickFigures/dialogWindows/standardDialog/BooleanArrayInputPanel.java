package standardDialog;

import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class BooleanArrayInputPanel extends BooleanInputPanel {

	
	ArrayList<JCheckBox> boxes=new ArrayList<JCheckBox>();
	
	public BooleanArrayInputPanel(String labeln, boolean[] b) {
		super(labeln, false);
		setArray(b);
		this.setLayout(new GridBagLayout());
		placeItems(this,0,0);
	}
	
	public BooleanArrayInputPanel(String labeln, boolean[] b, ArrayList<JCheckBox> check) {
		super(labeln, false);
		this.setBoxes(check);
		setArray(b);
		this.setLayout(new GridBagLayout());
		placeItems(this,0,0);
	}
	
	public void setBoxes(ArrayList<JCheckBox> check) {
		boxes=check;
		for(JCheckBox b: check) {b.addItemListener(this);};
	}
	
	public void setArray(boolean[] b) {
		for(int i=0; i<b.length; i++) {
			if (boxes.size()>i) boxes.get(i).setSelected(b[i]);
			else addBox( b[i]);
		}
	}
	
	public boolean[] getArray() {
		boolean[] b=new boolean[boxes.size()];
		for(int i=0; i<b.length; i++) {
			b[i]=boxes.get(i).isSelected();
		}
		return b;
	}
	
	public void addBox(boolean selected) {
		JCheckBox newbox = new JCheckBox("", selected);
		boxes.add( newbox);
		 newbox.addItemListener(this);
	}
	
	@Override
	public void placeItems(Container jp, int x0, int y0) {
		GridBagConstraints gc = new GridBagConstraints();
		
		gc.insets=firstInsets;
		gc.gridx=x0;
		gc.gridy=y0;
		gc.anchor = GridBagConstraints.EAST;
		jp.add(label, gc);
		gc.gridx++;
		gc.insets=lastInsets;
		gc.anchor = GridBagConstraints.WEST;
		jp.add(getAllBoxPanel(), gc);
		
		
	}
	
	public JPanel getAllBoxPanel() {
		JPanel output = new JPanel();
		output.setLayout(new FlowLayout());
		for(JCheckBox j:boxes) output.add(j);
		return output;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void itemStateChanged(ItemEvent arg0) {
		int index = boxes.indexOf(arg0.getSource());
		if (index>-1) {
			BooleanInputEvent bi = new BooleanInputEvent(this, boxes.get(index), boxes.get(index).isSelected());
			
			this.notifyListeners(bi);
		}
		
	}

}