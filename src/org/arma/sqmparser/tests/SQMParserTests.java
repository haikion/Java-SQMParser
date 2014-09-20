package org.arma.sqmparser.tests;

import static org.junit.Assert.*;
import org.arma.sqmparser.SQMParser;
import org.junit.Test;

public class SQMParserTests {

	static String filePath1 = "testMissions/missionSimple.sqm";
	static String filePathOut1 = "testMissions/missionSimple_edited.sqm";
	
/*	@Test
	public void testParse() 
	{	
		SQMParser parser = new SQMParser();
		
		assertTrue(parser.parse(filePath1));
	}

	@Test
	public void testDeleteString() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteInteger() 
	{
		fail("Not yet implemented");
	}
*/
	@Test
	public void testGetClassString() 
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePath1);
		String text = parser.getClassString("1");
		assertTrue(text != null);
	}

	@Test
	public void testWrite()
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePath1);
		parser.deleteByID("1");
		parser.write(filePathOut1);
	}
/*
	@Test
	public void testWrite() 
	{
		fail("Not yet implemented");
	}
*/
}
