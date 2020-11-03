package standardDialog;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import graphicalObjects_BasicShapes.RectangularGraphic;
import graphicalObjects_BasicShapes.BasicGraphicalObject;
import menuUtil.SmartJMenu;

public class GraphicJMenuItem extends JMenuItem {

	/**
	 * 
	 * 
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private GraphicDisplayComponent displayedGraphicalObject=new 	GraphicDisplayComponent("String", null, false);

	public GraphicJMenuItem(String message) {
		super(message);
		displayedGraphicalObject=new 	GraphicDisplayComponent(message, null, false);
	}
	
	public GraphicJMenuItem(String message, BasicGraphicalObject b) {
		super(message);
		displayedGraphicalObject=new 	GraphicDisplayComponent(message,b, false);
	}
	
	
	public void paintComponent(Graphics g) {
		getDisplayedGraphicalObject().setSelected(this.isArmed());
		getDisplayedGraphicalObject().paintComponent(g);

	}
	
	public Dimension getSize() {
		return getDisplayedGraphicalObject().getSize();
	}
	
	public Dimension  getPreferredSize() {
		return getDisplayedGraphicalObject().getPreferredSize();
		}

	public GraphicDisplayComponent getDisplayedGraphicalObject() {
		return displayedGraphicalObject;
	}

	public void setDisplayedGraphicalObject(GraphicDisplayComponent displayedGraphicalObject) {
		this.displayedGraphicalObject = displayedGraphicalObject;
	}
	
	
	public static void main(String[] args) {
		JFrame jf = new JFrame();
		
		JMenuBar b=new JMenuBar();
		jf.setJMenuBar(b);
		JMenu m1=new SmartJMenu("Menu 1");
		b.add(m1);
		m1.add(new GraphicJMenuItem("Hello"));
		m1.add(new GraphicJMenuItem("Wow"));
		m1.add(new GraphicJMenuItem("nope"));
		GraphicJMenuItem i = new GraphicJMenuItem("nope", RectangularGraphic.blankRect(new Rectangle(0, 0,49,30), Color.blue));
		//i. setDisplayedGraphicalObject(new GraphicDisplayComponent(null, new RectangularGraphic(0,0,20,30), false));
		
		m1.add(i);
		
		JMenuItem i2=new JMenuItem("hello", new 	GraphicDisplayComponent("",RectangularGraphic.blankRect(new Rectangle(0, 0,49,30), Color.red), false));
		m1.add(i2);
		jf.pack();jf.setVisible(true);
		
	}
}
