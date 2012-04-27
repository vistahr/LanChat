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

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.ChatPing;
import de.vistahr.lanchat.model.AbstractChatResponse;
import de.vistahr.network.SLCP;
import de.vistahr.util.NetworkUtil;

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
		
		globalSettedMsg = new ChatMessage("testname", "testmessage", dateSend, NetworkUtil.getMacAddressHash());
		protocolv1MsgOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"message\" version=\"" + SLCP_VERSION + "\"><tstamp>" + dateSend.getTime() + "</tstamp><from>testname</from><message>testmessage</message><id>" + NetworkUtil.getMacAddressHash() + "</id></lanchat>";
		
		globalSettedPing = new ChatPing("testname", NetworkUtil.getMacAddressHash());
		protocolv1PingOutput = "<?xml version=\"1.0\" encoding=\"" + SLCP.ENCODING + "\" standalone=\"no\"?><lanchat type=\"ping\" version=\"" + SLCP_VERSION + "\"><from>testname</from><id>" + NetworkUtil.getMacAddressHash() + "</id></lanchat>";
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
		AbstractChatResponse msgOutFromParse = slcp.parse(protocolv1MsgOutput);
		assertEquals(globalSettedMsg.toString(), msgOutFromParse.toString());
		assertNotNull(msgOutFromParse);
	}
	
	@Test
	public void testParsePingString() throws ParseException {
		AbstractChatResponse pingOutFromParse = slcp.parse(protocolv1PingOutput);
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
