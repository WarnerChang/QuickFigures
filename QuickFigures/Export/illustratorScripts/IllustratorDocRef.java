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
package illustratorScripts;

import java.awt.Dimension;

/**a java class that generates scripts to create and modify a document in 
adobe illustrator*/
public class IllustratorDocRef extends IllustratorObjectRef {

	
		public String createDocumentScript(boolean RGB, double width, double height) {
			String colorspace="DocumentColorSpace.CMYK";
			if (RGB)colorspace="DocumentColorSpace.RGB";
			set=true;
			width *=getGenerator().scale;
			height*=getGenerator().scale;
			String output="var "+refname+" = app.documents.add("+colorspace+","+width+","+height+");";
			addScript(output);
			return output;
		
}
		
		public String createDocumentScript(boolean RGB, Dimension d) {
			return createDocumentScript(RGB, d.width, d.height);
		}
		
		
		
	public 	String setReftoActiveDocument() {
			set=true;
			String output=getAssignment()+" app.activeDocument;";
			addScript(output);
			return output;
		}
		
		String setToOpen(String path) {
			set=true;
			String output= getAssignment()+"app.open('"+path+"');";
			
			addScript(output);
			 return output;
		}
		String setToPSFile(String path) {
			set=true;
			String output="var fileRef = File( '"+path+"'); " +'\n'+
			"if (fileRef != null) {var "+refname+" = open(fileRef, DocumentColorSpace.RGB);}";
			addScript(output);
			 return output;
		}
		
		public RasterEffectOpsRef getRasterEffectOps() {
			RasterEffectOpsRef out=new RasterEffectOpsRef();
			out.setToDocument(this);
			return out;
		}
		
		
		

}