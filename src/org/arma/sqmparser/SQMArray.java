/**
 * Handles everything related to the SQM arrays (parameterName[]={value1,value2...};).
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SQMArray {
	ArrayList<String> values_ = new ArrayList<String>();
	String name_ = "";
	//private static Logger logger = Logger.getLogger(SQMArray.class);
	private static final List<String> DISALLOWED_VALUES = 
		    Collections.unmodifiableList(Arrays.asList("{", "};", "}"));
	private static final String NOT_NAME_REGEX = "\\[\\][\\s|\\S]*=[\\s|\\S]*";
	
	public void add(String value) 
	{
		if (DISALLOWED_VALUES.contains(value))
		{
			return;
		}
		//FIXME: Why?
		value = value.replace(",", ""); //TODO: Replace only when outside parenthesis.
		//logger.debug("adding value: "+value);
		values_.add(value);
	};
	
	public SQMArray(String text) 
	{
		text = text.trim();
		parseValues(text);
		name_ = text.replaceAll(NOT_NAME_REGEX, "");
	}
	
	public String getText() 
	{
		String rVal = name_+"[]"+"={";
		if (values_.size() == 0) 
		{
			return rVal;
		}
		for (int i = 0; i < values_.size() - 1; ++i)
		{
			rVal = rVal + values_.get(i) + ",";
		}
		//Last value
		rVal = rVal + values_.get(values_.size() -1) + "};";
		return rVal;
	}

	public void parseValues(String statement) 
	{
		name_ = statement.replaceAll(NOT_NAME_REGEX, "");
		statement = statement.replaceAll("(\\s|\\S)*=(\\s|\\S)*\\{", "");
		statement = statement.replace("}", "");
		statement = statement.replace(";", "");
		for (String value : statement.split(",") )
		{
			value = value.trim();
			values_.add(value);
		}
	}

	public ArrayList<String> getValues()
	{
		return values_;
	}

	public String getName() {
		return name_;
	}
};
