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
 * Date Modified: Jan 4, 2021
 * Version: 2021.1
 */
package locatedObject;

import java.awt.Color;

/**an interface for object that can be filled*/
public interface Fillable {
	
	/**getter and setter methods for the fill color*/
	public void setFillColor(Color c);
	public Color getFillColor();
	boolean isFilled();
	void setFilled(boolean fill);
	public PaintProvider getFillPaintProvider();
	public void setFillPaintProvider(PaintProvider p);
	
	/**returns true if the item can currently be filled*/
	public boolean isFillable();
}
