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
package genericMontageLayoutToolKit;
import java.util.ArrayList;

import javax.swing.JMenuItem;

import genericMontageLayoutToolKit.GeneralLayoutEditorTool;
import gridLayout.BasicMontageLayout;
import gridLayout.LayoutSpaces;
import standardDialog.StandardDialog;
import standardDialog.choices.ChoiceInputPanel;


public class LabelSpaceAdjusterTool  extends GeneralLayoutEditorTool implements LayoutSpaces{

	int mode=0;
	
	public void performDragEdit(boolean shift) {
		BasicMontageLayout layout = getCurrentLayout();
		if (mode==0) {
			if (shift) {
				
				 getEditor().addRightLabelSpace(layout, getMouseDisplacementX());
				 getEditor().addBottomLabelSpace(layout, getMouseDisplacementY());
				layout.resetPtsPanels();
				
				
			} else {
			 getEditor().addLeftLabelSpace(layout, -getMouseDisplacementX());
			 getEditor().addTopLabelSpace(layout, -getMouseDisplacementY());
			 layout.resetPtsPanels();
			
			}
			}
		
		if (mode==1) {
		
				if (shift) {
					 getEditor().addRightSpecialSpace(layout, getMouseDisplacementX());
					 getEditor().addBottomSpecialSpace(layout, getMouseDisplacementY());
				} else {
					 getEditor().addLeftSpecialSpace(layout, getMouseDisplacementX());
					 getEditor().addTopSpecialSpace(layout, getMouseDisplacementY());
				}
		
			
		
	}
		
		layout.setMontageProperties();
	}
	
	
	@Override
	public void showOptionsDialog() {
		StandardDialog gd = new StandardDialog(getClass().getName().replace("_",
		" "), true);

String[] option2 = new String[] {
		"Label Space Adjuster (Top/Left), (shift Bottom/Right) ",
		"Montage Canvas Size Adjuster (Top/Left, shift for Bottom/Right)" };
gd.add("Adjust ",new ChoiceInputPanel("Adjust ", option2, mode));

gd.showDialog();

if (gd.wasOKed()) {
	mode = gd.getChoiceIndex("Adjust ");
	
}

	}
	
	{createIconSet("icons/NonMontageSpaceEditorTool.jpg",
			"icons/NonMontageSpaceEditorToolPressed.jpg",
			"icons/NonMontageSpaceEditorToolRollOver.jpg"
			);}
	
	public ArrayList<JMenuItem> getPopupMenuItems() {
		ArrayList<JMenuItem> output = new ArrayList<JMenuItem>();
	
		 
		return output;
		//return new MontageEditCommandMenu(this.getCurrentLayout()).getNonMontageSpacePopupMenuItemsForMainLayout();
	}
	
	@Override
	public String getToolTip() {
		if (mode==0)  {return "Adjust Label Spaces (try holding shift)";}
			return "Adjust Montage Layout Position";
		}
	
	public String getToolName() {return getToolTip();}
	
}