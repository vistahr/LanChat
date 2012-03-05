package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;

public class ChatMessageTest {

	ChatMessage cm;
	Date now;
	
	@Before
	public void setUp() {
		now = new Date();
		cm = new ChatMessage("testname", "arg ... doh", now, 96);
	}

	@After
	public void tearDown() throws Exception {
		cm = null;
	}
	
	
	@Test
	public void testChatMessage() {
		assertNotNull(cm);
	}


	@Test
	public void testGetSetWritten() {
		assertEquals(now.hashCode(), cm.getWritten().hashCode());
		Date later = new Date();
		cm.setWritten(later);
		assertEquals(later.hashCode(), cm.getWritten().hashCode());
	}

	@Test
	public void testGetSetChatName() {
		assertEquals("testname", cm.getChatName());
		cm.setChatName("wtf! buggy name?");
		assertEquals("wtf! buggy name?", cm.getChatName());
	}


	@Test
	public void testGetSetChatMessage() {
		assertEquals("arg ... doh", cm.getChatMessage());
		cm.setChatMessage("haha, sounds great!");
		assertEquals("haha, sounds great!", cm.getChatMessage());
	}



	@Test
	public void testGetSetID() {
		assertEquals(96, cm.getID());
		cm.setID(500);
		assertEquals(500, cm.getID());
	}



}
