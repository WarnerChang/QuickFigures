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
import selectedItemMenus.BasicMultiSelectionOperator;
import uiForAnimations.TimeLineOperator;

public class BasicTimeLineOperator extends BasicMultiSelectionOperator implements TimeLineOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected DisplayedImage display;
	protected TimeLineDialog ui;

	@Override
	public String getMenuCommand() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDisplay(DisplayedImage diw) {
		this.display=diw;
		
	}

	@Override
	public void setUI(TimeLineDialog dialog) {
		this.ui=dialog;
		
	}

}
