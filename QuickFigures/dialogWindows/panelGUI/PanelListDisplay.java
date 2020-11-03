package panelGUI;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import channelMerging.ChannelEntry;
import genericMontageKit.PanelList;
import genericMontageKit.PanelListElement;
import graphicalObjects_FigureSpecific.MultichannelDisplayLayer;
import graphicalObjects_FigureSpecific.PanelManager;
import standardDialog.ColorDimmingBox;


/**experimenting with a way for the user to look through the parts of a panel list*/
public class PanelListDisplay extends JList implements ActionListener, DropTargetListener, KeyListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelList list=null;
	Vector<PanelListElement> elements=new Vector<PanelListElement>();
	private specialCellRenderer render= new specialCellRenderer();
	private PanelManager panelManager;
	
	
	public PanelListDisplay(PanelManager man) {
		this.panelManager=man;
		this.setList(man.getStack());
		this.setDragEnabled(true);
		addListSelectionListener(new listener1(this));
		new DropTarget(this, this);
		this.addKeyListener(this);
	}
	
	void setList(PanelList list) {
		this.list=list;
		elements.clear();
		elements.addAll(list.getPanels());
		this.setListData(elements);
	}
	

	
	
	void selectListSelectedDisplays() {
		for(int i=0; i<elements.size(); i++) {
			 elements.get(i).selectLabelAndPanel(false);;
			
			
		}
		
		
		int[] index1 = this.getSelectedIndices();
		
		for(int i=0; i<index1.length; i++) {
			PanelListElement ele = elements.get(index1[i]);
			ele.selectLabelAndPanel(true);
			
			ele.getPanelGraphic().updateDisplay();
		}
	
	}
	
	public static void main(String[] arg) {
		
		;
		//ImagePlusWrapper wrap = new ImagePlusWrapper(IJ.openImage());
	//	MultichannelImageDisplay multi = new IJ1MultiChannelCreator().creatMultiChannelDisplayFromUserSelectedImage(true, null);
	//	showMultiChannel(multi);
		
	}
	
	public static void showMultiChannel(MultichannelDisplayLayer multi) {
		JFrame jf = new JFrame();
		jf.setLayout(new FlowLayout());
		PanelListDisplay distpla = new PanelListDisplay(multi.getPanelManager());
		jf.add(distpla);
		JButton swapButton=new JButton("Swap Items");
		jf.add(swapButton);
		swapButton.addActionListener(distpla);
		jf.pack();jf.setVisible(true);
		
	}
	
	public  ListCellRenderer	getCellRenderer() {return render;}
	
	
	class specialCellRenderer extends  DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int theindex;
		private int panelNumber;
		private boolean focus;
		private boolean isSelected;
		public Component 	getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			theindex=index;
			focus=cellHasFocus;
			Component out = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (out instanceof specialCellRenderer) {
				specialCellRenderer c=(specialCellRenderer) out;
				c.focus=cellHasFocus;
				c.panelNumber=theindex-1;
				c.isSelected=isSelected;
					{this.setFont(this.getFont().deriveFont(Font.BOLD).deriveFont((float)20.0));}
				if (isSelected) {
					c.panelNumber=theindex-1;
		
					}
			}
		
			return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		}	
		
		
		public void paint(Graphics g) {
			//super.paint(g);
			
			int dim=theindex;
			if (dim==-1) {dim=theindex;}
			//if (this.channelNumber==-1||channelNumber> theChannelentries.size()) this.channelNumber=box.getSelectedIndex();
			//dim = this.channelNumber;
			PanelListElement panel = list.getPanels().get(theindex);
			ArrayList<ChannelEntry> theChannelentries = panel.getChannelEntries();
			
			boolean merge=panel.designation+0==0+PanelListElement.MergeImageDes;
			int size=theChannelentries.size()+1 ;
			
			int[] lengths=new int[size];
			Color[] colors=new Color[size];
			ArrayList<String> names=new ArrayList<String>();
			String all="";
			
			int i=0;
			int start = 0;
			
				start=1;
				;
				String panelType="(c="+panel.originalChanNum+ ", f="+panel.originalFrameNum+", z="+panel.originalSliceNum+")";
				if (merge) 	panelType="(Merge "+", f="+panel.originalFrameNum+", z="+panel.originalSliceNum+")";;
				names.add(panelType);
			
				lengths[0]=panelType.length();
				colors[0]=Color.black;
				all+=panelType;
			
			
			for(; i<theChannelentries.size(); i++ ) {
				ChannelEntry chan=theChannelentries.get(i);
				String real = chan.getRealChannelName();
				names.add(real); lengths[i+start]=real.length();
				colors[i+start]=chan.getColor();
				all+=real;
				
			}
			
			
			//super.paint(g);
			double w = g.getFontMetrics().getStringBounds(all, g).getWidth();
			g.setColor(Color.blue);
			if (focus||isSelected) g.fillRect(1,1, (int)w, this.getFont().getSize()+2);
			ColorDimmingBox.drawRainBowString(g, 1,this.getFont().getSize()+1, names, lengths, colors);
			//if (theChannelentries.size()>0) ChannelEntryBox.drawRainbowString(g, theChannelentries.get(0), 1,this.getFont().getSize()+1);
			//else {ChannelEntryBox.drawRainbowString(g, null, 1,this.getFont().getSize()+1);}
			
		}
	
	}
	

	class listener1 implements ListSelectionListener {

		public listener1(PanelListDisplay panelListDisplay) {
			
		}

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			selectListSelectedDisplays() ;
			
		}}


	@Override
	public void actionPerformed(ActionEvent arg0) {
	
		swapItems() ;
		
	}
	
	void swapItems() {
		int[] ind = this.getSelectedIndices();
		if (ind.length>1) {
			PanelListElement panel1 = elements.get(ind[0]);
			PanelListElement panel2 = elements.get(ind[1]);
			swapItems(panel1, panel2);
		}
		
	}
	
	void swapItems(PanelListElement panel1, PanelListElement panel2) {
		
		int ind1 = elements.indexOf(panel1);
				int ind2 = elements.indexOf(panel2);
			
			panelManager.getStack().swapPanelLocations(panel1, panel2);
			
			
			panelManager.updatePanels();
			if (panel1.getChannelLabelDisplay()!=null)panel1.getChannelLabelDisplay().updateDisplay(); 
			
			
			elements.set(ind1, panel2);
			elements.set(ind2, panel1);
			//this.setListData(elements);
			this.repaint();
		
		
	}
	
	void removeItem(PanelListElement panel12) {
		
			elements.remove(panel12);
		
			
			panelManager.getStack().remove(panel12);
			
			panelManager.removeDisplayObjectsFor(panel12);
			panelManager.updatePanels();
			panelManager.getDisplay().onImageUpdated();
			
			//this.setListData(elements);
			this.repaint();
		
		
	}




	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void drop(DropTargetDropEvent dtde) {
		if (dtde.getDropTargetContext().getComponent() ==this) {
			int index = getIndexForPoint(dtde.getLocation());
			
			if (index>-1) {
				PanelListElement e1 = elements.get(index);
				PanelListElement e2 = elements.get(this.getSelectedIndex());
				swapItems(e1, e2);
			}
			;
		}
		
	}




	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
		
	}
	
	int getIndexForPoint(Point2D pt) {
		for(int i=0; i<elements.size(); i++) {
			if (getCellBounds(i,i).contains(pt)) return i;
		}
		
		return -1;
	}




	@Override
	public void keyPressed(KeyEvent arg0) {
		if (arg0.getKeyCode()==KeyEvent.VK_DELETE) {
		}
		if (arg0.getKeyCode()==KeyEvent.VK_BACK_SPACE) {
			removeSelectedPanels();
			
 		}
		
	}
	
	public void removeSelectedPanels() {
		Object[] index1 = this.getSelectedValues();
		for(Object o: index1) {
			if (o instanceof PanelListElement) {
				this.removeItem((PanelListElement) o);
			}
		}
	}




	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}




	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public
	Dimension  	getPreferredSize() {
	return 	new Dimension(300,100) ;
	}
	
	
	
}
