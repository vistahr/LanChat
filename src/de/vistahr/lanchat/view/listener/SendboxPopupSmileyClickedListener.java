package de.vistahr.lanchat.view.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.view.component.RootView;

public class SendboxPopupSmileyClickedListener extends AbstractListener implements ActionListener {

	public SendboxPopupSmileyClickedListener(RootViewModel m, RootView v) {
		super(m, v);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem item = (JMenuItem)e.getSource();
		view.getSendbox().setText(view.getSendbox().getText() + item.getText());
	}

}
