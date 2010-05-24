package edu.washington.cs.cse403.camps.admin;

import java.sql.*;

public class GraphData {
	private Connection con;
	public GraphData(){
	}

	public boolean connect(String host,int port, String database_name,String user, String pass){
		try {
			

			//Register the JDBC driver for MySQL.
			Class.forName("com.mysql.jdbc.Driver");

			//Define URL of database server for
			// database named mysql
			String url =
				"jdbc:mysql://"+host+":"+port+"/"+database_name;

			//Get a connection to the database for a
			// user named root with a blank password.
			// This user is the default administrator
			// having full privileges to do anything.
			con = DriverManager.getConnection(url,user,pass);

			//Display URL and connection information
			System.out.println("URL: " + url);
			System.out.println("Connection: " + con);
			return true;
		}catch( Exception e ) {
			e.printStackTrace();
			return false;
		}//end catch
	}
	public void fillData(MapModel model){
		try {
			//Get a Statement object
			Statement stmt= con.createStatement();

			/*
			//Create the new database
			stmt.executeUpdate(
			"INSERT INTO node(x,y) VALUES(0,0)");
			//Register a new user named auser on the
			// database named JunkDB with a password
			// drowssap enabling several different
			// privileges.

	      stmt.executeUpdate(
	          "GRANT SELECT,INSERT,UPDATE,DELETE," +
	          "CREATE,DROP " +
	          "ON JunkDB.* TO 'auser'@'localhost' " +
	          "IDENTIFIED BY 'drowssap';");
			 */
//			Query the database, storing the result
			// in an object of type ResultSet
			ResultSet rs = stmt.executeQuery(
			"SELECT * FROM node LEFT OUTER JOIN location ON node.node_id = location.node_id");

			//Use the methods of class ResultSet in a
			// loop to display all of the data in the
			// database.
			//System.out.println("Display all results:");
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
					node = new Building(id,loc_id,x,y,priName,abbrev);
					//System.out.println("Name : "+priName+" Abbrev: "+abbrev);
				}else{
					node = new Node(id,x,y);
				}
				
				//String str = rs.getString("test_val");
				//System.out.println("Node: ("+x+","+y+")");
				
				model.add(node);
			}
			rs = stmt.executeQuery(
			"SELECT * FROM path_type");
			while(rs.next()){
				int id = rs.getInt("path_type_id");
				String name = rs.getString("name");
				double costMultiplier = rs.getDouble("cost_multiplier");
				PathType cur = new PathType(id,name,costMultiplier);
				model.add(cur);
			}
			
			rs = stmt.executeQuery(
			"SELECT * FROM edge");
			while(rs.next()){
				int id = rs.getInt("edge_id");
				int node_a = rs.getInt("node_a");
				int node_b = rs.getInt("node_b");
				boolean bidirectional = rs.getBoolean("bidirectional");
				int path_type_id = rs.getInt("path_type_id");
				PathType pathType = model.getPathType(path_type_id);
				Node start = model.getNode(node_a);
				Node end = model.getNode(node_b);
				Edge cur = new Edge(id,start,end,pathType,bidirectional);
				model.add(cur);
			}

			/*
		      //Display the data in a specific row using
		      // the rs.absolute method.
		      System.out.println(
		                        "Display row number 2:");
		      if( rs.absolute(2) ){
		        int theInt= rs.getInt("test_id");
		        String str = rs.getString("test_val");
		        System.out.println("\ttest_id= " + theInt
		                             + "\tstr = " + str);
		      }//end if

		      //Delete the table and close the connection
		      // to the database
		      stmt.executeUpdate("DROP TABLE myTable");
			 */
			//con.close();
		}catch( Exception e ) {
			e.printStackTrace();
		}
	}
	public Node createNode(int x, int y) throws SQLException {
		int nodeId;
		Statement stmt= con.createStatement();
		stmt.executeUpdate(
		"INSERT INTO node(x,y) VALUES("+x+","+y+")");
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
		if(rs.next()){
			nodeId = rs.getInt("id");
		}else{
			throw new SQLException("Could not find primary id of row inserted!");
		}
		return new Node(nodeId,x,y);
	}
	public Building createBuilding(String priName, String abbreviation, int x, int y) throws SQLException{
		int nodeId;
		Statement stmt= con.createStatement();
		stmt.executeUpdate(
		"INSERT INTO node(x,y) VALUES("+x+","+y+")");
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
		if(rs.next()){
			nodeId = rs.getInt("id");
		}else{
			throw new SQLException("Could not find primary id of row inserted!");
		}
		int locationId;
		stmt= con.createStatement();
		stmt.executeUpdate(
		"INSERT INTO location(node_id) VALUES("+nodeId+")");
		rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
		if(rs.next()){
			locationId = rs.getInt("id");
		}else{
			throw new SQLException("Could not find primary id of row inserted!");
		}
		stmt.executeUpdate(
		"INSERT INTO location_name(name,type,location_id) VALUES('"+priName+"','Primary',"+locationId+")");
		stmt.executeUpdate(
		"INSERT INTO location_name(name,type,location_id) VALUES('"+abbreviation+"','Abbreviation',"+locationId+")");
		return new Building(nodeId,locationId,x,y,priName,abbreviation);
	}
	public Edge createEdge(Node start,Node end, boolean bidirectional, PathType path_type) throws SQLException{
		int edgeId;
		Statement stmt= con.createStatement();
		stmt.executeUpdate(
		"INSERT INTO edge(node_a,node_b,bidirectional,path_type_id) " +
		"VALUES("+start.getNodeId()+","+end.getNodeId()+","+(bidirectional ? "1" : "0")+","+path_type.getId()+")");
		ResultSet rs = stmt.executeQuery("SELECT LAST_INSERT_ID() as id");
		if(rs.next()){
			edgeId = rs.getInt("id");
		}else{
			throw new SQLException("Could not find primary id of row inserted!");
		}
		return new Edge(edgeId,start,end,path_type,bidirectional);
	}
	public void removeNode(Node node) {
		try{
			Statement stmt= con.createStatement();
			stmt.executeUpdate("DELETE FROM node WHERE node_id = "+node.getNodeId());
		}catch(Exception E){
			System.err.println("System may not have been able to remove Node: "+node+" from database:\n "+E);
			E.printStackTrace();
		}
	}
	public void removeEdge(Edge e) {
		try{
			Statement stmt= con.createStatement();
			stmt.executeUpdate("DELETE FROM edge WHERE edge_id = "+e.getId());
		}catch(Exception E){
			System.err.println("System may not have been able to remove Edge: "+e+" from database:\n "+E);
			E.printStackTrace();
		}
	}
	public boolean modifyNode(int nodeID, int newX, int newY){
		try{
			Statement stmt= con.createStatement();
			stmt.executeUpdate("UPDATE node SET x = "+newX+" , y = "+newY+" WHERE node_id = "+nodeID);
			return true;
		}catch(Exception E){
			System.err.println("System may not have been able to update Node: "+nodeID+" in database:\n "+E);
			E.printStackTrace();
			return false;
		}
	}
	public void close(){
		if(con != null){
			try{
				con.close();
			}catch (Exception E){
				E.printStackTrace();
			}
		}
		
	}
}
