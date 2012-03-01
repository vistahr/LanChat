package de.vistahr.network.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.Message;
import de.vistahr.network.SLCP;

public class SLCPTest {
	
	SLCP slcp;
	ChatMessage globalSettedMsg;
	
	String protocolv1MsgOutput;
	String protocolv1PingOutput;
	
	private final static int SLCP_VERSION = 1;
	
	
	@Before
	public void setUp() throws Exception {
		slcp = new SLCP(SLCP_VERSION);
		
		Date dateSend = new Date();
		
		globalSettedMsg = new ChatMessage("testname", "testmessage", dateSend, 96);
		protocolv1MsgOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"message\" version=\"" + SLCP_VERSION + "\"><date>" + dateSend.toString() + "</date><from>testname</from><message>testmessage</message><id>96</id></lanchat>";
		protocolv1PingOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"ping\" version=\"" + SLCP_VERSION + "\"><from>testname</from><id>96</id></lanchat>";
		
	}

	@After
	public void tearDown() throws Exception {
		slcp = null;
		globalSettedMsg = null;
		protocolv1MsgOutput = "";
		protocolv1PingOutput = "";
	}

	@Test
	public void testSLCP() {
		assertNotNull(slcp);
	}

	@Test
	public void testParseByteArray() throws ParseException {
		fail("not implemented");
	}

	@Test
	public void testParseString() throws ParseException {
		Message msgOutFromParse = slcp.parse(protocolv1MsgOutput);
		assertEquals(globalSettedMsg, msgOutFromParse);
	}

	@Test
	public void testGenerateMessage() {
		String xmlOutput = slcp.generate(globalSettedMsg, "message");
		assertEquals(protocolv1MsgOutput, xmlOutput);
	}

	@Test
	public void testGeneratePing() {
		String xmlOutput = slcp.generate(globalSettedMsg, "ping");
		assertEquals(protocolv1PingOutput, xmlOutput);
	}

	@Test
	public void testGenerate() {
		String xmlOutput = slcp.generate(globalSettedMsg, "message");
		assertEquals(protocolv1MsgOutput, xmlOutput);
	}

}
