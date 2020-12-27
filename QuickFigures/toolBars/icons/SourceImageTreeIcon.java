/**
 * Author: Greg Mazo
 * Date Modified: Dec 23, 2020
 * Copyright (C) 2020 Gregory Mazo
 * 
 */
/**
 
 * 
 */
package icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

import javax.swing.Icon;

import iconGraphicalObjects.IconTraits;

/**
 An icon for display layers. cosmetic improvement on old icon done
 @see MultichannelDisplayLayer
 */
public class SourceImageTreeIcon  implements Icon, IconTraits{
	int height = TREE_ICON_HEIGHT;
	int width = TREE_ICON_WIDTH;
	protected boolean dash=true;
	
	Color dashColor1=Color.white;
	Color dashColor2=Color.white;
	Color fillColor=Color.black;
	
	
	@Override
	public int getIconHeight() {
	
		return height;
	}

	@Override
	public int getIconWidth() {
		
		return width;
	}
	
	/**draws two letters in a black rectangle*/
	@Override
	public void paintIcon(Component arg0, Graphics arg10, int arg2, int arg3) {
		
		if (arg10 instanceof Graphics2D) {
		Graphics2D g2d = (Graphics2D) arg10;
		int w = width;
		int h = height;
		int size=Math.min(w, h)/2+1;
		Rectangle2D[] r2 = new Rectangle2D[] {
				new Rectangle2D.Double(arg2, arg3,size, size),
				new Rectangle2D.Double(arg2+size/2+1, arg3+size/2+1,size, size),
				new Rectangle2D.Double(arg2+size+1, arg3+size,size, size)};
		for(Rectangle2D r:r2)
			drawRectangle(g2d, r);
		}
		}

	/**
	 * @param g2d
	 * @param r
	 */
	public void drawRectangle(Graphics2D g2d, Rectangle2D r) {
		g2d.setColor(fillColor);
		g2d.fill(r);
		
		int dashForm = 2;
		BasicStroke bs = new BasicStroke(0, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 12, new float[] {dashForm,dashForm}, 0);
		g2d.setColor(dashColor1);
		g2d.setStroke(bs);
		g2d.draw(r);
		
		bs = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 12, new float[] {dashForm,dashForm}, dashForm);
		g2d.setColor(dashColor2);
		g2d.setStroke(bs);
		g2d.draw(r);
	}
	
	
	/**when given 1 or two color arguments, this sets up the colors for the two letters shown
	  if 1 color is given, both letters will be given the same color*/
	public void setColors(Color... c){
		if (c==null||c.length==0) return;
		 dashColor1=c[0];
		if (c.length==1) dashColor2=c[0];
		else  dashColor2=c[1];
	}
	
}