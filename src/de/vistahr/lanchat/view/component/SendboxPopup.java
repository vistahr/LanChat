/**
 * 
 * 	Copyright 2012 Vince. All rights reserved.
 * 	
 * 	Redistribution and use in source and binary forms, with or without modification, are
 * 	permitted provided that the following conditions are met:
 * 	
 * 	   1. Redistributions of source code must retain the above copyright notice, this list of
 * 	      conditions and the following disclaimer.
 * 	
 * 	   2. Redistributions in binary form must reproduce the above copyright notice, this list
 * 	      of conditions and the following disclaimer in the documentation and/or other materials
 * 	      provided with the distribution.
 * 	
 * 	THIS SOFTWARE IS PROVIDED BY Vince ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * 	WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * 	FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL Vince OR
 * 	CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * 	CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * 	SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.lanchat.view.component;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import de.vistahr.lanchat.util.PropertiesUtil;
import de.vistahr.lanchat.util.SmileyUtil;
import de.vistahr.lanchat.util.SmileysEnum;
import de.vistahr.lanchat.view.listener.SendboxPopupSmileyClickedListener;

public class SendboxPopup extends JPopupMenu {

	private static final long serialVersionUID = 2878641957422140438L;

	public SendboxPopup(RootView v) {
		// get all smileys and add every element into an JItem
		SmileysEnum[] smileys = SmileysEnum.values();
		
		for(SmileysEnum s: smileys) {
			JMenuItem smileyItem = null;
			String smileyKey = s.toString();
			
			if(!smileyKey.equals("")) {
				URL smileyResource = SmileyUtil.class.getResource(PropertiesUtil.getSmileyPathByKey(smileyKey));
				if(smileyResource != null) {
					smileyItem = new JMenuItem(s.getSmiley(), new ImageIcon(smileyResource));
					smileyItem.addActionListener(new SendboxPopupSmileyClickedListener(null, v));
					add(smileyItem);
				}
					
			}
		}
		
	}
	
}
