package org.arma.sqmparser.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.arma.sqmparser.ClassNode;
import org.arma.sqmparser.MissionRoot;
import org.junit.Test;

public class MissionRootTests {
	MissionRoot missionRoot_;
	String rootTextClassInside = "version=11" + "\n" +
								  "class groups\n" +
		      					  "{\n" +
		      					  "	items=1;" + "\n" +
		      					  "	class item0\n" +
		      					  "	{\n" +
		      					  "		id=1;"		+"\n"+
		      					  "		position[]={3445.2603,19,3613.9331};\n" +
		      					  "	};\n" +
		      					  "};\n";
	
	@Test
	public void testGetAllClasses() 
	{
		missionRoot_ = new MissionRoot(rootTextClassInside);
		assertEquals(2, missionRoot_.getAllClasses().size());
	}
	@Test
	public void testGetClassesByName() 
	{
		missionRoot_ = new MissionRoot(rootTextClassInside);
		ArrayList<ClassNode> classNodes = missionRoot_.getClassesByName("groups");
		assertEquals(1, classNodes.size());		
	}
}
