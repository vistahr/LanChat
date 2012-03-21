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
package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.ChatMessage;
import de.vistahr.lanchat.model.RootViewModel;

public class ChatTest {

	RootViewModel cvd;
	
	@Before
	public void setUp() throws Exception {
		cvd = new RootViewModel();
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
