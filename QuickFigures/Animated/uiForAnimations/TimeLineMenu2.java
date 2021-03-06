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
package uiForAnimations;

import applicationAdapters.DisplayedImage;
import selectedItemMenus.LayerSelectionSystem;

public class TimeLineMenu2 extends TimeLineMenu {

	public TimeLineMenu2(DisplayedImage diw, LayerSelectionSystem ls, TimeLineDialog dialog) {
		super(diw, ls, dialog);
		// TODO Auto-generated constructor stub
	}
	
	void setup() {
		this.setText("Time Line"); this.setName("Time Line");
		this.addOperation(new TimeLineRange(0));
		this.addOperation(new PlayTimeLine(0));
}


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

}
