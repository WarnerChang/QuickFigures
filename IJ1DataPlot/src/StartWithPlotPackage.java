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



import appContextforIJ1.ImageDisplayTester;
import applicationAdapters.StartApplication;
import basicMenusForApp.MenuBarForApp;
import basicMenusForApp.MenuBarItemInstaller;
import dialogs.DataShapeSyncer;
import fileread.ExampleShower;
import fileread.ExcelFileToBarPlot;
import fileread.ExcelFileToComplexCategoryPlot;
import fileread.ExcelFileToKaplanPlot;
import fileread.ExcelFileToXYPlot;
import fileread.ExcelRowToJTable;
import fileread.ShowTable;
import imageDisplayApp.ImageWindowAndDisplaySet;
import includedToolbars.ObjectToolset1;
import includedToolbars.QuickFiguresToolBar;
import includedToolbars.ToolInstallers;
import logging.IssueLog;
import plotCreation.ColumnPlotCreator;
import plotCreation.XYPlotCreator;
import plotCreation.XYPlotCreator.xyPlotForm;
import plotTools.ColumnSwapTool;
import plotTools.TTestTool;
import selectedItemMenus.SelectionOperationsMenu;

public class StartWithPlotPackage extends StartApplication implements MenuBarItemInstaller, ToolInstallers{

		static boolean alreadyInstalled=false;
	
	public static void main(String[] args) {
		IssueLog.sytemprint=false;
		IssueLog.windowPrint=true;
		install();
		startToolbars(true);
		ImageDisplayTester.setupImageJ();
		 ImageWindowAndDisplaySet.createAndShowNew("Figure", 400,300);
		
	}
	
	public static void install() {
		if (alreadyInstalled) return;
		StartWithPlotPackage freeRun = new  StartWithPlotPackage();
		MenuBarForApp.addMenuBarItemInstaller(freeRun);
		ObjectToolset1.includeBonusTool(new StartWithPlotPackage());
		
		for(int i=0; i<=5; i++)SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(i));
	
		alreadyInstalled=true;
		
	}

	@Override
	public void addToMenuBar(MenuBarForApp installer) {
		try {
			installer.installItem(new ExampleShower(0, true));
			installer.installItem(new ExampleShower(1, true));
			installer.installItem(new ExampleShower(2, true));
		installer.installItem(new ExampleShower(0, false));
		installer.installItem(new ExampleShower(1, false));
		installer.installItem(new ExampleShower(2, false));
		
		installer.installItem(new ExampleShower(4, true));
		installer.installItem(new ExampleShower(4, false));
		
		
		installer.installItem(new ExcelFileToBarPlot(ColumnPlotCreator.BAR_AND_SCATTER));
		installer.installItem(new ExcelFileToBarPlot(ColumnPlotCreator.STANDARD_BAR_PLOT));
		installer.installItem(new ExcelFileToBarPlot(ColumnPlotCreator.SCATTER_POINTS));
		installer.installItem(new ExcelFileToBarPlot(ColumnPlotCreator.BOX_PLOT));
		installer.installItem(new ExcelFileToBarPlot(ColumnPlotCreator.TUKEY_BOX_PLOT));
		for(xyPlotForm form:XYPlotCreator.xyPlotForm.values()) installer.installItem(new ExcelFileToXYPlot(form));
		
		installer.installItem(new ExcelFileToComplexCategoryPlot(0));
		installer.installItem(new ExcelFileToComplexCategoryPlot(1));
		installer.installItem(new ExcelFileToComplexCategoryPlot(2));
		installer.installItem(new ExcelFileToComplexCategoryPlot(3));
		installer.installItem(new ExcelFileToKaplanPlot());
		installer.installItem(new ExcelRowToJTable());
		installer.installItem(new ShowTable(1));
		installer.installItem(new ShowTable(9));
		
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	@Override
	public void installTools(QuickFiguresToolBar toolset) {
		try {toolset.addToolBit(new TTestTool());} catch (Throwable t) {
			t.printStackTrace();
		}
		toolset.addToolBit(new ColumnSwapTool());
	}
	


}
