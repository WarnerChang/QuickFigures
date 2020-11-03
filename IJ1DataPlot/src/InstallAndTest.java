


import java.io.File;
import java.io.IOException;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import applicationAdapters.ToolbarTester;
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
import imageDisplayApp.ImageAndDisplaySet;
import includedToolbars.ObjectToolset1;
import includedToolbars.QuickFiguresToolBar;
import includedToolbars.ToolInstallers;
import logging.IssueLog;
import plotTools.ColumnSwapTool;
import plotTools.TTestTool;
import selectedItemMenus.SelectionOperationsMenu;

public class InstallAndTest  implements MenuBarItemInstaller, ToolInstallers{

	static boolean alreadyInstalled=false;
	
	public static void main(String[] args) {
		IssueLog.sytemprint=true;
	
		//ObjectToolset1.includeBonusTool(new GamePlayer());
		install();
		ImageAndDisplaySet set = ToolbarTester.showExample(true);
		//new ExcelFileToXYPlot(2).performActionDisplayedImageWrapper(set);
		try {
			new ExcelFileToComplexCategoryPlot(1).createPlotFromFileExcelFile(null,
					new File("C:/Users/Greg Mazo/Documents/FigureWizold/ExampleGrouped.xlsx"));
		} catch (InvalidFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void install() {
		if (alreadyInstalled) return;
		InstallAndTest freeRun = new  InstallAndTest();
		MenuBarForApp.addMenuBarItemInstaller(freeRun);
		ObjectToolset1.includeBonusTool(new InstallAndTest());
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(0));
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(1));
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(2));
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(3));
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(4));
		SelectionOperationsMenu.addNewOperator(new DataShapeSyncer(5));
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
		
		
		installer.installItem(new ExcelFileToBarPlot(0));
		installer.installItem(new ExcelFileToBarPlot(1));
		installer.installItem(new ExcelFileToBarPlot(2));
		installer.installItem(new ExcelFileToBarPlot(3));
		installer.installItem(new ExcelFileToBarPlot(4));
		installer.installItem(new ExcelFileToXYPlot(0));
		installer.installItem(new ExcelFileToXYPlot(1));
		installer.installItem(new ExcelFileToXYPlot(2));
		installer.installItem(new ExcelFileToComplexCategoryPlot(0));
		installer.installItem(new ExcelFileToComplexCategoryPlot(1));
		installer.installItem(new ExcelFileToComplexCategoryPlot(2));
		installer.installItem(new ExcelFileToComplexCategoryPlot(3));
		installer.installItem(new ExcelFileToKaplanPlot(0));
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