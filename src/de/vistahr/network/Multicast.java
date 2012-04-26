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

package de.vistahr.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.Charset;

import javax.swing.event.EventListenerList;

import org.apache.commons.codec.binary.Base64;

import de.vistahr.lanchat.event.MulticastReceiveEvent;
import de.vistahr.network.listener.IMulticastReceiveListener;
import de.vistahr.util.logger.JLoggerUtil;


/**
 * Multicast support with socket handling, receive and send
 * functionality.
 * 
 * @author vistahr
 *
 */
public class Multicast {
	
	private String networkGroup;
	private int networkPort;

	private InetAddress group;
	private MulticastSocket socket = null;
	
	private static Charset ENCODING = Charset.defaultCharset();
	
	
	private EventListenerList listeners = new EventListenerList();
	
	
	/**
	 * Set up the basic connection data
	 * @param networkGroup
	 * 			Networkgroup
	 * @param networkPort
	 * 			Networkport
	 * @param multicastPort
	 * 			Multicastport
	 * @throws IOException 
	 */
	public Multicast(final String networkGroup,final int networkPort) throws IOException {
		this.networkGroup  = networkGroup;
		this.networkPort   = networkPort;
	}
	
	/**
	 * Get MulticastSocket
	 * @return MulticastSocket
	 */
	public MulticastSocket getSocket() {
		return socket;
	}

	/**
	 * Get Ntwourkgroup
	 * @return
	 */
	public String getNetworkGroup() {
		return networkGroup;
	}

	/**
	 * Get Networkport
	 * @return
	 */
	public int getNetworkPort() {
		return networkPort;
	}
	
	/**
	 * Open a new Socket with the existing connection data
	 * @return MulticastSocket object. This object is needed for sending and receiving data.
	 * @throws IOException
	 */
	public MulticastSocket openSocket() throws IOException {
		if(socket == null) {
			group  = InetAddress.getByName(networkGroup);
			socket = new MulticastSocket(networkPort);
			socket.joinGroup(group);
		}
		return socket;
	}
	
	/**
	 * Close the current socket
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {
		socket.leaveGroup(this.group);
		socket = null;
	}
	
	/**
	 * Send a given message
	 * @param stringMsg
	 * 			Message that will be sent
	 * @throws IOException
	 */
	public void send(String stringMsg) throws IOException { 
		// to base 64
		byte[] message64 = Base64.encodeBase64(stringMsg.getBytes());
		socket.send(new DatagramPacket(message64, message64.length , InetAddress.getByName(this.networkGroup) ,this.networkPort));
	}
	
	/**
	 * Open the socket and starts the infinity receiver loop.
	 * @param r 
	 * 			Receiver interface
	 * @throws IOException
	 */
	public void startReceive() throws IOException {
		byte[] bytes = new byte[65536]; 
	    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
	    
		 while(true) {
			try {
				socket.receive(packet);
			} catch (NullPointerException e) {
				JLoggerUtil.getLogger().warn("NullPointerException - Thread stopped in receiving. Break receiverloop.");
				break;
			}
			
			if(packet.getLength() != 0) {
				String message = new String(packet.getData(),0,packet.getLength());
				byte[] byteMsg = Base64.decodeBase64(message);
				notifyReceiver(new MulticastReceiveEvent(this, new String(byteMsg,ENCODING.toString())));
			}
		}
	}
	
	/**
	 * Get the networkdata
	 */
	@Override
	public String toString() {
		return this.networkGroup + ":" + this.networkPort; 
	}
	
	
	public void addReceiveListener(IMulticastReceiveListener listener) {
		listeners.add(IMulticastReceiveListener.class, listener);
	}
	
	
	public void removeReceiveListener(IMulticastReceiveListener listener) {
		listeners.remove(IMulticastReceiveListener.class, listener);
	}
	
	
	public synchronized void notifyReceiver(MulticastReceiveEvent event) {
		for(IMulticastReceiveListener l: listeners.getListeners(IMulticastReceiveListener.class)) {
			l.receive(event);
		}
	}
	
	
}