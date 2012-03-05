package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.ChatViewData;

public class ChatViewDataTest {

	ChatViewData cvd;
	
	@Before
	public void setUp() throws Exception {
		cvd = new ChatViewData();
	}

	@After
	public void tearDown() throws Exception {
		cvd = null;
	}

	@Test
	public void testChatData() {
		assertNotNull(cvd);
	}

	@Test
	public void testIsMute() {
		assertFalse(cvd.isMute());
	}

	@Test
	public void testSetMute() {
		cvd.setMute(true);
		assertTrue(cvd.isMute());
	}

	@Test
	public void testGetSetChatMessage() {
		String msg = cvd.getChatMessage();
		assertEquals("", msg);
		
		String testMessage = "wow, awesome msg";
		cvd.setChatMessage(testMessage);
		assertEquals(testMessage, cvd.getChatMessage());
	}

	@Test
	public void testGetSetChatname() {
		String name = cvd.getChatname();
		assertEquals("", name);
		
		String testChatname;
		
		testChatname = "coolNewChatuser_yeah";
		cvd.setChatname(testChatname);
		assertEquals(testChatname, cvd.getChatname());
		
		testChatname = "coolNewChatuser_yeahblaaaFooooBar";
		cvd.setChatname(testChatname);
		assertNotSame(testChatname, cvd.getChatname());
		
		testChatname = "häääüö";
		cvd.setChatname(testChatname);
		assertNotSame(testChatname, cvd.getChatname());
		
		testChatname = "User.WTF";
		cvd.setChatname(testChatname);
		assertNotSame(testChatname, cvd.getChatname());
	}

	@Test
	public void testGetEntries() {
		ArrayList<ChatMessage> listMsg = cvd.getEntries();
		assertTrue(listMsg.isEmpty());
	}
	
	@Test
	public void testAddEntry() {
		cvd.addEntry(new ChatMessage("argName", "bla message", new Date()));
		ArrayList<ChatMessage> listMsg = cvd.getEntries();
		assertEquals(1, listMsg.size());
	}

	@Test
	public void testGetLastEntry() {
		ChatMessage testInsertCM = new ChatMessage("argName", "bla message", new Date());
		cvd.addEntry(testInsertCM);
		ChatMessage cm = cvd.getLastEntry();
		assertEquals(testInsertCM, cm);
	}
	

}
