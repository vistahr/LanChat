package de.vistahr.lanchat.model;

import java.util.Date;
import java.util.Hashtable;
import java.util.Observable;



public class ChatModel extends Observable {
	
	private String chatname;
	private Hashtable<Date, String> entries;
	
	
	public ChatModel() {
		entries = new Hashtable<Date, String>();
	}
	
	public String getChatname() {
		return chatname;
	}
	
	public Hashtable<Date, String> getEntries() {
		return entries;
	}
	
	public void setChatname(String chatname) {
		this.chatname = chatname;
		setChanged();
		notifyObservers(this);
	}
	
	
	public void addEntry(Date date, String message) {
		entries.put(date, message);
		setChanged();
		notifyObservers(this);
	}


	
}
