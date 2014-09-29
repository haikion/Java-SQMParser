package org.arma.sqmparser.tests;

import static org.junit.Assert.*;
import org.arma.sqmparser.SQMParser;
import org.junit.Test;

public class SQMParserTests {

	static String filePath1 = "testMissions/missionSimple.sqm";
	static String filePathOut1 = "testMissions/missionSimple_edited.sqm";
	static String filePathComplicated = "testMissions/mission_complicated.sqm";
	static String filePathOut2 = "testMissions/complicated_edited.sqm";

	@Test
	public void testGetClassText() 
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePath1);
		String text = parser.getClassTextByParameter("id", "1");
		assertTrue(text != null);
	}

	@Test
	public void testWrite()
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePath1);
		parser.deleteByParameter("id", "1");
		parser.write(filePathOut1);
	}
	@Test
	public void testWriteComplicated()
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePathComplicated);
		parser.deleteByParameter("id", "1");
		parser.write(filePathOut2);		
	}
}
