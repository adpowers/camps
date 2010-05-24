package edu.washington.cs.cse403.camps.admin;

/**
 * Building represents a type of node that is also a building. So buildings are
 * always nodes, but nodes are not always buildings.
 * @author Ryan
 */
public class Building extends Node{
	private int buildingID; // the building ID key
	private String primaryName; // the full name. Eg, "Hutchinson Hall"
	private String abbreviation; // the abbreviation. Eg, "HUT"
	
	/**
	 * Constructor
	 * @param nodeId - the node id key
	 * @param buildingID - the building ID key
	 * @param xLocation - the x-coordinate pixel location, based of top-left corner of map image
	 * @param yLocation - the y-coordinate pixel location
	 * @param priName - the primary name
	 * @param abbrev - the abbreviated name
	 */
	public Building(int nodeId, int buildingID, int xLocation, int yLocation, String priName, String abbrev){
		super(nodeId,xLocation,yLocation);
		this.primaryName = priName;
		this.abbreviation = abbrev;
		this.buildingID = buildingID;
	}
	public String getAbbreviation() {
		return abbreviation;
	}
	public String getPrimaryName() {
		return primaryName;
	}
	public int getBuildingID() {
		return buildingID;
	}

	
}