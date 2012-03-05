package de.vistahr.network.test;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.ChatPing;
import de.vistahr.lanchat.model.ChatResponse;
import de.vistahr.network.SLCP;

public class SLCPTest {
	
	SLCP slcp;
	ChatMessage globalSettedMsg;
	ChatPing globalSettedPing;
	
	String protocolv1MsgOutput;
	String protocolv1PingOutput;
	
	private final static int SLCP_VERSION = 1;
	
	final Date dateSend = new Date();
	
	
	@Before
	public void setUp() throws Exception {
		slcp = new SLCP(SLCP_VERSION);
		
		globalSettedMsg = new ChatMessage("testname", "testmessage", dateSend, 96);
		protocolv1MsgOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"message\" version=\"" + SLCP_VERSION + "\"><tstamp>" + dateSend.getTime() + "</tstamp><from>testname</from><message>testmessage</message><id>96</id></lanchat>";
		
		globalSettedPing = new ChatPing("testname", dateSend, 96);
		protocolv1PingOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"ping\" version=\"" + SLCP_VERSION + "\"><tstamp>" + dateSend.getTime() + "</tstamp><from>testname</from><id>96</id></lanchat>";
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

	@Test(expected=ParseException.class)
	public void testParseEmptyMessageString() throws ParseException {
		protocolv1MsgOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"message\" version=\"" + SLCP_VERSION + "\"><tstamp></tstamp><from></from><message></message><id></id></lanchat>";	
		slcp.parse(protocolv1MsgOutput);
	}	
	
	@Test(expected=ParseException.class)
	public void testParseWrongMessageString() throws ParseException {
		protocolv1MsgOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"message\" version=\"" + SLCP_VERSION + "\"><tstamp>äöä</tstamp><from>argh+##+?)</from><message></message><id>abc</id></lanchat>";	
		slcp.parse(protocolv1MsgOutput);
	}
	
	@Test
	public void testParseMessageString() throws ParseException {
		ChatResponse msgOutFromParse = slcp.parse(protocolv1MsgOutput);
		assertEquals(globalSettedMsg.toString(), msgOutFromParse.toString());
		assertNotNull(msgOutFromParse);
	}
	
	@Test
	public void testParsePingString() throws ParseException {
		ChatResponse pingOutFromParse = slcp.parse(protocolv1PingOutput);
		assertEquals(globalSettedPing.hashCode(), pingOutFromParse.hashCode());
		assertNotNull(pingOutFromParse);
	}
	
	
	@Test
	public void testGenerateMessage() {
		String xmlOutput = slcp.generate(globalSettedMsg, "message");
		assertEquals(protocolv1MsgOutput, xmlOutput);
		assertNotSame("", xmlOutput);
	}

	@Test
	public void testGeneratePing() {
		String xmlOutput = slcp.generate(globalSettedMsg, "ping");
		assertEquals(protocolv1PingOutput, xmlOutput);
		assertNotSame("", xmlOutput);
	}

	@Test
	public void testGenerate() {
		String xmlOutput = slcp.generate(globalSettedMsg, "message");
		assertEquals(protocolv1MsgOutput, xmlOutput);
		assertNotSame("", xmlOutput);
	}

}
