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
package de.vistahr.lanchat.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.vistahr.lanchat.listener.ReceiveListener;
import de.vistahr.lanchat.model.Name;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.view.component.MessageDialog;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.lanchat.view.listener.ChangeChatnameListener;
import de.vistahr.lanchat.view.listener.MuteListener;
import de.vistahr.lanchat.view.listener.RootWindowListener;
import de.vistahr.lanchat.view.listener.ScrollListener;
import de.vistahr.lanchat.view.listener.SendListener;
import de.vistahr.lanchat.view.listener.TrayMouseListener;
import de.vistahr.network.Multicast;

/**
 * Main Chatcontroller
 * @author vistahr
 */
public class RootController {
	
	private RootViewModel model;
	private RootView view;
	private Multicast mcast;
	
	private ExecutorService exec;
	
	
	// Multicast settings
	public static String MULTICAST_URL = "230.0.0.1";
	public static int MULTICAST_GROUP = 4447;

	
	public RootController(RootViewModel m, RootView v) {
		model = m;
		view  = v;
		
		// start
		startApplication();
		
		// run ui
		initData();
		addListeners();
	}
	
	
	private void initData() {
		// Set default Username
		try {
			model.setChatName(InetAddress.getLocalHost().getHostName());
		} catch (UnknownHostException e) {
			model.setChatName(Name.getDefaultFallbackName());
		}
	}
	
	
	private void startApplication() {
		// open multicast connection
		try {
			mcast = new Multicast(MULTICAST_URL, MULTICAST_GROUP);
			mcast.openSocket();
		} catch (IOException e) {
			new MessageDialog(e.getMessage());
		}
		
		// run receiverloop
		runReceiver();
	}
	
	
	private void runReceiver() {
		// Receivertaskloop
		exec = Executors.newSingleThreadExecutor();
		Runnable receiverTask = new Runnable() {
			@Override
			public void run() {
				try {
					mcast.startReceive();
					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		exec.submit(receiverTask);
	}
	

	/**
	 * Add listeners
	 */
	private void addListeners() {
		// receiver
		mcast.addReceiveListener(new ReceiveListener(model, view));
		// send 
		view.getSendbutton().addActionListener(new SendListener(model, view, mcast));
		// send 
		view.getSendbox().addActionListener(new SendListener(model, view, mcast));
		// mute
		view.getMutebutton().addActionListener(new MuteListener(model, view));
		// chatname
		view.getChatname().addFocusListener(new ChangeChatnameListener(model, view));
		// window listeners
		view.getMainframe().addWindowListener(new RootWindowListener(model, view, mcast, exec));
		// autoscroll to bottom // TODO cannot croll to top
		view.getChatscroller().getVerticalScrollBar().addAdjustmentListener(new ScrollListener(model, view));
		// tray icon
		view.getTray().getIcon().addMouseListener(new TrayMouseListener(model, view));
	}
	
	

}
