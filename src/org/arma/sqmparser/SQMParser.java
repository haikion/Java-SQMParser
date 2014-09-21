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
	private String filePath_ = "/home/niko/mission.sqm";
	private MissionRoot missionRoot_;
	private String fileString_;
	
	static String readFile(String path) 
			  throws IOException 
	{ 
		Charset encoding =  StandardCharsets.UTF_8;
		
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}
	
	public boolean parseFile( String filePath )
	{
		filePath_ = filePath;
		try {
			fileString_ = readFile(filePath_);
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

	public String getClassString(String id) {
		ClassNode  cNode = getClassByID(id);
		if (cNode == null)
		{
			return null;
		}
		return cNode.getText();
	}
}
