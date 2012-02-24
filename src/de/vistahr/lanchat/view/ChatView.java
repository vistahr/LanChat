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

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import de.vistahr.lanchat.model.ChatModel;


/**
 * GUI class handle the swing gui with all components,
 * the windows listeners and the tray support.
 * 
 * @author vistahr
 *
 */
public class ChatView implements Observer {
	

	// Mainframe & basics
	private JFrame frame;
	private TrayIcon trayIcon;
	public static String APP_NAME = "LanChat - blabla your life";
	
	// Components
	private JButton btnQuit 		= new JButton("leave it");
	private JLabel lblChatname 		= new JLabel("Chatname:");
	private JTextField txtChatname	= new JTextField(12);
	private JEditorPane paneChatbox = new JEditorPane();
	private JLabel lblSendMsg 		= new JLabel("Message:");
	private JTextField txtSendMsg 	= new JTextField(20);
	private JButton btnSendMsg		= new JButton("send");
	private JButton btnMute 		= new JButton(new ImageIcon(getClass().getResource("/res/unmute.png")));
	
	
	
	public JButton getBtnQuit() {
		return btnQuit;
	}


	public String getTxtChatname() {
		return txtChatname.getText();
	}


	public void setTxtSendMsg(String msg) {
		this.txtSendMsg.setText(msg);
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
	
	
	private void setTxtChatname(String name) {
		txtChatname.setText(name);
	}

	
	private void setPaneChatbox(Hashtable<Date, String> entries) {
		Date str;
		Set<Date> set = entries.keySet();
		
		Iterator<Date> itr = set.iterator();
	    while (itr.hasNext()) {
	      str = itr.next();
	      paneChatbox.setText(str + ": " + entries.get(str));
	    }
		
	}


	
	/**
	 * Constructor set up the UI and listeners
	 */
	public ChatView(Observable model) {
		model.addObserver(this);
		initialize();
	}
	
	
	public void initialize() {
		// Systemlook
		try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
		

		// Frame
		frame = new JFrame(APP_NAME);
		
		// Components settings
		paneChatbox.setEditable(false);
		JScrollPane editorScrollPane = new JScrollPane(paneChatbox);
		paneChatbox.setPreferredSize(new Dimension(320, 150));
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		// Panels
		JPanel panelTopL = new JPanel();
		panelTopL.add(btnQuit);
		
		JPanel panelTopR = new JPanel();
		panelTopR.add(lblChatname);
		panelTopR.add(txtChatname);
		panelTopR.add(btnMute);
		
		JPanel panelTop = new JPanel(new BorderLayout());
		panelTop.add(panelTopL, BorderLayout.LINE_START);
		panelTop.add(panelTopR, BorderLayout.LINE_END);
		
		
		JPanel panelCenter = new JPanel();
		panelCenter.add(editorScrollPane);
		
		JPanel panelBottomL = new JPanel();
		panelBottomL.add(lblSendMsg);
		panelBottomL.add(txtSendMsg);
		panelBottomL.add(btnSendMsg);
		
		JPanel panelBottomR = new JPanel();
		btnMute.setPreferredSize(new Dimension(20,20));
		
		
		
		JPanel panelBottom = new JPanel(new BorderLayout());
		panelBottom.add(panelBottomL, BorderLayout.LINE_START);
		panelBottom.add(panelBottomR, BorderLayout.LINE_END);
		
		
		// add Panels
		frame.getContentPane().add(panelTop, BorderLayout.PAGE_START);
		frame.getContentPane().add(panelCenter, BorderLayout.LINE_START);
		frame.getContentPane().add(panelBottom, BorderLayout.PAGE_END);
		
		// Frame settings
		frame.setSize(335,250);
		frame.setResizable(false);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		
		frame.setVisible(true);
		frame.setLocationRelativeTo(null);
		
		
		Image icon = new ImageIcon(getClass().getResource("/res/chat.png")).getImage();
		frame.setIconImage(icon);
		
		addWindowListener();
	}

	@Override
	public void update(Observable o, Object model) {
		setPaneChatbox(((ChatModel) model).getEntries());
		setTxtChatname(((ChatModel) model).getChatname());
	}

		

	
	
	// TODO - move to controller
	private void addWindowListener() {
		// Windows listener
		frame.addWindowListener(new WindowListener() {
			@Override
			public void windowOpened(WindowEvent e) {}
			@Override
			public void windowIconified(WindowEvent e) {
				// hide
				frame.setState(JFrame.ICONIFIED);
				frame.setVisible(false);
				SystemTray tray = SystemTray.getSystemTray();
	        	try {
					tray.add(getTrayIcon());
					//showTrayMessageDialog(APP_NAME, "minimized to tray");
				} catch (AWTException ex) {
					showMessageDialog(ex.getMessage());
				}
			}
			@Override
			public void windowDeiconified(WindowEvent e) {}
			@Override
			public void windowDeactivated(WindowEvent e) {}
			@Override
			public void windowClosing(WindowEvent e) {}
			@Override
			public void windowClosed(WindowEvent e) {}
			@Override
			public void windowActivated(WindowEvent e) {}
		});		
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
	
	
	/**
	 * Initialize and set up the trayicon
	 * @return TrayIcon object
	 */
	private TrayIcon getTrayIcon() {
		
		if (SystemTray.isSupported()) {
			
			if(this.trayIcon == null) {
			    Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getResource("/res/chat.png"));
			    this.trayIcon = new TrayIcon(icon, APP_NAME);
			    trayIcon.setImageAutoSize(true);
			    trayIcon.addMouseListener(new MouseListener() {
			        public void mouseClicked(MouseEvent e) {
			        	// Get back
			        	frame.setVisible(true);
			        	frame.setState(JFrame.NORMAL);
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





}