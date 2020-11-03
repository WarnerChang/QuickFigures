package channelLabels;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import channelMerging.ChannelEntry;
import graphicalObjects_BasicShapes.TextGraphic;
import logging.IssueLog;
import utilityClassesForObjects.TextLine;
import utilityClassesForObjects.TextLineSegment;
import utilityClassesForObjects.TextParagraph;

public class ChannelLabelProperties implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**Links channel names to text lines*/
	private HashMap<String, TextLine> list=null;
	
	private HashMap<String, TextLine> getTextList() {
		if (list==null) {
			list=new HashMap<String, TextLine>();
		}
		
		return list;
	}
	
	public static String[] mergeTexts=new String[] {"Merge", "merge", "Merged", "merged", "use custom", "Overlay", "overlay"};
	public static String[][] split2MergeTexts=new String[][] {new String[]{"Mer", "ge"},new String[]{"mer", "ge"} ,new String[]{"Mer", "ged"},new String[]{"mer", "ged"}, new String[]{"Mer", "ge"}, new String[]{"Over", "lay"}, new String[]{"over", "lay"}};
	public static String[][] split3MergeTexts=new String[][] {new String[]{"M","er", "ge"},new String[]{"m","er", "ge"} ,new String[]{"Me","rg", "ed"},new String[]{"me","rg", "ed"}, new String[]{"Me","rg", "e"}, new String[]{"Ov","er", "lay"}, new String[]{"ov", "er", "lay"}};
	public static String[][] split4MergeTexts=new String[][] {new String[]{"M","e","r", "ge"},new String[]{"m","e","r", "ge"} ,new String[]{"M","er","ge", "d"},new String[]{"m","er","ge", "d"},new String[]{"m","er","ge", "d"}, new String[]{"Ov","er", "la","y"}, new String[]{"ov", "er", "la","y"}};
	
	public static final int MergeText=0, noMergeLabel=1, Multiline_Labels=3, Singleline_Labels=4, SoniStyle=5, Merge_Style=6;
	public static final int space=1, nothing=0, slash=2, custom=3;
	
	public static String[] separatorOptions=new String[] { "nothing", "a space", "/", "use custom"};
	public static String[] separatorTexts=new String[] {"", " ", "/", "?"};
	public static String[] mergeLabelOptions=new String[] {"'Merge' text",  "No Merge Label ", "Only Label the Merge ", "Multiline Channel Labels", "Single Line Channel Labels", "Soni Style", "Color Merge Style"};
	
	public String getSeparatorText() {
		
		if (this.getSaparatorOption()==custom) return this.getCustomSeparator();
		String o = separatorTexts[saparatorOption];
		return o;
	}
	
	private String customMergeText="Merge";
	private String customSeparator="";
	private int saparatorOption=0;
	

	private int mergeTextOption=0;
	private int mergeLabelTypeOption=0;

	public static Color emptyColor=new Color(255,255,255, 255);
	
	public ChannelLabelProperties copy() {
		ChannelLabelProperties output = new ChannelLabelProperties();
		output.setCustomMergeText(getCustomMergeText());
		output.setCustomSeparator(getCustomSeparator());
		output.setMergeLabelType(getMergeLabelType());
		output.setMergeTextOption(getMergeTextOption());
		return output;
	}
	
	public String getMergeText(){
		String ot = mergeTexts[getMergeTextOption()];
		if (this.getMergeTextOption()==4) return this.getCustomMergeText();
		return ot;
		
	}
	
	public int getMergeLabelType() {return mergeLabelTypeOption;}
	public int getMergeTextOption() {return mergeTextOption;}

	public void setMergeLabelType(int mergeLabelTypeOption) {
		this.mergeLabelTypeOption = mergeLabelTypeOption;
	}

	public void setMergeTextOption(int mergeTextOption) {
		this.mergeTextOption = mergeTextOption;
	}

	public int getSaparatorOption() {
		return saparatorOption;
	}

	public void setSaparatorOption(int saparatorOption) {
		this.saparatorOption = saparatorOption;
	}

	public String getCustomMergeText() {
		return customMergeText;
	}

	public void setCustomMergeText(String customMergeText) {
		
		
		
		this.customMergeText = customMergeText;
	}

	/**Sets the merge text. if text matches one of the options, changes to that option, custon otherwise*/
	public void setMergeText(String customMergeText) {
		for(int i=0; i<mergeTexts.length; i++) {
			String t=mergeTexts[i];
			if(t.equals(customMergeText)) 
				{mergeTextOption=i; 
					return;}
			
		}
		mergeTextOption=4;
		this.customMergeText=customMergeText;
	}

	public String getCustomSeparator() {
		return customSeparator;
	}

	public void setCustomSeparator(String customSeparator) {
		this.customSeparator = customSeparator;
	}

	public static Color fuseColors(ArrayList<ChannelEntry> chanEntries) {
		// TODO Auto-generated method stub
		ArrayList<Color> arr = new ArrayList<Color> ();
		for(ChannelEntry en:chanEntries) {
			arr.add(en.getColor());
		}
		return fuseColors(arr);
	}
	
	public static Color fuseColors(Iterable<Color> cs) {
		int r=0;
		int g=0;
		int b=0;
		int a=0;
		for(Color c: cs) {
			if (c==null) continue;
			r+=c.getRed();
			g+=c.getGreen();
			b+=c.getBlue();
			a+=c.getAlpha();
		}
		if (r>255) r=255;
		if (g>255) g=255;
		if (b>255) b=255;
		if (a>255) a=255;
		return new Color(r,g,b);
		
		
		
		
	}

	public void copyOptionsFrom(ChannelLabelProperties copy) {
		this.customMergeText=copy.customMergeText;
		this.customSeparator=copy.customSeparator;
		saparatorOption=copy.saparatorOption;
		mergeTextOption=copy.mergeTextOption;
		mergeLabelTypeOption=copy.mergeLabelTypeOption;
	}

	/**Creates a line of text to match the channel of name label and adds it to the hashmap*/
	public void createTextLineForChannel(ChannelEntry c) {
		String label=c.getLabel();
		if (this.getTextLineForChannel(c.getLabel())!=null) return;
		
		TextLine lin = new TextLine( generateDisplayParaGraph());
		
		boolean standardLoci=isStandardLociLabel(label);//if the channel is in standard loci format
		
		/**if the label is in the formal 'c:#/#, this truncates the label*/
		if (standardLoci ) { 
			
					String label2 = label.substring(0, 5)+";"+label.substring(5, label.length());
					if (c.getRealChannelName()!=null) label2=c.getRealChannelName();
					
					lin.addFromCodeString(label2, c.getColor());
			} else
		lin.addFromCodeString(label, c.getColor());
		
		for(TextLineSegment seg:lin) {
			seg.setTextColor(new Color(0,0,0,0));//makes sure the color is transparent so it will not be copied 
		}
		if (lin.getText().length()>20) {
			IssueLog.log("Line for channel  name is is too long: "+lin.getText());
			String newT = lin.getText();
			newT=newT.substring(newT.length()-20);
			lin.getFirstSegment().setText(newT);
		}
		getTextList().put(label, lin);
		
	}
	
	boolean isStandardLociLabel(String label) {
		if (!label.startsWith("c:")) return false;
		if (label.charAt(3)!='/') return false;
		return true;
	}
	
	/**creates a paragraph pane to be used by UI classes to edit the channel panels*/
	TextParagraph generateDisplayParaGraph() {
		TextParagraph tp = new TextParagraph(new TextGraphic(""));
		tp.getParent().setTextColor(new Color(0,0,0,0));
		return tp;
		
	}

	
/**Returns a Text line for the given channel entry. always returns a Text line
   */
	public TextLine getTextLineForChannel(ChannelEntry label) {
		TextLine output = getTextList().get(label.getLabel());
		if (output==null) {
			createTextLineForChannel(label);
		}
		return getTextList().get(label.getLabel());
	}
	
	
	private TextLine getTextLineForChannel(String label) {
		return getTextList().get(label);
	}
	
	
	
	

}