package externalToolBar;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.Icon;


/**An icon that paints multiple other icons on top of each other*/
public class CompoundIcon implements Icon {

	private Icon[] icons;
	public boolean drawMenuIndicator=true;

	public CompoundIcon(Icon... icons) {
		this.icons=icons;
	}
	
	public CompoundIcon(boolean indicatorDrawn, Icon... icons) {
		this.icons=icons;
		drawMenuIndicator=indicatorDrawn;
	}

	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return icons[0].getIconHeight();
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return icons[0].getIconWidth();
	}

	@Override
	public void paintIcon(Component arg0, Graphics arg1, int arg2, int arg3) {
		for(Icon icon: icons) {
			if (icon==null) continue;
			icon.paintIcon(arg0, arg1, arg2, arg3);
		}
		if (drawMenuIndicator) {
			if (arg1 instanceof Graphics2D) {
				((Graphics2D) arg1).setStroke(new BasicStroke(1));
				((Graphics2D) arg1).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				
			}
			
			arg1.setColor(Color.red.darker());
			int liny=arg2+21;
			int linx=arg3+18;
			int linw=2;
			
			arg1.drawLine(linx, liny, linx+linw, liny);
			arg1.drawLine(linx, liny,      linx+linw/2, liny+1);
			arg1.drawLine(linx+linw, liny, linx+linw/2, liny+1);
		}

	}

}