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
 * Date Modified: Jan 4, 2021
 * Version: 2021.1
 */
package locatedObject;

/**Interface for any object that may be selected*/
public interface Selectable {
	
	/**Sets to selected*/
	public void select();
	/**Sets to unselected*/
	public void deselect();
	
	/**returns true if selected*/
	public boolean isSelected();

	/**Sets whether this object not only the selected object
	 * the object that is in focus (amoung other selected objects)*/
	boolean makePrimarySelectedItem(boolean isFirst);
}
