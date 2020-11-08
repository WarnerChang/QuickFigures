package objectDialogs;

import channelMerging.MultiChannelWrapper;
import standardDialog.ChannelEntryBox;
import standardDialog.ComboBoxPanel;
import standardDialog.NumberInputPanel;
import standardDialog.StandardDialog;

public class ChannelSliceAndFrameSelectionDialog extends StandardDialog {

	/**
	 * 
	 */
	{this.setModal(true);this.setWindowCentered(true);}
	private static final long serialVersionUID = 1L;
	private int channel=0;
	private int frame;
	private int slice;
	
	MultiChannelWrapper mw;
	
	public ChannelSliceAndFrameSelectionDialog( int channel, int slice, int frame, MultiChannelWrapper mw) {
		this.mw=mw;
		this.setChannel(channel);
		this.setFrame(frame);
		this.setSlice(slice);
		
		
		
	}
	
	/**Adds the options for the channel selection dialog. if there are no possible options,
	 * returns false*/
	public boolean addOptions(boolean haschannel, boolean hasslice, boolean hasframe) {
		
			StandardDialog dialog = this;
			MultiChannelWrapper multiChannel = mw;
		
			
			if(multiChannel!=null&&multiChannel.nFrames()<2) {
				hasframe=false;
			}
			if(multiChannel!=null&&multiChannel.nSlices()<2) {
				hasslice=false;
			}
			
			
			if (haschannel) {
				addChannelSelectionToDialog(dialog, multiChannel, getChannel());
				
				}
		if (hasframe) {
			addFrameSelectionToDialog(dialog, multiChannel,  getFrame());
			
		}
		if (hasslice) {
			addSliceSelectionToDialog(dialog, multiChannel, getSlice());
			
		}
		if(hasslice==false&&hasframe==false&&haschannel==false) return false;
		return true;
	}

	public static void addChannelSelectionToDialog(StandardDialog dialog, MultiChannelWrapper multiChannel, int chan) {
		if (multiChannel==null) {
		dialog.add("chan", new NumberInputPanel("The channel ", chan));
}
		if (multiChannel!=null) {
			ChannelEntryBox box = new ChannelEntryBox(multiChannel.getChannelEntriesInOrder(), "Merge");
			 ComboBoxPanel mergeCombo = new standardDialog.ComboBoxPanel("Channel", box);
			 
			 dialog.add("chan",mergeCombo);
		}
	}

	public static void addSliceSelectionToDialog(StandardDialog dialog, MultiChannelWrapper multiChannel, int slice) {
		if (multiChannel!=null&&multiChannel.nFrames()<150) {
			dialog.add("slice", new NumberInputPanel("The slice ", slice, true, true, 1, multiChannel.nSlices()));
		}
		else 
		dialog.add("slice", new NumberInputPanel("The slice ", slice));
	}

	public static void addFrameSelectionToDialog(StandardDialog dialog, MultiChannelWrapper multiChannel, int frame) {
		if (multiChannel!=null&&multiChannel.nFrames()<150) {
			dialog.add("frame", new NumberInputPanel("The frame ",frame, true, true, 1, multiChannel.nFrames()));
		}
		else 
		dialog.add("frame", new NumberInputPanel("The frame ", frame));
	}
	
	@Override 
	public void onOK() {
		this.setFrame(this.getNumberInt("frame"));
		this.setSlice(this.getNumberInt("slice"));
		if (mw==null)
		this.setChannel(this.getNumberInt("chan"));
		else this.setChannel(this.getChoiceIndex("chan"));
	}
	
	public void show3DimensionDialog() {
		if (addOptions(true, true, true))
		this.showDialog();
	}
	public void show2DimensionDialog() {
		if (addOptions(false, true, true))
		this.showDialog();
	}
	
	public void showFrameDialog() {
		if (addOptions(false, false, true))
		this.showDialog();
	}
	
	public void showSliceDialog() {
		if (addOptions(false, true, false));
		this.showDialog();
	}

	public int getChannel() {
		return channel;
	}

	public void setChannel(int channel) {
		this.channel = channel;
	}

	public int getFrame() {
		return frame;
	}

	public void setFrame(int frame) {
		this.frame = frame;
	}

	public int getSlice() {
		return slice;
	}

	public void setSlice(int slice) {
		this.slice = slice;
	}

}