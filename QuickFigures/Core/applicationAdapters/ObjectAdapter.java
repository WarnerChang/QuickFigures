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
package applicationAdapters;

/**this is a class for any sort of object that is 
   within a 2d space*/
public interface ObjectAdapter<ImagePlus, Roi> {
	
	//public ObjectWrapper< Roi> makeWrapper(Roi roi);
	
	/**gets the shape of the object*/
	//public Shape getShape(Roi roi);
	
	/**sets the currently selected object to shape s*/
	//public void select(ImagePlus imp, Shape s, int width);
	
	/**sets the currently selected object*/
	//public void select(ImagePlus imp, Roi newroi, int width) ;
	
	/**returns the bounding box of the object*/
	//public Rectangle getBounds(Roi roi);
	
	/**returns the current selection*/
	//public Rectangle getSelectionBounds(ImagePlus imp) ;
	
	
	/**set the stroke width and color of the object*/
	//public void setSelectionStroke(Roi roi, int width, Color c);
	
	/**sets the currently selected object to none. deletes the selection*/
	//public void deselect(ImagePlus imp);
	
	/**returns the current selection*/
	//public Roi getSelectionObject(ImagePlus imp) ;
	
	
	
	/**removes an objects from the image*/
//	public abstract void takeRoiFromImage(Roi roi, ImagePlus ml) ;
	/**adds an objects to the image*/
	//public abstract void addRoiToImage(Roi roi, ImagePlus ml) ;
	/**adds an objects to the image*/
	//public abstract void addRoiToImageBack(Roi roi, ImagePlus ml) ;
	/**sets the locataion of the roi*/
	//public void setRoiLocation(Roi roi, int x, int y);
	
	
	//public Roi createTextObject(String text, Font font, Color c, int x, int y);
	//public Roi createFilledRectangleObject(Rectangle rect, Color fillColor);
	//public Roi createRectangleObject(Rectangle rect, int i, Color strokeColor);

	

	//void setSelectedObject(ImageWrapper imp, locatedObject lastRoi);
	
}
