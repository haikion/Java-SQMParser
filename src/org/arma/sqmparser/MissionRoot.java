/*
 * Handles everything related to the mission.sqm root
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */


package org.arma.sqmparser;

import java.util.ArrayList;
import java.util.Stack;

public class MissionRoot extends Element
{	
	public MissionRoot( String text )
	{
		super(text);
		super.readText(text);
	}
	
	/**
	 * @return all of the classes
	 */
	public ArrayList<ClassNode> getAllClasses()
	{
		ArrayList<ClassNode> allClasses = new ArrayList<ClassNode>();
		Stack<ClassNode> stack = new Stack<ClassNode>();
		//Fill the stack. missionRoot is not classNode so
		//has to be done separately.
		for (ClassNode classNode : getChildren())
		{
			stack.push(classNode);
		}
		
		while (!stack.empty())
		{
			ClassNode classNode = stack.pop();
			allClasses.add(classNode);
			for (ClassNode child : classNode.getChildren())
			{
				//Put up for processing
				stack.push(child);
			}
		}
		return allClasses;
	}
}