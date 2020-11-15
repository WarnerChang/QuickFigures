package figureFormat;

import java.io.Serializable;
import java.util.ArrayList;

import logging.IssueLog;
import utilityClassesForObjects.ObjectContainer;

/**Class contains methods for two purposes.
	 1) Identify items that belong to a certain category (which share the same format)
	 2) Apply the traits that are characteristic of that format
	 */
public abstract class ItemPicker<ItemType extends Serializable> implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**the model item that has been selected as an example of the target format*/
	protected ItemType modelItem;
	
	public ItemPicker() {
	}
	
	public ItemPicker(ItemType model) {
		modelItem=model;
	}
	

	/**returns true if the object has the right traits to belong to the category*/
	abstract boolean isDesirableItem(Object object);
	/**returns a String describing what sort of category the picker targets*/
	public abstract String getOptionName() ;
	
	/**Applies the format to the target*/
	public abstract void applyProperties(Object target);
	/**Applies the format to the list*/
	public  void applyPropertiesToList(Iterable<?> list) {
	  for(Object o: list) {
		  applyProperties(o);
	  }
  }
	
	/**Returns a new list containing a subset of the input list.
	  The returned subset contains only objects that 
	  fit the criteria for the category*/
	@SuppressWarnings("unchecked")
	ArrayList<ItemType> getDesiredItemsOnly(ArrayList<?> input) {
		ArrayList<ItemType> output = new ArrayList<ItemType>();
		for(Object ob:input) {
			if (isDesirableItem(ob)) try {
				output.add((ItemType) ob);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return output;
	}
	
	/**Returns an array of objects from the container that match the criteria. */
	public ArrayList<ItemType> getDesiredObjects(ObjectContainer theContainer) {
		return getDesiredItemsOnly(theContainer.getLocatedObjects());
	}
	

	
	public String getKeyName() {
		return getOptionName();
	}


/**the getter method for the model item*/
	public ItemType getModelItem() {
		return modelItem;
	}
	/**the and setter method for the model item. allows the item to be set to null
	  but does not allow anything that fails the isDesirableItem method to be set*/
	public void setModelItem(Object modelItem) {
		if (modelItem==null) {
			this.modelItem=null;
			return;
		}
		if (!isDesirableItem(modelItem)) return;
		try{this.modelItem = (ItemType)modelItem;} catch (Exception e) {
			e.printStackTrace();
			IssueLog.log("problem. wrong class");
		}
	}
	
	

}