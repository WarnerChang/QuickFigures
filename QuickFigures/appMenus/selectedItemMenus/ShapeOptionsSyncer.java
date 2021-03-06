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
/**
 * Author: Greg Mazo
 * Date Modified: Jan 6, 2021
 * Version: 2021.1
 */
package selectedItemMenus;

import javax.swing.Icon;

import iconGraphicalObjects.DialogIcon;
import objectDialogs.ShapeGraphicOptionsSwingDialog;

/**shows a dialog for altering the appearence of many shapes*/
public class ShapeOptionsSyncer extends BasicMultiSelectionOperator{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getMenuCommand() {
		return "Set Shape Options";
	}
	

	@Override
	public void run() {
		ShapeGraphicOptionsSwingDialog mt = new ShapeGraphicOptionsSwingDialog(getAllArray(), false);
		if (mt.hasItems())
			mt.showDialog();
	}
	
	public Icon getIcon() {
		return DialogIcon.getIcon();
	}

}
