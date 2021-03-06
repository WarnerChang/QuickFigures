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
package handles.miniToolbars;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import actionToolbarItems.EditManyObjects;
import actionToolbarItems.SetAngle;
import graphicalObjects_Shapes.ArrowGraphic;
import graphicalObjects_Shapes.PathGraphic;
import graphicalObjects_Shapes.ShapeGraphic;
import selectedItemMenus.MultiSelectionOperator;

/**A set of smart handles that acts as a mini toolbar for shapes*/
public class ShapeActionButtonHandleList2 extends ActionButtonHandleList {

	private Color[] standardColor=new Color[] { Color.white, Color.black,Color.blue, Color.green, Color.red,  Color.cyan, Color.magenta, Color.yellow , new Color(0,0,0,0)};
	
	{maxGrid=12;
	numHandleID=80000;//large number selected so that id numbers do not conflict with paths
	}

	private ShapeGraphic shape;
	public static final int ARROW_ONLYFORM=4, NORMAL_FORM=0;
	private int specialForm=NORMAL_FORM;


	public ShapeActionButtonHandleList2(ShapeGraphic t) {
		this.shape=t;
		addItems();
		updateLocation();
	}
	
	public ShapeActionButtonHandleList2(ShapeGraphic t, int form) {
		this.shape=t;
		this.specialForm=form;
		if(specialForm==ARROW_ONLYFORM) {
			this.createForArrowHeadType();
		} else
			addItems();
		updateLocation();
	}

	public void updateLocation() {
		ShapeGraphic t=shape;
		Rectangle bounds = t.getOutline().getBounds();
		super.setLocation(new Point2D.Double(bounds.getX()+5, bounds.getMaxY()+40));
	
	}
	public void updateHandleLocations(double magnify) {
		 
		super.updateHandleLocations(magnify);
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void addItems() {
		
		EditManyObjects itemForIcon=null;
	
		if (shape.isFillable()){
			itemForIcon = new EditManyObjects(false, shape.getFillColor());
			itemForIcon.setModelItem(shape);
	
			GeneralActionListHandle hf = addOperationList(itemForIcon, fillColorActs());
			hf.setAlternativePopup(new ColoringButton(itemForIcon, 78341));
		
		}
		
		
		itemForIcon = new EditManyObjects(true, shape.getStrokeColor());
		itemForIcon.setModelItem(shape);
		
		GeneralActionListHandle h = addOperationList(itemForIcon, strokeColorActs());
		h.setAlternativePopup(new ColoringButton(itemForIcon, 782417));
		
		
		
		itemForIcon=new EditManyObjects(true, 2);
		itemForIcon.setModelItem(shape);
		
		addOperationList(itemForIcon, getStrokes() );
		
		
		
		
			itemForIcon=new EditManyObjects(BasicStroke.JOIN_BEVEL, null);
			itemForIcon.setModelItem(shape);
			GeneralActionListHandle cj =new GeneralActionHandleJoiner(itemForIcon, this.numHandleID,getJions()) ;
			super. addOperationList(itemForIcon, cj );
			cj.setxShift(cj.getxShift() + 4);
			cj.setyShift(cj.getyShift() + 6);
		
		
		itemForIcon=new EditManyObjects(null, BasicStroke.CAP_BUTT);
		itemForIcon.setModelItem(shape);
		 addOperationList(itemForIcon, getCaps() );
		 
		 itemForIcon=new EditManyObjects(true, new float[] {2,2});
			itemForIcon.setModelItem(shape); itemForIcon.alwaysDashIcon=true;
			addOperationList(itemForIcon, getDashes() );
		 
		if(shape instanceof ArrowGraphic) {
			createArrowButtons();
		 
				 } else 
		if(shape instanceof PathGraphic) {
			 itemForIcon=new EditManyObjects((PathGraphic) shape, shape.isClosedShape());
			 itemForIcon.setModelItem(shape);
			 addOperationList(itemForIcon,new EditManyObjects[] {new EditManyObjects(null, true), new EditManyObjects(null, false)} );
				 
					 }
		else {
			addSetAngleButton();
		}

		setLocation(location);
	}

	/**
	adds a set angle button to the list
	 */
	private void addSetAngleButton() {
		SetAngle itemForIcon2 = new SetAngle(45);
		GeneralActionListHandle h4 = addOperationList(itemForIcon2,SetAngle.createManyAngles() );
			h4.itemForInputPanel=new SetAngle(shape);
	}



	/**
	 returns the stroke colors options
	 */
	public EditManyObjects[] strokeColorActs() {
		return EditManyObjects.getForColors(true, standardColor);
	}

	/**
	returns the fill colors options
	 */
	public EditManyObjects[] fillColorActs() {
		return EditManyObjects.getForColors(false, standardColor);
	}
	
	/**
	 generates all of the handles for the arrows
	 */
	public void createArrowButtons() {
		EditManyObjects itemForIcon;
		itemForIcon=new EditManyObjects((ArrowGraphic) shape, 1, 0, 1);
		 itemForIcon.setModelItem(shape);
		 addOperationList(itemForIcon,EditManyObjects. createOptionsforNumberOfArrowHeads() );
		 createForArrowHeadType();
	}

	/**creates the arrow head style action handles*/
	protected void createForArrowHeadType() {
		EditManyObjects itemForIcon;
		
		 itemForIcon=new EditManyObjects((ArrowGraphic) shape, 1, 2, ArrowGraphic.SECOND_HEAD);
		 itemForIcon.setModelItem(shape);
		  GeneralArrowHeadButton h2 = new GeneralArrowHeadButton(itemForIcon, numHandleID,EditManyObjects.createForArrow2(ArrowGraphic.SECOND_HEAD),ArrowGraphic.SECOND_HEAD);
		h2.usePalete=true;
		  super. addOperationList(itemForIcon, h2 );
		
		itemForIcon=new EditManyObjects((ArrowGraphic) shape, 1, 2, ArrowGraphic.FIRST_HEAD);
		 itemForIcon.setModelItem(shape);
		 
		 GeneralActionListHandle h = new GeneralArrowHeadButton(itemForIcon, numHandleID,EditManyObjects.createForArrow2(ArrowGraphic.FIRST_HEAD),ArrowGraphic.FIRST_HEAD);
		 h.usePalete=true;
		 super. addOperationList(itemForIcon, h );
		 
		
	}

	/**An action handle that is only visible if the given arrow head is visible*/
	class GeneralArrowHeadButton extends GeneralActionListHandle{

		private int hNumber;
		public GeneralArrowHeadButton(MultiSelectionOperator i, int num, MultiSelectionOperator[] items, int headNumber) {
			super(i, num, items);
			hNumber=headNumber;
		}
		@Override
		public boolean isHidden() {
			if (shape instanceof ArrowGraphic) {
					ArrowGraphic arrowGraphic = (ArrowGraphic) shape;
					if (hNumber==ArrowGraphic.SECOND_HEAD && arrowGraphic.headsAreSame()) { return true;}
					if (arrowGraphic.getNHeads()<hNumber) return true;
				
			}
			
			return false;
		}
		private static final long serialVersionUID = 1L;}

	/**A handle that is only visible if the given shape has joints. Circles and strait lies should not
	  have joints. */
	class GeneralActionHandleJoiner extends GeneralActionListHandle {

		public GeneralActionHandleJoiner(MultiSelectionOperator i, int num, MultiSelectionOperator[] items) {
			super(i, num, items);
		}
		@Override
		public boolean isHidden() {
			return !shape.doesJoins();
		}
		private static final long serialVersionUID = 1L;}


	
	public static EditManyObjects[] getDashes() {
		return new EditManyObjects[] {
				new EditManyObjects(true, new float[] {2,2}),
				new EditManyObjects(true, new float[] {}),
				
				new EditManyObjects(true, new float[] {4,4}),
				new EditManyObjects(true, new float[] {8,8}),
			
				new EditManyObjects(true, new float[] {8,16}),
				new EditManyObjects(true, new float[] {12,24})
				,new EditManyObjects(true, new float[] {4,4,8,4})
			
	};
	}
	
	public static EditManyObjects[] getStrokes() {
		return new EditManyObjects[] {
				new EditManyObjects(true, (float) 0.5),
				new EditManyObjects(true, 1),
				new EditManyObjects(true, 2),
				new EditManyObjects(true, 4),
				new EditManyObjects(true, 8),
				new EditManyObjects(true, 16),
				new EditManyObjects(true, 30),
			};
	}
	

	public static EditManyObjects[] getJions() {
		return new EditManyObjects[] {
				new EditManyObjects(BasicStroke.JOIN_BEVEL, null),
				new EditManyObjects(BasicStroke.JOIN_MITER, null),
				new EditManyObjects(BasicStroke.JOIN_ROUND, null)
				
				};
	}
	
	public static EditManyObjects[] getCaps() {
		return new EditManyObjects[] {
				new EditManyObjects(null, BasicStroke.CAP_BUTT),
				new EditManyObjects(null, BasicStroke.CAP_ROUND),
				new EditManyObjects(null, BasicStroke.CAP_SQUARE)
				
				};
	}
	

}
