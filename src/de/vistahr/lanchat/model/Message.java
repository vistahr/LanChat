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
 * 	SERVICES{ LOSS OF USE, DATA, OR PROFITS{ OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * 	ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * 	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * 	ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 	
 * 	The views and conclusions contained in the software and documentation are those of the
 * 	authors and should not be interpreted as representing official policies, either expressed
 * 	or implied, of Vince.
 */
package de.vistahr.lanchat.model;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Chat message interface
 * @author vistahr
 */
public abstract class Message {
	
	
	private String chatName;
	private String chatMessage;	
	private Date written;
	private int ID;
	
	public static String DATE_FORMAT = "HH:mm:ss";

	
	
	public Message(String chatName, String chatMessage, Date written) {
		this.chatName = chatName;
		this.chatMessage = chatMessage;	
		this.written = written;
		this.ID = 0;
	}
	

	public Message(String chatName, String chatMessage, Date written, int ID) {
		this.chatName = chatName;
		this.chatMessage = chatMessage;	
		this.written = written;
		this.ID = ID;
	}
	
	
	public Date getWritten() {
		return written;
	}
	
	
	public void setWritten(Date written) {
		this.written = written;
	}
	
	
	public String getChatName() {
		return chatName;
	}
	
	
	public void setChatName(String chatName) {
		this.chatName = chatName;
	}
	
	
	public String getChatMessage() {
		return chatMessage;
	}
	
	
	public void setChatMessage(String chatMessage) {
		this.chatMessage = chatMessage;
	}

	
	public int getID() {
		return ID;
	}

	
	public void setID(int ID) {
		this.ID = ID;
	}
	
	
	public String toString() {
		SimpleDateFormat df = new SimpleDateFormat(DATE_FORMAT);
		return "[" + df.format(getWritten()) + "] " + getChatName() + ": " + getChatMessage();
	}


	
}
