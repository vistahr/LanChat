package de.vistahr.lanchat.view.component;

import javax.swing.JList;
import javax.swing.JScrollPane;

public class UserListScroller extends JScrollPane {
	
	private static final long serialVersionUID = 6319363143021539954L;

	public UserListScroller(JList list) {
		super(list);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	}
}
