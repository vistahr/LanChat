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

import java.awt.Dimension;

import javax.swing.JFrame;

import de.vistahr.lanchat.util.PropertiesUtil;
import de.vistahr.util.JLoggerUtil;


public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1099533524779117586L;

	
	
	public void setIncomingAppIcon() {
		setIconImage(PropertiesUtil.getLanchatPropertyImage("ICON_INCOMING"));
	}
	
	public void setDefaultAppIcon() {
		setIconImage(PropertiesUtil.getLanchatPropertyImage("ICON_APP"));
	}	

	public MainFrame(String appName) {
		super(appName);
		
		// Frame settings
		setPreferredSize(new Dimension(400,250));
		setMinimumSize(new Dimension(400,250));
		setResizable(true);
		pack();
		setLocationRelativeTo(null);
		
		// icon - frame
		try {
			setDefaultAppIcon();
			
		} catch(NullPointerException e) {
			JLoggerUtil.getLogger().warn("MainFrame - Cannot load resource " + PropertiesUtil.getLanchatPropertyString("ICON_APP"));
		}
	}
	
	

	
}
