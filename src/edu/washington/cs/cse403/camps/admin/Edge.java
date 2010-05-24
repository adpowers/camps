package edu.washington.cs.cse403.camps.admin;

/**An edge is a path between two Nodes.
 */
public class Edge{
	private int id;
	private boolean bidirectional;
	private PathType pathType;
	private Node startingLocation;
	private Node endingLocation;

	/**Create a new edge
	 * @param startingLocation the Node where this edge originates
	 * @param endingLocation the Node where this edge terminates
	 */
	public Edge(int id, Node startingLocation, Node endingLocation, PathType type, boolean bidirectional){
		this.pathType = type;
		this.id = id;
		this.bidirectional = bidirectional;
		this.startingLocation = startingLocation;
		this.endingLocation = endingLocation;
		startingLocation.add(this);
		if (bidirectional) endingLocation.add(this);
	}

	/**Get the node where this edge starts
	 * @return the starting node*/
	public Node getStartingNode(){
		return startingLocation;
	}

	/**Get the node where this edge ends
	 * @return the ending node*/
	public Node getEndingNode(){
		return endingLocation;
	}

	/**Tests edge equality
	 * @return true if both objects are edges that start and end at the same locations with equal length.*/
	public boolean equals(Object o){
		if(!(o instanceof Edge)){
			return false;
		}
		Edge other = (Edge)o;
		return (other.getId() == id);
	}

	/**The length of this edge
	 * @return length of edge*/
	public double getLength() {
		return Math.sqrt(Math.pow(endingLocation.getyLocation()-startingLocation.getyLocation(), 2)+Math.pow(endingLocation.getxLocation()-startingLocation.getxLocation(), 2));
	}

	/**Useful info about this edge
	 * @return a String representation of this edge.
	 */
	public String toString(){
		return "This edge is " +getLength() + " long connecting " + startingLocation + " to " + endingLocation;
	}

	public int getId() {
		return id;
	}

	public boolean isBidirectional() {
		return bidirectional;
	}

	public PathType getPathType() {
		return pathType;
	}
}