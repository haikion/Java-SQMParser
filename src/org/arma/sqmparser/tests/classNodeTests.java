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
	String classTextArray = "class Item0\n" +
							"{\n" +
							"	position[]={3445.2603,19,3613.9331};" +
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
	
	String classTextArrayMultiline = "class Item0\n" +
									 "{\n" +
									 "	position[]=" +
									 "  {	 	   3445.2603," +
									 " 			   19," +
									 " 			   3613.9331" +
									 "  };" +
									 "};";
	@Test
	public void constructorTest()
	{
		ClassNode node = new ClassNode(classTextSimple, null);
		assertEquals("\n"+classTextSimple, node.getText());
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
		assertEquals("\n"+classTextArray, node.getText());
		ArrayList<SQMArray> parameters = node.getArrays();
		assertEquals(1, parameters.size());
	}
	@Test
	public void arrayTestLines()
	{
		ClassNode node = new ClassNode(classTextArrayMultiline, null);
		assertEquals("\n"+classTextArrayMultiline, node.getText());
		ArrayList<SQMArray> parameters = node.getArrays();
		assertEquals(1, parameters.size());
	}
	@Test
	public void classIndsideClass()
	{
		ClassNode node = new ClassNode(classTextClassInside, null);
		assertEquals("\n"+classTextClassInside, node.getText());
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
		ClassNode child = node.getClassByID("1");
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
	}
}
