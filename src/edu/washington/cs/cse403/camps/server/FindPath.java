package edu.washington.cs.cse403.camps.server;
import java.util.*;
/**
 * FindPath --
 * This implements an algorithm to find the path
 * between a start Node and an destination Node
 * in a graph. 
 * 
 * Each node corrispondes to a uniquic 2D point
 * 
 * Current use paradigm
 * 
 * 1 - create a find path using the constructer
 * 2 - call run() ( may be called from a seperate thread)
 * 3 - poll runState using getRunState() 
 * 		- if it equals NOPATH no path could be found
 * 		- if it equals FOUNDPATH call getPath() to retrieve the correct path.
 * 
 * @author Justin Lundberg
 */
public class FindPath implements Runnable{
		  public static final int NOTRUN = 0;
		  public static final int RUNNING = 1;
		  public static final int NOPATH = 2;
		  public static final int FOUNDPATH = 3;

		  private Node start;
		  private Node destination;
		  private int runState; //FOUNDPATH,NOPATH,RUNNING, NOTRUN
		  //public Transport transport; // UML class diagram.
		  private List path; // null until path is found
		  private double pathCost;
		  private double pathDistance;
		  private double pathTime;
		 // private int visitCount = 0;
		  private HashMap visited;
		  private TransportationMethod transport;
		  
		  /**
		   * @deprecated
		   * 
		   * FindPath 
		   * Initializes an object to find the 
		   * optimal path between a start node
		   * and a destination node
		   * 
		   * @param start
		   * @param destination
		   * 
		   */
		  public FindPath(Node start, Node destination){
			  this( start, destination, null);
		  }
		  
		  /**
		   * FindPath 
		   * Initializes an object to find the 
		   * optimal path between a start node
		   * and a destination node
		   * 
		   * @param start -- the starting node of the path
		   * @param destination -- then desired end point
		   * @param transport -- the means of transportation in use ( walking, biking, ...)
		   */
		  public FindPath(Node start, Node destination, TransportationMethod transport){
					 this.start = start;
					 this.destination = destination;
					 this.runState = NOTRUN; 	
					 this.path = null;
					 this.pathCost = 0;
					 this.pathDistance=0;
					 this.pathTime=0;
					 this.visited = new HashMap();
					 this.transport = transport;
					 assert (null != this.start) : "null FindPath.start parameter";
					 assert null != this.destination : "null FindPath.destination parameter";
		  }

		  /**
		   * An implemetion of the runnable interface
		   * wich is safe to be called multiple times
		   * use getState() to determine if this class
		   * has found a path.
		   */
		  public void run(){// generate the path
					 if( NOTRUN != runState ) return;
					 System.out.println("About to Start");
					 runState = RUNNING;
					 if( ! findDest() ){
						 System.out.println("Entering No Path Runstate");
						 runState = NOPATH;
						 return;
					 }
					 runState = FOUNDPATH;
					 path = createPath();
					 visited = null;
		  }
		  
		  /**
		   * rundFindPath() -- singal call interface --
		   * will return a list of the nodes in the shortest path
		   * between the object's start node and its destination node.
		   * 
		   * @return List -- the list of Nodes of the optimal path betwee start
		   *        and destination
		   * 
		   * WARNING will not exibit correct behavior if called wile a previous 
		   * call to the same object is still running. if this is violated it 
		   * may incorrecly return a null list.
		   */
		  public List runFindPath(){// generate the path
			  if(NOTRUN == runState){
				  runState = RUNNING; 
				  if( ! findDest() ){
					  runState = NOPATH;
					  return new LinkedList(); // should be of length zero
				  }
				  runState = FOUNDPATH;
				  path = createPath();
				  visited = null;
			  }else if(RUNNING == runState){
				  assert false; return null;
			  }else if(NOPATH == runState){
				  return new LinkedList(); // should be of length zero
			  }else if( null == path){
				  assert false; // have not completed call to create path
			  }
				  return getPath();
		  }//will not exibit correct behavior if called mutiple times.
		  
		  /**
		   * 
		   * @return the state of the object, one of the following
		   * NOTRUN 
		   * RUNNING
		   * NOPATH 
		   * FOUNDPATH 
		   *  
		   */
		  public int getRunState(){
			  return runState;
		  }

		  /**
		   * must not be called unless getRunState() == FOUNDPATH
		   * @return List of the optimal path between start and destination
		   */
		  public List getPath(){
			  assert FOUNDPATH == runState;
			  		 return path;
		  }
		  /**
		   * @deprecated
		   *  must not be called unless getRunState() == FOUNDPATH
		   * @return the cost to take the path, as used internally in the algorithm
		   */
		  public double getPathCost(){ // time
			  assert FOUNDPATH == runState;
					 return pathCost;
		  }
		  
		  /**
		   *  must not be called unless getRunState() == FOUNDPATH
		   * @return the distance of the path found
		   */
		  public double getPathDistance(){
			  assert FOUNDPATH == runState;
				 return pathDistance;
		  }

		  /**
		   *  must not be called unless getRunState() == FOUNDPATH
		   * @return the estimated time to travel the path found
		   * 		given the current distance
		   */
		  public double getPathTime(){
			  assert FOUNDPATH == runState;
				 return pathTime;
		  }
		  
		  
		  
		  private boolean findDest(){
					 PriorityQueue	 queue = new PriorityQueue();
					 HashMap nodeToQueueNode = new HashMap();
					 if(start.equals(destination)){
						 return true;
					 }
					 QueueNode startQueueNode = new QueueNode(0,start);
					 nodeToQueueNode.put(start, startQueueNode);
					 queue.add(startQueueNode);
					 while(!queue.isEmpty()){
						 QueueNode currQueueNode = (QueueNode)queue.poll();
						 Node currNode = currQueueNode.getNode();
						 Iterator eIter = currNode.getNeighborsIter();
						 while(eIter.hasNext()){
							 Edge edge = (Edge)eIter.next();
							 Node other = edge.getOtherNode(currNode);
							 if(!edge.isBidirectional() && other.equals(edge.getStart()))
								 continue;
							 if(!nodeToQueueNode.containsKey(other)){ // if niegbhor is not already in visited hashmap add it
								 nodeToQueueNode.put(other, new QueueNode(Double.MAX_VALUE,other)); // newly create key points to itself
							 }
							 QueueNode otherQueueNode = (QueueNode)nodeToQueueNode.get(other); 
							double tmp_cost = currQueueNode.getCost() + edge.getCost(transport);//Dykstra
							 //double tmp_cost = currQueueNode.getCost() + edge.getCost(transport) + (otherQueueNode.getNode().calcDistanceTo(destination) *transport.getCostMultiplyer() );//A*
							 if( tmp_cost  < otherQueueNode.getCost() // A*
									 && (
											 otherQueueNode.getNode().equals(destination) || !otherQueueNode.getNode().isBuilding())){//Makes sure we don't go through buildings unless we are at our destination
								 //otherQueueNode.setCost(currQueueNode.getCost() + edge.getCost());//Dykstra -- old
								 otherQueueNode.setCost(tmp_cost);
								 visited.put(other, currNode);
								 if(queue.contains(otherQueueNode)){
									 queue.remove(otherQueueNode); 
								 }
								 queue.add(otherQueueNode);
							 }
						 }
						 //if(currNode.equals(destination)) break; // may not be necessary but I do not see the equivilent of this test anywhere else v23 JEL
					 }
					 if(nodeToQueueNode.get(destination) == null){
						 return false;
					 }
					 pathCost = ((QueueNode)nodeToQueueNode.get(destination)).getCost();
					 					 return true;
		  }

		  private List createPath(){
				Node currNode = destination;
				Node nextNode = null;
		  		LinkedList list = new LinkedList();
		  		list.addFirst(currNode);
		  		while(currNode != start){
		  			nextNode = (Node) visited.get(currNode);
		  			pathDistance += currNode.calcDistanceTo(nextNode); //will include the distance to the centor of the destination building
		  			pathTime += (currNode.getNeighbor(nextNode)).getCost(transport); // will not include the distance to the centor of the destination building
		  			currNode = nextNode;
		  			list.addFirst(currNode);
		  		}// visited.get(currNode) should never be null, it was constructed in findDest()
		  			assert start == currNode;
					 return list;
		  }


		  class QueueNode implements Comparable{
					 private double cost; 
					 private Node node;
					 QueueNode(double cost, Node node ){
								this.node = node;
								this.cost = cost;
								assert null != node;
								assert  0 < cost;
					 }
					 public int compareTo(Object comp){
								// assumes that object comp is QueueNode
								assert true == ( comp instanceof QueueNode );
								double tmp = ( this.cost - ( (QueueNode)comp).getCost() );
								if( tmp < 0) return -1;
								else if( tmp > 0)return 1;
								else return 0;

					 }
					 double getCost(){
								return this.cost;
					 }
					 Node getNode(){
								return this.node;
					 }
					public void setCost(double cost) {
						this.cost = cost;
					}
		  }
}
