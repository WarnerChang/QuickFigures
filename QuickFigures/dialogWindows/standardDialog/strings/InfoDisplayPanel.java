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
package standardDialog.strings;

import java.awt.Rectangle;

import javax.swing.JLabel;

/**A special string panel that simply displays JLabel with information int than 
  a text field for user input*/
public class InfoDisplayPanel extends  StringInputPanel{

	private JLabel content=new JLabel();
	
	
	
	public InfoDisplayPanel(String labeln, String contend) {
		super(labeln, contend);
		setContentText(contend);
	}
	
	
	public InfoDisplayPanel(String labeln, Rectangle contend) {
		super(labeln, contend.toString());
		setToDimension(contend);
	}
	
	
	
	public void setContentText(String contend) {
		getTextField().setText(contend);
	}

	
	private static final long serialVersionUID = 1L;


	
	protected JLabel getTextField() {
		if (content==null) {
			content=new JLabel();
		}
		return content;
	}


}
