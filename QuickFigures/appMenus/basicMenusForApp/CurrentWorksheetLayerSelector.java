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

import java.io.Serializable;
import java.util.ArrayList;

import applicationAdapters.ImageWorkSheet;
import graphicActionToolbar.CurrentFigureSet;
import graphicalObjects.FigureDisplayWorksheet;
import graphicalObjects.ZoomableGraphic;
import graphicalObjects_LayerTypes.GraphicLayer;
import graphicalObjects_LayerTypes.LayerStructureChangeListener;
import selectedItemMenus.LayerSelectionSystem;
import utilityClasses1.ArraySorter;

/**A layer selector that returns the selected items in whatever worksheet is the currently active one*/
public class CurrentWorksheetLayerSelector  implements LayerSelectionSystem , Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**returns the selected layer*/
	@Override
	public GraphicLayer getSelectedLayer() {
		LayerStructureChangeListener<ZoomableGraphic, GraphicLayer> tree = getWorksheet().getTopLevelLayer().getTree();
		if (tree!=null)
		return getWorksheet().getTopLevelLayer().getTree().getSelectedLayer();
		return getWorksheet().getTopLevelLayer();
	}

	/**returns the selected items*/
	@Override
	public ArrayList<ZoomableGraphic> getSelecteditems() {
		FigureDisplayWorksheet contain = getWorksheet();
		if (contain==null) return new ArrayList<ZoomableGraphic>();
		ArrayList<ZoomableGraphic> all = contain.getTopLevelLayer().getAllGraphics();
		ArraySorter.removeNonSelectionItems(all);
		return all;
	}

	/**returns the worksheet*/
	@Override
	public FigureDisplayWorksheet getWorksheet() {
		return new CurrentFigureSet().getCurrentlyActiveOne();
	}

	@Override
	public ImageWorkSheet getImageWrapper() {
		return getWorksheet().getAsWrapper();
	}
	


}
