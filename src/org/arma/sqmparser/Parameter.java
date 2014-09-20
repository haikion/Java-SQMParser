/*
 * Handles everything related to the parameters (parameterName=parameterValue).
 * Does not handle array elements.
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class Parameter
{
	private String name_ = "";
	private String value_ = "";
	private String text_ = "";
	private static Pattern PARAMETER_GROUPING_REGEX = Pattern.compile("(.*)=(.*)");
	private static Logger logger = Logger.getLogger(Element.class);
	
	public Parameter( String text )
	{
		text_ = text.trim();
		Matcher match = PARAMETER_GROUPING_REGEX.matcher(text_);
		if (! match.matches()) 
		{
			return;
		}
		name_ = match.group(1);
		value_ = match.group(2);
		value_ = value_.replace(";", "");
		logger.debug("addded new parameter name="+name_+" value="+value_);
	}
	
	public String getValue( )
	{
		return value_;
	}
	
	public void setValue(String value)
	{
		value_ = value;
		updateText();
	}
	
	public String getName( )
	{
		return name_;
	}
	
	public String getText( ) 
	{
		return text_;
	}
	
	private void updateText( )
	{
		text_ = name_ + "=" + value_ + ";";
	}
}
