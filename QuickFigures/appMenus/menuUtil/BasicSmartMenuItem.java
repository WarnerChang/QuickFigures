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
package menuUtil;

import java.awt.Color;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import applicationAdapters.CanvasMouseEvent;
import icons.EmptyIcon;
import undo.UndoManagerPlus;

/**A special JMenu item that also stores an undo manager*/
public class BasicSmartMenuItem extends JMenuItem implements  SmartMenuItem{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected CanvasMouseEvent me;
	protected UndoManagerPlus undoManager;
	
	public BasicSmartMenuItem() {
		super();
	}
	
	public BasicSmartMenuItem(String st) {
		this(st, new EmptyIcon());
	}
	
	public BasicSmartMenuItem(String st, Icon icon) {
		super(st);
		this.setIcon(icon);
	}

	@Override
	public void setLastMouseEvent(CanvasMouseEvent e) {
		this.me=e;
		
	}

	@Override
	public void setUndoManager(UndoManagerPlus undoManager) {
		this.undoManager=undoManager;
		
	}

	@Override
	public UndoManagerPlus getUndoManager() {
		return undoManager;
	}
	
	/**sets whether the menu item is greyed out*/
	public void setGreyOut(boolean grey) {
		if(grey) {
			this.setForeground(Color.lightGray);
		}
		else {
			this.setForeground(Color.black);
		}
	}

}
