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

import java.awt.Font;

/**a java class that generates scripts to create and modify a character attributes object in 
  adobe illustrator*/
public class CharAttributesRef extends IllustratorObjectRef {
	
	String setfont(String font) {
		if (font.equals("SansSerif")) font="SerifBold";
		String output="try{";
		output+=refname+".textFont = app.textFonts.getByName('"+font+"');} catch (err) {}";
		addScript(output);
		return output;
	}
	
	public String setStrikeThrough(boolean strike) {
		String output="try{";
		output+=refname+".strikeThrough = true;} catch (err) {}";
		addScript(output);
		return output;
	}
	
	public String setUnderline(boolean strike) {
		String output="try{";
		output+=refname+".underline = true;} catch (err) {}";
		addScript(output);
		return output;
	}
	
	
	public String setfont(Font f) {
		
			String o=setfont(f.getFontName())+'\n' ;
		return o+ setfontSize(f.getSize());
	}
	

	
	String setfontSize(double font) {
		
		font*=getGenerator().scale;
		if (font==0) return "";
		String output="";
		output+=refname+".size = "+font+";";
		addScript(output);
		return output;
	}
	
}
