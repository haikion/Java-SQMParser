/**
 * @(#) SQMParser.java
 * SQM Parser class. Allows easy editing of SQM files through Java application.
 * Supports: deletion of classes, parameter adding, parameter removal, parameter editing
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

public class SQMParser
{
	private String filePath_;
	private MissionRoot missionRoot_;
	private String fileString_;
	
	private static String readFileToString(String path) 
			  throws IOException 
	{ 
		Charset encoding =  StandardCharsets.UTF_8;
		
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	/**
	 * Parses the contents of a mission.sqm file.
	 * @param filePath is path to the file
	 * @return returns true if the file was succesfully read
	 */
	public boolean parseFile( String filePath )
	{
		filePath_ = filePath;
		try {
			fileString_ = readFileToString(filePath_);
		} catch (IOException e) {
			return false;
		}
		missionRoot_ = new MissionRoot(fileString_);
		return true;
	}
	
	/**
	 * @return all of the classes
	 */
	public ArrayList<ClassNode> getAllClasses()
	{
		return missionRoot_.getAllClasses();
	}
	
	/**
	 * @param id is a id parameter value of a class to be removed
	 * @return returns true if class was deleted
	 */
	public boolean deleteByID( String id )
	{
		ClassNode classNode = missionRoot_.getClassByID(id);
		if (classNode == null)
		{
			return false;
		}
		classNode.delete();
		return true;
	}

	/**
	 * @param name is a name of a classes to be searched.
	 * @return ArrayList of ClassNodes with given name
	 */
	public ArrayList<ClassNode> getClassesByName(String name)
	{
		return missionRoot_.getClassesByName(name);
	}	
	
	/**
	 * Finds classes with given ID
	 * @param "id" is id parameter value of a class to be searched.
	 * @return Found classNode or null if nothing was found
	 */
	public ClassNode getClassByID( String id )
	{
		ClassNode node;
		try {
			node = missionRoot_.getClassByID(id);
		} 
		catch (java.lang.NullPointerException e)
		{
			return null;
		}
		return node;
	}
	
	/**
	 * 
	 * @param name is name of the parameter
	 * @param value is value of the parameter
	 * @return class with given parameter with the given value
	 */	
	public ClassNode getClassByParameter( String name, String value )
	{
		ClassNode node;
		try {
			node = missionRoot_.getClassByParameter(name, value);
		} 
		catch (java.lang.NullPointerException e)
		{
			return null;
		}
		return node;
	}	

	/**
	 * 
	 * @param name is name of the array
	 * @param values is value of the array
	 * @return class with given parameter with the given value
	 */	
	public ClassNode getClassByArray( String name, ArrayList<String> values )
	{
		ClassNode node;
		try {
			node = missionRoot_.getClassByArray(name, values);
		} 
		catch (java.lang.NullPointerException e)
		{
			return null;
		}
		return node;
	}	
	
	/**
	 * @param outputFilePath is path to the file to be written
	 * @return returns true if a file was written
	 */
	public boolean write( String outputFilePath )
	{
		 Writer writer = null;
		 
		 try 
		 {
		     writer = new BufferedWriter(new OutputStreamWriter(
		           new FileOutputStream(outputFilePath), "utf-8"));
		     writer.write(missionRoot_.getText());
		 } catch (IOException ex) 
		 {
		   // report
		 } 
		 try 
		 {
			 writer.close();
			 return true;
		 } 
		 catch (Exception ex) {}
		 return false;
	}

	/**
	 * @param id is a id parameter value to be found
	 * @return Returns string of a class (Same as copy paste from the mission.sqm)
	 */
	public String getClassStringByID(String id) {
		ClassNode  cNode = getClassByID(id);
		if (cNode == null)
		{
			return null;
		}
		return cNode.getText();
	}
}
