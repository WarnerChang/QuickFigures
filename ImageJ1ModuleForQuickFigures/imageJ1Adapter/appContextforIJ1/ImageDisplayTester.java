package appContextforIJ1;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import javax.swing.JFrame;

import appContext.CurrentAppContext;
import applicationAdapters.ToolbarTester;
import ij.IJ;
import ij.ImageJ;
import ij.Menus;
import ij.Prefs;
import ij.io.Opener;
import ij.io.PluginClassLoader;
import includedToolbars.ActionToolset1;
import includedToolbars.ActionToolset2;
import includedToolbars.ObjectToolset1;
import loci.formats.FormatException;
import loci.plugins.BF;
import logging.IssueLog;

public class ImageDisplayTester extends ToolbarTester {

	public static void main(String[] args) {
	//	System.setProperty("apple.laf.useScreenMenuBar", "true");
	  //  System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Name");
		IssueLog.sytemprint=true;
		IssueLog.windowPrint=false;
		ij1Test(false);
		showExample(true);
		
		String exampleimage = "C:\\Users\\Greg Mazo\\Desktop\\Quickfigures\\examples form dsRNA red eGFP green/Image- 0001_taken_01_29_2020.tif";
		 if (new File(exampleimage).exists())
			 	IJ.open(exampleimage);
		 
		
	}
	
	
	public static void openZVI() {
		String file1="/Users/Greg Mazo/Desktop/red blu green tab order/bl green red image order.zvi";
		try {
			BF.openImagePlus(file1);
		} catch (FormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void ij1Test(boolean useNormalPath) {
		CurrentAppContext.setMultichannelContext(new IJ1MultichannelContext());
		
		try {
			   /** Opens an image file using the Bio-Formats plugin. */
		   
		        String className = "loci.plugins.BF";
		        String methodName = "openImagePlus";
		        
		            Class<?> c = IJ.getClassLoader().loadClass(className);
		            IJ.getClassLoader().loadClass("loci.plugins.LociImporter");
		            if (c==null)
		                 IssueLog.log("failed to load bioformats");
		          
		            
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
		if (useNormalPath) try {
			 String newhome="/Applications/ImageJ/plugins";
			 String ijpath="/Applications/ImageJ";
			/** Field homeDir = Prefs.class.getDeclaredField("homeDir");
			 homeDir .setAccessible(true);
			String output=""+homeDir.get(null);*/
			 
			 
			PluginClassLoader pcl = new PluginClassLoader(newhome);
		
			
			IssueLog.log("The class loader is "+IJ.getClassLoader());
		Field field = IJ.class.getDeclaredField("classLoader");
		field.setAccessible(true);
		field.set(null, pcl);
		IssueLog.log("The class loader is "+IJ.getClassLoader());



		field=Menus.class.getDeclaredField("pluginsPath");
		field.setAccessible(true);
		field.set(null, newhome);
			//IJ.getClassLoader()
		field=Prefs.class.getDeclaredField("homeDir");
		field.setAccessible(true);
		field.set(null, ijpath);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		 
		//loadLociImporter();
		
		ImageJ ij = new ImageJ();;
		loadLociImporter(ij);
		
		    // IJ.openImage uses (new Opener()).openImage(path, n);
		//however, I am not sure I understand how this manages with the bioformats
	}


	public static void loadLociImporter(ImageJ ij) {
		try {
			//IJ.getClassLoader().loadClass("loci.plugins.LociImporter");
			//new PluginClassLoader("").loadClass("loci.plugins.LociImporter");
			
			//if (Menus.getCommands()!=null)
			if (ij!=null)Menus.installPlugin("loci.plugins.LociImporter", Menus.IMPORT_MENU, "Bio-Formats", "", ij);
			Menus.getCommands().put("Bio-Formats Importer", "loci.plugins.LociImporter");
			Field f = new Opener().getClass().getDeclaredField("bioformats");
			f.setAccessible(true);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public static void startToolbars(boolean appclose) {
		ObjectToolset1 toolset = showInnitial();
		if (appclose) {
			toolset.getframe().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		new ActionToolset1().run("go");;
		new ActionToolset2().run("go");;
		
		ij1Test(false);
	}
	
	
	
	
}