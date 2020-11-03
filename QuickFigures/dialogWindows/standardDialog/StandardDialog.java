package standardDialog;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.undo.UndoManager;

import channelMerging.ChannelEntry;
import channelMerging.MultiChannelWrapper;
import graphicActionToombar.CurrentSetInformerBasic;
import logging.IssueLog;
import menuUtil.SmartPopupJMenu;
import undo.AbstractUndoableEdit2;
import undo.UndoManagerPlus;
import utilityClassesForObjects.ScaleInfo;

/**inspired by imageJ's generic dialog. */
public class StandardDialog extends JDialog implements KeyListener, ActionListener, StringInputListener, NumberInputListener, BooleanInputListener,ChoiceInputListener, FontInputListener, ObjectEditListener ,ColorInputListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public StandardDialog() {}
	public StandardDialog(String st) {
		this.setTitle(st);
	}
	
	/**Creates a dialog of title st. If the boolean is true. dialog will be modeal and centered */
	public StandardDialog(String title, boolean modal) {
		this.setTitle(title);
		this.setModal(modal);
		this.setWindowCentered(modal);
	}

	{this.setLayout(new GridBagLayout());}
	protected ArrayList<SwingDialogListener> listen=new ArrayList<SwingDialogListener>();
	
	protected int gy=0;
	protected int gx=0;
	protected int gxmax=0;
	protected int gymax=0;
	
	protected JButton OKBut=createOkButton() ;
	protected JButton CancelBut=createCancelButton() ;
	private boolean centerWindow=false;
	
	protected ArrayList<JButton> additionButtons=new ArrayList<JButton>();
	private boolean hideCancel=false;
	
	boolean useMainPanel=true;
	
	public JTabbedPane removeOptionsTab() {
		JTabbedPane output = this.getOptionDisplayTabs();
		this.remove(output);
		return output;
	}
	
	private JTabbedPane optionDisplayTabs=new JTabbedPane(); {
		GridBagConstraints mainPanelgc = new GridBagConstraints();
		mainPanelgc.gridx=1;
		mainPanelgc.gridy=1;
		this.add(getOptionDisplayTabs(), mainPanelgc);
		
	}
	protected GriddedPanel mainPanel=SetupmainPanel();
	
	
	public GriddedPanel SetupmainPanel() {
		GriddedPanel mainPanel=new GriddedPanel();
		mainPanel.setLayout(new GridBagLayout());
	
		getOptionDisplayTabs().addTab("", mainPanel);
		
		return mainPanel;
	}
	
	public void addButton(JButton b) {
		additionButtons.add(b);
	}
	
	public void moveGrid(int x, int y) {
		gy+=y;
		gx+=x;
		if (gx>gxmax) gxmax=gx;
		if (gy>gymax) gymax=gy;
	}
	
	protected GridBagConstraints getCurrentConstraints() {
		GridBagConstraints output=new GridBagConstraints();
		output.gridx=gx;
		output.gridy=gy;
		return output;
	}
	
	JButton createOkButton() {
		JButton OKBut=new JButton("OK");
		OKBut.addActionListener(this);
		OKBut.setActionCommand("OK");
		
		return OKBut;
	}
	JButton createCancelButton() {
		JButton OKBut=new JButton("Cancel");
		OKBut.addActionListener(this);
		OKBut.setActionCommand("Cancel");
		
		return OKBut;
	}
	
	protected HashMap<String, StringInputPanel> Strings=new HashMap<String, StringInputPanel>();
	protected HashMap<String, NumberInputPanel> Numbers=new HashMap<String, NumberInputPanel>();
	protected HashMap<String, NumberArrayInputPanel> NumberSets=new HashMap<String, NumberArrayInputPanel>();
	protected HashMap<String, ComboBoxPanel> choices=new HashMap<String, ComboBoxPanel>();
	protected HashMap<String, FontChooser> fonts=new HashMap<String, FontChooser>();
	protected HashMap<String, BooleanInputPanel> bools=new HashMap<String, BooleanInputPanel>();
	protected HashMap<String, ItemSelectblePanel> items=new HashMap<String,ItemSelectblePanel>();
	protected HashMap<String, ColorListChoice> colors=new HashMap<String, ColorListChoice>();
	protected HashMap<String, BooleanArrayInputPanel> boolSets=new HashMap<String, BooleanArrayInputPanel>();

	private ArrayList<ChannelEntry> channelEnt;

	private boolean wasOKed;

	private JPanel theButtonPanel;

	private JButton[] bonusButtons;

	public AbstractUndoableEdit2 undo;//this undo is added to an undo 

	public UndoManager currentUndoManager=new CurrentSetInformerBasic().getUndoManager();
	
	
	public void add(String key, StringInputPanel st) {
		Strings.put(key, st);
		st.addStringInputListener(this);
		st.setKey(key);
		place(st);
	}
	
	public String getString(String key) {
		StringInputPanel ob = Strings.get(key);
		if(ob==null) return "";
		return ob.getTextFromField();
	}
	
	protected boolean setStringField(String key, String content) {
		StringInputPanel ob = Strings.get(key);
		if(ob==null) return false;
		ob.setContentText(content);;
		return true;
	}
	
	public void add(String key, NumberInputPanel st) {
		if (st instanceof NumberArrayInputPanel) {NumberSets.put(key, (NumberArrayInputPanel) st);} else
		Numbers.put(key, st);
		st.setKey(key);
		st.addNumberInputListener(this);
		place(st);
	}
	
	public void add(  String key, NumberInputPanel... st) {
		int k=0;
		for(int i=0; i<st.length; i++) {
			this.add(key+i, st[i]);
			this.moveGrid(2, -1);
			k++;
		}
		this.moveGrid(-2*k, 0);
	}
	
	public double getNumber(String key) {
		NumberInputPanel ob = Numbers.get(key);
		if(ob==null) return 0;
		return ob.getNumber();
	}
	
	public int getNumberInt(String key) {
		NumberInputPanel ob = Numbers.get(key);
		if(ob==null) return 0;
		return (int)ob.getNumber();
	}
	
	public void setNumber(String key, double number) {
		NumberInputPanel ob = Numbers.get(key);
		if(ob==null) return;
		ob.setNumber(number);
	}
	
	
	
	/**will return the nubmer array with specified key. Or an empty array if not found*/
	public float[] getNumberArray(String key) {
		NumberArrayInputPanel ob = NumberSets.get(key);
		if(ob==null) return new float[] {};
		return ob.getArray();
	}
	
	public Point2D getPoint(String key) {
		float[] ar = getNumberArray(key);
		if (ar.length<2) return null;
		return new Point2D.Double(ar[0], ar[1]);
	}
	
	public void add(String key, ComboBoxPanel st) {
		
		if (st instanceof ColorComboboxPanel) {
			colors.put(key, (ColorComboboxPanel) st);
		} else
		choices.put(key, st);
		st.setKey(key);
		st.addChoiceInputListener(this);
		
		place(st);
	}
	public int getChoiceIndex(String key) {
		ComboBoxPanel ob = choices.get(key);
		if(ob==null) {
			ItemSelectblePanel ob2=items.get(key);
			if(ob2==null) return 0;
			return ob2.getSelectedItemNumber();
		}
		return ob.getSelectedIndex();
	}
	
	public void add(String key, FontChooser st) {
		fonts.put(key, st);
		st.addFontInputListener(this);
		st.setKey(key);
		place(st);
	}
	
	public void add(String key, ColorInputPanel st) {
		colors.put(key, st);
		//st.addObjectEditListener(this);
		st.addColorInputListener(this);
		st.setKey(key);
		place(st);
	}
	
	
	public void add(String key, BooleanInputPanel st) {
		if (st instanceof BooleanArrayInputPanel) {
			boolSets.put(key, (BooleanArrayInputPanel) st);
		} else
		bools.put(key, st);
		st.addBooleanInputListener(this);
		st.setKey(key);
		place(st);
	}
		
	public boolean getBoolean(String key) {
		BooleanInputPanel ob = bools.get(key);
		if(ob==null) return false;
		return ob.isChecked();
	}
	
	public boolean[] getBooleanArray(String key) {
		BooleanArrayInputPanel ob = boolSets.get(key);
		if(ob==null) return new boolean[] {false};
		return ob.getArray();
	}
	
	public void add(String key, ItemSelectblePanel st) {
		items.put(key, st);
		st.addChoiceInputListener(this);
		st.setKey(key);
		place(st);
	}
	
	public Object[] getItemsSelected(String key) {
		ItemSelectblePanel ob = items.get(key);
		if(ob==null) return null;
		return ob.getBox().getSelectedObjects();
	}


	
	
	public void place(OnGridLayout st) {
		if (useMainPanel)
			{
			getMainPanel().place(st);
			} else
		st.placeItems(this, gx, gy);
		if (gxmax<st.gridWidth())gxmax=st.gridWidth();
		gy+=st.gridHeight();
	}
	
	public Font getFont(String key) {
		FontChooser ob = fonts.get(key);
		if(ob==null) return null;
		return ob.getSelectedFont();
	}
	
	public Color getColor(String key) {
		ColorListChoice ob = colors.get(key);
		if(ob==null) return null;
		return ob.getSelectedColor();
	}
	

	

	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	  Insets getInsets(int top, int left, int bottom, int right) {
	       
	            return new Insets(top, left, bottom, right);
	      
	    }
	  
	  
	  protected JPanel addButtonPanel(Container cont) {
		   GridBagConstraints c = getCurrentConstraints();
		  
		   JPanel ButtonPanel = generateButtonPanel();
		   c.gridx=1;
		   c.gridy=this.gymax+2;
		
		 // c.gridwidth=gxmax+2;
		
		  c.anchor=GridBagConstraints.EAST;
		cont.add(ButtonPanel, c);
		return ButtonPanel;
	  }
	  
	  public JPanel generateButtonPanel() {
		  JPanel ButtonPanel=new JPanel();
		  ButtonPanel.setLayout(new FlowLayout());
		  for(JButton b:this.additionButtons) {ButtonPanel.add(b);}
		if (!hideCancel)  ButtonPanel.add(CancelBut);
		  ButtonPanel.add(OKBut);
		 if (this.bonusButtons!=null) for(JButton b:this.bonusButtons) {ButtonPanel.add(b);}
		  return ButtonPanel;
	  }
	  
	  public void showDialog() {
		this.theButtonPanel=addButtonPanel(this);
		  makeVisible();
		  
	  }
	  
	  
	  public void makeVisible() {
		  pack();
		  if (this.centerWindow()) center(this);
		  setVisible(true);
	  }
	
	public static void main(String[] args) {
		StandardDialog sd = new StandardDialog();
		sd.add("text1", new StringInputPanel("give me text ", "default input"));
		sd.add("num", new NumberInputPanel("Select number", 5));
		sd.add("combo", new ComboBoxPanel("Select", new String[] {"a","b", "v", "d"}, 3));
		FontChooser sb = new FontChooser(new Font("Arial", Font.BOLD, 12));
		sd.add("font", sb);
		NumberArrayInputPanel pai = new NumberArrayInputPanel(4, 0);
		sd.add("array panel", pai);
		sd.add("combo", new BooleanInputPanel("check ", true));
		AngleInputPanel pai2 = new AngleInputPanel("Angle ", 0, true);
		sd.add("angle", pai2);
		FixedEdgeSelectable f = new FixedEdgeSelectable(3);
		ItemSelectblePanel is = new ItemSelectblePanel("Selecgt Edge", f);
		sd.add("edge fix", is);
		
		sd.setModal(true);
		sd. showDialog();
		
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource()==OKBut) {
			this.onOK();
			this.wasOKed=true;
			this.setVisible(false);
			resolveUndo();
		}
		if (arg0.getSource()==CancelBut) {
			this.revertAll();
			this.onCancel();
			if(undo!=null) undo.undo();
			
			this.setVisible(false);
		}
		
	}
	
	protected void resolveUndo() {
		try {
			if(undo!=null)
				{undo.establishFinalState();
			if (currentUndoManager!=null)
				currentUndoManager.addEdit(undo);
				}
		} catch (Exception e) {
			IssueLog.log(e);
		}
	}
	
	/**what action to take when the ok button is pressed*/
	protected void onOK() {
		
	}
	protected void onCancel() {
		 
	}
	protected void afterEachItemChange() {
		
	}	

	
	/**called when a font selection has changed*/
	public void onFontChange(String key, Font f) {}
	
	
	
	public void notifyAllListeners(JPanel key, String string) {
		afterEachItemChange();
		DialogItemChangeEvent di = new DialogItemChangeEvent(this, key) ;
		di.setStringKey(string);
		onListenerLotification(di);
	}
	public void onListenerLotification(DialogItemChangeEvent di) {
		for(SwingDialogListener l:listen ) {if (l!=null)l.itemChange(di);}
	}
	
	public void addDialogListener(SwingDialogListener l) {
		listen.add(l);
	}
	public void removeDialogListener(SwingDialogListener l) {
		listen.remove(l);
	}

	@Override
	public void numberChanged(ChoiceInputEvent ne) {
		
		notifyAllListeners(ne.getSourcePanel(), ne.getKey());
		
	}

	@Override
	public void booleanInput(BooleanInputEvent bie) {
		notifyAllListeners(bie.getSourcePanel(), bie.getKey());
		
	}

	@Override
	public void numberChanged(NumberInputEvent ne) {
		notifyAllListeners(ne.getSourcePanel(), ne.getKey());
		
	}

	@Override
	public void StringInput(StringInputEvent sie) {
		notifyAllListeners(sie.getSourcePanel(), sie.getKey());
		
	}

	@Override
	public void FontChanged(FontInputEvent fie) {
		notifyAllListeners(fie.getSourcePanel(), fie.getKey());
		
	}

	@Override
	public void objectEdited(ObjectEditEvent oee) {
		notifyAllListeners(null, oee.getKey());
		
	}
	@Override
	public void ColorChanged(ColorInputEvent fie) {
		notifyAllListeners(fie.getSourcePanel(), fie.getKey());
		
	}
	
	 /** Positions the specified window in the center of the screen. 
	    I copies this from ij.GUI in imageJ*/
    public static void center(Window w) {
        Dimension screensize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension windowsize = w.getSize();
        if (windowsize.width==0)
            return;
        int newleft = screensize.width/2-windowsize.width/2;
        int newtop = (screensize.height-windowsize.height)/2;
        if (newtop<0) newtop = 0;
        w.setLocation(newleft, newtop);
    }
	public boolean centerWindow() {
		return centerWindow;
	}
	public void setWindowCentered(boolean centerWindow) {
		this.centerWindow = centerWindow;
	}

	
	public void addScaleInfoToDialog(ScaleInfo si) {
		GriddedPanel omp = this.getMainPanel();
		this.setMainPanel(new GriddedPanel());
		
		this.add("units",new StringInputPanel("Units ", si.getUnits(),  5));
		this.add("pw",new NumberInputPanel("Pixel Width ", si.getPixelWidth(), 4));
	
		this.add("ph",new NumberInputPanel("Pixel Height ", si.getPixelHeight(), 4));
	
		
		this.getOptionDisplayTabs().addTab("Calibration", this.getMainPanel());
		this.setMainPanel(omp);
	}

	public void setScaleInfoToDialog(ScaleInfo si) {
		si.setUnits(this.getString("units"));
		si.setPixelWidth(this.getNumber("pw"));
		
		si.setPixelHeight(this.getNumber("ph"));
		
	}
	public JTabbedPane getOptionDisplayTabs() {
		return optionDisplayTabs;
	}
	
	public void setTabName(String newName) {
		getOptionDisplayTabs().setTitleAt(0, newName);
	}

	public GriddedPanel getMainPanel() {
		return mainPanel;
	}
	
	protected void setMainPanel( GriddedPanel g) {
		mainPanel=g;
	}
	
	public void addSubordinateDialog(String shortLabel,
			StandardDialog dis) {
		if (dis==null) return;
		
		if (dis.getOptionDisplayTabs().getTabCount()>1) {
			getOptionDisplayTabs().addTab(shortLabel, dis.removeOptionsTab());
		} else {
			Component p=dis.getOptionDisplayTabs().getTabComponentAt(0);//.getMainPanel();
			if (p==null) p=dis.getMainPanel();
			if (p==null) return;
			dis.remove(p);
			getOptionDisplayTabs().addTab(shortLabel, p);}
	}

	
	public JButton alternateCloseButton() {
		JButton cc = new JButton("Close");
		cc.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				resolveUndo();
			}

			});
		
		return cc;
	}
	
	public void addChannelCheckBoxes(MultiChannelWrapper mw) {
		int chan=mw.nChannels();
		channelEnt=new ArrayList<ChannelEntry>();
		
		for(int i=1; i<=chan; i++) {
			ChannelEntry entry = mw.getSliceChannelEntry(i, 1, 1);
			channelEnt.add(entry);
			this.add(entry.getRealChannelName(), new BooleanInputPanel(entry.getRealChannelName(), true, new ColorCheckbox(entry.getColor())));
		}
		
	}
	
	public ArrayList<ChannelEntry> getChannelsChosen() {
		ArrayList<ChannelEntry> channelEnt2 = new ArrayList<ChannelEntry>();
		if (channelEnt==null) return channelEnt2;
		for(ChannelEntry entry: channelEnt) {
			if(entry==null) continue;
			boolean result = this.getBoolean(entry.getRealChannelName());
			if (!result) channelEnt2.add(entry);
		}
		
		return channelEnt2;
	}
	public boolean wasOKed() {
		// TODO Auto-generated method stub
		return wasOKed;
	}
	
	/**restores all fields and input panels to original values*/
	public void revertAll() {
		for(StringInputPanel i:Strings.values()) {
			i.revert();
		}
		for(NumberInputPanel i:Numbers.values()) {
			i.revert();
		}
		for(NumberInputPanel i:NumberSets.values()) {
			i.revert();
		}
		for(ComboBoxPanel i:choices.values()) {
			i.revert();
		}
		for(BooleanInputPanel i:bools.values()) {
			i.revert();
		}
		for(ItemSelectblePanel i:items.values()) {
			i.revert();
		}
		for(ColorListChoice i:colors.values()) {
			if (i instanceof ColorComboboxPanel )((ColorComboboxPanel) i).revert();
		}
		for(BooleanInputPanel i:boolSets.values()) {
			i.revert();
		}
		for(FontChooser i:fonts.values()) {
			i.revert();
		}
		
		this.repaint();
		
	}
	public JPanel getTheButtonPanel() {
		return theButtonPanel;
	}
	
	public void setBonusButtons(JButton... buttons ) {
		this.bonusButtons=buttons;
	}
	
	public boolean hasContent() {
		if (Strings.keySet().size()>0) return true;
		if (Numbers.keySet().size()>0) return true;
		if (NumberSets.keySet().size()>0) return true;
		if (choices.keySet().size()>0) return true;
		if (fonts.keySet().size()>0) return true;
		if (bools.keySet().size()>0) return true;
		if (boolSets.keySet().size()>0) return true;
		if (items.keySet().size()>0) return true;
		if (colors.keySet().size()>0) return true;
		
		return false;
	}
	
	public static Double getNumberFromUser(String st, double starting) {
		return getNumberFromUser(st, starting, false);
	}
	public static Double getNumberFromUser(String st, double starting, boolean angle) {
		StandardDialog sd = new StandardDialog(st);
		sd.setModal(true);
		sd.setWindowCentered(true);
		if (angle) sd.add(st, new AngleInputPanel(st, starting, true));
		else
		sd.add(st, new NumberInputPanel(st, starting, 4));
		
		sd.showDialog();
		
		if(sd.wasOKed) {
			return sd.getNumber(st);
		}
		return starting;
	}
	
	public static Point2D getPointFromUser(String st, Point2D starting) {
		StandardDialog sd = new StandardDialog(st);
		sd.setModal(true);
		sd.setWindowCentered(true);
		sd.add(st, new PointInputPanel(st, starting));
		
		
		sd.showDialog();
		
		if(sd.wasOKed) {
			return sd.getPoint(st);
		}
		return starting;
	}
	public boolean isHideCancel() {
		return hideCancel;
	}
	public void setHideCancel(boolean hideCancel) {
		this.hideCancel = hideCancel;
	}
	
	public SmartPopupJMenu createInPopup() {
		SmartPopupJMenu output = new SmartPopupJMenu();
		output.add(this.getMainPanel());
		return output;
	}
	
	public static Component combinePanels(OnGridLayout... i1) {
		JPanel j = new JPanel(); 
		j.setLayout(new GridBagLayout());
		for(int i=0; i<i1.length; i++)
			i1[i].placeItems(j, 0, 1+i);
		return j;
	}
	
}