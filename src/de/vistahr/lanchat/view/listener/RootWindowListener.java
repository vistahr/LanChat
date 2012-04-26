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

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ExecutorService;

import javax.swing.JFrame;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.network.Multicast;
import de.vistahr.network.SLCP;
import de.vistahr.util.logger.JLoggerUtil;

public class RootWindowListener extends AbstractListener implements WindowListener {

	ExecutorService exec;
	Multicast mcast;
	Timer timer;
	
	public RootWindowListener(RootViewModel m, RootView v, Multicast mc, ExecutorService e, Timer t) {
		super(m, v);
		
		exec  = e;
		timer = t;
		mcast = mc;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		try {
			// send quit message
			ChatMessage msg = new ChatMessage(model.getChatName().getName(), "leaved", new Date());
			SLCP sender = new SLCP(SLCP.VERSION_V1);
			mcast.send(sender.generateMessage(msg));
			mcast.closeSocket();
			
			// leave chat
			model.removeUserListEntry(msg.getID());

			view.getMainframe().setVisible(false);
			view.getMainframe().dispose();
			
			timer.cancel();
			timer.purge();
			
			exec.shutdown();
			
			JLoggerUtil.getLogger().info("Application shutting down successful.");
			
		} catch (IOException ex) {
			JLoggerUtil.getLogger().warn(ex.getMessage());
			
		} catch (NullPointerException ex) {
			JLoggerUtil.getLogger().warn("Hard exit");
			System.exit(1);
		}		
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
		SystemTray tray = SystemTray.getSystemTray();
		
    	try {
			tray.add(view.getTray().getIcon());
			view.getTray().setDefaultTrayIcon();
			view.getMainframe().setState(JFrame.ICONIFIED);
			view.getMainframe().setVisible(false);
			
		} catch (IllegalAccessException ex) {
			JLoggerUtil.getLogger().warn("Tray not supported");
			
		} catch (AWTException ex) {
			JLoggerUtil.getLogger().warn("AWTException in windowIconified");
		}
	}
	
	@Override
	public void windowDeiconified(WindowEvent e) {
		try {
			view.getTray().setDefaultTrayIcon();
		} catch (IllegalAccessException e1) {
			JLoggerUtil.getLogger().warn("Tray not supported");
		}
	}

	@Override
	public void windowActivated(WindowEvent e) {
		view.getSendbox().setCaretPosition(0);
		view.getSendbox().requestFocus();
		view.getMainframe().setDefaultAppIcon();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	
}