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

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Rectangle;

import utilityClassesForObjects.LocatedObject2D;

public interface ObjectCreator {

	public LocatedObject2D createTextObject(String label, Color c, Font font,FontMetrics f, int lx, int ly,
			double angle, boolean antialiasedText);
	
	public LocatedObject2D createImageObject(String name, PixelWrapper pix, int x, int y) ;

	public LocatedObject2D createRectangularObject(Rectangle r) ;
	
}
