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

package de.vistahr.lanchat;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

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
	private int multicastPort;
	
	private InetAddress group;
	private MulticastSocket socket;
	
	/**
	 * Set up the basic connection data
	 * @param networkGroup
	 * 			Networkgroup
	 * @param networkPort
	 * 			Networkport
	 * @param multicastPort
	 * 			Multicastport
	 */
	public Multicast(String networkGroup,int networkPort,int multicastPort) {
		this.networkGroup  = networkGroup;
		this.networkPort   = networkPort;
		this.multicastPort = multicastPort;
	}
	
	/**
	 * Close the current socket
	 * @throws IOException
	 */
	public void closeSocket() throws IOException {
		this.socket.leaveGroup(this.group);
	}
	
	/**
	 * Open a new Socket with the existing connection data
	 * @return MulticastSocket object. This object is needed for sending and receiving data.
	 * @throws IOException
	 */
	public MulticastSocket openSocket() throws IOException {
		this.group  = InetAddress.getByName(this.networkGroup);
		this.socket = new MulticastSocket(this.networkPort);
		socket.joinGroup(this.group);
		
		return this.socket;
	}
	
	/**
	 * Send a given message
	 * @param stringMsg
	 * 			Message that will be sent
	 * @throws IOException
	 */
	public void send(String stringMsg) throws IOException {
		MulticastSocket socket =  new MulticastSocket(this.multicastPort);
		byte[] message = stringMsg.getBytes("UTF8");
		// to base 64
		String message64 = new BASE64Encoder().encode(stringMsg.getBytes("UTF8")); 
		message = message64.getBytes("UTF8");
		socket.send(new DatagramPacket(message, message.length , InetAddress.getByName(this.networkGroup) ,this.networkPort));
	}
	
	/**
	 * Open the socket and starts the receiving loop.
	 * @param r 
	 * 			Receiver interface
	 * @throws IOException
	 */
	public void receive(Receiver r) throws IOException {
		MulticastSocket socket = this.openSocket();
		
		byte[] bytes = new byte[65536]; 
	    DatagramPacket packet = new DatagramPacket(bytes, bytes.length);
	    
    
		while(true) { 
			socket.receive(packet);
			if(packet.getLength() != 0) {
				String message = new String(packet.getData(),0,packet.getLength(), "UTF8"); 
				byte[] byteMsg = new BASE64Decoder().decodeBuffer(message);
				r.onReceive(new String(byteMsg));
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
	
	
}