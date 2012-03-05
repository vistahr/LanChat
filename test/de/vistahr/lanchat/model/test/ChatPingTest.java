package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatPing;

public class ChatPingTest {

	ChatPing cp;
	Date now;
	
	@Before
	public void setUp() {
		now = new Date();
		cp = new ChatPing("testname", now, 96);
	}

	@After
	public void tearDown() throws Exception {
		cp = null;
	}
	
	@Test
	public void testChatPing() {
		assertNotNull(cp);
	}

	@Test
	public void testGetSetWritten() {
		assertEquals(now.hashCode(), cp.getWritten().hashCode());
		Date later = new Date();
		cp.setWritten(later);
		assertEquals(later.hashCode(), cp.getWritten().hashCode());
	}

	@Test
	public void testGetSetChatName() {
		assertEquals("testname", cp.getChatName());
		cp.setChatName("wtf! buggy name?");
		assertEquals("wtf! buggy name?", cp.getChatName());
	}



	@Test
	public void testGetSetID() {
		assertEquals(96, cp.getID());
		cp.setID(500);
		assertEquals(500, cp.getID());
	}



}
