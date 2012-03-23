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
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.resource.Bundle;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.lanchat.view.listener.AbstractListener;
import de.vistahr.network.SLCP;

public class ReceiveListener extends AbstractListener implements IMulticastReceiveListener {
	
	
	public ReceiveListener(RootViewModel m, RootView v) {
		super(m, v);
	}
	
	
	@Override
	public void receive(MulticastReceiveEvent event) {
		try {
			// Parse incoming data
			SLCP receiver = new SLCP(SLCP.VERSION_V1);
			try {
				final AbstractChatResponse resp = receiver.parse(event.getData());
				if(resp instanceof ChatMessage) {
					// add entry to the model to display it on the panechatbox
					model.addEntry((ChatMessage)resp);
					// activate gui, when in background
					if(!view.getMainframe().isActive()) {
						view.getMainframe().toFront();
					}
				}
				// when muted, hide tray messages
				if(!model.isMute()) {
					view.getTray().showTrayMessageDialog("incoming message", model.getLastEntry().getChatMessage().getMessage());
					playSound(getClass().getResource(Bundle.getString("SOUND_INCOMING")));
				}
				
			} catch(ParseException ex) {
				// continue - invalid input TODO -> log
			}
			
		} catch (NullPointerException ex) {
			// continue empty messaga data TODO -> log
		}
	}
	
	// TODO
	private void playSound(URL res) {
	}
	
}
