package edu.washington.cs.cse403.camps.admin;

import java.util.*;
/**Nodes are points on the map that represent buildings or intersections of Edges (paths in the map)
 */
public class Node{
	//private String name;
	private int nodeId;
	private int xLocation;
	private int yLocation;
	private ArrayList<Edge> neighbors;

	/** Create a new Node.
	 * @param name of this node.
	 * @param xLocation X coordinate of this node.
	 * @param yLocation Y coordinate of this node.
	 */
	public Node(int nodeId, int xLocation, int yLocation){
		this.nodeId = nodeId;
		this.xLocation = xLocation;
		this.yLocation = yLocation;
		neighbors = new ArrayList<Edge>();
	}

	/**Get the x coordinate of this node
	 * @return the node's x coordinate
	 */
	public int getxLocation(){
		return xLocation;
	}

	/**Get the y coordinate of this node
	 * @return the node's y coordinate
	 */
	public int getyLocation(){
		return yLocation;
	}

	/**Useful info about this node
	 * @return a String representation of this node.
	 */
	public String toString(){
		return "node: "+nodeId+" at " + xLocation + "," + yLocation;
	}

	/**Add an edge that exits from this node.  Will not accept edges that do not have this node as their starting location.
	 * @param e the edge that departs from this node.
	 */
	public void add(Edge e) {
		if (e.getStartingNode() == this || (e.getEndingNode() == this && e.isBidirectional())){
			if(!neighbors.contains(e)){
				neighbors.add(e);
			}
		} else {
			ErrorHandler.foundError("Attempted to add an invalid exit to this node.");
		}
	}
	
	public void remove(Edge e){
		for(int i = 0; i < neighbors.size(); i++) {
			Edge cur = neighbors.get(i);
			if(cur == e){
				neighbors.remove(i);
				return;
			}
		}
	}

	/**Edges attached to this node
	 * @return an ArrayList of edges that are attached to this node*/
	public ArrayList<Edge> getExits(){
		return new ArrayList<Edge>(neighbors);
	}

	/**Returns the Edge that connects this node with the given node or null if none is found
	 * @param dest the destination node
	 * @return returns the edge connecting the two nodes */
	public Edge getExit(Node dest) {
		for(int i = 0; i < neighbors.size(); i++) {
			Edge e = neighbors.get(i);
			if (e.getEndingNode() == dest || e.getStartingNode() == dest) {
				return e;
			}
		}
		return null;
	}

	/**Tests Node equality
	 * @return true if the Nodes have same name, location, and edges leaving them.*/
	public boolean equals(Object obj){
		if (!(obj instanceof Node)){
			return false;
		}
		Node other = (Node)obj;
		if (this == other){
			return true;
		}
		if (!other.toString().equals(toString())){ //name, x pos, y pos  :)
			return false;
		}
		if (!(other.getExits().size() == neighbors.size())){
			return false;
		}
		Iterator it = neighbors.iterator();
		while (it.hasNext()){
			Edge temp = (Edge)it.next();
			if (other.getExits().indexOf(temp) < 0){
				return false;
			}
		}
		return true;
	}

	/**Calculates the distance from this node to the specified node
	 * @param otherNode the other location to calculate distance to
	 * @return the distance between these nodes*/
	public double distanceTo(Node otherNode){
		return Math.sqrt((Math.pow(xLocation-otherNode.getxLocation(), 2) + Math.pow(yLocation-otherNode.getyLocation(), 2)));
	}

	public int getNodeId() {
		return nodeId;
	}

	public void setXLocation(int location) {
		xLocation = location;
	}

	public void setYLocation(int location) {
		yLocation = location;
	}
}