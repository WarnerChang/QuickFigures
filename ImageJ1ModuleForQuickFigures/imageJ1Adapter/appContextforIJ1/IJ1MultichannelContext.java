package appContextforIJ1;

import java.util.ArrayList;

import appContext.MultichannelContext;
import applicationAdaptersForImageJ1.ImagePlusWrapper;
import channelMerging.MultiChannelWrapper;
import ij.IJ;
import ij.ImagePlus;
import ij.WindowManager;
import ij.io.OpenDialog;
import logging.IssueLog;
import multiChannelFigureUI.MultiChannelDisplayCreator;

public class IJ1MultichannelContext implements MultichannelContext {

	IJ1MultiChannelCreator item=new IJ1MultiChannelCreator();
	
	@Override
	public MultiChannelDisplayCreator getMultichannelOpener() {
		// TODO Auto-generated method stub
		return item;
	}

	@Override
	public MultiChannelDisplayCreator createMultichannelDisplay() {
		// TODO Auto-generated method stub
		return new IJ1MultiChannelCreator();
	}
	


	@Override
	public ArrayList<MultiChannelWrapper> getallVisibleMultichanal() {
		// TODO Auto-generated method stub
		int[] list1 = WindowManager.getIDList();
		ArrayList<MultiChannelWrapper> output=new ArrayList<MultiChannelWrapper>();
		
		if (list1==null|| list1.length==0) IssueLog.log("No multichannel images are open");
		if (list1!=null) for(int i: list1) {
			
			ImagePlus im = WindowManager.getImage(i);
			if(im==null) continue;
			output.add(new ImagePlusWrapper(WindowManager.getImage(i)));
		}
		return output;
	}

	@Override
	public MultiChannelWrapper getCurrentMultichanal() {
		// TODO Auto-generated method stub
		return new ImagePlusWrapper(IJ.getImage());
	}

	@Override
	public String getDefaultDirectory() {
		 return OpenDialog.getDefaultDirectory();
	}

}