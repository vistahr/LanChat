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

import java.util.Date;


public abstract class ChatResponse {

	public final static String DATE_OUT_FORMAT = "HH:MM:ss";
	
	private Name chatName;
	private Message chatMessage;	
	private Date written;
	private int ID;
	
	
	

	public ChatResponse(final String chatName, final String chatMessage, final Date written, final int ID) {
		setChatName(chatName);
		setChatMessage(chatMessage);
		this.written = written;
		this.ID = ID;
	}
	
	
	public Date getWritten() {
		return written;
	}
	
	
	public void setWritten(Date written) {
		this.written = written;
	}
	
	
	public Name getChatName() {
		return chatName;
	}
	
	
	public void setChatName(String cn) {
		chatName = new Name(cn);
	}
	
	
	public Message getChatMessage() {
		return chatMessage;
	}
	
	
	public void setChatMessage(String cm) {
		chatMessage = new Message(cm);
	}

	
	public int getID() {
		return ID;
	}

	
	public abstract String toString();


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ID;
		result = prime * result
				+ ((chatMessage == null) ? 0 : chatMessage.hashCode());
		result = prime * result
				+ ((chatName == null) ? 0 : chatName.hashCode());
		result = prime * result + ((written == null) ? 0 : written.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ChatResponse))
			return false;
		ChatResponse other = (ChatResponse) obj;
		if (ID != other.ID)
			return false;
		if (chatMessage == null) {
			if (other.chatMessage != null)
				return false;
		} else if (!chatMessage.equals(other.chatMessage))
			return false;
		if (chatName == null) {
			if (other.chatName != null)
				return false;
		} else if (!chatName.equals(other.chatName))
			return false;
		if (written == null) {
			if (other.written != null)
				return false;
		} else if (!written.equals(other.written))
			return false;
		return true;
	}

	

	
}
