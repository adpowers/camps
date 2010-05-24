package edu.washington.cs.cse403.camps.server;

import java.util.*;

/**
 * Represents the type of a path edge. Example: Stairs. We currently have two types of paths - 'Default' 
 * and 'Stairs'. There is also an arraylist of associated transportation methods. PathType 
 * 
 * @author Jian Wu
 */

public class PathType {
	private int ID;  // the id key associated w/ this type of path
	private String name;  // the string name describing this type of path
	private double costMultiplier;  // the cost associated w/ taking this path. The costMultiplier must be >= 1. 
	private HashSet transportationMethods; // associated transportation methods for this path type
	private static final double transportCostDefualt=Double.MAX_VALUE/100; // 4.0 * pathCost * 1/220  = min/ft
	//private static final double transportCostDefualt=Double.MAX_VALUE;
	
	/**
	 * Constructor - assigns the ID, name, and costMultiplier
	 */
	public PathType(int ID, String name, double costMultiplier){
		this.ID = ID;
		this.costMultiplier = costMultiplier;
		this.name = name;
		this.transportationMethods = new HashSet();
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
		return getCostMultiplyer(null);
	}
	
	// @return the total cost, taking into consideration the transportation method (ie, default or on stairs)
	public double getCostMultiplyer(TransportationMethod trans){
		if(transportationMethods.contains(trans)){
			return costMultiplier*trans.getCostMultiplyer();
		}else{
			return transportCostDefualt;
		}
	}
	
	// @return a list of the transportation methods associated w/ this path type
	public ArrayList getTransportationMethods(){
		return new ArrayList(transportationMethods);
	}
	
	/**
	 * Adds a transportation method to this path type
	 * @param method - the transportation method to add
	 */
	public void addMethod(TransportationMethod method){
		if(!this.transportationMethods.contains(method))
			this.transportationMethods.add(method);
	}
	// @return the string representation of this path type
	public String toString(){
		return "Path Type "+name+" with cost "+costMultiplier;
	}
	
}
