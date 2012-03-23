package de.vistahr.lanchat.model.test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.vistahr.lanchat.model.Name;

public class NameTest {


	Name name;
	
	@Before
	public void setUp() throws Exception {
		name = new Name("testname");
	}

	@After
	public void tearDown() throws Exception {
		name = null;
	}

	@Test
	public void testName() {
		assertNotNull(name);
	}

	@Test
	public void testGetSetName() {
		String n;
		
		n = name.getName();
		assertEquals("testname", n);

		n = "coolNewChatuser_yeah";
		name.setName(n);
		assertEquals(n, name.getName());
		
		
		n = "wtf! buggy name?";
		name.setName(n);
		assertNotSame(n, name.getName());
		
		
		n = "coolNewChatuser_yeahblaaaFooooBar";
		name.setName(n);
		assertNotSame(n, name.getName());
		
		n = "häääüö";
		name.setName(n);
		assertNotSame(n, name.getName());
		
		n = "User.WTF";
		name.setName(n);
		assertNotSame(n, name.getName());
	}
	
	
	@Test 
	public void testSetNameLength() {
		name.setName("asdasdadfsadgfasgsdfgdfgadfgdafgasdfagasdasdadfsadgfasgsdfgdfgadfgdafgasdfagasdasdadfsadgfasgsdfgdfgadfgdafgasdfagasdasdadfsadgfasgsdfgdfgadfgdafgasdfag");
		assertTrue(name.getName().length()<=Name.getMaximumNameLength());
	}

	
	@Test(expected=IllegalArgumentException.class)
	public void testSetNameIllegalArgumentException() {
		String testChatname = "";
		name.setName(testChatname);
	}
	
	@Test(expected=IllegalStateException.class)
	public void testSetNameNullPointerException() {
		String testChatname = null;
		name.setName(testChatname);
	}
	
}
