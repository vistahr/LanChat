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
package de.vistahr.lanchat.task;

import java.io.IOException;
import java.util.TimerTask;

import de.vistahr.lanchat.model.ChatPing;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.network.Multicast;
import de.vistahr.network.SLCP;
import de.vistahr.util.logger.JLoggerUtil;


public class SendPingTask extends TimerTask   {
	
	private Multicast mcast;
	private RootViewModel model;
	

	
	public SendPingTask(RootViewModel m, Multicast mc) {
		mcast = mc;
		model = m;
	}
	
	@Override
	public void run() {
		ChatPing ping =  new ChatPing(model.getChatName().getName());
		
		SLCP sender = new SLCP(SLCP.VERSION_V1);
		try {
			String resp = sender.generatePing(ping);
			mcast.send(resp);
			
		} catch (IOException e) {
			JLoggerUtil.getLogger().warn("Send Ping failed - IOException");
		}
	}
	
	
}
