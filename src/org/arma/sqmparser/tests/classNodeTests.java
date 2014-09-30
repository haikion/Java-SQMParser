package org.arma.sqmparser.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.arma.sqmparser.ClassNode;
import org.arma.sqmparser.Parameter;
import org.arma.sqmparser.SQMArray;
import org.junit.Test;

public class classNodeTests 
{
	
	String classTextSimple = "class Item0\n" +
				  			 "{\n" +
				  			 "	id=2;\n" +
				  			 "};";
	String classTextParameter = "class Item0\n" +
 			 				 			 "{\n" +
 			 				 			 "init=this addweapon \"\"ACRE_PRC148_UHF\"\";" + "\n" +
 			 				 			 "};";
	String classTextValue = "class Item0\n" +
							"{\n" +
							"	id=3;\n" +
							"};";	
	String classTextArray = "class Item0"								+ "\n" +
							"{"											+ "\n" +
							"	position[]={3445.2603,19,3613.9331};"	+ "\n" +
							"};";
	String classTextClassInside = "class groups\n" +
							      "{\n" +
							      "	items=1;" + "\n" +
							      "	class item0\n" +
							      "	{\n" +
							      "		id=1;"		+"\n"+
							      "		position[]={3445.2603,19,3613.9331};\n" +
							      "	};\n" +
							      "};\n";
	
	String classTextArrayMultiline = "class Item0"				+"\n"+
									 "{"						+"\n"+
									 "	position[]="			+"\n"+
									 "  {	 	   3445.2603,"	+"\n"+
									 " 			   19,"			+"\n"+
									 " 			   3613.9331"	+"\n"+
									 "  };"						+"\n"+
									 "};";
	
	String classTextArrayMultiline2 = 
									  "   "								+ "\n" +
									  "class Mission"					+ "\n" +
									  "{"								+ "\n" +
									  "  	addOns[]="					+ "\n" +
									  "		{" 							+ "\n" +
									  "			\"hellskitchen\"," 		+ "\n" +
									  "			\"cacharacters_e\"," 	+ "\n" +
									  "			\"caweapons_e\","		+ "\n" +
									  "			\"ace_c_men\"," 		+ "\n" +
									  "			\"CA_Modules_Alice2\"," + "\n" +
									  "			\"ca_modules_silvie\"," + "\n" +
									  "		};"							+ "\n" +
									  "};";
	
	String classTextArrayMultiline3 = 	""									+ "\n" +
										"class Mission"						+ "\n" +
										"{"									+ "\n" +
										"	parameter=22;"					+ "\n" +
										"	addOns[]="						+ "\n" +
										"	{"								+ "\n" +
										"		\"acex_ru_men_naval\","		+ "\n" +
										"		\"smd_units\","				+ "\n" +
										"		\"utes\""					+ "\n" +
										"	};"								+ "\n" +
										"};";
	
	
	@Test
	public void getClassByParameterTest()
	{
		ClassNode node = new ClassNode(classTextClassInside, null);
		ClassNode found = node.getClassByParameter("id", "1");
		assertTrue(found != null);
	}
	@Test
	public void constructorTest()
	{
		ClassNode node = new ClassNode(classTextSimple, null);
		assertEquals(classTextSimple, node.getText());
		ArrayList<Parameter> parameters = node.getParameters();
		assertEquals(1, parameters.size());
		Parameter para = parameters.get(0);
		assertEquals("id", para.getName());
		assertEquals("2", para.getValue());
	}
	@Test
	public void arrayTestAmount()
	{
		ClassNode node = new ClassNode(classTextArray, null);
		assertEquals(classTextArray, node.getText());
		ArrayList<SQMArray> parameters = node.getArrays();
		assertEquals(1, parameters.size());
	}
	@Test
	public void arrayTestLines()
	{
		ClassNode node = new ClassNode(classTextArrayMultiline, null);
		assertEquals(classTextArrayMultiline, node.getText());
		ArrayList<SQMArray> parameters = node.getArrays();
		assertEquals(1, parameters.size());
	}
	@Test
	public void arrayTestLines2()
	{
		ClassNode node = new ClassNode(classTextArrayMultiline2, null);
		ArrayList<String> parameters = node.getArrays().get(0).getValues();
		assertEquals(7, parameters.size());
	}
	@Test
	public void arrayTestLines3()
	{
		ClassNode node = new ClassNode(classTextArrayMultiline3, null);
		ArrayList<String> parameters = node.getArrays().get(0).getValues();
		assertEquals(3, parameters.size());
	}
	@Test
	public void classIndsideClass()
	{
		ClassNode node = new ClassNode(classTextClassInside, null);
		assertEquals(classTextClassInside, node.getText());
		ArrayList<ClassNode> parameters = node.getChildren();
		assertEquals(1, parameters.size());
	}
	@Test
	public void valueChange()
	{
		ClassNode node = new ClassNode(classTextSimple, null);
		node.setParameter("id", "3");
		node.updateText();
		assertEquals("\n"+classTextValue, node.getText());
	}
	@Test
	public void deleteChild()
	{
		ClassNode node = new ClassNode(classTextClassInside, null);
		assertEquals(1, node.getChildren().size());
		ClassNode child = node.getClassByParameter("id", "1");
		child.delete();
		assertEquals(0, node.getChildren().size());
	}
	@Test
	public void testParameter()
	{
		ClassNode node = new ClassNode(classTextParameter, null);
		Parameter parameter = node.getParameter("init");
		assertEquals("this addweapon \"\"ACRE_PRC148_UHF\"\"", parameter.getValue());
	}
	@Test
	public void testArrayGet()
	{
		ClassNode node = new ClassNode(classTextClassInside, null);
		//position[]={3445.2603,19,3613.9331};" +
		ArrayList<String> values = new ArrayList<String>();
		values.add("3445.2603");
		values.add("19");
		values.add("3613.9331");
		ClassNode found = node.getClassByArray("position", values);
		assertTrue(found != null);
	}
}
