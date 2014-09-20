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
	public static final List<String> DISALLOWED_VALUES = 
		    Collections.unmodifiableList(Arrays.asList("{", "};", "}"));
	
	public void add(String value) 
	{
		if (DISALLOWED_VALUES.contains(value))
		{
			return;
		}
		value = value.replace(",", ""); //TODO: Replace only when outside parenthesis.
		//logger.debug("adding value: "+value);
		values_.add(value);
	};
	
	public SQMArray(String text) 
	{
		text = text.trim();
		if (text.contains("};")) 
		{
			parseArrayLine(text);
			return;
		}	
		name_ = text.replaceAll("\\=(.*)", "");
	}
	
	public String getText() 
	{
		String rVal = name_+"={";
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

	public void parseArrayLine(String statement) 
	{
		name_ = statement.replaceAll("\\=(.*)", "");
		statement = statement.replaceAll("(.*)\\=\\{", "");
		statement = statement.replace("}", "");
		statement = statement.replace(";", "");
		for (String value : statement.split(",") )
		{
			values_.add(value);
		}
	}

	public String getName() {
		return name_;
	};
};
