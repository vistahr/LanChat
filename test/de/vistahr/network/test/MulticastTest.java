package de.vistahr.network.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.controller.ChatController;
import de.vistahr.network.Multicast;
import de.vistahr.network.Receivable;

public class MulticastTest {
	
	Multicast m;
	Thread t;
	
	@Before
	public void setUp() throws Exception {
		m = new Multicast(ChatController.MULTICAST_URL, ChatController.MULTICAST_GROUP, ChatController.MULTICAST_PORT);
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
					m.receive(new Receivable() {
						@Override
						public void onReceive(String data) {
							assertEquals(curMsg, data);
							
						}
					});
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
