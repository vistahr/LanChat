package de.vistahr.lanchat;

import de.vistahr.lanchat.controller.ChatController;
import de.vistahr.lanchat.model.ChatModel;

public class LanChat {

	
	public static void main(String[] args) {
		ChatModel model = new ChatModel();
		ChatController cc = new ChatController(model);
	}
	
	
}
