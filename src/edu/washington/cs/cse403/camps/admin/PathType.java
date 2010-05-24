package edu.washington.cs.cse403.camps.admin;

/**
 * Represents the type of a path edge. Example: Stairs. We currently have two types of paths - 'Default' 
 * and 'Stairs'. There is also an arraylist of associated transportation methods.
 * @author Ryan
 */
public class PathType {
	private int id;  // the associated id to this path
	private String name;  // the string name associated w/ this path
	private double costMultiplier;  // the cost associated w/ this path, to be multiplied to the default cost (not defined here)
	
	/**
	 * Constructor - assigns the ID, name, and costMultiplier
	 */	
	public PathType(int id, String name, double costMultiplier){
		this.id = id;
		this.name = name;
		this.costMultiplier = costMultiplier;
	}
	// @return id key
	public int getId() {
		return id;
	}
	// @param id to set
	public void setId(int id) {
		this.id = id;
	}
	// @return name of path
	public String getName() {
		return name;
	}
	// @param name to set
	public void setName(String name) {
		this.name = name;
	}
	// @return string representation
	public String toString(){
		return name;
	}
	public boolean equals(Object obj){
		if(obj instanceof PathType){
			return ((PathType)obj).getId() == this.id;
		}else{
			return false;
		}
	}
}
