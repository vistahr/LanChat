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
package de.vistahr.lanchat.view.component;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.Iterator;

import javax.swing.JEditorPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.util.JLoggerUtil;

public class Chatbox extends JEditorPane {

	private static final long serialVersionUID = 4887666361002358164L;

	public Chatbox() {
		super();
		setEditable(false);
		setPreferredSize(new Dimension(320, 150));
		
		setContentType("text/html");
		HTMLEditorKit kit = new HTMLEditorKit();
		setEditorKit(kit);
		
		StyleSheet styleSheet = kit.getStyleSheet();
		styleSheet.addRule("body {color:#000; font-family:dialog; font-size:8.8px; }");
	}
	
	public void setContent(ArrayList<ChatMessage> entries) {
		String message = "<span style='color:#999999;font-style:italic;font-size:8px;'>connected...</span>";
		
		synchronized (entries) {
			Iterator<ChatMessage> itr = entries.iterator();
			while (itr.hasNext()) {
		    	try {
		    		message = message + "\n<br />" + itr.next().toString();
		    		
		    	} catch(ConcurrentModificationException e) {
		    		JLoggerUtil.getLogger().warn("ConcurrentModificationException in setContent of Chatbox");
		    		break;
		    	}
		    }
		    setText(message.toString());
		}
	}
	
}
