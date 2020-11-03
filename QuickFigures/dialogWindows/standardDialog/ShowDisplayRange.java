package standardDialog;

import java.awt.Color;
import java.awt.Graphics;


/**A JComponent to show a distribution of pixel values and the min/max of a display range*/
public class ShowDisplayRange extends GraphicComponent  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	protected int[] range;
	private Color lineFolor=Color.DARK_GRAY;
	
	{width=140;
	 height=100;}


	private double maxShow;
	private double minShow;


	private double topShow;
	
	double displayRangeMin=0;
	double displayRangeMax=0;
	
	public void setMinMax(double min, double max) {
		displayRangeMin=min;
		displayRangeMax=max;
	}
	
	public void setRange(int[] range) {
		this.range=range;
		maxShow=findMaxOfDistributionHistogram(range);
		minShow=findMinOfDistributionHistogram(range);
		topShow=findLargestOfDistributionHistogram(range);
	}
	
	public static double findMinOfDistributionHistogram(int[] basis) {
		double slidermin=0;
		for(int i=0; i<basis.length; i++) {
			if (basis[i]==0) {slidermin=i;} else break;
		}
		return slidermin;
	}
	
	public static double findMaxOfDistributionHistogram(int[] basis) {
		double slidermax=basis.length-1;
		for(int i=basis.length-1; i>1; i--) {
			if (basis[i]==0) {slidermax=i;} else break;
		}
		return slidermax;
	}
	
	public static double findLargestOfDistributionHistogram(int[] basis) {
		double slidermax=0;
		for(int i=basis.length-1; i>1; i--) {
			if (basis[i]>slidermax) {slidermax=basis[i];} 
		}
		return slidermax;
	}
	
	@Override
	public void paintComponent(Graphics g) {
	//	getSnappingBehaviour().snapLocatedObjects(r2, r1);
		
		g.setColor(background);
		g.fillRect(0, 0,width,  height);
		
		
		g.setColor(getLineColor());
		for(int i=0; i<range.length; i++) {
			paintLine(g, i, range[i]);
		}
		
		g.setColor(Color.black);
		g.drawLine((int)convertValueToXY(displayRangeMin), height, (int)convertValueToXY(displayRangeMax), 0);
	}

	private void paintLine(Graphics g, int i, int j) {
		double linHeight= (j/topShow)*height;
		double linX=convertValueToXY(i);
		if (i>maxShow) return;
		if (i<minShow) return;
		g.drawLine((int)linX, (int)(height-linHeight), (int)linX, height);
	}
	
	/**Converts a value position to this components xy*/
	double convertValueToXY(double i) {
		double linX= width*(i-minShow)/(maxShow-minShow);
		return linX;
	}

	public Color getLineColor() {
		return lineFolor;
	}

	public void setLineColor(Color lineFolor) {
		this.lineFolor = lineFolor;
	}

	public int getHeight() {return height;}
	public int getWidth() {return width;}
	
}