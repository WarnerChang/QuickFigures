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
package export.pptx;

import java.io.File;
import java.io.FileInputStream;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xslf.usermodel.XMLSlideShow;
import org.apache.poi.xslf.usermodel.XSLFPictureData;
import org.apache.poi.xslf.usermodel.XSLFPictureShape;
import org.apache.poi.xslf.usermodel.XSLFShapeContainer;

import graphicalObjects_SpecialObjects.ImagePanelGraphic;
import illustratorScripts.PlacedItemRef;
import logging.IssueLog;

/**subclass of OfficeObjectMaker that creates an image panel in powerpoint*/
public class ImagePanelImmitator implements OfficeObjectMaker {
	
	private ImagePanelGraphic imagepanel;

	public ImagePanelImmitator(ImagePanelGraphic ipg) {
		this.imagepanel=ipg;
		
	}

	/**Adds the image panel to the slide*/
	@Override
	public XSLFPictureShape addObjectToSlide(XMLSlideShow ppt, XSLFShapeContainer slide) {
		ImagePanelGraphic ipg=imagepanel;
		try {
		;
		File f = PlacedItemRef.prepareFile(ipg.getProcessedImageForDisplay(), ipg.getName());
		byte[] pictureData;
		
			pictureData = IOUtils.toByteArray(new FileInputStream(f.getAbsolutePath()));
		

        int idx = ppt.addPicture(pictureData, XSLFPictureData.PICTURE_TYPE_PNG);
       
        XSLFPictureShape pic = slide.createPicture(idx);
     
      
        pic.setAnchor(ipg.getBounds());//TODO Check if using bounds2d instead of bounds has an effect
       f.deleteOnExit();
       return pic;
       
		} catch (Exception e) {
			IssueLog.logT(e);
		}
		
		return null;
	}

}
