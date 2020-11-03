package dataTableDialogs;

import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JTable;
import javax.swing.SortOrder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

/**A special row sorter class that leaves the first row alone and
  puts blank rows at the very end*/
public class SmartRowSorter extends TableRowSorter<TableModel> {



	private JTable table;
	HashMap<Integer, String> zeroCells=new HashMap<Integer, String>();
	
	
	public SmartRowSorter(TableModel model, JTable area) {
		super( model);
		this.table=area;
		
	}
	
	public void 	toggleSortOrder(int col){
		super.toggleSortOrder(col);
		
		// order=orderCells.get(col);
		//order=-order;
		//orderCells.put(col, order);
		
	}
	
	public SortOrder getOrderForCol(int col) {
		for(javax.swing.RowSorter.SortKey i:super.getSortKeys()) {
			if (i.getColumn()==col) return i.getSortOrder();
		}
		return null;
	}

	public Comparator<Object> getComparator(int col) {
		return new smartComparator(col, table);
	}
	
	protected void 	fireSortOrderChanged() {
		
	}
	
	


	public class smartComparator implements Comparator<Object> {

		private int col;
		//private Object zeroCell;

		public smartComparator(int col, JTable tab) {
			this.col=col;
			if (zeroCells.get(col)==null) {
				for(int col2=0; col2<tab.getColumnCount(); col2++){
						Object zeroCell = tab.getValueAt(0, col2);
						zeroCells.put(col2, zeroCell+"");
			}
			}
			
			
		}

		@Override
		public int compare(Object arg0, Object arg1) {
			
			if (isEmpty(arg0)&&isEmpty(arg0)) return 0;
			String zeroCell = zeroCells.get(col);
			if (zeroCell!=null) {
				if (arg1.equals(zeroCell) &&!isEmpty(arg0))  {
					return getOrder();
				}
				if (arg0.equals(zeroCell)&&!isEmpty(arg1)) {
					return -getOrder();
				}
			}
			
			if (isEmpty(arg0)){return getOrder();} else
			if (isEmpty(arg1)) return -getOrder();
			
			try {
				double d1 = Double.parseDouble(arg0.toString());
				double d2 = Double.parseDouble(arg1.toString());
				return ((d1>d2)? 1:-1);
			} catch (Throwable t) {
				//t.printStackTrace();
			}
			
			if (arg0.getClass().equals(String.class)
					&&arg1.getClass().equals(String.class)
					) return ( (String)arg0).compareTo((String) arg1);
			return 0;
		}
		
		public int getOrder() {
			SortOrder ord = getOrderForCol(col);
			if (ord==SortOrder.ASCENDING) return 1;
			else return -1;
		}

		public boolean isEmpty(Object arg0) {
			if (arg0.toString().trim().equals("")) return true;
			return arg0.equals("");
		}

	}

	

}