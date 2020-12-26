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
package popupMenusForComplexObjects;


import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import fLexibleUIKit.ObjectAction;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_Shapes.ShapeGraphic;
import menuUtil.SmartPopupJMenu;
import undo.Edit;
import utilityClassesForObjects.LocatedObject2D;
import menuUtil.PopupMenuSupplier;

public class ShapeGraphicMenu extends SmartPopupJMenu implements 
PopupMenuSupplier  {

	/**
	 * 
	 */
	
	static final String options="Options";//, backGroundShap="Outline Shape";
	
	ShapeGraphic barG;
	public ShapeGraphicMenu(ShapeGraphic textG) {
		super();
		this.barG = textG;
		this.addAllMenuItems(createMenuItems());
	}

	public ArrayList<JMenuItem> createMenuItems() {
		ArrayList<JMenuItem> j=new ArrayList<JMenuItem>();
		j.add( new ObjectAction<ShapeGraphic>(barG) {
			public void actionPerformed(ActionEvent e) {
				item.showOptionsDialog();
			}}.createJMenuItem("Options"));
			
		j.add( new ObjectAction<ShapeGraphic>(barG) {
			public void actionPerformed(ActionEvent e) {
				LocatedObject2D copy = item.copy();
				copy.moveLocation(5, 25);
				performUndoable(
						Edit.addItem(item.getParentLayer(), (ZoomableGraphic) copy)
						);
			}}.createJMenuItem("Duplicate"));
		
		j.add( new ObjectAction<ShapeGraphic>(barG) {
			public void actionPerformed(ActionEvent e) {
				LocatedObject2D copy = item.createPathCopy();
				copy.moveLocation(5, 25);
				performUndoable(
						Edit.addItem(item.getParentLayer(),(ZoomableGraphic) copy)
						);
				
			}}.createJMenuItem("Duplicate Points"));
		
		j.add( new ObjectAction<ShapeGraphic>(barG) {
			public void actionPerformed(ActionEvent e) {
				ZoomableGraphic copy = (ZoomableGraphic)item.createPathCopy();
				GraphicLayer layer = item.getParentLayer();
				
				performUndoable(
						Edit.addItem(layer, copy),
						Edit.swapItemOrder(layer, item, copy),
						Edit.removeItem(layer, item)
						);
			}}.createJMenuItem("Replace With Points"));
		
		return j;
	}
	


	private static final long serialVersionUID = 1L;

	@Override
	public JPopupMenu getJPopup() {
		// TODO Auto-generated method stub
		return this;
	}


	
	
	
	
}
