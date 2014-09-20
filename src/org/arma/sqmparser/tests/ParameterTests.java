package org.arma.sqmparser.tests;

import static org.junit.Assert.*;
import org.junit.Test;
import org.arma.sqmparser.Parameter;

public class ParameterTests {
	@Test 
	public void testText() {
		Parameter newParameter = new Parameter("id=10");
		assertEquals("id=10", newParameter.getText());
	}
	@Test
	public void testValue() {
		Parameter newParameter = new Parameter("id=10");
		assertEquals("id", newParameter.getName());
		assertEquals("10", newParameter.getValue());
	}
}
