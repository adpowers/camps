package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * JUnite (4.1) test for GraphData class.
 * @author jcwp5
 *
 */
public class GraphDataTest {
	GraphData gd1;
	
	@Before
	public void setUp() throws Exception {
		gd1 = new GraphData();

	}

	@Test
	@Ignore
	/**
	 * Ignoring the constructor
	 */
	public void testGraphData() {
		fail("Not yet implemented");
	}

	@Test
	/**
	 * Testing getNodeById(int id) method with a random case "node_id = 30".  
	 * Ensure it return a none null object when a exist ID is given.  And ensure
	 * null is return when an invalid id is given
	 */
	public void testGetNodeById() {
		Node test_node = gd1.getNodeById(30);
		assertTrue(test_node!= null);
		
		//make sure it returns null when the given id doesn't exist in the database
		Node test_null_node = gd1.getNodeById(923742623);
		assertTrue(test_null_node == null);
		
	}
	
	@Test
	/**
	 * From inspecting the database, Node 30 has following properties. x = 2602, y = 722, 
	 * This test method tests node return by the getNodeById(30) method witht he properties listed
	 * above
	 */
	public void testGetNodeByIdProperties(){
		Node test_node = gd1.getNodeById(30);
		assertTrue(test_node.getID() == 30);
		assertTrue(test_node.getLocation().x == 2602);
		assertTrue(test_node.getLocation().y == 722);

		

	}
	@Test
	/**
	 * Test the neighbors of a random node has been initialized correctly.
	 */
	public void testGetNodeByIdNeighbors(){
		Node test_node = gd1.getNodeById(30);
		ArrayList neighbors = test_node.getNeighbors();
		assertTrue(neighbors.size()==3);
		for(int i=0; i<neighbors.size(); i++){
			Node neighbor = ((Edge) neighbors.get(i)).getOtherNode(test_node);
			int neighbor_id = neighbor.getID();
			assertTrue(neighbor_id == 29 || neighbor_id == 44 || neighbor_id == 70);

		}
	}

	@Test
	/**
	 * Ensure getBuildingByName() method return a none null object when a correct building name is 
	 * given.  Also ensure that null is return when an invalid building name is given.
	 */
	public void testGetBuildingByName() {
		Building test_building = gd1.getBuildingByName("Burke Museum");
		assertTrue (test_building != null);
		
		//Make sure it returns null when the given name doesn't exist in the database
		Building test_null_building = gd1.getBuildingByName("burke musum");
		assertTrue(test_null_building == null);
		
		
	}
	
	@Test
	/**
	 * Test the returned building from getBuildingByName() method has the correct
	 * properties
	 */
	public void testGetBuildingByNameProperties(){
		Building test_building = gd1.getBuildingByName("Burke Museum");
		assertTrue (test_building.getID() == 28);
		assertTrue (test_building.getAbbreviation().equals("BMM"));
		assertTrue (test_building.getLocation().x==2521);
		assertTrue (test_building.getLocation().y==721);
	}

	@Test
	/**
	 * Same idea as testGetBuildingByName() and testGetNodeById()
	 */
	public void testGetTransportationMethodById() {
		TransportationMethod test_tm = gd1.getTransportationMethodById(1);
		assertTrue(test_tm != null);
		
		TransportationMethod test_null_tm = gd1.getTransportationMethodById(1000);
		assertTrue(test_null_tm == null);
		
	}
	@Test
	/**
	 * Same idea as testGetBuildingByName() and testGetNodeById()
	 */
	public void testGetTransportationMethodByIdProperties(){
		TransportationMethod test_tm = gd1.getTransportationMethodById(1);
		assertTrue(test_tm.getID() == 1);
		assertTrue(test_tm.getName().equalsIgnoreCase("Walk"));
		assertTrue(test_tm.getCostMultiplyer() == 1.0);
	}

	@Test
	@Ignore
	/**
	 * Not sure what to test for this method. [JCW]
	 */
	public void testGetMatchingBuildings() {
		fail("Not yet implemented");
	}

	@Test
	/**
	 * Ensured getNodes() method return the correct number of nodes in the database.
	 * There are 1097 nodes in the database as for now. 
	 */
	public void testGetNodes() {
		HashMap test_nodes = gd1.getNodes();
		assert(test_nodes == null);
		assert(test_nodes.size()== 1097);
	}

	@Test
	/**
	 * Ensured getEdges() method return the corect number of edges in the database
	 * There are 1615 edges in the database as for now.
	 */
	public void testGetEdges() {
		ArrayList test_edeges = gd1.getEdges();
		assert(test_edeges!=null);
		assert(test_edeges.size() == 1615);
	}

	@Test
	/**
	 * Ensured getPathTypes() return the correct number of path type in the database
	 * There are 2 path types in the database as for now.
	 */
	public void testGetPathTypes() {
		HashMap test_pathtypes = gd1.getPathTypes();
		assert(test_pathtypes != null);
		assert(test_pathtypes.size()==2);
	}

	@Test
	/**
	 * Ensured getTransportationMethods() return the correct number of transportation methods in the database
	 * There are 4 transportation methods in the database as for now.
	 */
	public void testGetTransportationMethods() {
		HashMap test_tms = gd1.getTransportationMethods();
		assert(test_tms != null);
		assert(test_tms.size() == 4);
	}

}
