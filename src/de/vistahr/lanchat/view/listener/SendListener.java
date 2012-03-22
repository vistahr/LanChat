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
import java.io.IOException;
import java.util.Date;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.view.component.MessageDialog;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.network.Multicast;
import de.vistahr.network.SLCP;

public class SendListener extends AbstractListener implements ActionListener {

	private Multicast mcast;
	
	public SendListener(RootViewModel m, RootView v, Multicast mc) {
		super(m, v);
		mcast = mc;
	}

	
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			if(view.getSendbox().getText().equals("")) {
				throw new IllegalArgumentException();
			}	
			
			// fill model
			model.setChatMessage(view.getSendbox().getText());
			
			// send
			ChatMessage msg = new ChatMessage(view.getChatname().getText(), view.getSendbox().getText(), new Date());
			SLCP sender = new SLCP(SLCP.VERSION_V1);
			mcast.send(sender.generateMessage(msg));
			model.setChatMessage("");
			
		} catch(IOException ex) {
			new MessageDialog(ex.getMessage());
			
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			
		} catch (IllegalArgumentException ex) {
			// no msg output for chatname & message
		}
	}

}