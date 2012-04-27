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
package de.vistahr.lanchat.view.component;

import java.awt.EventQueue;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.UIManager;

import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.util.PropertiesUtil;
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
public class RootView implements Observer {


	// Mainframe & basics
	private MainFrame mainframe;

	// Components
	private ChatNameText chatname;
	
	private Chatbox chatbox;
	private ChatboxScroller chatscroller;
	private ChatboxPopupMenu chatboxPopup;
	
	private SendBox sendbox;
	private SendboxPopup sendboxPopup;
	
	private SendButton sendbutton;
	private MuteButton mutebutton;
	private Tray tray;
	private UserList userList;
	private UserListScroller userListScroller;
	
	// Panels
	private JPanel mainPanel;

	
	
	public MainFrame getMainframe() {
		return mainframe;
	}


	public ChatNameText getChatname() {
		return chatname;
	}

	public Chatbox getChatbox() {
		return chatbox;
	}

	public ChatboxScroller getChatscroller() {
		return chatscroller;
	}

	public SendBox getSendbox() {
		return sendbox;
	}

	public SendButton getSendbutton() {
		return sendbutton;
	}

	public MuteButton getMutebutton() {
		return mutebutton;
	}

	public JPanel getMainPanel() {
		return mainPanel;
	}

	
	public Tray getTray() {
		return tray;
	}


	public UserList getUserList() {
		return userList;
	}


	public UserListScroller getUserListScroller() {
		return userListScroller;
	}


	public ChatboxPopupMenu getChatboxPopup() {
		return chatboxPopup;
	}


	public SendboxPopup getSendboxPopup() {
		return sendboxPopup;
	}


	/**
	 * Constructor set up the UI and listeners
	 */
	public RootView(Observable model) {
		model.addObserver(this);
		initialize();
		positionLayout();
		mainframe.setVisible(true);
	}
	
	
	private void initialize() {
		
		// set app name for mac
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", PropertiesUtil.getLanchatPropertyString("APP_NAME"));
		// use osx jmenu 
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		
		// systemlook		
		try {
	        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (Exception e) {
	        e.printStackTrace();
	    }	
				
		// Frame
		mainframe = new MainFrame(PropertiesUtil.getLanchatPropertyString("APP_NAME"));
		
		
		// Components
		/////////////
		mainPanel 			= new JPanel();
		chatname 			= new ChatNameText();
		mutebutton 			= new MuteButton();
		chatbox 			= new Chatbox();
		chatscroller 		= new ChatboxScroller(chatbox);
		sendbox 			= new SendBox();
		sendbutton 			= new SendButton();
		tray 				= new Tray();
		userList 			= new UserList();
		userListScroller 	= new UserListScroller(userList);
		chatboxPopup		= new ChatboxPopupMenu();
		sendboxPopup		= new SendboxPopup(this);
		userList.setFixedCellWidth(getMainframe().getWidth() / 4);
	}
	
	
	private void positionLayout() {
		BindingFactory bf = new BindingFactory();
		mainPanel = new JPanel(new RelativeLayout());
		// top
		mainPanel.add(chatname,new RelativeConstraints(bf.topEdge(), bf.leftEdge()));
		mainPanel.add(mutebutton, new RelativeConstraints(bf.rightEdge(), bf.topEdge()));
		// right side
		mainPanel.add(userListScroller, new RelativeConstraints(bf.rightEdge(), bf.below(mutebutton), bf.above(sendbutton)));
		// center
		mainPanel.add(chatscroller, new RelativeConstraints(bf.below(mutebutton), bf.leftEdge(), bf.leftOf(userListScroller), bf.above(sendbox)));
		// bottom
		mainPanel.add(sendbox, new RelativeConstraints(bf.bottomEdge(), bf.leftEdge(), bf.leftOf(sendbutton)));
		mainPanel.add(sendbutton, new RelativeConstraints(bf.bottomEdge(), bf.rightEdge()));
		// add to mainframe
		mainframe.add(mainPanel);
	}
	
	
	/**
	 * Refresh observed Data
	 */
	@Override
	public void update(Observable o, Object model) {
		
		RootViewModel rvm = ((RootViewModel) model);
		
		
		// pingchange refresh only the userlist
		if(rvm.isPingChange()) {
			userList.setListData(rvm.getUserList());
			rvm.setPingChange(false);
			
		// other fields, only changed by new messages
		} else {

			chatbox.setContent(rvm.getEntries());
			
			// scroll every update to bottom, cause updating the entries 
			// scrolls automatically to the top. 
			EventQueue.invokeLater(new Runnable(){
				public void run() {
					chatscroller.scrollChatboxToBottom();
				}
			});
			
			if(!chatname.hasFocus())
				chatname.setText(rvm.getChatName().getName());
			
			if(rvm.getChatMessage().getMessage().equals("#delete-Content#"))
				sendbox.setText("");
			
		}
		
	}

	
}