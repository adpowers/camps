package edu.washington.cs.cse403.camps.server;

import java.awt.Point;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.GsonBuilder;

import edu.washington.cs.cse403.camps.model.EdgeList;
import edu.washington.cs.cse403.camps.model.NodeList;

/**
 * GraphData class is the representation of the entire database.  The goal of this class
 * is to increase speed. This class is initiated when the server first starts up and loads 
 * all the data from the database into the memory of the path-finding algorithm.  So the 
 * program will have fast system memory access of data instead of going to the database every time.
 */

public class GraphData {
  private Map<edu.washington.cs.cse403.camps.model.PathType, PathType> pathTypes;
  private Map<Integer, TransportationMethod> transportationMethods;

  private Map<Integer, Node> nodes;
	private List<Edge> edges;
	private TreeMap<String, Node> buildingSearchTree;
	
	public GraphData() {
	  pathTypes = Maps.newHashMap(); // concurrent?
	  transportationMethods = Maps.newHashMap();
		nodes = Maps.newHashMap();
		edges = Lists.newArrayList();
		buildingSearchTree = Maps.newTreeMap();
		init();
	}
	
	/**
	 * Fill in the instance of this class
	 */
	private void init() {
	  String baseDir = "/Users/Andrew/Documents/workspace/Camps/";
	  NodeList nodeList = readObjectFromFileWithGson(new File(baseDir, "nodes.json"), NodeList.class);
	  EdgeList edgeList = readObjectFromFileWithGson(new File(baseDir, "edges.json"), EdgeList.class);

	  for (edu.washington.cs.cse403.camps.model.PathType modelPathType : edu.washington.cs.cse403.camps.model.PathType.values()) {
	    pathTypes.put(modelPathType, new PathType(0, modelPathType.name(), modelPathType.costMultiplier));
	  }
	  
	  int i = 0;
	  for (edu.washington.cs.cse403.camps.model.TransportationMethod modelMethod : edu.washington.cs.cse403.camps.model.TransportationMethod.values()) {
	    transportationMethods.put(i, new TransportationMethod(i, modelMethod.name(), modelMethod.costMultiplier));
      i++;
    }
  
    for (PathType pathType : pathTypes.values()) {
      Set<String> exclude = Sets.newHashSet();
      if (pathType.getName().equals("Stairs")) {
        exclude = Sets.newHashSet("Bike", "Wheelchair");
      }
      for (TransportationMethod transportationMethod : transportationMethods.values()) {
        if (!exclude.contains(transportationMethod.getName())) {
          pathType.addMethod(transportationMethod);
        }
      }
    }

    for (edu.washington.cs.cse403.camps.model.Node modelNode : nodeList.nodes) {
      Node node;
      if (modelNode.name == null) {
        node = new Node(modelNode.nodeId, new Point(modelNode.x, modelNode.y));
      } else {
        node = new Building(modelNode.nodeId, modelNode.x, modelNode.y, modelNode.name, modelNode.abbreviation);
        buildingSearchTree.put(modelNode.name, node);
        buildingSearchTree.put(modelNode.abbreviation, node);
      }
      nodes.put(modelNode.nodeId, node);
    }

    for (edu.washington.cs.cse403.camps.model.Edge modelEdge : edgeList.edges) {
      Node startNode = nodes.get(modelEdge.startNodeId);
      Node endNode = nodes.get(modelEdge.endNodeId);
      PathType pathType = pathTypes.get(modelEdge.pathType);
      Edge edge = new Edge(modelEdge.edgeId, pathType, startNode, endNode, modelEdge.bidirectional);
      startNode.add(edge);
      endNode.add(edge);
      edges.add(edge);
    }
  }
  
  private <T> T readObjectFromFileWithGson(File file, Class<T> classOfT) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(file));
      return new GsonBuilder().disableHtmlEscaping().create().fromJson(br, classOfT);
    } catch (Exception e) {
      throw new RuntimeException("Error reading object from file.", e);
    } finally {
      if (br != null) {
        try {
          br.close();
        } catch (Exception e) {
        }
      }
    }
  }
  
  public List getMatchingBuildings(String substring){
    Set primaryMatches = new TreeSet();
    Set secondaryMatches = new TreeSet();
    Iterator iter = buildingSearchTree.keySet().iterator();
    while(iter.hasNext()){
      String cur = (String)iter.next();
      String lowerCase = cur.toLowerCase();
      
      if(lowerCase.startsWith(substring.toLowerCase())){
        Node val = (Node)buildingSearchTree.get(cur);
        primaryMatches.add(val);
      }else if(lowerCase.contains(substring.toLowerCase())){
        Node val = (Node)buildingSearchTree.get(cur);
        secondaryMatches.add(val);
      }
    }
    ArrayList temp = new ArrayList(primaryMatches);
    temp.addAll(secondaryMatches);
    return temp;
  }
  
  /**
   * Give all the nodes in the database
   * @return A map of the nodes. key is the node ID, Value is a node class
   */
  public HashMap getNodes(){
    return new HashMap(nodes);
  }
  
  /**
   * Give all the edges in the database
   * @return An arraylist of edges.
   */
  public ArrayList getEdges(){
    return new ArrayList(edges);
    
  }
  
  /**
   * Give all the PathTypes in the database
   * @return A map of PathType.  Key is the PathType ID, Value is a PathType class
   */
  public HashMap getPathTypes(){
    return new HashMap(this.pathTypes);
  }
  
  /**
   * Give all the TransportationMethods in the database
   * @return A map of TransportationMethods.  Key is TransportationMethod ID, Value is a TransportationMethod class
   */
  public Collection<TransportationMethod> getTransportationMethods() {
    return transportationMethods.values();
  }
  
  public Node getNodeById(int id){
    return (Node)nodes.get(new Integer(id));
  }
  
  public Building getBuildingByName(String name){
    return (Building)buildingSearchTree.get(name);
  }
  
  public TransportationMethod getTransportationMethodById(int id){
    return (TransportationMethod)transportationMethods.get(new Integer(id));
  }

}
