package de.vistahr.lanchat.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.view.component.RootView;

public class ChatboxPopupClearListener extends AbstractListener implements ActionListener {

	public ChatboxPopupClearListener(RootViewModel m, RootView v) {
		super(m, v);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		model.removeEntries();
	}

}
