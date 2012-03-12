package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.Message;

public class MessageTest {

	Message message;
	
	@Before
	public void setUp() throws Exception {
		message = new Message("");
	}

	@After
	public void tearDown() throws Exception {
		message = null;
	}

	@Test
	public void testMessage() {
		assertNotNull(message);
	}

	@Test
	public void testGetSetMessage() {
		String testMsg;
		
		testMsg = message.getMessage();
		assertEquals("", testMsg);
		
		testMsg = "wtf! buggy message?";
		message.setMessage(testMsg);
		assertEquals(testMsg, message.getMessage());
		
		testMsg = "a temporary awesome testmessage :-X";
		message.setMessage(testMsg);
		assertEquals(testMsg, message.getMessage());
		
		testMsg = "häääüö < ??? !!!!";
		message.setMessage(testMsg);
		assertEquals(testMsg, message.getMessage());
		
		testMsg = "WTF. i dnt know";
		message.setMessage(testMsg);
		assertEquals(testMsg, message.getMessage());
	}



}
