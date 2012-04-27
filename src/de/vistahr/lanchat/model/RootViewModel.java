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
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Vector;

import de.vistahr.util.JLoggerUtil;

/**
 * Main model, which holds the data of the view
 * @author vistahr
 */
public class RootViewModel extends Observable {
	
	private Name chatName;
	private Message chatMessage;
	private ArrayList<ChatMessage> entries;
	
	private Map<Integer, Name> userList;
	
	private boolean mute;

	private boolean pingChange;
	
	public RootViewModel() {
		entries 	= new ArrayList<ChatMessage>();
		chatMessage = new Message("");
		chatName 	= new Name(Name.getDefaultFallbackName());
		mute 		= false;
		userList	= new HashMap<Integer, Name>();
	}
	
	
	public boolean isMute() {
		return mute;
	}

	public void setMute(boolean mute) {
		this.mute = mute;
	}

	public Message getChatMessage() {
		return chatMessage;
	}

	public Name getChatName() {
		return chatName;
	}
	
	public ArrayList<ChatMessage> getEntries() {
		return entries;
	}
	
	
	public Vector<Name> getUserList() {
		Vector<Name> entries = new Vector<Name>();
		entries.addAll(userList.values());
		return entries;
	}


	public void setChatName(String cn) {
		chatName = new Name(cn);
		setChanged();
		notifyObservers(this);
	}

	public void setChatMessage(String cm) {
		chatMessage = new Message(cm);
		setChanged();
		notifyObservers(this);
	}
	
	public boolean isPingChange() {
		return pingChange;
	}
	
	public void setPingChange(boolean pc) {
		pingChange = pc;
	}


	public void addEntry(ChatMessage cm) {
		synchronized (entries) {
			entries.add(cm);
		}
		setChanged();
		notifyObservers(this);
	}
	
	public void removeEntries() {
		synchronized (entries) {
			entries.clear();
		}
		setChanged();
		notifyObservers(this);
	}	
	
	public void addUserListEntry(int ID, Name name) {
		userList.put(ID, name);
		setChanged();
		notifyObservers(this);
	}	
	
	public void removeUserListEntry(int ID) {
		userList.remove(ID);
		setChanged();
		notifyObservers(this);
	}
	
	public ChatMessage getLastEntry() {
		ChatMessage msg = null;
		try {
			if(getEntries().size() > 0)
				msg = getEntries().get(getEntries().size() - 1);
		} catch (ArrayIndexOutOfBoundsException e) {
			JLoggerUtil.getLogger().warn("ArrayIndexOutOfBoundsException");
		}
		
		return msg;
	}


	
}
