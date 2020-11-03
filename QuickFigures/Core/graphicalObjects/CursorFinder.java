package graphicalObjects;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graphicalObjects_BasicShapes.ComplexTextGraphic;
import graphicalObjects_BasicShapes.TextGraphic;
import utilityClassesForObjects.PathPointList;
import utilityClassesForObjects.TextLineSegment;
import utilityClassesForObjects.TextPrecision;

public class CursorFinder {

	public CursorFinder() {
		// TODO Auto-generated constructor stub
	}
	
	public void setCursorFor(TextGraphic textob, Point2D location) {
		if(location==null||textob==null) return;
		if (!(textob instanceof ComplexTextGraphic) ) {
			location=PathPointList.projectPointOntoLine(location, textob.getBaseLineStart(), textob.getBaseLineEnd());
		double distclick = textob.getBaseLineStart().distance(location);
		double distclickEnd = StringWidth(textob.getText(), textob.getFont(), CursorFinder.graphics1());
		double cursor = textob.getText().length()*distclick/distclickEnd;
		
		textob.setCursorPosition((int)Math.round(cursor));

} else {
	ComplexTextGraphic comp=(ComplexTextGraphic) textob;
	TextLineSegment segment = comp.getSegmentAtPoint(location);
	ArrayList<TextLineSegment> a = comp.getParagraph().getAllSegments();
	boolean outofBounds = false;
	/**If you drag outside of the text item the user probably meant the begging or the end*/
	if(segment==null &&a.size()>0) {
		outofBounds = true;
		if (textob.getBaseLineStart().distance(location)<textob.getFont().getSize())
			{segment=a.get(0); outofBounds=false;}//if near the begining, will chose the first segment
		else segment=a.get(a.size()-1);//if not near the beggining, choses the last segment
	}
	
	if (segment!=null) {
		Point2D location2 = PathPointList.projectPointOntoLine(location, segment.transformedBaseLineStart, segment.transformedBaseLineEnd);

		location=location2;
	comp.setCursorSegment(segment);
	double distclick = segment.transformedBaseLineStart.distance(location);
	//double distclickToEnd = segment.transformedBaseLineEnd.distance(new Point(x, y));
	double distclickBeginToEnd = CursorFinder.StringWidth(segment.getText(), segment.getFont(), graphics1()); //segment.transformedBaseLineStart.distance(segment.transformedBaseLineEnd);
//	double cursor = segment.getText().length()*distclick/distclickBeginToEnd;
	int forward=closest(segment.getText(),segment.getFont() , distclick/distclickBeginToEnd);
	if(outofBounds) forward++;//fixes a bug whereby it doesnt read the last spot if you drag beyond
		//if (distclickToEnd>distclickBeginToEnd) textob.setCursorPosition(0); else
		textob.setCursorPosition(textob.getCursorPosition()+forward);
		
	} /**Obsolete: If you drag outside of the text item the user probably meant the begging or the endelse {
		
		ComplexTextGraphic comp2=(ComplexTextGraphic) textob;
		
		textob.setCursorPosition(comp2.getMaxNeededCursor());
		
		if (textob.getBaseLineStart().distance(location)<textob.getFont().getSize())
			textob.setCursorPosition(0);
	}*/
}
		
	}
	
	
	private static Graphics2D graphics1() {
		BufferedImage img=new BufferedImage((int)(1000), (int)(1000),  BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.getGraphics();
		return (Graphics2D) g;
	}
	
	/**determines what index of the segment is closest to the location based on the ratio of the distance to clickpoint to the length of text*/
	private static int closest(String st, Font f, double ratio) {
		
		 double[] widths = relativeWidths(st, f, (Graphics2D)  graphics1());
		 for(int i=0; i<st.length(); i++) {
				if (i>0) {
					double before = widths[i-1];
					double after = widths[i];
					
					if (ratio>before &&ratio<before-ratio) return 0;
					
					double diff = after-before;//new element for intermediate positions
					if (ratio>before&&ratio<after) {
							return i-1;
					}
					/**this part need for user to be able to select the last letter*/
					if (i==st.length()-1&&ratio>after+0.5*diff) {
						return i+1;//reached the end of the seqment
						}
					
					if (i==st.length()-1&&ratio>after) {
						return i;//reached the end of the seqment
						}
					
					
					
					
				}
			}
		 return st.length()-1;
	}
	
	/**Given a string, check every substring that starts at 0 and
	 * computes the relative length of that string*/
	private static double[] relativeWidths(String st, Font f, Graphics2D g) {
		double total = StringWidth(st, f, g);
		double[] output = new double[st.length()];
		for(int i=0; i<st.length(); i++) {
			output[i]=StringWidth(st.substring(0, i),f,g)/total;
		}
		return output;
	}
	
	private static double StringWidth(String st, Font f, Graphics2D g) {
		FontMetrics metricsi=TextPrecision.createPrecisForFont(f).getInflatedMetrics(f, g);
		double newwidth = metricsi.stringWidth(st)/TextPrecision.createPrecisForFont(f).getInflationFactor();
		return newwidth;
	}
}