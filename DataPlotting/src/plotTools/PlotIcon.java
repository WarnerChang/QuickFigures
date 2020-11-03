package plotTools;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.io.Serializable;

import javax.swing.Icon;

public class PlotIcon implements Icon, Serializable {
	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Color[] barColors=new Color[] {Color.red, Color.green, Color.blue, Color.gray};
	
	int positionAx1=4;
	int positionAx2=22;
	
		private int[] barH=new int[] {10, 14, 5};
	int bh=10;
	int bx=3;
	int bw=3;
	Line2D xAxis=new Line2D.Double(positionAx1, positionAx2, positionAx2, positionAx2);
	Line2D yAxis=new Line2D.Double(positionAx1, positionAx2, positionAx1, positionAx1);

	Rectangle bar1=createBarRectangle(10, 3, bw);
	Rectangle bar2=createBarRectangle(14, 9, bw);
	Rectangle bar3=createBarRectangle(5, 15, bw);

	
	
	public PlotIcon() {}
	
	public PlotIcon(Color[] c, int[] barH) {
		barColors=c;
		if(barH!=null&&barH.length>2) this.barH=barH;
		
		 bar1=createBarRectangle(this.barH[0], 3, bw);
		 bar2=createBarRectangle(this.barH[1], 9, bw);
		 bar3=createBarRectangle(this.barH[2], 15, bw);
	}

	protected Rectangle createBarRectangle(int bh, int bx, int bw) {
		return new Rectangle(positionAx1+bx, bh, bw, positionAx2-bh);
	}
	
	
	@Override
	public int getIconHeight() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int getIconWidth() {
		// TODO Auto-generated method stub
		return 24;
	}

	@Override
	public void paintIcon(Component arg0, Graphics g, int arg2, int arg3) {
		g.setColor(Color.black);
		
		Graphics2D g2d=(Graphics2D) g;
		g2d.setStroke(new BasicStroke(1));
		
		//g.translate(arg2, arg3);
		g2d.draw(xAxis);
		g2d.draw(yAxis);
		
		g.setColor(barColors[0]);
		g2d.fill(bar1);
		
		g.setColor(barColors[1]);
		g2d.fill(bar2);
		
		g.setColor(barColors[2]);
		g2d.fill(bar3);
		//g.translate(-arg2, -arg3);
		this.paintLayer2Icon(arg0, g2d, arg2, arg3);
	}
	
	public void paintLayer2Icon(Component arg0, Graphics g, int arg2, int arg3) {}

}
