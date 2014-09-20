/*
 * Handles everything related to the mission.sqm root
 * Does most of the parsing.
 * Author: Niko Häikiö 
 * Created: 20.09.2014
 */


package org.arma.sqmparser;

public class MissionRoot extends Element
{	
	public MissionRoot( String text )
	{
		super(text);
		super.readText(text);
	}
}