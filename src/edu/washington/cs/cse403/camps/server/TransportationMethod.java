package edu.washington.cs.cse403.camps.server;

/**
 * Represents a method of transportation.  Example, Walk and Bike.
 * 
 * @author Jian Wu
 */
public class TransportationMethod {
	private int ID;  // id key associated w/ the name; eg, 1=Walk
	private String name; // name of the transportation, such as "Walk"
	private double cost_multiplier; // cost associated w/ this transportation. For example, the cost (in terms of time) is greater for walking than for biking
	
	/**
	 * Constructor, assigns the ID, name, and cost_multiplier, as defined above
	 */
	public TransportationMethod(int ID, String name, double cost_multiplier){
		this.ID = ID;
		this.name = name;
		this.cost_multiplier = cost_multiplier;
	}
	
	// @return the id key
	public int getID(){
		return ID;
	}
	// @return the string name
	public String getName(){
		return name;
	}
	// @return the cost multiplier
	public double getCostMultiplyer(){
		return cost_multiplier;
	}
	// toString representation of the transportation method
	public String toString(){
		return "Transportation Method "+name+" with cost "+cost_multiplier;
	}
	public boolean equals(Object obj){
		 if(obj instanceof TransportationMethod){
			 if(ID == ((TransportationMethod)obj).getID()){
				 return true;
			 }
		 }
		 return false;
	 }
}
