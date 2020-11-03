package fLexibleUIKit;

import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.undo.UndoableEdit;

import graphicActionToombar.CurrentSetInformerBasic;
import menuUtil.SmartJMenu;
import menuUtil.SmartPopupJMenu;
import menuUtil.MenuSupplier;
import undo.UndoManagerPlus;
import utilityClassesForObjects.RectangleEdges;

/**this will generate a working popup menus from the annotated methods in
  an object.  I wrote it because I wanted to sometimes write the 
  code for popup menus quickly with a little less complexity each time*/
public class MenuItemExecuter implements ActionListener, MenuSupplier {
	private Object o;
	//Class<MenuItemMethod> c=;
	private HashMap<MenuItemMethod, Method> map=new HashMap<MenuItemMethod, Method>();
	private HashMap<String, Method> mapComms=new HashMap<String, Method>();
	private MenuSupplier partner=null;
//	private ArrayList<Method> methodsList=new ArrayList<Method> ();
	private SmartPopupJMenu popupMenu;
	private UndoManagerPlus undoManager;
	
	public MenuItemExecuter(Object o) {
		this.o=o;
		innitiallizeMap();
	}

	public ArrayList<MenuItem> findItems() {
		//this.o=o;
		
		ArrayList<MenuItem> output = new ArrayList<MenuItem>();
		for(MenuItemMethod k: map.keySet()) {
			output.add(generateMenuItemFrom(k, map.get(k)));
		}	return output;
	}
	
	public ArrayList<JMenuItem> findJItems() {
		//this.o=o;
		Set<MenuItemMethod> keySet = map.keySet();
		
		/**need a sorted list*/
		ArrayList<MenuItemMethod> allKeys= new ArrayList<MenuItemMethod>();
		allKeys.addAll(keySet);
		
		Collections.sort(allKeys, new Comparator<MenuItemMethod>() {

			@Override
			public int compare(MenuItemMethod arg0, MenuItemMethod arg1) {
				return arg0.orderRank()-arg1.orderRank();
			}});
		
		
		HashMap<String, JMenu> submenus=new HashMap<String, JMenu>();
		ArrayList<JMenuItem> output = new ArrayList<JMenuItem>();
		for(MenuItemMethod k: allKeys ) {
			JMenuItem item = generateJMenuItemFrom(k, map.get(k));
			
			
			if (!k.permissionMethod().equals("")) try{
				Method pMethod = o.getClass().getMethod(k.permissionMethod());
				if (pMethod!=null) {
					Object b = pMethod.invoke(o);
				/**will not include this menu item in the list if the permission method is null or false*/
					if (b==null||b.toString().equals("false")) continue;
					
					
				}
				
			}catch (Throwable t) {t.printStackTrace();}
			
			
			
			if (k.subMenuName().equals(""))
				output.add(item);
			else {
				
				String submenuName = k.subMenuName();
				if (submenuName.contains("<")) submenuName=submenuName.split("<")[0];//if divideed, we need to put the first submenu into the hashmap
		
				JMenu submen = submenus.get(submenuName);
				if (submen==null) {
						{
								submen=new JMenu(submenuName);
								submenus.put(submenuName, submen);
								output.add(submen);
						}
				}
				
				if (k.subMenuName().contains("<"))
						submen=SmartJMenu. getSubmenuFromPath(submen, k.subMenuName().split("<"), 1);
				
				submen.add(item);
			}
		}	
		
		if(this.getPartner()!=null) {
			output.addAll(getPartner().findJItems());
		}
		
		return output;
	}
	
	public JPopupMenu getJPopup() {
		SmartPopupJMenu p=new SmartPopupJMenu();
		ArrayList<JMenuItem> arr = findJItems();
		for(JMenuItem a:arr) {
			p.add(a);
			}
		this.popupMenu=p;
		return p;
	}
	
	public JMenu getJMenu() {
		JMenu p=new SmartJMenu("");
		ArrayList<JMenuItem> arr = findJItems();
		for(JMenuItem a:arr) {p.add(a);}
		
		return p;
	}
	
	
	private void innitiallizeMap() {
		map.clear();
		mapComms.clear();
		if(o==null) return;
		Method[] methods = o.getClass().getMethods();
		for(Method m:methods) {
		MenuItemMethod anns = m.getAnnotation(MenuItemMethod.class);
	//	IssueLog.log("checking method "+m.getName());
		if(anns==null ) continue;
	//	IssueLog.log("found menu annotation in  "+m.getName());
			map.put(anns, m);
			mapComms.put(anns.menuActionCommand(), m);
		}
	}
	
	public MenuItem generateMenuItemFrom(MenuItemMethod anns, Method m) {
		MenuItem mi = new MenuItem(anns.menuText());
		mi.addActionListener(this);
		mi.setActionCommand(anns.menuActionCommand());
		return mi;
	}
	public JMenuItem generateJMenuItemFrom(MenuItemMethod anns, Method m) {
		JMenuItem mi = new JMenuItem(anns.menuText());
		mi.addActionListener(this);
		mi.setActionCommand(anns.menuActionCommand());
		return mi;
	}
	
	

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Method m = mapComms.get(arg0.getActionCommand());
		try {
			
			Object item = m.invoke(o, new Object[] {});
			if (getUndoManager()!=null &&item instanceof UndoableEdit) {
				getUndoManager().addEdit((UndoableEdit) item);
			}
			
			new CurrentSetInformerBasic().getCurrentlyActiveDisplay().updateDisplay();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@MenuItemMethod(menuActionCommand = RectangleEdges.RIGHT_SIDE_BOTTOM+"", menuText = "")
	void go() {}



	public MenuSupplier getPartner() {
		return partner;
	}

	public void setPartner(MenuSupplier partner) {
		this.partner = partner;
	}
	
	public UndoManagerPlus getUndoManager() {
		if (undoManager==null & popupMenu!=null && this.popupMenu.getUndoManager()!=null) {
			this.undoManager=popupMenu.getUndoManager();
		}
		return undoManager;
	}
}
