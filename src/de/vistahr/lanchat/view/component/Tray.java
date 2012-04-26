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

import java.awt.Image;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.util.MissingResourceException;

import de.vistahr.lanchat.util.settings.PropertiesUtil;
import de.vistahr.util.logger.JLoggerUtil;


public class Tray {
	
	private TrayIcon trayIcon = null;
	
	
	public void setIncomingTrayIcon() throws IllegalAccessException {
		getIcon().setImage(PropertiesUtil.getLanchatPropertyImage("ICON_INCOMING"));
	}
	
	public void setDefaultTrayIcon() throws IllegalAccessException {
		getIcon().setImage(PropertiesUtil.getLanchatPropertyImage("ICON_APP"));
	}	
	
	private boolean initTrayIcon(Image icon, String iconTooltip) {
		if (SystemTray.isSupported()) {
			try {
				trayIcon = new TrayIcon(icon, iconTooltip);
				trayIcon.setImageAutoSize(true);
				
			} catch(MissingResourceException e) {
				JLoggerUtil.getLogger().warn("MissingResourceException in changeTrayIcon - Icon not found");
			}
			
			return true;
		}
		
		return false;
	}
	

	public TrayIcon getIcon() throws IllegalAccessException {
		if (SystemTray.isSupported()) {
			if(trayIcon == null)
				initTrayIcon(PropertiesUtil.getLanchatPropertyImage("ICON_APP"), PropertiesUtil.getLanchatPropertyString("APP_NAME"));
			return trayIcon;
		}
		throw new IllegalAccessException("SystemTray not supported.");
	}
	
	
	public void showTrayMessageDialog(String header, String message) {
		trayIcon.displayMessage(header, message, TrayIcon.MessageType.INFO);
	} 
	
}
