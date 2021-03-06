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
import java.util.Timer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.vistahr.lanchat.listener.ReceiveListener;
import de.vistahr.lanchat.model.Name;
import de.vistahr.lanchat.model.RootViewModel;
import de.vistahr.lanchat.task.SendPingTask;
import de.vistahr.lanchat.view.component.MessageDialog;
import de.vistahr.lanchat.view.component.RootView;
import de.vistahr.lanchat.view.listener.ChangeChatnameListener;
import de.vistahr.lanchat.view.listener.ChatboxMouseListener;
import de.vistahr.lanchat.view.listener.ChatboxPopupClearListener;
import de.vistahr.lanchat.view.listener.MuteListener;
import de.vistahr.lanchat.view.listener.RootWindowListener;
import de.vistahr.lanchat.view.listener.ScrollListener;
import de.vistahr.lanchat.view.listener.SendListener;
import de.vistahr.lanchat.view.listener.SendboxMouseListener;
import de.vistahr.lanchat.view.listener.TrayMouseListener;
import de.vistahr.network.Multicast;
import de.vistahr.util.JLoggerUtil;

/**
 * Main Chatcontroller
 * @author vistahr
 */
public class RootController {
	
	private RootViewModel model;
	private RootView view;
	private Multicast mcast;
	
	private ExecutorService exec;
	private Timer timer;
	
	
	// Multicast settings
	public static String MULTICAST_URL = "230.0.0.1";
	public static int MULTICAST_GROUP  = 4447;

	
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
		
		view.getSendbox().requestFocus();
	}
	
	
	private void startApplication() {
		// open multicast connection
		try {
			mcast = new Multicast(MULTICAST_URL, MULTICAST_GROUP);
			mcast.openSocket();
			
		} catch (IOException e) {
			new MessageDialog(e.getMessage());
		}
		
		// Executer serverive
		exec = Executors.newCachedThreadPool();
		
		// run receiverloop
		runReceiverTask();
		runPingTask();
	}
	
	

	private void runPingTask() {
	    Runnable pingTask = new Runnable() {
			@Override
			public void run() {
				timer = new Timer();
				timer.schedule(new SendPingTask(model, mcast) ,100 ,5000);
			}
		};
		exec.execute(pingTask);
	}
	
	
	private void runReceiverTask() {
		// Receivertaskloop
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
		exec.execute(receiverTask);
	}
	

	/**
	 * Add listeners
	 */
	private void addListeners() {
		mcast.addReceiveListener(new ReceiveListener(model, view));
		view.getSendbutton().addActionListener(new SendListener(model, view, mcast));
		view.getSendbox().addActionListener(new SendListener(model, view, mcast));
		view.getMutebutton().addActionListener(new MuteListener(model, view));
		view.getChatname().addFocusListener(new ChangeChatnameListener(model, view));
		view.getMainframe().addWindowListener(new RootWindowListener(model, view, mcast, exec, timer));
		view.getChatscroller().getVerticalScrollBar().addAdjustmentListener(new ScrollListener(model, view));
		try {
			view.getTray().getIcon().addMouseListener(new TrayMouseListener(model, view));
		} catch (IllegalAccessException e) {
			JLoggerUtil.getLogger().warn("Tray not supported");
		}
		view.getChatbox().addMouseListener(new ChatboxMouseListener(model, view));
		view.getChatboxPopup().getClearItem().addActionListener(new ChatboxPopupClearListener(model, view));
		view.getSendbox().addMouseListener(new SendboxMouseListener(model, view));
	}
	
	

}
