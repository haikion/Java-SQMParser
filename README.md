SQMParser
=================

SQM parser written in Java

This library allows easy editing of Arma (2 or 3) mission.sqm file in Java application.

Currently working:
	- Removal of classes (units etc)
	- Editing of parameters
	- Parameter deletion
	- Parameter adding
	
What's not working:
	- Adding of new Classes



Known Bugs
==========
Please report!

Usage
=====
<ol>
<il>	1. Create SQMParser object</il>
<il>	2. Open mission.sqm file</il>
<il>	3. Edit file</il>
<il>	4. Save file (to new path)</il>
</ol>

Examples
=======
```
import org.arma.sqmparser.SQMParser;

class SQMParserUser 
{
	public static void main(String[] args)
	{
		SQMParser parser = new SQMParser(); //Create new SQMParser object
		
		parser.parseFile("mission.sqm");  //open existing mission.sqm file
		parser.deleteByID("1"); //Delete unit with ID 1
		parser.write("mission_edited.sqm"); //write new mission.sqm file
	}
}
```
