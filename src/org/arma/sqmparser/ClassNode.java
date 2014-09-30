/*
 * Saves and handles everything related to the SQM class element.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */

package org.arma.sqmparser;

//import java.lang.reflect.Array;
//import java.util.Arrays;
//import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class ClassNode extends Element
{
	private Element parent_;
	private String name_ = "";
	private Pattern CLASS_NAME_REGEX = Pattern.compile("class\\s*(.*)");
	private final static String CLASS_START_REGEX = "class.*";
	private final static Logger logger = Logger.getLogger(ClassNode.class);
	private final static String INDENT = "	";
	//private final static List<String> CLASSES_WITHOUT_ITEMS = Arrays.asList("Mission", "OutroLoose", "OutroWin");
	
	public ClassNode( String text, Element parent )
	{
		super(text);
		parent_ = parent;
		Matcher m = CLASS_NAME_REGEX.matcher(text);
		m.find();
		name_ = m.group(1);
		String content = text.replaceFirst(CLASS_START_REGEX, "");
		content = content.replaceFirst("\\{", "");
		//Remove last two letters (};)
		content = content.substring(0,content.length()-3);
		content = content.trim();
		super.readText(content);
		logger.debug("Added new class name=" + name_);		
	}
	
	/**
	 * @return id of the class
	 */
	public String getID()
	{
		Parameter parameter = getParameter("id");
		if (parameter != null)
		{
			return parameter.getValue();
		}
		return null;
	}
	
	/**
	 * @return name of the class 
	 */
	public String getName( )
	{
		return name_;
	}
	
	/**
	 * @return removes class from the mission
	 */
	public boolean delete()
	{
		if (parent_ == null)
		{
			logger.debug("Warning: No parent found for class " + getName());
			return false;
		}
		for (int i = 0; i < parent_.getChildren().size(); ++i)
		{
			if (parent_.getChildren().get(i) == this)
			{
				//Remove last reference to itself.
				parent_.getChildren().remove(i);
				parent_.updateText();
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Add a parent to the class
	 * @param element is a parent
	 */
	public void addParent(Element element) 
	{
		parent_ = element;
	}
	
	/**
	 * @return parent of the class
	 */
	public Element getParent() 
	{
		return parent_;
	}
	
	/**
	 * Updates text according to the changes
	 */
	@Override
	public void updateText()
	{
		String text = "";
		String content = "";		
		//Update items count
		/*
		int itemCount = 0;
		itemCount = getChildren().size();
		if (itemCount > 1 &&
				! CLASSES_WITHOUT_ITEMS.contains(getName()))
		{
			setParameterNoUpdate("items", Integer.toString(itemCount));
		}
		else
		{
			//Remove items parameter if 1 or less children exist.
			removeParameter("items");
		}
		*/
		//Items parameter does not seem to be necessary.
		//item<number> names would have to consist of numbers less than items-1.
		removeParameter("items");
		//sets content as text.
		super.updateText();
		content = super.getText();
		//Add class definition around the content.
		text = "\n" + "class "+name_+"\n"+
				"{"+"\n";
		//indent content
		text += content.replaceAll("(?m)^", INDENT);
		text += "\n"+"};";
		super.setText(text);
		if (parent_ == null) //Should not be possible. Exists for testability
		{
			logger.debug("Warning: class "+getName()+" doesn't have a parent!");
			return;
		}
		//Call parent to update its text as well.
		parent_.updateText();
	}
}
