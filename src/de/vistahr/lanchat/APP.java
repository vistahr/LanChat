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

package de.vistahr.lanchat;


import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ImageIcon;

import de.vistahr.lanchat.view.ChatView;


/**
 * Main class handle the action listeners and 
 * starts the receiver loop.
 * 
 * 
 * @author vistahr
 *
 */
public class APP implements Receivable, Runnable {
	
	
	private ChatView gui;
	private Multicast multicast;
	private boolean muteSound = false;
	
	
	/**
	 * Starts a new LanChat in a single thread
	 * @param args
	 * 			not implemented
	 */
	public static void main(String[] args) {
		Thread t = new Thread(new APP(new Multicast("230.0.0.1",4447,4446)));
		t.start();
	}
	
	
	/**
	 * Constructor - set up the gui
	 * @param mcast
	 * 			Multicast object
	 */
	public APP(Multicast mcast) {
		this.multicast = mcast;
		this.gui = new ChatView();
	}

	
	/**
	 * Display the GUI, set up the listeners and start the receiving loop.
	 */
	@Override
	public void run() {
		// init GUI
		Image icon = new ImageIcon(getClass().getResource("/res/chat.png")).getImage();
		this.gui.getFrame().setIconImage(icon);
		
		this.gui.getFrame().setLocationRelativeTo(null);
		this.gui.getFrame().setVisible(true);
		
		// set default name
		try {
			this.gui.txtChatname.setText(InetAddress.getLocalHost().getHostName());
			
			// listeners
			this.gui.btnSendMsg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { sendMessagePressed(e); } 
			});
			this.gui.txtSendMsg.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { sendMessagePressed(e); } 
			});
			this.gui.btnQuit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { quitChatPressed(e); } 
			});
			this.gui.btnMute.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) { mutePressed(e); } 
			});
			
			// Connectiondetails
			this.gui.paneChatbox.setText("connected to " + this.multicast);
			this.multicast.send(this.gui.txtChatname.getText() + " connected");
			
			// Receiver
			multicast.receive(this);

		} catch (IOException ex) {
			this.gui.showMessageDialog(ex.getMessage());
		}
	}
	
	/**
	 * Toggle action, event when mute button pressed
	 * @param e
	 */
	protected void mutePressed(ActionEvent e) {
		if(this.muteSound == false) {
			this.muteSound = true;
			this.gui.btnMute.setIcon(new ImageIcon(getClass().getResource("/res/mute.png")));
		} else {
			this.muteSound = false;
			this.gui.btnMute.setIcon(new ImageIcon(getClass().getResource("/res/unmute.png")));
		}
	}
	
	/**
	 * Action event, when quit button pressed
	 * @param e
	 */
	protected void quitChatPressed(ActionEvent e) {
		try {
			this.multicast.send(this.gui.txtChatname.getText() + " leaved");
			this.multicast.closeSocket();
		} catch (IOException ex) {
			this.gui.showMessageDialog(ex.getMessage());
		}
		System.exit(1);
	}

	/**
	 * Action event, when send message pressed
	 * @param e
	 */
	protected void sendMessagePressed(ActionEvent e) {
		try {
			if(this.gui.txtChatname.getText().trim().length() == 0)
				throw new IllegalArgumentException("invalid chatname");
				
			if(this.gui.txtSendMsg.getText().trim().length() == 0)
				throw new IllegalArgumentException("invalid chatmessage");
			
			// send
			Date sendDate = new Date();
			SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
			multicast.send("[" + df.format(sendDate) + "] " + this.gui.txtChatname.getText() + ": " + this.gui.txtSendMsg.getText());
			this.gui.txtSendMsg.setText("");
			
		} catch(Exception ex) {
			this.gui.showMessageDialog(ex.getMessage());
		}
	}


	/**
	 * Callback function of the Reveicer interface
	 */
	@Override
	public void onReceive(String message) {
		this.gui.paneChatbox.setText(message + "\n" + this.gui.paneChatbox.getText());
		this.gui.showTrayMessageDialog("incoming message", message);
		if(this.muteSound == false)
			this.playSound("/res/ding.wav");
	}
	
	
	/**
	 * Play soundfile
	 * @param soundPath
	 * 			relative Resource Path
	 */
	@Deprecated
	public void playSound(String soundPath) {
		AudioClip sound;
		sound = Applet.newAudioClip(getClass().getResource(soundPath));
		sound.play();	
	}


}