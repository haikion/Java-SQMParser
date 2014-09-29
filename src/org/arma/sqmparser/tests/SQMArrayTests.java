package org.arma.sqmparser.tests;

import static org.junit.Assert.*;

import org.arma.sqmparser.SQMArray;
import org.junit.Test;

public class SQMArrayTests {

	String arrayLine1 = "					position[]={3129.9126,29.2691,4325.1934};";

	String arrayLines2 = "        	addOns[]="                          + "\n" +
									"{"                                 + "\n" +
										"\"utes\""                      + "\n" +
									"};"; 
	
	//@Test
	public void singleLine() 
	{
		SQMArray testArray = new SQMArray(arrayLine1);
		
		assertEquals("position[]={3129.9126,29.2691,4325.1934};", testArray.getText());
	}

	@Test
	public void name()
	{
		SQMArray testArray = new SQMArray(arrayLine1);
		assertEquals("position", testArray.getName());
	}
}
