

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
import de.vistahr.network.Multicast;
import de.vistahr.network.Receivable;


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
	 
	public static void main(String[] args) {
		Thread t = new Thread(new APP(new Multicast("230.0.0.1",4447,4446)));
		t.start();
	}
	*/
	
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
		
		
		this.gui.getFrame().setLocationRelativeTo(null);
		this.gui.getFrame().setVisible(true);
		
		// set default name
		try {
			
			
			
			
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
