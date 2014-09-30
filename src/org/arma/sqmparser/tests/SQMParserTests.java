package org.arma.sqmparser.tests;

import static org.junit.Assert.*;
import org.arma.sqmparser.ClassNode;
import org.arma.sqmparser.SQMParser;
import org.junit.Test;

public class SQMParserTests {

	final static String filePath1 = "testMissions/missionSimple.sqm";
 	final static String filePathOut1 = "testMissions/missionSimple_edited.sqm";
	final static String filePathComplicated = "testMissions/mission_complicated.sqm";
	final static String filePathComplicatedOut = "testMissions/complicated_edited.sqm";
	final static String filePathArray = "testMissions/missionArray.sqm";
 	final static String filePathArrayOut = "testMissions/missionArray_edited.sqm";
	
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
		parser.write(filePathComplicatedOut);		
	}
	@Test
	public void testReadArray()
	{
		SQMParser parser = new SQMParser();
		
		parser.parseFile(filePathArray);
		ClassNode node = parser.getClassesByName("Mission").get(0);
		assertEquals(3, node.getArrays().get(0).getValues().size());
	}
}
