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
package de.vistahr.lanchat.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;

import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.util.PropertiesUtil;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.util.JLoggerUtil;

public class MuteListener extends AbstractListener implements ActionListener {

	public MuteListener(RootViewModel m, RootView v) {
		super(m, v);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(!model.isMute()) {
			try {
				model.setMute(true);
				view.getMutebutton().setIcon(new ImageIcon(PropertiesUtil.getLanchatPropertyImage("ICON_MUTE")));
				
			} catch(NullPointerException ex) {
				JLoggerUtil.getLogger().warn("Cannot load resource " + PropertiesUtil.getLanchatPropertyString("ICON_MUTE"));
			}
			
		} else {
			try {
				model.setMute(false);
				view.getMutebutton().setIcon(new ImageIcon(PropertiesUtil.getLanchatPropertyImage("ICON_UNMUTE")));
				
			} catch(NullPointerException ex) {
				JLoggerUtil.getLogger().warn("Cannot load resource " + PropertiesUtil.getLanchatPropertyString("ICON_UNMUTE"));
			}
		}
	}

}
