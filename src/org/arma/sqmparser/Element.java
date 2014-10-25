/**
 * (A god class called) Element. Generalization of classes and mission root.
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;

public abstract class Element
{
	//Matches parameter
	private final static String PARAMETER_REGEX = ".+=.+;";
	//Matches Array parameter
	private final static String ARRAY_REGEX = ".+\\[\\]=[\\s|\\S]*?\\};";
	//Matches class definition. Not used because fails when class is inside class.
	//private final static String CLASS_REGEX = "class(.|\n)+};";
	private final static String WORD_REGEX ="\\S+";
	private final static Pattern STATEMENT_REGEX = Pattern.compile(ARRAY_REGEX+"|"+PARAMETER_REGEX+"|"+WORD_REGEX); //Pattern.compile(".+=.+\";|\\S+"); 
	private ArrayList<Parameter> parameters_ = new ArrayList<Parameter>();
	private ArrayList<ClassNode> children_ = new ArrayList<ClassNode>();
	private ArrayList<SQMArray> sQMArrays_ = new ArrayList<SQMArray>();
	
	private String text_;
	private static Logger logger = Logger.getLogger(Element.class);

	
	private static enum states { 
		CLASS_FOUND, //Class keyword was found
		IN_ROOT,  //Not inside classes
		IN_CLASS,  //Inside class parentheses
		CLASS_NAMED //class name was found.
	}
	
	public Element(String text) 
	{
		text_ = fixIndent(text);
	}
	
	public ClassNode getChildByName(String name)
	{
		for (ClassNode child : children_)
		{
			if (name.equals(child.getName()))
			{
				return child;
			}
		}
		return null;
	}
	
	//Gets top most classes (inner classes not included!)
	public ArrayList<ClassNode> readText(String text)
	{
		//State machine to go through the text word by word.
		states state = states.IN_ROOT;
		int beginIdx = 0, endIdx = 0, indent = 0;
		SQMArray newArray = null;
		Matcher matcher = STATEMENT_REGEX.matcher(text);
		while (matcher.find())
		{
			String statement = matcher.group();
			statement = statement.trim();
			logger.debug("Processing word: \"" + statement + 
					"\" State=" + state.toString() +
						" indent=" + indent);
			if (statement.length() == 0) {
				continue;
			}
			if (state == states.IN_CLASS)
			{
				//indent watchers
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
					sQMArrays_.add(newArray);
				}
				else if (statement.matches(PARAMETER_REGEX)) 
				{
					Parameter newParameter = new Parameter(statement, this);
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
					children_.add(newClass);
					state = states.IN_ROOT;
				}
			}
		}
		return children_;
	}
	
	/**
	 * Attempts to add new parameter. returns false
	 * if parameter already exists
	 */
	public boolean addParameter(String name, String value)
	{
		if ( getParameter(name) != null )
		{
			return false;
		}
		Parameter newParameter = new Parameter(name+"="+value+";");
		parameters_.add(newParameter);
		return true;
	}
	/**
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
	
	public Parameter getParameter(String parameterName)
	{
		for ( Parameter parameter : getParameters() )
		{
			String name = parameter.getName();
			if (name.equals(parameterName))
			{
				return parameter;
			}
		}
		return null;
	}
	
	public SQMArray getArray(String name) 
	{
		for ( SQMArray parameter : getArrays() )
		{
			String parameterName = parameter.getName();
			if (name.equals(parameterName))
			{
				return parameter;
			}
		}
		return null;
	}
	
	/**
	 * sets parameter value. If parameter is not found new parameter will
	 * created with given parameter name.
	 * @param parameterName is a name of the parameter
	 * @param newValue is a new value for the parameter
	 */
	public void setParameter(String parameterName, String newValue)
	{
		//No change
		if ( newValue.equals(getParameter(parameterName)) )
		{
			return;
		}
		setParameterNoUpdate(parameterName, newValue);
		updateText();
	}
	
	protected void setParameterNoUpdate(String parameterName, String newValue)
	{
		Parameter parameter = getParameter(parameterName);
		if (parameter == null)
		{
			Parameter newParameter = new Parameter(parameterName + "=" +newValue+";");
			parameters_.add(newParameter);
			return;
		}
		parameter.setValue(newValue);
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
		return children_;
	}

	/**
	 * Searches through the mission and
	 * tries to return a class with given parameter value.
	 */
	public ClassNode getClassByParameter(String name, String value)
	{
		for (ClassNode child : children_)
		{
			Parameter parameter = child.getParameter(name);
			ClassNode grandChild;

			if (parameter != null && parameter.getValue().equals(value))
			{
				return child;
			}
			else if ((grandChild = child.getClassByParameter(name, value)) != null)
			{
				return grandChild;
			}
		}
		return null;
	}
	
	/**
	 * Searches through the mission and
	 * tries to return a class with given parameter value.
	 */
	public ClassNode getClassByArray(String name, ArrayList<String> values)
	{
		for (ClassNode child : children_)
		{
			SQMArray parameter = child.getArray(name);
			ClassNode grandChild;

			if (parameter != null && parameter.getValues().equals(values))
			{
				return child;
			}
			else if ((grandChild = child.getClassByArray(name, values)) != null)
			{
				return grandChild;
			}
		}
		return null;
	}
	
	protected void setText(String text) 
	{
		text_ = text;
	}
	
	/**
	 * Corrects indent by figuring tab offset
	 * from the first "{” line.
	 * @param text
	 * @return
	 */
	private String fixIndent(String text)
	{
		Scanner scanner = new Scanner(text);
		scanner.nextLine();
		String line = scanner.nextLine();
		scanner.close();
		String tabs = line.replace("{", "");
		if (! tabs.matches("\t+"))
		{
			return text; //Didn't contain indent
		}
		text = text.replaceAll("(?m)^"+tabs, "");
		return text;
	}
	
	public void updateText()
	{
		text_ = "";
		//Write array parameters
		for ( int i = 0 ; i < getArrays().size(); ++i )
		{
			SQMArray array = getArrays().get(i);
			text_ += array.getText();
			text_ += "\n";
		}
		//Write normal parameters
		for ( int i = 0 ; i < getParameters().size(); ++i )
		{
			Parameter parameter = getParameters().get(i);
			if ( i == getParameters().size()-1) //Last->no linebreak
			{
				text_ += parameter.getText();
				break;
			}
			text_ += parameter.getText()+"\n";
		}
		//Write child classes
		for (ClassNode child : getChildren())
		{
			text_ += "\n" + child.getText();
		};
	}
	
}
