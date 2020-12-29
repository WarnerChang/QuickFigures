/*******************************************************************************
 * Copyright (c) 2020 Gregory Mazo
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
package imageDisplayApp;

import layout.RetrievableOption;

/**stores a value that indicates whether to allow automatic resizing of
  the canvas of worksheets to fit the objects inside*/
public class CanvasOptions {
	
	public static CanvasOptions current=new CanvasOptions();
	
	@RetrievableOption(key = "Resize After Layout Edit", label="Automatically Enlarge Canvas To Fit layouts?")
	public boolean resizeCanvasAfterEdit=true;

}
