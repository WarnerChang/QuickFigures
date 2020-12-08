/*******************************************************************************
 * Copyright (c) 2020 Gregory Mazo
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
package advancedChannelUseGUI;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import channelLabels.ChannelLabelManager;
import channelMerging.ChannelEntry;
import figureEditDialogs.ChannelSliceAndFrameSelectionDialog;
import genericMontageKit.PanelListElement;
import graphicActionToolbar.CurrentFigureSet;
import graphicalObjects_FigureSpecific.MultichannelDisplayLayer;
import graphicalObjects_FigureSpecific.PanelManager;
import iconGraphicalObjects.ChannelUseIcon;
import menuUtil.SmartPopupJMenu;
import standardDialog.StandardDialog;
import standardDialog.channels.ChannelEntryBox;
import standardDialog.choices.ComboBoxPanel;
import undo.CombinedEdit;
import undo.PanelManagerUndo;


/**A gui that show a list of panels*/
public class PanelListDisplayGUI extends JFrame implements ListSelectionListener, ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private PanelListDisplay listPanels;
	private PanelManager pm;
	private ChannelLabelManager cm;
	private ChannelListDisplay listChannels;
	
	JButton addPanelButton=new JButton("+"); 
	
	JButton removePanelButton=new JButton("-"); 
	
	JButton addChannelButton=new JButton("+");
	JButton removeChannelButton=new JButton("-");
	JButton chooseChannelButton=new JButton("Add/Remove Channels");{chooseChannelButton.setIcon(new ChannelUseIcon());}
	
	JButton alterZButton=new JButton("Z"); 
	
	JButton alterTButton=new JButton("T"); 
	JLabel panelListLabel=new JLabel("Panel List");
	JLabel chanListLabel=new JLabel("Channel List");
	
	
		public PanelListDisplayGUI(PanelManager pm, ChannelLabelManager cm) {
			this.cm=cm;
			pm.setChannelUseMode(PanelManager.ADVANCED_CHANNEL_USE);
			pm.getPanelList().setChannelUpdateMode(true);
			GridBagLayout gb = new GridBagLayout();
			GridBagConstraints gc=new GridBagConstraints();
			setLayout(gb);
			gc.gridx=1;
			gc.gridy=1;
			
			boolean multipleChannel=pm.getMultiChannelWrapper().nChannels()>1;
			
			this.pm=pm;
			listPanels=new PanelListDisplay(pm);
			listChannels=new ChannelListDisplay(pm, pm.getPanelList().getMergePanel());
			listChannels.setPanelListPartner(listPanels);
			this.setTitle("Panels for: "+pm.getMultiChannelWrapper().getTitle());
			listPanels.addListSelectionListener(this);
			
			
			gc.gridwidth=4;
			this.add(createScrollPane(listPanels, 300), gc);
			listPanels.addMouseListener(createPanelEditListener());
			this.addWindowListener(listPanels);
			this.addMouseListener(listPanels);
			gc.gridx=6;
			gc.gridy=1;
			if ( multipleChannel)this.add(createScrollPane(listChannels, 250), gc);
			gc.gridwidth=1;
			
			/**
			gc.gridx=6;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			if ( multipleChannel)	this.add(addChannelButton, gc);
			gc.gridx=7;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			if ( multipleChannel)this.add(removeChannelButton, gc);
			*/
			gc.gridx=8;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			if ( multipleChannel)this.add(chooseChannelButton, gc);
			
			
			gc.gridwidth=1;
			
			gc.gridx=8;
			gc.gridy=0;
			if ( multipleChannel)add(chanListLabel, gc);
			
			gc.gridx=1;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			this.add(addPanelButton, gc);
			gc.gridx=2;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			this.add(removePanelButton, gc);
			gc.gridx=3;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			this.add( alterZButton, gc);
			gc.gridx=4;
			gc.gridy=2;
			gc.anchor=GridBagConstraints.WEST;
			this.add( alterTButton, gc);
			
			
			addPanelButton.addActionListener(this);
			addChannelButton.addActionListener(this);
			chooseChannelButton.addActionListener(this);
			alterZButton.addActionListener(this);
			alterTButton.addActionListener(this);
			removePanelButton.addActionListener(this);
			removeChannelButton.addActionListener(this);
			
			
			this.setLocation(400, 300);
			
			gc.gridx=1;
			gc.gridy=0;
			add(panelListLabel, gc);
			
			
			
			
			this.pack();
		}

		public JScrollPane createScrollPane(JList<?> list, int width) {
			JScrollPane jScrollPane = new JScrollPane(list);
			
			jScrollPane.setPreferredSize(new Dimension(width, 150));
			return jScrollPane;
		}
		
		private MouseListener createPanelEditListener() {
			// TODO Auto-generated method stub
			return new MouseListener() {

				@Override
				public void mouseClicked(MouseEvent e) {
					if(e.getClickCount()==2) {
						editPanel();
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}};
		}

		public static void main(String[] arg) {
			
			;
			//ImagePlusWrapper wrap = new ImagePlusWrapper(IJ.openImage());
			//MultichannelImageDisplay multi = new IJ1MultiChannelCreator().creatMultiChannelDisplayFromUserSelectedImage(true, "/Users/mazog/Desktop/Mon Wiz stuff/Control;Model Source Stack.tif");
		//	showMultiChannel(multi);
			
		}
		
		public static void showMultiChannel(MultichannelDisplayLayer multi) {
		
			PanelListDisplayGUI distpla = new PanelListDisplayGUI(multi.getPanelManager(), multi.getChannelLabelManager());
			
			distpla.setVisible(true);
			
		}

		@Override
		public void valueChanged(ListSelectionEvent arg0) {
			int indexsel = listPanels.getSelectedIndex();
			if (indexsel>-1&&indexsel<listPanels.elements.size()) {
				PanelListElement panel= listPanels.elements.get(listPanels.getSelectedIndex());
				listChannels.setPanel(panel);
				}
		}
		
		

		@Override
		public void actionPerformed(ActionEvent arg0) {
			CombinedEdit e2=PanelManagerUndo.createFor(pm);
			if (arg0.getSource()==this.addPanelButton) {
				
				  addPanel();
			}
			
			if (arg0.getSource()==chooseChannelButton) {
				this.displayMenu(arg0);
			}
			
			if (arg0.getSource()==this.addChannelButton) {
				
				{
						ArrayList<ChannelEntry> chans = pm.getMultiChannelWrapper().getChannelEntriesInOrder();
					
						StandardDialog sd = new StandardDialog();
						sd.add("Channel ", new ComboBoxPanel("Chan: ", new ChannelEntryBox(pm.getMultiChannelWrapper().getChannelEntriesInOrder())));
						sd.setModal(true);
						sd.showDialog();
						
						int chan = sd.getChoiceIndex("Channel ");
						
						ChannelEntry newChan = chans .get(chan-1);
						
						listChannels.elements.add(newChan);
						getPrimarySelectedPanel().addChannelEntry(newChan);
			}
				afterChannelAddSubtract();
			}
			
			if (arg0.getSource()==this.removeChannelButton) {
				listChannels.removeSelectedChannels();
			}
			if (arg0.getSource()==this.removePanelButton) {
				listPanels.removeSelectedPanels() ;
			}
			
			if(arg0.getSource()==this.alterTButton) {
				this.editPanelFrame();
			}
			if(arg0.getSource()==this.alterZButton) {
				this.editPanelSlice();
			}
			e2.establishFinalState();
			new CurrentFigureSet().addUndo(e2);
			
		}

		/**
		 * 
		 */
		public void afterChannelAddSubtract() {
			repaint();
			listChannels.updateDisplay();
			listChannels.repaint();
			
			pack();
		}

		/**
		 * @param arg0
		 */
		public void displayMenu(ActionEvent arg0) {
			SmartPopupJMenu ppopme = new SmartPopupJMenu();
			ArrayList<AvailableChannelsItem> items = listChannels.makeentries(listChannels.getPanel());
			for(AvailableChannelsItem item: items) {
				ppopme.add(item);
				item.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						
					afterChannelAddSubtract();
						
					}});
			}
			ppopme.show((Component) arg0.getSource(), 0,0);
		}

		public void addPanel() {
			PanelListElement panel = pm.addSingleChannelPanel(pm.getPanelList());
			  cm.generateChanelLabel(panel);
			
			  listPanels.setList(pm.getPanelList());
			  pm.putSingleElementOntoGrid(panel, true);
			  panel.getChannelLabelDisplay().updateDisplay();
			
			pack();
			
		}
		
		private void editPanel() {
			editPanelSliceAndFrame(getPrimarySelectedPanel());
		}

		public PanelListElement getPrimarySelectedPanel() {
			return listChannels.getPanel();
		}
		private void editPanelSliceAndFrame(PanelListElement panel) {
			ChannelSliceAndFrameSelectionDialog dia = new ChannelSliceAndFrameSelectionDialog(panel.targetChannelNumber,panel.targetSliceNumber, panel.targetFrameNumber,pm.getMultiChannelWrapper());
			dia.show2DimensionDialog();
			for(PanelListElement panel1: getSelectedPanels()) {
				panel1.setFrameNumber(dia.getFrame());
				panel1.setSliceNumber(dia.getSlice());
			}
				
			updatePanelDisplay();
		}
		
		private void editPanelSlice() {
			editPanelSlice(getPrimarySelectedPanel());
		}
		private void editPanelFrame() {
			editPanelFrame(getPrimarySelectedPanel());
		}
		private void editPanelSlice(PanelListElement panel) {
			ChannelSliceAndFrameSelectionDialog dia = new ChannelSliceAndFrameSelectionDialog(panel.targetChannelNumber,panel.targetSliceNumber, panel.targetFrameNumber,pm.getMultiChannelWrapper());
			dia.showSliceDialog();
			for(PanelListElement panel1: getSelectedPanels()) {
				panel1.setSliceNumber(dia.getSlice());
			}
				
			updatePanelDisplay();
		}
		private void editPanelFrame(PanelListElement panel) {
			ChannelSliceAndFrameSelectionDialog dia = new ChannelSliceAndFrameSelectionDialog(panel.targetChannelNumber,panel.targetSliceNumber, panel.targetFrameNumber,pm.getMultiChannelWrapper());
			dia.showFrameDialog();
			for(PanelListElement panel1: getSelectedPanels()) {
				panel1.setFrameNumber(dia.getFrame());
			}
				
			updatePanelDisplay();
		}

		public void updatePanelDisplay() {
			pm.updatePanels();
			pm.updateDisplay();
			pack();
		}
		
		Iterable<PanelListElement> getSelectedPanels() {
			return listPanels.getSelectedValuesList();
		}
		
		@Override
		public
		Dimension  	getPreferredSize() {
			return 	new Dimension(600,400) ;
		}
		
		
}
