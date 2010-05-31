package edu.washington.cs.cse403.camps.server;

import edu.washington.cs.cse403.camps.model.TransportationMethod;


/**
 * Represents the path between two node for the path finding algorithm
 * @author Justin Lundberg
 */
public class Edge{ //should implement Comparable?? do we want to have sorted list of edges?
  public static double DEFAULT_VELOCITY_INVERSE = 0.0045454545454545454545454545454545; 
  // 1 / (220 ft/min) = min/ft  // the default conversion from distance to 1/walking speed.

  private PathType pathType; 
  private double length;  // length of an edge (in pixel values)
  private Node startNode; // the starting node/building
  private Node endNode; // the destination node/building
  private boolean bidirectional; // whether this edge can be traveled both directions (eg, not a one-way door)
  private int ID; // id key of this edge 

  // Constructor
  public Edge(int ID, PathType pType, Node startNode, Node endNode, boolean bidirectional){
    this.ID = ID;
    this.pathType = pType; //-- UML
    this.startNode = startNode;
    this.endNode = endNode;
    assert this.startNode != this.endNode; // keeps implementation in O(m log n) time instead of O(m*n)
    this.bidirectional = bidirectional;
    this.length = startNode.calcDistanceTo(endNode);
  }
  // Default Constructor that assumes a bidirectional edge 
  public Edge(int ID, PathType pType, Node startNode, Node endNode){
    this(ID, pType, startNode, endNode, true);
  }

  // @return the start node
  public Node getStart(){
    return startNode;
  }
  // @return the end node
  public Node getEnd(){
    return endNode;
  }
  // @return the other node defining the edge, given a node
  public Node getOtherNode( Node other ){
    return other.equals(startNode) ? endNode : startNode;
  }

  // @return the cost of this edge
  public double getCost(){
    return this.getCost(null);
  }
  // @return the cost of this edge, considering the transportation type
  public double getCost(TransportationMethod method){
    if(startNode.isBuilding() || endNode.isBuilding()){
      return 0;
    }
    return DEFAULT_VELOCITY_INVERSE * length * pathType.getCostMultiplyer(method);
  }
  // @return true if this edge is bidirectional, false otherwise
  public boolean isBidirectional(){
    return bidirectional;
  }

  public boolean equals(Object obj){
    if( !( obj instanceof Edge) ) return false;

    Edge tEdge = (Edge) obj;

    return  this.startNode == tEdge.getStart() && this.endNode == tEdge.getEnd() && this.bidirectional == tEdge.isBidirectional();
    // WARNING ignores differences in PathType

  }

  public String toString(){
    return "Edge from "+startNode+" to "+endNode;
  }


}
