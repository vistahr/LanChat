package de.vistahr.lanchat.model;

import java.util.Date;


public interface Message {
	
	public Date getWritten();
	public void setWritten(Date written);
	
	public String getChatName();
	public void setChatName(String chatName);
	
	public String getChatMessage();
	public void setChatMessage(String chatMessage);
	
	public int getID();
	public void setID(int ID);
	
	public String toString();
	
}
