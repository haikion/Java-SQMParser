/**
 * (A god class called) Element. Generalization of classes and mission root.
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class Element
{
	private static String PARAMETER_REGEX = ".*=.*";
	private static String ARRAY_REGEX = ".*\\[\\]=.*";
	private static Pattern NON_WHITESPACE_STRING_REGEX = Pattern.compile("\\S+"); 
	private ArrayList<Parameter> parameters_ = new ArrayList<Parameter>();
	private ArrayList<ClassNode> classNodes_ = new ArrayList<ClassNode>();
	private ArrayList<SQMArray> sQMArrays_ = new ArrayList<SQMArray>();
	
	private String text_;
	private static Logger logger = Logger.getLogger(Element.class);

	
	private static enum states { 
		CLASS_FOUND, //Class keyword was found
		IN_ROOT,  //Not inside classes
		IN_CLASS,  //Inside class parentheses
		CLASS_NAMED, //class name was found.
		IN_ARRAY //Inside SQM array
	}
	
	public Element(String text) 
	{
		text_ = "\n" + text;
	}
	
	//Gets top most classes (inner classes not included!)
	public ArrayList<ClassNode> readText(String text)
	{
		//State machine to go through the text word by word.
		states state = states.IN_ROOT;
		int beginIdx = 0, endIdx = 0, indent = 0;
		SQMArray newArray = null;
		Matcher matcher = NON_WHITESPACE_STRING_REGEX.matcher(text);
		//for ( String statement : text.split("\\s+") )
		while (matcher.find())
		{
			String statement = matcher.group();
			//logger.debug("Processing word: \"" + statement + 
			//	"\" State=" + state.toString() +
			//		" indent=" + indent);
			//indent watchers
			statement = statement.trim();
			if (statement.length() == 0) {
				continue;
			}
			if (state == states.IN_ARRAY || state == states.IN_CLASS)
			{
				if (statement.contains("{")) 
				{
					++indent;
				}
				if (statement.contains("}"))
				{
					--indent;
				}
			}
			
			if (state == states.IN_ROOT)
			{
				if (statement.equals("class")) 
				{
					beginIdx = matcher.start();
					state = states.CLASS_FOUND;
				}
				else if (statement.matches(ARRAY_REGEX))
				{
					newArray = new SQMArray(statement);
					if (statement.contains("}")) 
					{
						newArray.parseArrayLine(statement);
						logger.debug("Added new (one line) array name="+newArray.getName());						
						sQMArrays_.add(newArray);
					}
					else if ( statement.contains("{") )
					{
						indent = 1;
						state = states.IN_ARRAY;
					}
					else
					{
						indent = 0;
						state = states.IN_ARRAY;
					}
				}
				else if (statement.matches(PARAMETER_REGEX)) 
				{
					Parameter newParameter = new Parameter(statement);
					parameters_.add(newParameter);
				}
				
			}
			else if (state == states.CLASS_FOUND)
			{
				if ( statement.contains("{") )
				{
					indent = 1;
				}
				else
				{
					indent = 0;
				}
				state = states.IN_CLASS;
			}
			else if (state == states.IN_CLASS)
			{
				if (statement.contains("};") && indent == 0) 
				{
					endIdx = matcher.end();
					//Create new classNode and advance to next state
					String classText = text.substring(beginIdx, endIdx);
					ClassNode newClass = new ClassNode(classText, this);
					classNodes_.add(newClass);
					state = states.IN_ROOT;
				}
			}
			else if (state == states.IN_ARRAY)
			{
				newArray.add(statement);
				if (statement.contains("}") && indent == 0) 
				{
					sQMArrays_.add(newArray);
					logger.debug("Added new array name="+newArray.getName());
					state = states.IN_ROOT;
				}
			}
		}
		return classNodes_;
	}
	
	/*
	 * Attempts to add new parameter. returns false
	 * if parameter already exists
	 */
	public boolean addParameter(String name, String value)
	{
		if ( findParameter(name) != null )
		{
			return false;
		}
		Parameter newParameter = new Parameter(name+"="+value+";");
		parameters_.add(newParameter);
		return true;
	}
	/*
	 * Attempts to remove  a parameter. returns false 
	 * if parameter doesn't exist.
	 */
	public boolean removeParameter(String name)
	{
		for (int i = 0; i < parameters_.size(); ++i)
		{
			Parameter parameter = parameters_.get(i);
			if (parameter .getName().equals(name))
			{
				parameters_.remove(i);
			}
		}
		return true;
	}
	
	protected Parameter findParameter(String parameterName)
	{
		String name;
		
		for ( Parameter parameter : getParameters() )
		{
			name = parameter.getName();
			if (name.equals(parameterName))
			{
				return parameter;
			}
		}
		return null;
	}
	
	public boolean setParameterValue(String parameterName, String newValue)
	{
		Parameter parameter = findParameter(parameterName);
		if (parameter == null)
		{
			return false;
		}
		parameter.setValue(newValue);
		return true;
	}
	
	public ArrayList<Parameter> getParameters( )
	{
		return parameters_;
	}
	
	public ArrayList<SQMArray> getArrays( )
	{
		return sQMArrays_;
	}

	public String getText() {
		return text_;
	}
	
	public ArrayList<ClassNode> getChildren() {
		return classNodes_;
	}
	
	/*
	 * Searches through the mission and
	 * tries to return a class with given id.
	 */
	public ClassNode getClassByID(String id)
	{		
		ClassNode tmp;
		String childID;
		
		for (ClassNode child : classNodes_)
		{
			childID = child.getID();
			logger.debug("Searching id="+childID+" name="+
					child.getName()+" ChildrenCount="+child.getChildren().size());
			if (id.equals(childID))
			{
				return child;
			}
			else if ( (tmp = child.getClassByID(id)) != null)
			{
				return tmp;
			}
		}
		return null;
	}

	public void setText(String text) 
	{
		text_ = text;
	}
	public void updateText()
	{
		text_ = "";
		//Write array parameters
		for (SQMArray sArray : getArrays())
		{
			text_ += sArray.getText()+"\n";
		}
		//Write normal parameters
		for (Parameter parameter : getParameters() )
		{
			text_ += parameter.getText();
		}
		//Write child classes
		for (ClassNode child : getChildren())
		{
			text_ += child.getText();
		};
	}
	
}
