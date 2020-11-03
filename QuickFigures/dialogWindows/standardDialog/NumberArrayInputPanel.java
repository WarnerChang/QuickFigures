package standardDialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class NumberArrayInputPanel extends NumberInputPanel implements KeyListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private ArrayList<NumericTextField> fields=new ArrayList<NumericTextField>();
	private ArrayList<Double> numbers=new ArrayList<Double>();
	private ArrayList<Double> numbersOriginal=new ArrayList<Double>();//first innitialization of the numbers
	int precision=1;
	
	public NumberArrayInputPanel(int nNumbers, int precis) {
		precision=precis;
		for(int i=0; i<nNumbers; i++) {
			addFieldAndNumber(0) ;
		}
	
	}
	
	public void setItemFont(Font f) {
		super.setItemFont(f);
		for(NumericTextField f2:fields) {
			f2.setFont(f);
		}
	 }
	
	
	public void addFieldAndNumber(double number) {
		NumericTextField f = new NumericTextField(number, precision);
		f.addKeyListener(this);
		fields.add(f);
		numbers.add(new Double(0));
		numbersOriginal.add(new Double(0));
		this.add(f);
		
	}
	
	public void setLabel(String st) {
		label.setText(st);
	}

	public void setNumber(int index, Double value) {
		if (numbers==null) return;
		while(index>=numbers.size()) addFieldAndNumber(0);
		if (value!=null)numbers.set(index, new Double(value)); else numbers.set(index, null);
		if (value!=null)fields.get(index).setNumber(value);
		else fields.get(index).setText("");
	}
	
	public float[] getArray() {
		ArrayList<Float> oo=new ArrayList<Float>();
		for(NumericTextField f: fields) {
			if (!f.isBlank()) oo.add(new Float(f.getNumberFromField()));
			
		}
		float[] output = new float[oo.size()];
		for(int i=0; i<oo.size(); i++) {output[i]=oo.get(i);}
		
		
		return output;
		
	}
	
	public void setArray(float[] f) {
		if (f==null) return;
		for(int i=0; i<fields.size(); i++) {
			if (i<f.length) fields.get(i).setNumber(f[i]);
			else fields.get(i).setText("");
		
			
			if (i<f.length)  numbersOriginal.set(i, new Double(f[i]));
			else numbersOriginal.set(i,null);
		}
	}
	
	public void revert() {
		for(int i=0; i<fields.size(); i++) {
			Double numberI = numbersOriginal.get(i);
			this.setNumber(i, numberI );
		}
	}
	
	//public NumberArrayInputPanel(float[] nums) {}
	//public NumberArrayInputPanel(double[] nums) {}
	//public NumberArrayInputPanel(int[] nums) {}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		Object source = arg0.getSource();
		if(source instanceof NumericTextField) {
			double d=((NumericTextField) source).getNumberFromField();
			int i = fields.indexOf(source);
			numbers.set(i, d);
		NumberInputEvent nai = new NumberInputEvent(this, (Component)arg0.getSource(), d, getArray());
		super.notifyListeners(nai);
		}
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	public static void main(String[] args) {
		JFrame ff = new JFrame("frame");
		ff.setLayout(new FlowLayout());
		ff.add(new JButton("button"));
		NumberArrayInputPanel sb = new NumberArrayInputPanel(3, 1);
		sb.setNumber(5, 3.12888888);
		sb.setLabel("input array of numbers");
		ff.add(sb);
		ff.pack();
		
		ff.setVisible(true);
	}
	
	@Override
	public void placeItems(Container jp, int x0, int y0) {
		GridBagConstraints gc = new GridBagConstraints();
		gc.gridx=x0;
		gc.gridy=y0;
		gc.insets=firstInsets;
		gc.anchor = GridBagConstraints.EAST;
		jp.add(label, gc);
		gc.anchor = GridBagConstraints.WEST;
		gc.gridx++;
		gc.gridwidth=3;
			jp.add( fieldPanel() , gc);
		
		
		
		
	}
	
	JPanel fieldPanel() {
		JPanel fieldPanel=new JPanel();
		fieldPanel.setLayout(new FlowLayout());
		for(NumericTextField f: fields) {
			
			fieldPanel.add(f);
			}
		return fieldPanel;
	}
	
/**
	@Override
	public void keyReleased(KeyEvent arg0) {
		int index = fields.indexOf(arg0.getSource());
		if(index>-1) {
			//slider.setValue((int)field.getNumberFromField());
			//number=field.getNumberFromField();
			numbers.set(index, fields.get(index).getNumberFromField());
			
			notifyListeners(new NumberInputEvent(this,  fields.get(index), fields.get(index).getNumberFromField()) );
		}
	}*/

}
