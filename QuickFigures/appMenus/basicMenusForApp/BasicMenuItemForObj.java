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
package basicMenusForApp;

import javax.swing.Icon;

import applicationAdapters.DisplayedImage;

/**An abstract superclass for menu bar items*/
public abstract class BasicMenuItemForObj implements MenuItemForObj {

	/**Performs a task specific to the menu item. */
	@Override
	public void performActionDisplayedImageWrapper(DisplayedImage diw) {
		
	}

	@Override
	public String getCommand() {
		return getMenuPath()+getNameText();
	}

	

	/**The icon for this menu item*/
	@Override
	public Icon getIcon() {
		return null;
	}
	
	/**The icon for the menu that contains this item*/
	public Icon getSuperMenuIcon() {
		return null;
	}

}
