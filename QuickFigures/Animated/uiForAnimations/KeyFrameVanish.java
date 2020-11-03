package uiForAnimations;

import animations.KeyFrameCompatible;
import graphicActionToombar.CurrentSetInformerBasic;
import graphicalObjects.ZoomableGraphic;
import utilityClassesForObjects.Hideable;

public class KeyFrameVanish extends KeyFrameAssign{

	
	
	
	public KeyFrameVanish(boolean update) {
		super(update);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getMenuCommand() {
		return "Make Vanishing Key Frame";
	}
	
	/**removes the selected item, period. it the item is mortal,
	   it will call its kill() method which should let some listeners know
	   of its demise*/
	public void actioinOnSelected(ZoomableGraphic selectedItem) {
		
		if (selectedItem instanceof KeyFrameCompatible && selectedItem instanceof Hideable) {
			KeyFrameCompatible  m=(KeyFrameCompatible ) selectedItem;
			((Hideable)selectedItem).setHidden(true);
			int frame = new CurrentSetInformerBasic().getCurrentlyActiveDisplay().getCurrentFrame();
			m.getOrCreateAnimation().recordKeyFrame(frame);
		}
		
		
		
		
	}
	

}
