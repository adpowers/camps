package edu.washington.cs.cse403.camps.server;

import java.util.*;
import java.awt.Point;

/**
 * Represents a Node in the path finding algorithm. The node can be a building or not a building. 
 * Either way, it's just a point along the path that connects edges.
 * author: Justin Lundberg
 */

public class Node{ 
	public static double PX_TO_FT = 1000 / 730; // 1000 feet = 730 pixels in the image
	
	// private int xCordinate;
	// private int yCordinate;
	//private Iterator iterN; // neighbors iterator;
	private int ID; // id key associated with this node
	private Point location; // the pixel point location, w/ origin at the top-left corner of whichever map image we use
	private ArrayList neighbors; // the list of neighboring nodes
		  
	// constructor - assigns the ID and Point location
	public Node(int ID, Point location){
		this.ID = ID;
		this.location = location;
		neighbors = new ArrayList();
	}
	  
	public int getID(){
		return this.ID;
	}
	  
	public boolean isBuilding(){
		return (this instanceof Building);
	}
	  
	public Point getLocation(){ 
		return new Point(location);
	}
	
	/*
	 * post condition: returns an ArrayList 
	 * 	consisiting of edges connecting to this
	 * 	Node
	*/ 
	public ArrayList getNeighbors(){
		return neighbors;
	}
	 
	public Iterator getNeighborsIter(){
		return neighbors.iterator();
	}
	public Edge getNeighbor(Node node){
			Iterator nIter =  getNeighborsIter();
			Edge tmpEdge= null;
			while( nIter.hasNext() ){
				tmpEdge = (Edge)nIter.next();
				if( tmpEdge.getOtherNode(this) == node) return tmpEdge;
			}
			return null;
	}
	 
	
	/**
	 * @param e -- edge connecting this node to a new neighbor
	 * 		 -- precondition: e must not already be within this node's neighbors 
	 */
	public void add(Edge e){
		assert( neighbors.indexOf( e ) < 0 );
		neighbors.add( e );
	}	
	
	/*
	 *  for A* search
	 */
	public double calcDistanceTo(Node targ){
		return PX_TO_FT * location.distance( targ.getLocation() );
	}
	
	/**
	 * Decides whether or not this node equals another node
	 * @param obj - the node to compare it to (should be a node object
	 * @return true if the nodes are equal, false otherwise
	 */
	public boolean equals(Object obj){
		if(obj instanceof Node){
			if(ID == ((Node)obj).getID()){
				return true;
			}
		}
		return false;
	}
	
	// @return the string representation of this node
	public String toString(){
	return "Node at "+location.x+","+location.y;
	}
}
