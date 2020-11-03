package imageDisplayApp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import graphicalObjects_LayerTypes.GraphicLayerPane;
import logging.IssueLog;
import ultilInputOutput.FileChoiceUtil;
import ultilInputOutput.FileFinder;

public class ImageDisplayIO {
	public static GraphicSet readFromFile(File f) {
		//GraphicEncoder encoder = new GraphicEncoder(theSet.getGraphicLayerSet());
		FileInputStream fo;
		GraphicSet output=null;
		FileFinder.setWorkingDir(f);
		
		if (!f.exists()) {
			IssueLog.log("file"+f+" is non existent");
		}
		try {
			fo = new FileInputStream(f);
			ObjectInputStream oos = new ObjectInputStream(fo);
			Object o1=oos.readObject();
			Object o2=oos.readObject();
			
			
			//("read objects from file and will try to use them "+'\n'+o1.getClass()+" "+o2.getClass());
			if (o1 instanceof GraphicLayerPane&& o2 instanceof BasicImageInfo) {
				 GraphicLayerPane g=(GraphicLayerPane) o1;
				 BasicImageInfo b=(BasicImageInfo) o2;
				 
				output= new GraphicSet(g,b);
				output.onItemLoad(output.getLayer());
			}
			
		
			fo.close();
			//return true;
		} catch (Exception e) {
			if (e instanceof java.lang.ClassNotFoundException) {
				FileChoiceUtil.OkOrNo("Class not found. "+"File likely saved with earlier version");
			}
			IssueLog.log(e);;
			return null;
		}
		
		output.setSavePath(f.getAbsolutePath());
		String name = f.getName();
		if(name.endsWith(".ser")) name=name.replace(".ser", "");
		output.setTitle(name);
		
		return output;
		
		
	}
	
	
	

	public static boolean writeToFile(File f, GraphicSet theSet) {
		//GraphicEncoder encoder = new GraphicEncoder(theSet.getGraphicLayerSet());
		FileOutputStream fo;
		try {
			fo = new FileOutputStream(f);
			
			ObjectOutputStream oos = new ObjectOutputStream(fo);
			
			oos.writeObject(theSet.getLayer());
			oos.writeObject(theSet.getBasics());
			
		oos.flush();
		fo.close();
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
	}
	
	public static ImageAndDisplaySet showFile(File f) {
		if (f==null) return null;
			GraphicSet set = ImageDisplayIO.readFromFile(f);
			if (set==null) return null;
			return new ImageAndDisplaySet(set);
	}
	

}