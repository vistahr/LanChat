package de.vistahr.lanchat.controller;

import java.io.IOException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.vistahr.lanchat.model.ChatModel;
import de.vistahr.lanchat.view.ChatView;
import de.vistahr.network.Multicast;
import de.vistahr.network.Receivable;




public class ChatController implements Observer {
	
	ChatModel model;
	ChatView view;
	Multicast mcast;
	
	public ChatController(ChatModel m) {
		model = m;
		
		ChatView view = new ChatView(model);
		
		mcast = new Multicast("230.0.0.1",4447,4446);
		
		// Receivertask
		ExecutorService exec = Executors.newSingleThreadExecutor();
		Runnable receiverTask = new Runnable() {
			@Override
			public void run() {
				try {
					mcast.receive(new Receivable() {
						@Override
						public void onReceive(String data) {
							model.addEntry(new Date(), data);
						}
					});
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		exec.submit(receiverTask);
		
		
		
		
		try {
			mcast.send("test 123"); // TODO DEBUG
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

/*
	@Override
	public void onReceive(String data) {
		model.addEntry(new Date(), data);
	}
*/

	
	
	@Override
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub
		// model updaten wenn view was Šndert
	}
	
	
	
}
