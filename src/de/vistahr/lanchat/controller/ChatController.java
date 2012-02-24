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
package de.vistahr.lanchat.controller;



import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ImageIcon;

import de.vistahr.lanchat.model.ChatData;
import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.view.ChatView;
import de.vistahr.network.Multicast;
import de.vistahr.network.Receivable;


public class ChatController {
	
	private ChatData model;
	private ChatView view;
	private Multicast mcast;
	private boolean muteSound = false;
	
	
	
	public ChatController(ChatData m, ChatView v) {
		model = m;
		view  = v;
		
		// add Listeners
		addActionListeners();
		
		try {
			// Set default Username
			model.setChatname(InetAddress.getLocalHost().getHostName());
			
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		

		mcast = new Multicast("230.0.0.2",4447,4446);
		
		// Receivertaskloop
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Runnable receiverTask = new Runnable() {
			@Override
			public void run() {
				try {
					mcast.receive(new Receivable() {
						@Override
						public void onReceive(String data) {
							// TODO add Protocol
							model.addEntry(new ChatMessage(model.getChatname(), data, new Date())); 
							/* TODO sound & tray msg
							this.gui.showTrayMessageDialog("incoming message", message);
							if(this.muteSound == false)
								this.playSound("/res/ding.wav");
								*/
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		exec.submit(receiverTask);
		
	}
	

	
	private void addActionListeners() {
		// send 
		view.getBtnSendMsg().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessagePressed(e);
			}
		});
		
		// send 
		view.getJTextfieldSendMsg().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendMessagePressed(e);
			}
		});
		// quit
		view.getBtnQuit().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				quitChatPressed(e);
			}
		});
		// mute
		view.getBtnMute().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mutePressed(e);
			}
		});
	}
	
	
	/**
	 * Toggle action, event when mute button pressed
	 * @param e
	 */
	private void mutePressed(ActionEvent e) {
		if(muteSound == false) {
			muteSound = true;
			view.getBtnMute().setIcon(new ImageIcon(getClass().getResource("/res/mute.png")));
		} else {
			muteSound = false;
			view.getBtnMute().setIcon(new ImageIcon(getClass().getResource("/res/unmute.png")));
		}
	}
	
	
	/**
	 * Action event, when quit button pressed
	 * @param e
	 */
	private void quitChatPressed(ActionEvent e) {
		try {
			ChatMessage msg = new ChatMessage(model.getChatname(), "leaved", new Date());
			mcast.send(msg.toString());
			mcast.closeSocket();
			
		} catch (IOException ex) {
			view.showMessageDialog(ex.getMessage());
		}
		System.exit(1);
	}

	
	/**
	 * Action event, when send message pressed
	 * @param e
	 */
	private void sendMessagePressed(ActionEvent e) {
		try { // TODO
			/*if(model.getChatname().trim().length() == 0)
				throw new IllegalArgumentException("invalid chatname");
				
			if(model.getChatMessage().trim().length() == 0)
				throw new IllegalArgumentException("invalid chatmessage");
			*/
			// send
			ChatMessage msg = new ChatMessage(model.getChatname(), model.getChatMessage(), new Date());
			mcast.send(msg.toString());
			//model.setChatMessage("");
			
		} catch(Exception ex) {
			view.showMessageDialog(ex.getMessage());
			ex.printStackTrace();
		}
	}
	
}
