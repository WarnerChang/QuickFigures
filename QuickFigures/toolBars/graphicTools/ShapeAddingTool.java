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
package graphicTools;

import java.awt.Rectangle;

import javax.swing.Icon;

import graphicalObjects_Shapes.ShapeGraphic;

/**interface for tools that add a shape to a worksheet*/
public interface ShapeAddingTool {

	String getShapeName();
	public Icon getIcon() ;

	/**Crates a shape with the given boundind box*/
	ShapeGraphic createShape(Rectangle rectangle);
	

}
