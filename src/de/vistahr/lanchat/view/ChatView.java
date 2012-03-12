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
package de.vistahr.lanchat.view;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import de.vistahr.lanchat.model.Chat;
import de.vistahr.lanchat.model.ChatMessage;
import edu.cmu.relativelayout.BindingFactory;
import edu.cmu.relativelayout.RelativeConstraints;
import edu.cmu.relativelayout.RelativeLayout;


/**
 * GUI class handle the swing gui with all components,
 * the windows listeners and the tray support.
 * 
 * @author vistahr
 *
 */
public class ChatView implements Observer {
	
	{  // init systemlook before instantiate components
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
	}
	
	public static final String APP_NAME = "LanChat - blabla your life";
	
	public static final String RES_PATH = "/res/";
	public static final String RES_ICON_MUTE = "mute.png";
	public static final String RES_ICON_UNMUTE = "unmute.png";
	public static final String RES_ICON_APP = "chat.png";
	public static final String RES_SOUND_SEND = "ding.wav";
	
	// Mainframe & basics
	private JFrame frame;
	private TrayIcon trayIcon;
	
	// Components
	private JTextField txtChatname		 = new JTextField(10);
	private JEditorPane paneChatbox 	 = new JEditorPane();
	private JScrollPane editorScrollPane = new JScrollPane(paneChatbox);
	private JTextField txtSendMsg 		 = new JTextField("");
	private JButton btnSendMsg			 = new JButton("Send");
	private JButton btnMute 			 = new JButton();
	
	// Panels
	private JPanel mainPanel;

	/**
	 * Constructor set up the UI and listeners
	 */
	public ChatView(Observable model) {
		model.addObserver(this);
		initialize();
		initNewLayout();
		frame.setVisible(true);
	}
	
	

	public JFrame getFrame() {
		return frame;
	}
	
	public JTextField getJTextfieldChatname() {
		return txtChatname;
	}
	
	public String getTxtChatname() {
		return txtChatname.getText();
	}

	public JTextField getJTextfieldSendMsg() {
		return txtSendMsg;
	}
	
	public String getTxtSendMsg() {
		return txtSendMsg.getText();
	}

	public JButton getBtnSendMsg() {
		return btnSendMsg;
	}

	public JButton getBtnMute() {
		return btnMute;
	}
	
	public JScrollPane getEditorScrollPane() {
		return editorScrollPane;
	}

	private void setTxtChatname(String name) {
		txtChatname.setText(name);
	}
	
	private void setTxtSendMsg(String msg) {
		txtSendMsg.setText(msg);
	}
	
	private void setPaneChatbox(ArrayList<ChatMessage> entries) {
		String message = "connected...";
		
		Iterator<ChatMessage> itr = entries.iterator();
	    while (itr.hasNext()) {
	    	try {
	    		message = message + "\n" + itr.next().toString();
	    	} catch(ConcurrentModificationException e) {
	    		break;
	    	}
	    }
	    paneChatbox.setText(message.toString());
	}
	
	public void scrollChatboxToBottom() {
		getEditorScrollPane().getVerticalScrollBar().setValue(Integer.MAX_VALUE);
	}
	
	
	private void initialize() {
		// Frame
		frame = new JFrame(APP_NAME);
		
		// Frame settings
		frame.setPreferredSize(new Dimension(350,250));
		frame.setResizable(true);
		//frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // own closemethod implemented
		frame.pack();
		frame.setLocationRelativeTo(null);
		
		// Components settings
		paneChatbox.setEditable(false);
		paneChatbox.setPreferredSize(new Dimension(320, 150));
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		// chatname
		//txtChatname.setHorizontalAlignment(JTextField.RIGHT);
		
		
		// icon - mutebutton
		btnMute.setPreferredSize(new Dimension(30,28));
		try {
			btnMute.setIcon(new ImageIcon(getClass().getResource(RES_PATH + RES_ICON_UNMUTE)));
		} catch(NullPointerException e) {
			showMessageDialog("Cannot load resource " + RES_PATH + RES_ICON_UNMUTE);
		}
		
		// icon - frame
		try {
			Image icon = new ImageIcon(getClass().getResource(RES_PATH + RES_ICON_APP)).getImage();
			frame.setIconImage(icon);
		} catch(NullPointerException e) {
			showMessageDialog("Cannot load resource " + RES_PATH + RES_ICON_APP);
		}
		
	}
	
	
	private void initNewLayout() {
		BindingFactory bf = new BindingFactory();
		// top
		mainPanel = new JPanel(new RelativeLayout());
		mainPanel.add(txtChatname,new RelativeConstraints(bf.topEdge(), bf.leftEdge()));
		mainPanel.add(btnMute, new RelativeConstraints(bf.rightEdge(), bf.topEdge()));
		// center
		mainPanel.add(editorScrollPane, new RelativeConstraints(bf.below(txtChatname), bf.leftEdge(), bf.rightEdge(), bf.above(txtSendMsg)));
		// bottom
		mainPanel.add(txtSendMsg, new RelativeConstraints(bf.bottomEdge(), bf.leftEdge(), bf.leftOf(btnSendMsg)));
		mainPanel.add(btnSendMsg, new RelativeConstraints(bf.bottomEdge(), bf.rightEdge()));
		// add to mainframe
		frame.add(mainPanel);
	}
	
	
	/**
	 * Refresh observed Data
	 */
	@Override
	public void update(Observable o, Object model) {
		setPaneChatbox(((Chat) model).getEntries());
		setTxtChatname(((Chat) model).getChatName().getName());
		setTxtSendMsg(((Chat) model).getChatMessage().getMessage());
	}

	
	
	/**
	 * Initialize and set up the trayicon
	 * @return TrayIcon object
	 */
	public TrayIcon getTrayIcon() {
		// check if os supports tray
		if (SystemTray.isSupported()) {
			if(this.trayIcon == null) {
			    Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/chat.png"));
			    trayIcon = new TrayIcon(icon, APP_NAME);
			    trayIcon.setImageAutoSize(true);
			    trayIcon.addMouseListener(new MouseListener() {
			        public void mouseClicked(MouseEvent e) {
			        	// Get back
			        	getFrame().setVisible(true);
			        	getFrame().setState(JFrame.NORMAL);
			        	SystemTray.getSystemTray().remove(getTrayIcon());
			        }
					@Override
					public void mousePressed(MouseEvent e) {}
					@Override
					public void mouseReleased(MouseEvent e) {}
					@Override
					public void mouseEntered(MouseEvent e) {}
					@Override
					public void mouseExited(MouseEvent e) {}
			    });
			}
		    return trayIcon;
		}
		return null;
	}
	
	
	/**
	 * Creates an warning dialog box
	 * @param message
	 * 			Message that will shown in the Dialogbox
	 */
	public void showMessageDialog(String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.WARNING_MESSAGE);
	}
	
	
	/**
	 * Creates an trayicon dialog
	 * @param header
	 * 			message will shown in tray header
	 * @param message
	 * 			tray message
	 */
	public void showTrayMessageDialog(String header, String message) {
		getTrayIcon().displayMessage(header, message, TrayIcon.MessageType.INFO);
	}	
	

}