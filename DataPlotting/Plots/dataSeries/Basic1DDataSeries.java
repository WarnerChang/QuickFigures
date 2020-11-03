package dataSeries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**The simplest and most base class of data series*/
public class Basic1DDataSeries implements DataSeries, ErrorBarStyle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private double[] data;
	private double positionOnPlot;
	private double pOffset;
	
	
	public  Basic1DDataSeries(String name, double[] data) {
		this.setName(name);
		this.data=data;
	}
	
	public Basic1DDataSeries(String name2, ArrayList<Double> n) {
		this.setName(name2);
		data=new double[n.size()];
		for(int i=0; i<n.size(); i++) data[i]=n.get(i);
	}

	
	public double[] getRawValues() {
		return data.clone();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getColumnString() {
		String st=getName();
		for(double d: data) {st+=System.getProperty("line.separator")+d+"";}
		return st;
	}
	
	public double getMean() {
		double total=0;
		for(Double d: getRawValues()) {
			total+=d;
		}
		return total/getRawValues().length;
	}
	
	public double getSDDev() {
		double mean=getMean();
		double total=0;
		for(Double d: getRawValues()) {
			total+=Math.pow(d-mean, 2);
		}
		return Math.sqrt(total/(getRawValues().length-1));
	}
	
	public double getSEM() {
		return  getSDDev()/Math.sqrt(getRawValues().length);
	}
	
	public static double getMedian(double[] sorted ) {
		if (sorted.length==0) return 0;
		if (sorted.length==1) return sorted[0];
		if (sorted.length==2) return (sorted[0]+sorted[1])/2;
		if (sorted.length==3) return sorted[1];
		
		Arrays.sort(sorted);
		
		if (sorted.length%2==1) return sorted[sorted.length/2];
		
		return (sorted[sorted.length/2]+sorted[sorted.length/2-1])/2;//if no number is the exact middle
	}
	
	public double[] getSorted() {
		double[] sorted=data.clone();
		Arrays.sort(sorted);
		return sorted;
	}
	
	public double getQ1() {
		return getMedian(getHalf(data, false));
	}
	
	public double getQ3() {
		return getMedian(getHalf(data, true));
	}
	
	/**
	private ArrayList<Double> getWithoutOutliers() {
		double IQR15 = (getQ3()-getQ1())*1.5;
	}*/
	
	public static double[] getHalf(double[] sorted, boolean second ) {
		double[] output = new double[] {};
		if (sorted.length%2==0) output = new double[sorted.length/2] ;
		else output = new double[sorted.length/2+1] ;
		Arrays.sort(sorted);
		if (!second)for(int i=0; i<output.length; i++) output[i]=sorted[i];
		if (second)for(int i=0; i<output.length; i++) output[output.length-1-i]=sorted[sorted.length-1-i];
		return output;	
	}
	
	public double getMedian() {
		return getMedian(this.getRawValues().clone());
	}
	
	
	
	
	public String toString() {
		String st=name+": [";
		for(double d: data)st+=" "+d;
		st+="]";
		st+=" median: "+getMedian();
		return st;
	}
	
	static String arrayToString(double[] data) {
		String st= "[";
		for(double d: data)st+=" "+d+",";
		st+="]";
		return st;
	}
	
	public static void main(String[] args) {
		testHalfCut(new double[] {18, 13, 14, 32, 5, 26});
		
		testHalfCut(new double[] {18, 13, 14, 32, 5, 83, 6, 15, 26});
	}
	
	static void testHalfCut(double[] test) {
		Basic1DDataSeries dataseries1 = new Basic1DDataSeries("s1" , test);
		System.out.println( arrayToString(dataseries1.getSorted()));
		System.out.println( arrayToString(getHalf(dataseries1.data, false)));
		System.out.println( arrayToString(getHalf(dataseries1.data, true)));
		System.out.println( dataseries1.getQ1());
		System.out.println( dataseries1.getQ3());
	}

	public void setPositionOnPlot(double position) {
		this.positionOnPlot=position;
		
	}
	
	public double getPositionOnPlot() {
		return positionOnPlot;
	}
	
	public double getMin() {
		return getMin(data);
	}
	
	public double getMax() {
		return getMax(data);
	}
	
	public double getMax(double[] d) {
		double output=Double.MIN_VALUE;
		for(double n: d) {if (n>output) output=n; }
		return output;
	}
	
	/**returns true if a point with within 1.5 iqr of the quariles*/
	public boolean isWith15IQR(double d) {
		double q1 = this.getQ1();
		double q3 = this.getQ3();
		double threshold = q3+1.5*(q3-q1);
		if (d>threshold) return false;
		threshold = q1-1.5*(q3-q1);
		if (d<threshold) return false;
		return true;
	}
	
	/**among values within 1.5IQR of the first quarile, returns the max*/
	public double getMaxExcludingOutliers() {
		double q1 = this.getQ1();
		double q3 = this.getQ3();
		double threshold = q3+1.5*(q3-q1);
		
		double output=Double.MIN_VALUE;
		for(double n: data) {if (n>output&&n<threshold) output=n; }
		return output;
	}
	
	/**among values within 1.5IQR of the first quarile, returns the minimum*/
	public double getMinExcludingOutliers() {
		double q1 = this.getQ1();
		double q3 = this.getQ3();
		double threshold = q1-1.5*(q3-q1);
		
		double output=Double.MAX_VALUE;
		for(double n: data) {if (n<output&&n>threshold) output=n; }
		return output;
	}
	
	public double getMin(double[] d) {
		double output=Double.MAX_VALUE;
		for(double n: d) {if (n<output) output=n; }
		return output;
	}

	public void setData(double[] data2) {
		this.data=data2;
		
	}

	@Override
	public Basic1DDataSeries getIncludedValues() {
		return this;
	}

	@Override
	public int length() {
		return data.length;
	}

	@Override
	public double getValue(int y) {
		return data[y];
	}

	@Override
	public double getPosition(int x) {
		return positionOnPlot;
	}

	/**since this is just one dimension, it has these values for any positions*/
	@Override
	public Basic1DDataSeries getValuesForPosition(double position) {
		return this;
	}

	@Override
	public double[] getAllPositions() {
		return new double[] {this.getPositionOnPlot()};
	}

	@Override
	public double getPositionOffset() {
		return pOffset;
	}
	
	public void setPositionOffset(double off) {
		pOffset=off;
	}

	@Override
	public HashMap<Double, Double> getValueOffsetMap() {
		return null;
	}

	@Override
	public double[] getAllPositionsInOrder() {
		return this.getAllPositions();
	}

	@Override
	public DataPoint getDataPoint(int i) {
		return new BasicDataPoint(positionOnPlot, this.getValue(i));
	}

	public double getErrorBarLength(int errorDepiction, int upperOrLower) {
		double barExtends= getSDDev();
		if (errorDepiction==SEM) {barExtends= getSEM();}
		if (errorDepiction==SEM2) {barExtends= 2*getSEM();}
		if (errorDepiction==SEM3) {barExtends= 3*getSEM();}
		return barExtends;
	}

}