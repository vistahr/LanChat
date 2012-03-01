package de.vistahr.network.test;

import static org.junit.Assert.*;

import java.io.IOException;

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
		m = new Multicast(ChatController.MULTICAST_URL, ChatController.MULTICAST_PORT);
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
	

	@Test(timeout = 500)
	public void testSend() throws IOException {
		final String testSendString = "testSend";
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					m.receive(new Receivable() {
						@Override
						public void onReceive(String data) {
							assertEquals(testSendString, data);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		m.send(testSendString);
	}

	@Test(timeout = 500)
	public void testReceive() throws IOException {
		final String testSendString = "testReceive";
		t = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					m.receive(new Receivable() {
						@Override
						public void onReceive(String data) {
							assertEquals(testSendString, data);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		m.send(testSendString);
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
