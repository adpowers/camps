package edu.washington.cs.cse403.camps.admin;

import java.util.*;
import javax.swing.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.stream.*;

import java.awt.image.*;
import java.net.*;


/**MapModel keeps track of nodes and edges added to it, as well as viewers watching the model
 */
public class MapModel{
  private HashMap<Integer,Node> nodes;
  private HashMap<Integer,Edge> edges;
  private ArrayList viewers;
  private HashMap<Integer,PathType> path_types;
  public GraphData data;
  //private ArrayList visitedEdges; //if an algorithm visits edges, add those edges here
  
  private Node start, end; //for pathfinding, any functions interacting with two nodes int he model
  
  private BufferedImage mapImage;
  
  /**Create a MapModel**/
  public MapModel(GraphData data){
	  this.data = data;
    nodes = new HashMap<Integer,Node>();
    edges = new HashMap<Integer,Edge>();
    path_types = new HashMap<Integer,PathType>();
    viewers = new ArrayList();
    //visitedEdges = new ArrayList();
  }
  //Clear all data
  public void clear(){
	  nodes = new HashMap<Integer,Node>();
	  edges = new HashMap<Integer,Edge>();
	  path_types = new HashMap<Integer,PathType>();
	  notifyViewers();
  }
  
  /**Add a Node to this model
   * @param addMe the node to add*/
  public void add(Node addMe){
    if(getNode(addMe.getNodeId()) == null){//check for dupes
      nodes.put(new Integer(addMe.getNodeId()),addMe);
      notifyViewers();
    } else {
      ErrorHandler.foundError("Cannot create two nodes with the same name");
    } 
  }
  
  /**Tells all viewers of this model about changes*/
  public void notifyViewers() {
    Iterator it = viewers.iterator();
    while (it.hasNext()) {
      Viewer v = (Viewer)it.next();
      v.notifyViewer();
    }
  }
  
  public void add(PathType addMe){
	  path_types.put(new Integer(addMe.getId()), addMe);
  }
  
  /**Add an Edge to this model
   * @param addMe the edge to add*/
  public void add(Edge addMe){
    edges.put(new Integer(addMe.getId()),addMe);
    notifyViewers();
  }
  
  /**Add a viewer that's watching this model
   * @param addMe the Viewer to add*/
  public void add(Viewer addMe){
    viewers.add(addMe);
  }
  
  public void remove(Node node){
	  nodes.remove(new Integer(node.getNodeId()));
  }
  
  /**Get the node with id nodeId
   * @param nodeId the id of the node to search for
   * @return the desired Node, or null if the model doesn't contain a node named nodeId*/
  public Node getNode(int nodeId) {
	  return nodes.get(new Integer(nodeId));
  }
  
  /**Get the nodes in this model
   * @return an ArrayList of nodes in this model*/
  public Collection getNodes(){
    return nodes.values();
  }
  
  /**Get the edges in this model
   * @return an ArrayList of edges in this model*/
  public Collection getEdges(){
    return edges.values();
  }
  
  public Collection<PathType> getPathTypes(){
	  return path_types.values();
  }
  
  /**Find a node within the specified box
   * @param lowX left edge of the box
   * @param highX right edge of the box
   * @param lowY bottom of the box
   * @param highY top of the box
   * @return a the first node found within the box.  If there isn't a node within the box, returns null.*/
  public Node getNode(int lowX, int highX, int lowY, int highY){
    Iterator it = nodes.values().iterator();
    while(it.hasNext()){
      Node temp = (Node)it.next();
      if (temp.getxLocation() < highX && temp.getxLocation() > lowX && temp.getyLocation() < highY && temp.getyLocation() > lowY){
        return temp;
      }
    }
    return null;
  }
  
  public PathType getPathType(int id){
	  return path_types.get(new Integer(id));
  }
  
    
  /**String representation of the data contained in this model
   * @return a properly formatted String for later loading of this map*/
  public String mapData(){
    String export = "";
    Iterator it = nodes.values().iterator();
    
    while (it.hasNext()){//node data
      Node temp = (Node)it.next();
      export = export + temp.getNodeId() + "|" +temp.getxLocation() + "," + temp.getyLocation() + "\n";
    }
    
    it = nodes.values().iterator();
    while (it.hasNext()){ //edge data
      Node temp = (Node)it.next();
      ArrayList edges = (ArrayList)temp.getExits();
      Iterator edgesIter = edges.iterator();
      while (edgesIter.hasNext()){
        Edge currentEdge = (Edge)edgesIter.next();
        export = export + currentEdge.getStartingNode().getNodeId() + ":" + currentEdge.getEndingNode().getNodeId() + "\n";
      }
    }
    return export;
  }
  
  /**Save the data from this model to a text file using mapData()*/
  public void saveMap(){
    JFileChooser chooser = new JFileChooser();
    int result = chooser.showSaveDialog(null);
    
    if (result == JFileChooser.APPROVE_OPTION){
      File out = chooser.getSelectedFile();
      try{
        PrintWriter printer = new PrintWriter(new BufferedWriter(new FileWriter(out)));
        printer.print(mapData());
        printer.close();
      }
      catch (IOException e){
        ErrorHandler.foundError("Error while writing the file");
      }
    } else {
      ErrorHandler.foundError("No save selected.");
    }
  }
  
    
  /**Specify the starting location for pathfinding
   * @param startingNode the node to start at*/
  public void setStart(Node startingNode){
    start = startingNode;
    notifyViewers();
  }
  
  /**Specify the destination location for pathfinding
   * @param endingNode the node to end at*/
  public void setEnd(Node endingNode){
    end = endingNode;
    notifyViewers();
  }
  
  /**The origin location for pathfinding
   * @return a Node for paths to start at*/
  public Node getStart(){
    return start;
  }
  /**The destination location for pathfinding
   * @return a Node for paths to end at*/
  public Node getEnd(){
    return end;
  }
  
  /**Clears the state of this model while retaining all the edges and nodes.  Clears the starting and ending locations, as well as the visited paths*/
  public void reset(){
    start = null;
    end = null;
    //visitedEdges.clear();
    notifyViewers();
  }
  
  /**Creates an edge between the two nodes
   * @param start where to start
   * @param end where to end*/
  public void createEdge(Node start, Node end, PathType type, boolean bidirectional){
	  if(start == null || end == null || type == null){
		  System.err.println("Could not add edge because start,end, and type must all be non-null");
		  return;
	  }
	  
	  if(start.getExit(end) != null){
		  ErrorHandler.foundError("Cannot create duplicate edges.");
		  return;
	  }
	  if(end.getExit(start) != null && bidirectional){
		  ErrorHandler.foundError("Cannot create duplicate edges.");
		  return;
	  }
	  try{
		  Edge toAdd = data.createEdge(start, end, bidirectional, type);
		  add(toAdd);
	  }catch (Exception E){
		  System.err.println("Could not add edge: "+E);
	  }
  }
  
  public void removeEdge(Edge toRemove){
	  Node start = toRemove.getStartingNode();
	  Node end = toRemove.getEndingNode();
	  start.remove(toRemove);
	  if(toRemove.isBidirectional()) end.remove(toRemove);
	  data.removeEdge(toRemove);
	  edges.remove(new Integer(toRemove.getId()));
	  notifyViewers();
  }
  
  public void removeNode(Node toRemove){
	  
	  //Remove edges to other nodes from this one
	  List<Edge> connectedEdges = toRemove.getExits();
	  Iterator<Edge> iter = connectedEdges.iterator();
	  while(iter.hasNext()){
		  Edge cur = iter.next();
		  removeEdge(cur);
	  }
	  
	  //Remove edges from other nodes to this one
	  Iterator<Node> iter2 = nodes.values().iterator();
	  while(iter2.hasNext()){
		  Node cur = iter2.next();
		  Edge suspectEdge = cur.getExit(toRemove);
		  if(suspectEdge != null){
			  removeEdge(suspectEdge);
		  }
	  }
	  
	  data.removeNode(toRemove);
	  remove(toRemove);
	  notifyViewers();
	  
  }
  
  public void modifyNode(Node node, int newX, int newY){
	  if(data.modifyNode(node.getNodeId(), newX, newY)){
		  node.setXLocation(newX);
		  node.setYLocation(newY);
		  notifyViewers();
	  }
  }
  
  /**Set the image that represents this model.  Relevant to the model because we assume the image is unique to the model.  The model does not draw or alter the image in any way, that's up to viewers.
   * @param imageLocation where the image is relative to this application*/
  public void setImage(String imageLocation){
	  URL imageURL = getClass().getResource(imageLocation);
    try{
    	mapImage = ImageIO.read(imageURL);
    }
    catch(IOException e){
      ErrorHandler.foundError("problem reading the specified background image: "+e);
    }
  }
  
  /**Get the image representing the background of this map
   * @return the background image, or null if there isn't a background image*/
  //public BufferedImage getImage(){
  public BufferedImage getImage(){
    return mapImage;
  }
  
  /**Gets the resource location for this project
   * @param obj the object to locate
   * @return the URL location of this project */
  public URL getThisResource(String obj) {
      return getClass().getResource(obj);
  }
  
  /**Converts pixels to feet for scale based on the Big UW map
   * @param pix the number of pixels
   * @return returns the distance in feet
   */
  public int convertPixels(int pix) {
      double pixDouble = pix;
      return((int)(pixDouble/117*500));
  }
  
  /**Useful info about this MapModel
   * @return A string representation of this MapModel*/
  public String toString(){
    return "This model contains " + nodes.size() + " Nodes and " + edges.size() + " Edges.";
  }
}