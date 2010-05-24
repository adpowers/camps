package edu.washington.cs.cse403.camps.server;

import java.sql.*;
import java.util.*;
import java.awt.Point;

/**
 * GraphData class is the representation of the entire database.  The goal of this class
 * is to increase speed. Requesting database sql queries everytime a user makes a request 
 * slows down the system.  This class is initiated when the server first starts up and loads 
 * all the data from the database into the memory of the path-finding algorithm.  So the 
 * program will have fast system memory access of data instead of going to the database everytime.
 * 
 * @author Jian Wu
 */

public class GraphData {
	
	private Connection con;
	private HashMap nodes;
	private HashMap pathTypes;
	private HashMap transportationMethods;
	private ArrayList edges;
	private TreeMap buildingSearchTree;
	
	/****SQL queries ******/
	private String GET_NODES = "Select * " +
								"From node;";
	private String GET_EDGES = "Select * " +
								"From edge;";
	
	private String GET_PATHTYPES = "Select * " +
									"From path_type;";
	
	private String GET_TRANSPORTATIONMETHODS = "Select * " +
												"From transportation_method;";
	
	/**
	 * Constructor
	 */
	public GraphData() {
		nodes = new HashMap();
		edges = new ArrayList();
		pathTypes = new HashMap();
		transportationMethods = new HashMap();
		buildingSearchTree = new TreeMap();
		init();
	}
	
	/**
	 * Fill in the instance of this class, basically, it is our entire database
	 * @return true on sucessful excution, false if there is a sql exception
	 */
	private boolean init(){

		try{
			connect();
			Statement stmt = con.createStatement();
			
			
			ResultSet rs = stmt.executeQuery(
			"SELECT * FROM node LEFT OUTER JOIN location ON node.node_id = location.node_id");

			/***populate the nodes map*********/
			while(rs.next()){
				Node node;
				int x = rs.getInt("x");
				int y = rs.getInt("y");
				int id = rs.getInt("node_id");
				int loc_id = rs.getInt("location_id");
				if(loc_id != 0){
					String priName = "";
					String abbrev = "";
					Statement stmt2= con.createStatement();
					ResultSet rs2 = stmt2.executeQuery(
							"SELECT * FROM location_name WHERE location_id = "+loc_id);
					while(rs2.next()){
						String type = rs2.getString("type");
						if(type.equals("Primary")){
							priName = rs2.getString("name");
						}else if(type.equals("Abbreviation")){
							abbrev = rs2.getString("name");
						}
					}
					node = new Building(id,x,y,priName,abbrev);
					buildingSearchTree.put(priName, node);
					buildingSearchTree.put(abbrev, node);
					//System.out.println("Name : "+priName+" Abbrev: "+abbrev);
				}else{
					node = new Node(id,new Point(x,y));
				}
				nodes.put(new Integer(id),node);
			}
			
			/***populated the transportationMethods map ******/
			rs = stmt.executeQuery(GET_TRANSPORTATIONMETHODS);
			while(rs.next()){
				int ID = rs.getInt("transportation_id");
				String name = rs.getString("name");
				double cost_multiplyer = rs.getDouble("cost_multiplier");
				transportationMethods.put(new Integer(ID), new TransportationMethod(ID, name, cost_multiplyer));
			}
			
			/***populated the pathTypes map*******/
			rs = stmt.executeQuery(GET_PATHTYPES);
			
			while(rs.next()){
				int pathtype_id = rs.getInt("path_type_id");
				String pathtype_name = rs.getString("name");
				double pathtype_mult = rs.getDouble("cost_multiplier");
				PathType temp_type = new PathType(pathtype_id, pathtype_name, pathtype_mult);
				Statement stmt2 = con.createStatement();
				ResultSet temp_rs = stmt2.executeQuery("select transportation_id " +
														"From path_transportation " +
														"Where path_type_id = " + pathtype_id);
				while(temp_rs.next()){
					int transportation_id = temp_rs.getInt("transportation_id");
					TransportationMethod tm = (TransportationMethod) transportationMethods.get(new Integer(transportation_id));
					temp_type.addMethod(tm);
				}
				pathTypes.put(new Integer(pathtype_id), temp_type);
			}
			
			/****populated the edeges array and update the node's neighbor******/
			rs = stmt.executeQuery(GET_EDGES);
			while(rs.next()){
				int edgeID = rs.getInt("edge_id");
				int typeID = rs.getInt("path_type_id");
				int a = rs.getInt("node_a");
				int b = rs.getInt("node_b");
				boolean bidirectional = rs.getBoolean("bidirectional");
				Node node_a = (Node) nodes.get(new Integer(a));
				Node node_b = (Node) nodes.get(new Integer(b));
				PathType pType = (PathType) pathTypes.get(new Integer(typeID));
				Edge temp_edge = new Edge(edgeID, pType,node_a,node_b,bidirectional);	
				node_a.add(temp_edge);
				node_b.add(temp_edge);
				edges.add(temp_edge);
			}
			con.close(); //close the database connection
			return true;
		}catch (Exception e){
			e.printStackTrace();
			return false;
		}

		
	}
	/**
	 * Make a connection to the database
	 * @return true is successful connected, otherwise false
	 */
	private boolean connect(){
		try {
			

			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			//Define URL of database server for
			// database named mysql
			String url =
				"jdbc:mysql://localhost:3306/camps";

			//Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			con = DriverManager.getConnection(url,"camps","camps");

			//Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);
			return true;
		}catch( Exception e ) {
			e.printStackTrace();
			return false;
		}//end catch	
		
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
	public HashMap getTransportationMethods(){
		return new HashMap(this.transportationMethods);
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
