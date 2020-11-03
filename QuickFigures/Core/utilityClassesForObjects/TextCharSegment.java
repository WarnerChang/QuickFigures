package utilityClassesForObjects;

import java.awt.Color;
import java.awt.Font;
import java.awt.Shape;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class TextCharSegment implements  Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	char text=' ';
	TextLineSegment parent=null;
	static Font defaultFont=new Font("Arial", 1, 20);

	
	/**the location of the baseline start of the segment*/
	public Point2D.Double baseLine;
	public Point2D.Double baseLineend;
	
	/**the bounds of the segment*/
	public Rectangle2D bounds=null;
	
	public Point2D transformedBaseLineStart;
	public Point2D transformedBaseLineEnd;
	public Shape transformedBounds;
	
	public void move(double x, double y) {
		if (baseLine!=null) {
			baseLine.setLocation(baseLine.x+x, baseLine.y+y);
		}
		
		if (bounds!=null) {
			bounds.setRect(
					new Rectangle2D.Double(bounds.getX()+x, bounds.getY()+y, bounds.getWidth(), bounds.getHeight())
					);
		}
		
	}
	
	public TextCharSegment(char r) {
		
	}
	
	public TextCharSegment(String text, Color c) {
		
	}
	
	
	public char getText() {
		return text;
	}

	
	public void setText(char st) {
		text=st;
	}

	
	public Font getFont() {
		if (parent==null) return defaultFont;
		
		return parent.getFont();
	}

	
	public void setFont(Font font) {
		// TODO Auto-generated method stub
		
	}

	
	public Color getTextColor() {
		return parent.getTextColor();
		
	}


	
	public void setParent(TextLineSegment ti) {
		parent=ti;
		
	}

	
	public TextLineSegment getParent() {
		return parent;
	}

	


}