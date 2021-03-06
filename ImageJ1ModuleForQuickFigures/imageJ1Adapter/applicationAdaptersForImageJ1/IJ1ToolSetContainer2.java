/*******************************************************************************
 * Copyright (c) 2021 Gregory Mazo
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *******************************************************************************/
package applicationAdaptersForImageJ1;
import java.awt.Image;
import java.awt.PopupMenu;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import applicationAdapters.DisplayedImage;
import externalToolBar.AbstractExternalToolset;
import externalToolBar.DummyTool;
import externalToolBar.InterfaceExternalTool;
import externalToolBar.ToolChangeListener;
import ij.ImagePlus;
import ij.gui.ImageCanvas;
import ij.gui.Toolbar;
//import ij.plugin.tool.PlugInTool;
import logging.IssueLog;

/**Originally this class was written as part of a system to allow imageJ to use an external toolbar. 
  Classes could be written that would appear as icons in a separate toolbar but otherwise
  work like plugin tools for imageJ. 
 Eventually, updated such that all my tools worked independently of the imageJ.
 I might change it back to use imageJ again but so far there seems to be no need.
 It does not affect the user interface of quickfigures but might be interesting to programmers. */
@Deprecated
public class IJ1ToolSetContainer2  implements  MouseListener, ToolChangeListener<DisplayedImage> {
	
	 static IJ1ToolSetContainer2 instance=new IJ1ToolSetContainer2();
	
	//StandardIJ1MontageToolset[] toolbox=new StandardIJ1MontageToolset[]{ new StandardIJ1MontageToolset(this)};
	AbstractExternalToolset< DisplayedImage > toolbox=null;
	
	
	InterfaceExternalTool< DisplayedImage > current=new DummyTool< DisplayedImage>();
	
	
	
public IJ1ToolSetContainer2() {
		
	}

	public IJ1ToolSetContainer2(AbstractExternalToolset< DisplayedImage > toolset) {
		toolbox=toolset;
	}
	
	public AbstractExternalToolset< DisplayedImage > getToolbox() {
		return toolbox;
	}
	
	
	boolean isToolbox(AbstractExternalToolset< DisplayedImage > currentToolbar) {
		return currentToolbar==toolbox;
	}
	
	
	
	public void run(String arg) {
		//super.run(arg);//this class no longer extends plugintool

	}
	
	
	
	void setToolImageAndClickPoint(ImagePlus imp, MouseEvent arg0) {
		int x = arg0.getX();
		int y = arg0.getY();
		int onscreenx=imp.getCanvas().offScreenX((int)(x));
		int onscreeny=imp.getCanvas().offScreenY((int)(y));
		getCurrentTool().setImageAndClickPoint(new ImagePlusDisplayWrap(imp), onscreenx, onscreeny);
	}
	
	
	void switchedToIJToolbar() {
		
		if ( 
				/**!(Toolbar.getPlugInTool() instanceof IJ1ToolSetContainer2) &&obsolete. no longer use montage wizard tools as imageJ tools*/
				getToolbox().getCurrentTool()!=null) {
			for (AbstractExternalToolset<?> set : AbstractExternalToolset.openToolsets)set.resetButtonIcon();
		}
		
	}
	
	void switchedToolbar(AbstractExternalToolset<DisplayedImage> ij1Toolset) {
		toolbox=ij1Toolset;
	}
	
	
	public InterfaceExternalTool< DisplayedImage > getCurrentTool() {
		if (current==null) return new DummyTool<DisplayedImage>();
		return current;
		
	};
	
	
	public void mousePressed(ImagePlus imp, MouseEvent e) {
		try{
			;
		getCurrentTool().mousePressed(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));
		}
		catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseClicked(ImagePlus imp, MouseEvent e){
		try{
			getCurrentTool().mouseClicked(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));
}
	catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseDragged(ImagePlus imp, MouseEvent e) {
		try{
		getCurrentTool().mouseDragged(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));}
		catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseReleased(ImagePlus imp, MouseEvent e) {
		try {
		getCurrentTool().mouseReleased(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));}
	catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseExited(ImagePlus imp, MouseEvent e) {
	//	imp.getCanvas().removeMouseListener(this);
		//imp.getCanvas().disablePopupMenu(false);
		try {getCurrentTool().mouseExited(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));}
		catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseEntered(ImagePlus imp, MouseEvent e) {
		try{
		getCurrentTool().mouseEntered(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));}
	catch (Throwable t) {IssueLog.logT(t);}
	}
	public void mouseMoved(ImagePlus imp, MouseEvent e) {
		try{
		getCurrentTool().mouseMoved(new ImagePlusDisplayWrap(imp), new IJ1MEWrapper(imp, e));}
	catch (Throwable t) {IssueLog.logT(t);}
	}
	public String getToolIcon() {
		if (getCurrentTool()==null) return "Toolbox";
		String out=getCurrentTool().getToolIcon();
		if (out!=null) return out;
		//out=ijToolbarIcon(getToolbox().getCurrentTool().getToolImageIcon(), 0, 0);
		//IssueLog.log2("could not find ");
	//	IssueLog.log2(out);
		return out;
		//return getToolbox().getCurrentTool().getToolIcon();
	}
	
	public void showOptionsDialog() {
		getToolbox().showFrame();
		getCurrentTool().showOptionsDialog();
	}

	
	
	public String getToolName() {
		return getCurrentTool().getToolName();
	}
	
	

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
	//	switchedToIJToolbar() ;
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		//switchedToIJToolbar() ;
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
		if (arg0.getSource() instanceof ImageCanvas && arg0.isPopupTrigger()) {
	
			
			
		}
		
	}
	
	PopupMenu pm= new PopupMenu();

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource()==Toolbar.getInstance()) switchedToIJToolbar() ;
	}
	
	
	public static String ijToolbarIcon(Image i, int x0, int y0) {
		String output="";
		ImagePlus imp=new ImagePlus("dummy", i);
		for(int x=x0; x<x0+16; x++) {
			for(int y=y0; y<y0+16; y++) {
				output+=getIJMacroIconForPixel(imp, x, y,x0,  y0);
			}
			
		}
		
		return output;
		
	}
	public static String getIJMacroIconForPixel(ImagePlus imp, int x, int y, int x0, int y0) {
		String output="";
		//ImagePlus imp=new ImagePlus("dummy", i);
		imp=new ImagePlus("", imp.getProcessor().resize(16, 16));
		int[] color=imp.getPixel(x, y);
		output+="C"+hexDigits[(color[0]/16)]+""+hexDigits[(color[1]/16)]+""+hexDigits[(color[2]/16)]+" ";
		output+="D"+hexDigits[(x-x0)]+""+hexDigits[(y-y0)]+" ";
		
		return output;
	}
	 /** This array contains the 16 hex digits '0'-'f'. */
    public static final char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};



	@Override
	public void ToolChanged(
			InterfaceExternalTool<DisplayedImage> tool) {
		current=tool;
		this.run("");
		
	}


	
}
