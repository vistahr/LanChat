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
package de.vistahr.lanchat.listener;

import java.net.URL;
import java.text.ParseException;

import de.vistahr.lanchat.event.MulticastReceiveEvent;
import de.vistahr.lanchat.model.AbstractChatResponse;
import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.ChatPing;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.util.PropertiesUtil;
import de.vistahr.lanchat.util.SmileyUtil;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.lanchat.view.listener.AbstractListener;
import de.vistahr.network.SLCP;
import de.vistahr.network.listener.IMulticastReceiveListener;
import de.vistahr.util.JLoggerUtil;
import de.vistahr.util.SoundUtil;

public class ReceiveListener extends AbstractListener implements IMulticastReceiveListener {
	
	
	public ReceiveListener(RootViewModel m, RootView v) {
		super(m, v);
	}
	
	
	@Override
	public synchronized void receive(MulticastReceiveEvent event) {
		
		// Parse incoming data
		SLCP receiver = new SLCP(SLCP.VERSION_V1);
		try {
			final AbstractChatResponse resp = receiver.parse(event.getData());
			
			if(resp instanceof ChatMessage) {
				// Add entry to the model to display it on the panechatbox.
				// Before, parse all sileys and replace ascii with html img tags.
				ChatMessage chatMessage = (ChatMessage)resp;
				chatMessage.getChatMessage().setMessage(SmileyUtil.parseSmileysInString(chatMessage.getChatMessage().getMessage()));
				model.addEntry(chatMessage);
				// change trayicon (when application is minimized)
				view.getTray().setIncomingTrayIcon();
				
				if(!view.getMainframe().isActive())
					view.getMainframe().setIncomingAppIcon();
				
				
				// when muted, hide tray messages
				if(!model.isMute()) {
					view.getTray().showTrayMessageDialog("incoming message", model.getLastEntry().getChatMessage().getMessage());
					playSound(getClass().getResource(PropertiesUtil.getLanchatPropertyString("SOUND_INCOMING")));
				}
			}
			
			if(resp instanceof ChatPing) {
				model.addUserListEntry(resp.getID(),resp.getChatName());
				model.setPingChange(true);
			}
			
			
		} catch(ParseException ex) {
			JLoggerUtil.getLogger().warn("ParseException in receive. Invalid input detected, continue receiving.");
			
		} catch (IllegalAccessException e) {
			JLoggerUtil.getLogger().warn("IllegalAccessException in receive. Tray not supported.");
			
		} catch (NullPointerException ex) {
			JLoggerUtil.getLogger().warn("NullPointerException in receive. Empty message detected, continue receiving.");
		}
			
		
	}
	
	
	private void playSound(URL res) {
		try {
			SoundUtil.playWAV(ReceiveListener.class.getResource(PropertiesUtil.getLanchatPropertyString("SOUND_INCOMING")));
			
		} catch (InterruptedException e) {
			JLoggerUtil.getLogger().warn("InterruptedException in playSound");
		}
	}
	
}
