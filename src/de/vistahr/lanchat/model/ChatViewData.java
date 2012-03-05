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
package de.vistahr.lanchat.model;

import java.util.ArrayList;
import java.util.Observable;

/**
 * Main model, which holds the data of the view
 * @author vistahr
 */
public class ChatViewData extends Observable {
	
	private static final int CHATNAME_LENGTH = 20;
	private static final int CHATMSG_LENGTH = 500;
	
	private String chatName;
	private String chatMessage;
	private ArrayList<ChatMessage> entries;
	
	private boolean mute;
	
	
	public ChatViewData() {
		entries = new ArrayList<ChatMessage>();
		chatMessage = "";
		chatName = "";
		mute = false;
	}
	
	
	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	public String getChatMessage() {
		return chatMessage;
	}

	public String getChatname() {
		return chatName;
	}
	
	public ArrayList<ChatMessage> getEntries() {
		return entries;
	}
	
	
	public void setChatname(String cn) {
		String validCharPattern = "\\W";
		try {
			chatName = cn.trim().replaceAll(validCharPattern, "");
			if(cn.length() > CHATNAME_LENGTH)
				chatName = chatName.substring(0, CHATNAME_LENGTH);
				
		} catch(StringIndexOutOfBoundsException e) {
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(this);
	}

	public void setChatMessage(String cm) {
		chatMessage = cm.trim();
		if(cm.length() > CHATMSG_LENGTH)
			chatMessage = chatMessage.substring(0, CHATMSG_LENGTH);
		setChanged();
		notifyObservers(this);
	}
	
	public void addEntry(ChatMessage cm) {
		entries.add(cm);
		setChanged();
		notifyObservers(this);
	}
	
	
	public ChatMessage getLastEntry() {
		return getEntries().get(getEntries().size() - 1);
	}


	
}
