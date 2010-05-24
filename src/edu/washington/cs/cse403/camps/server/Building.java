package edu.washington.cs.cse403.camps.server;
import java.awt.Point;


/**
 * Represents a Node that is a Building in the path finding algorithm
 * author Ryan Adams
 * modified by: Justin Lundberg
 *
 */
public class Building extends Node implements Comparable{
	private String primaryName;
	private String abbreviation;
	//public Building(int nodeId, int buildingID, int xLocation, int yLocation, String priName, String abbrev){
	public Building( int buildingID, int xLocation, int yLocation, String priName, String abbrev){
		//super(nodeId,xLocation,yLocation);
		super(buildingID, new Point( xLocation, yLocation) );
		this.primaryName = priName;
		this.abbreviation = abbrev;
	}
	
	/**
	 * Compares this string name of this building w/ another building
	 * @return an int representing the string comparison result
	 */
	//It's okay if it returns -1. I figure if it is comparing something that isn't a building i don't care if it is always considered less.
	//As far as the code we have now, this compareTo will never get called on something that isn't a building
	//The reason for it ordering the buildings based on their primary Name ordering is so that getMatchingBuildings returns them in alphabetical order
	//because it iterates through an ordered set
	public int compareTo(Object obj){
		 if(obj instanceof Building){
			 return this.primaryName.compareTo(((Building)obj).getPrimaryName());
		 }else{
			 return -1;
		 }
	 }
	
	// @return abbreviation for the bldg
	public String getAbbreviation() {
		return abbreviation;
	}
	// @return the primary name string for the building
	public String getPrimaryName() {
		return primaryName;
	}
	// @return the string representation for the building object
	public String toString(){
		return "Building " + primaryName + " (" + abbreviation + ") at " + getLocation().x + "," + getLocation().y;
	}

	
}
