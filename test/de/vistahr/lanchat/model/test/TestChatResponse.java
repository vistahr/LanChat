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

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestChatResponse {
	
	MockChatResponse mock;
	Date now;
	
	@Before
	public void setUp() throws Exception {
		now = new Date();
		mock = new MockChatResponse("testname", "something written.", now, 96);
	}

	@After
	public void tearDown() throws Exception {
		mock = null;
	}


	
	@Test
	public void testChatResponseStringStringDate() {
		assertNotNull(new MockChatResponse("testname", "something written.", new Date()));
	}

	@Test
	public void testChatResponseStringStringDateInt() {
		assertNotNull(mock);
	}

	@Test
	public void testGetSetWritten() {
		assertEquals(now, mock.getWritten());
		Date later = new Date();
		mock.setWritten(later);
		assertEquals(later, mock.getWritten());
	}



	@Test
	public void testGetSetChatName() {
		assertEquals("testname", mock.getChatName().getName());
		mock.setChatName("newName");
		assertEquals("newName", mock.getChatName().getName());
	}



	@Test
	public void testGetSetChatMessage() {
		assertEquals("something written.", mock.getChatMessage().getMessage());
		mock.setChatMessage("simple message");
		assertEquals("simple message", mock.getChatMessage().getMessage());
	}


	@Test
	public void testGetID() {
		assertEquals(96, mock.getID());
	}




}
