package channelMerging;

import java.io.Serializable;
import java.util.ArrayList;

import channelMerging.SubStackSelectionInstructions.FrameUseInstructions;
import channelMerging.SubStackSelectionInstructions.SliceUseInstructions;
import gridLayout.BasicMontageLayout;
import logging.IssueLog;
import utilityClasses1.ArraySorter;

/**
Instructions on how to take that channels from a multichannel image
and make panels.
Used by many classes
*/
public class ChannelUseInstructions implements Serializable {

		
		private static final long serialVersionUID = 1L;
		public static final int MERGE_LAST=0, MERGE_FIRST=1, ONLY_MERGE_PANELS=3, NO_MERGE_PANELS=4;
		public static final int CHANNELS_IN_COLOR=0, CHANNELS_IN_GREYSCALE=1;
		public static final int NONE_SELECTED=0;
		
		
		
			/**These options are important for deciding how composite stacks are converted into RGB*/
			public int channelColorMode=CHANNELS_IN_GREYSCALE;
			
			/**Channel that is merged into every channel panel*/
		    public int eachMergeChannel=NONE_SELECTED;
		    
		    /**Channels panels are not created for some channel numbers*/
		    public ArrayList<Integer> excludedChannelPanels=new ArrayList<Integer>(), 
		    		 /**Channels for some channel numbers are excluded from the merge*/
		    		noMergeChannels=new ArrayList<Integer>();//excluded from the merge
		   
		    /**will skip channels after this one*/
		    public int ignoreAfterChannel= NONE_SELECTED;
		    
		    /**what to do with the Merge*/
		    public int MergeHandleing=MERGE_LAST;
		    
		    /**what amount of columns are recommended*/
		    public int idealColNum=5;

			private ChannelPanelReorder reorder;
			private FrameUseInstructions frameUseMethod;
			private SliceUseInstructions sliceUseMethod;
		 
		    /**returns a set of channel use instrcutions used when one wants to put
		     * channel panels as insets near or around a merged image*/
		    public static ChannelUseInstructions getChannelInstructionsForInsets() {
		    	ChannelUseInstructions out = new ChannelUseInstructions();
		    	out.ignoreAfterChannel=NONE_SELECTED;
		    	out.eachMergeChannel=NONE_SELECTED;
		    	out.channelColorMode=CHANNELS_IN_COLOR;
		    	out.excludedChannelPanels=new ArrayList<Integer>();
		    	out.noMergeChannels=new ArrayList<Integer>();
		    	return out;
		    }
		   
		    /**sets several values of fields*/
			public void setChannelHandleing(int ChannelsInGrayScale, int MergeFirst, int eachMergeChannel, ArrayList<Integer> excludedChannelPanels, ArrayList<Integer> noMergeChannels, int ignoreAfterChannel) {
				this.channelColorMode=ChannelsInGrayScale;
				this.eachMergeChannel=eachMergeChannel;
				this.excludedChannelPanels=excludedChannelPanels;
				this.noMergeChannels=noMergeChannels;
				this.MergeHandleing=MergeFirst;
				this.ignoreAfterChannel=ignoreAfterChannel;
			}
			
		
			
			
			/**true if merge panel goes last*/
			public boolean mergePanelLast() {
				return MergeHandleing==MERGE_LAST;
			}

			/**true if merge panel goes first*/
			public boolean mergePanelFirst() {
				return MergeHandleing==MERGE_FIRST;
			}
			
			/**returns true if ONLY the mergePanel should be included in the panel set*/
			public boolean onlyMergePanel() {
				return MergeHandleing==ONLY_MERGE_PANELS;
			}
			
			/**returns true if the mergePanel should be included*/
			public boolean addsMergePanel() {
				if (MergeHandleing==MERGE_LAST||MergeHandleing==ONLY_MERGE_PANELS||MergeHandleing==MERGE_FIRST) return true;
				
				return false;
			}

			/**returns true if the given channel should be excluded from the merge panel*/
			public boolean isMergeExcluded(int c) {
				for(Integer ex: noMergeChannels) {if (ex.intValue()==c) return true;}
				if (this.ignoreAfterChannel>0&&c>this.ignoreAfterChannel) return true;
				return false;
			}
			
			/**true is channel number c is to be excluded from the panel list. false otherwise*/
			public boolean isChanPanExcluded(int c) {
				for(Integer ex: excludedChannelPanels) {if (ex.intValue()==c) return true;}
				if (this.ignoreAfterChannel>0&&c>ignoreAfterChannel) return true;
				if (c==eachMergeChannel) return true;
				if (onlyMergePanel()) return true;
				return false;
			}
			
			/**returns true if panels from the given time frame will
			  be excluded according to these instructions*/
			public boolean isFrameExcluded(int frame) {
				if(this.getFrameUseInstructions()==null) return false;
				return getFrameUseInstructions().isExcluded(frame);
			}
			/**returns true if panels from the given z slice will
			  be excluded according to these instructions*/
			public boolean isSliceExcluded(int frame) {
				if(this.getSliceUseInstructions()==null) return false;
				return getSliceUseInstructions().isExcluded(frame);
			}
			
			/**Alters the other Instructions to have settings comparable to this one.
			  However, the selected frame or slice (if there is one) will be set to 1*/
			public void makeMatching(ChannelUseInstructions otherInstructions) {
				 makePartialMatching(otherInstructions);
				otherInstructions.reorder=new ChannelPanelReorder(this.getChanPanelReorder().currentOrder);
				if(this.getFrameUseInstructions()!=null) otherInstructions.setFrameUseMethod(getFrameUseInstructions().createDouble());
				if(this.getSliceUseInstructions()!=null) otherInstructions.setSliceUseMethod(getSliceUseInstructions().createDouble());
			} 
			
			/**changes the channel use options of the given instructions to match this one
			 NOTE: this uses the same array to store the excluded channels so the other will not be 
			 completely separated from these instructions.
			 */
			public void makePartialMatching(ChannelUseInstructions other) {
				other.channelColorMode=channelColorMode;
				other.eachMergeChannel=eachMergeChannel;
				
				other.MergeHandleing=MergeHandleing;
				other.ignoreAfterChannel=ignoreAfterChannel;
				other.idealColNum=idealColNum;
				
				other.excludedChannelPanels=excludedChannelPanels;
				other.noMergeChannels=noMergeChannels;
					} 
			
			/**Generates a duplicate of this set of instructions but without the channel order*/
			public ChannelUseInstructions duplicate() {
				ChannelUseInstructions output = new ChannelUseInstructions();
				makePartialMatching(output);
				output.excludedChannelPanels=new ArrayList<Integer>();output.excludedChannelPanels.addAll(excludedChannelPanels);
				output.noMergeChannels=new ArrayList<Integer>();output.noMergeChannels.addAll(noMergeChannels);
				output.frameUseMethod=this.frameUseMethod.duplicate();
				output.sliceUseMethod=this.sliceUseMethod.duplicate();
				return output;
			}
			
			/**Estimates the number of channel and merge panels
			  that will be needed for in each slice of the panel list
			  made for the multichannel image */
			public int estimateChanColsNeeded(MultiChannelImage imp) {
				int fs=1;
				if (this.onlyMergePanel()) return fs;
				
				int out=0;
				out=fs*(imp.nChannels());
				
				if (addsMergePanel()) out+=fs;
				
				for(int c=1; c<=imp.nChannels(); c++) {
					if (isChanPanExcluded(c)) out-=fs;
				}
				
				return out;
			}
			
			
			/**Estimates the number of rows and columns of a
			 grid layout needed to display the panels for 
			  the given image*/
			public int[] estimateBestMontageDims(MultiChannelImage image) {
				
				if (image==null) {
					IssueLog.log("No multichannel image available");
					return new int[]{1,1};
				}
				
				/**What is done if multiple channels are represented*/
				int nFramesUsed = estimateNFramesUsed(image);
				int nSlicesUsed = estimateNSlicesUsed(image);
				if (image.nChannels()>1 && !onlyMergePanel()) {
					int chanCols = estimateChanColsNeeded(image);
					int nSF=nFramesUsed*nSlicesUsed;
					
					/**what to do if the number of channel columns needed is above the desired total number of cols*/
					if(this.idealColNum<chanCols) {
						int rows = (chanCols*nSF)/idealColNum;
						int extra=(chanCols*nSF)%idealColNum;
						if(extra>0) rows++;
						return new int[] {rows, idealColNum};
					}
					
					return new int[] {nSF, chanCols}
					;}
				
				/**What to do if both frames and z slices are represented*/
				boolean singleChannelOfMergeUsed = image.nChannels()==1||this.onlyMergePanel();
				
				if (singleChannelOfMergeUsed&&nSlicesUsed>1&&nFramesUsed>1) {return new int[] {nFramesUsed,nSlicesUsed};}
				if (singleChannelOfMergeUsed&& nFramesUsed==1) {return gridFor(nSlicesUsed);};
				if (singleChannelOfMergeUsed&& nSlicesUsed==1) {return gridFor(nFramesUsed);};
				
				
				return new int[]{1,1};
			}

			/**the number of slices that will be included in the figure*/
			public int estimateNSlicesUsed(MultiChannelImage image) {
				if(this.getSliceUseInstructions()!=null) return getSliceUseInstructions().estimateNUsed(image);
				return image.nSlices();
			}
			/**the number of frames that will be included in the figure*/
			public int estimateNFramesUsed(MultiChannelImage image) {
				if(this.getFrameUseInstructions()!=null) return getFrameUseInstructions().estimateNUsed(image);
				return image.nFrames();
			}
			/**setter method for the instructions regarding which slices to include*/
			private void setSliceUseMethod(SliceUseInstructions sliceUseMethod) {
				this.sliceUseMethod = sliceUseMethod;
			}
			/**setter method for the instructions regarding which frames to include*/
			private void setFrameUseMethod(FrameUseInstructions frameUseMethod) {
				this.frameUseMethod = frameUseMethod;
			}
			/**getter method for the instructions regarding which frames to include*/
			public SubStackSelectionInstructions.FrameUseInstructions getFrameUseInstructions() {
				if(frameUseMethod==null) frameUseMethod=new SubStackSelectionInstructions.FrameUseInstructions(null);
				return frameUseMethod;
			}
			/**getter method for the instructions regarding which slices to include*/
			public SubStackSelectionInstructions.SliceUseInstructions getSliceUseInstructions() {
			if(sliceUseMethod==null) sliceUseMethod=new SubStackSelectionInstructions.SliceUseInstructions(null);
				return sliceUseMethod;
			}
			
			/**returns true if the instructions only use a subset of the frames or slices*/
			public boolean selectsSlices(MultiChannelImage mw) {
				if(mw.nFrames()>1&&this.getFrameUseInstructions()!=null &&!getFrameUseInstructions().selectsAll()) return true;
				if(mw.nSlices()>1&&this.getSliceUseInstructions()!=null &&!getSliceUseInstructions().selectsAll()) return true;
				
				return false;
			}
			
			/**Sets the instructions to use only the given time frame. 
			  if the given frame is null, uses all the frames*/
			public void limitStackUseToFrame(Integer frame) {
				if (frame!=null)setFrameUseMethod(new SubStackSelectionInstructions.FrameUseInstructions(frame));
			}
			
			/**Sets the instructions to use only the given z slice. 
			  if the given slice is null, uses all the slices*/
			public void limitStackUseToSlice(Integer slice) {
			if (slice!=null)setSliceUseMethod(new SubStackSelectionInstructions.SliceUseInstructions(slice));
			}
			
			/**Estimates the total number of panels that will be created
			  for display of the multichannel image.*/
			public int estimageNPanels(MultiChannelImage image) {
				int[] in = estimateBestMontageDims(image);
				return in[0]*in[1];
			}
			
			
			/**when given a number of panels containing slices or frames, determines a grid dimension for them
			  that will fit within the 'ideal' coluumn number option (changeable by user)*/
			public int[] gridFor(int i) {
				if (i<=idealColNum) return new int[] {1,i};
				
				return new int[] {1+i/idealColNum,idealColNum};
					
			}
			
			/**returns the number of rows and columns of panels that are needed to display the given images
			 * if the list contains more than one image, then this method
			 * does not take into account the number of time points and z sections. only channels */
			public int[] estimateBestMontageDims(ArrayList<MultiChannelImage> images) {
				
				if (images.size()==0) return new int[]{1,1};
				int[] output=estimateBestMontageDims(images.get(0));
				for(int i2=1; i2<images.size(); i2++) {
					int[] addition=estimateBestMontageDims(images.get(i2));
					if (this.onlyMergePanel()){output[1]=output[1]+addition[1];} else {output[0]=output[0]+addition[0];}
					if (addition[1]>output[1]) output[1]=addition[1];
				}

				if (this.onlyMergePanel()) {
					return this.gridFor(images.size());
				}
				return output;
			}
			
			
			
			/**alters the layout to ensure that sufficient rows and columns are available to fit the image
			 *if multiple images are present and each has more than one section/time frame this does not produce a perfect result
			 **/
			public void setDimensionForPanels(BasicMontageLayout p,ArrayList<MultiChannelImage> image) {
				int[] dims=estimateBestMontageDims(image);
				int col=dims[1];
				int row=dims[0];
				if(p.rowmajor) {
					p.setNColumns(col);
					p.setNRows(row);
				}
				else {
					p.setNColumns(row);
					p.setNRows(col);
				}
				
			}
			
			
			/**Called when channel panels have been swaped.  */
			public void onChannelSwap(int c1, int c2) {
	
				getChanPanelReorder().swap(c1, c2);
			}
			
			/**If these instructions target a specific time frame or z section
			 changes the selected Z section and T frame selected to be consistent with the CSF location provided.
			  */
	public void shareViewLocation(CSFLocation d) {
				
				if(this.frameUseMethod!=null&& frameUseMethod.method==SubStackSelectionInstructions.SINGLE_) frameUseMethod.setSelected(d.frame);
				if(this.sliceUseMethod!=null&& sliceUseMethod.method==SubStackSelectionInstructions.SINGLE_) sliceUseMethod.setSelected(d.slice);
				
			}
			
			/**returns the channel re-order object*/
			public ChannelPanelReorder getChanPanelReorder() {
				if (reorder==null) {
					reorder=new ChannelPanelReorder();
				}
				return reorder;
			}
			
			/**resets the channel order back to default*/
			public void clearChannelReorder() {
				reorder=null;
			}

		
			/**this nested class stores the order of the channel panels.
			  Objects of this class are used to determine channel panel orders
			 when panels are freshly created. The order may be modified when the user 
			 makes certain changes*/
			public static class ChannelPanelReorder implements Serializable {
				private static final long serialVersionUID = 1L;
				static final int[] defaultOrder=new int[] {0,1,2,3,4,5,6,7,8,9,10};
				private ArrayList<Integer> currentOrder;
				
				public ChannelPanelReorder() {
					ArrayList<Integer> order = new ArrayList<Integer>();
					for(int i: defaultOrder) {order.add(i);}
					this.currentOrder=order;
				}
				public ChannelPanelReorder(ArrayList<Integer> order) {
					this.currentOrder = new ArrayList<Integer>();
					currentOrder.addAll(order);
				}
				
				/**swaps the locations of two channels within the order*/
				void swap(int c1, int c2) {
					new ArraySorter<Integer>().swapObjectPositionsInArray(c1, c2, currentOrder);
				
				}
				/**returns the position of channel c in the order*/
				public int index(int c) {
					return currentOrder.indexOf(c);
				}
				/**imposes a particular order on this object*/
				public void setOrder(ArrayList<Integer> order) {
					currentOrder = new ArrayList<Integer>();
					currentOrder.addAll(order);
					for(int i: defaultOrder) {
						if(!currentOrder.contains(i))
							currentOrder.add(i);
						}
					}
				/**imposes the given order onto this object*/
				public void setOrder(ChannelPanelReorder chanPanelReorder) {
					setOrder(chanPanelReorder.currentOrder);
				}
			}

			

	
}
