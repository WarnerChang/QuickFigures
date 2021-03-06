import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import applicationAdapters.DisplayedImage;
import graphicalObjects.BasicCoordinateConverter;
import graphicalObjects.ZoomableGraphic;

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
 
 * 
 */

/**
 
 * 
 */
public class SplashMessage {


private int duration;
private ZoomableGraphic splashItem;
private long startSplash;
private Timer timer;
/**displays an item for a specified period of time*/
public void splashGraphic(ZoomableGraphic z, int time, DisplayedImage d) {
	this.duration=time;
	this.splashItem=z;
	startSplash=System.currentTimeMillis();
	
	timer = new Timer(time+1, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent e) {
			splashItem=null;
		d.updateDisplay();
			 timer.stop();//stops the timer when the animation is done
		}});
		
		timer.start();
}

public void drawSplash(Graphics2D g) {
	if (System.currentTimeMillis()-startSplash>duration)
		return;
	if(splashItem!=null)  {splashItem.draw(g, new BasicCoordinateConverter());}
}
}
