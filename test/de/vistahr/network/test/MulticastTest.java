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
package de.vistahr.network.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.controller.RootController;
import de.vistahr.network.Multicast;

public class MulticastTest {
	
	Multicast m;
	Thread t;
	
	@Before
	public void setUp() throws Exception {
		m = new Multicast(RootController.MULTICAST_URL, RootController.MULTICAST_GROUP);
		try {
			m.openSocket();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@After
	public void tearDown() throws Exception {
		m = null; // shutdown multicast
		t = null; // shutdown thread
	}

	@Test
	public void testMulticast() {
		assertNotNull(m);
	}
	
	@Test
	public void testOpenSocket() {
		assertNotNull(m.getSocket());
	}
	

	@Test
	public void testSendAndReceive() throws IOException {
		ArrayList<String> sStrings = new ArrayList<String>();
		sStrings.add("123");
		sStrings.add("abc");
		sStrings.add("öäü");
		sStrings.add("?!=$%&/(-.,;");
		sStrings.add("This is a message users can send o.O ;-) ");
		
		Iterator<String> iter = sStrings.iterator();
		while(iter.hasNext()) {
			sendAndReceive(iter.next());
		}
	}
	
	
	public void sendAndReceive(final String curMsg) throws IOException {
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					m.startReceive();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		m.send(curMsg);
	}



	@Test
	public void testToString() {
		assertEquals(m.getNetworkGroup() + ":" + m.getNetworkPort() , m.toString());
	}
	
	
	@Test 
	public void testCloseSocket() throws IOException {
		m.closeSocket();
		assertNull(m.getSocket());
	}
	
}
