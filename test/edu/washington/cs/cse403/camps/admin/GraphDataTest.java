package edu.washington.cs.cse403.camps.admin;

import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.*;
import org.junit.*;

/**
 * JUnit (4.1) test for GraphData class.
 * @author Ryan Adams
 *
 */
public class GraphDataTest {
	GraphData gd1;
	GraphData gd2;
	Building building;
	MapModel model;
	
	@Before
	public void setUp() throws Exception {
		gd1 = new GraphData();
		gd2 = new GraphData();
		gd2.connect("cubist.cs.washington.edu",3306,"cse403c-wi07_test_test","cse403c-wi07","bQa3Cq9rjxz,7P6F");
		model = new MapModel(gd2);
	}
	
	

	@Test
	/**
	 * Test connection failure
	 */
	public void testConnectFail(){
		assertFalse(gd1.connect("localhost", 9999, "test", "none", "none"));
	}
	
	@Test
	/**
	 * Test connection success
	 */
	public void testConnectSuccess(){
		assertTrue(gd1.connect("cubist.cs.washington.edu",3306,"cse403c-wi07_test","cse403c-wi07","bQa3Cq9rjxz,7P6F"));
	}

	@Test
	public void testCreateNode() throws SQLException {
		Node test = gd2.createNode(10, 20);
		assertEquals(10,test.getxLocation());
		assertEquals(20,test.getyLocation());
		gd2.fillData(model);
		assertEquals(test, model.getNode(test.getNodeId()));
		gd2.removeNode(test);
	}
	
	@Test
	public void testModifyNode() throws SQLException{
		//gd2.fillData(model);
		Node test = gd2.createNode(10, 20);
		gd2.modifyNode(test.getNodeId(), 20, 30);
		gd2.fillData(model);
		assertEquals(20,model.getNode(test.getNodeId()).getxLocation());
		assertEquals(30,model.getNode(test.getNodeId()).getyLocation());
		gd2.removeNode(test);
	}
	
	@Test
	public void testCreateBuilding() throws SQLException{
		Building test = gd2.createBuilding("Example Building", "EXB", 50, 50);
		assertEquals(50,test.getxLocation());
		assertEquals(50,test.getyLocation());
		assertEquals("Example Building",test.getPrimaryName());
		assertEquals("EXB",test.getAbbreviation());
		gd2.fillData(model);
		assertEquals(test, model.getNode(test.getNodeId()));
		gd2.removeNode(test);
	}
	
	@Test
	public void testCreateEdge() throws SQLException{
		Node node1 = gd2.createNode(10, 20);
		Node node2 = gd2.createNode(30, 25);
		PathType testType = new PathType(1,"Default",1);
		Edge test = gd2.createEdge(node1, node2, true, testType);
		assertEquals(node1,test.getStartingNode());
		assertEquals(node2,test.getEndingNode());
		assertTrue(test.isBidirectional());
		assertEquals(testType,test.getPathType());
		gd2.fillData(model);
		Collection edges = model.getEdges();
		Iterator iter = edges.iterator();
		boolean found = false;
		while(iter.hasNext()){
			Edge cur = (Edge)iter.next();
			if(cur.equals(test)){
				found = true;
			}
		}
		assertTrue(found);
		assertEquals(test, node1.getExit(node2));
		gd2.removeEdge(test);
		gd2.removeNode(node1);
		gd2.removeNode(node2);
	}
	
	@Test
	public void testFillData() throws SQLException{
		Node node1 = gd2.createNode(10, 20);
		Node node2 = gd2.createNode(30, 25);
		PathType testType = new PathType(1,"Default",1);
		Edge edge = gd2.createEdge(node1, node2, true, testType);
		Building building = gd2.createBuilding("Example Building", "EXB", 50, 50);
		gd2.fillData(model);
		assertEquals(node1,model.getNode(node1.getNodeId()));
		assertEquals(node2,model.getNode(node2.getNodeId()));
		assertEquals(building,model.getNode(building.getNodeId()));
		Iterator iter = model.getEdges().iterator();
		boolean found = false;
		while(iter.hasNext()){
			Edge cur = (Edge)iter.next();
			if(cur.equals(edge)){
				found = true;
			}
		}
		assertTrue(found);
		assertEquals(testType, model.getPathType(testType.getId()));
		gd2.removeEdge(edge);
		gd2.removeNode(node1);
		gd2.removeNode(node2);
		gd2.removeNode(building);
	}
	
	@Test
	public void testRemoveEdge() throws SQLException{
		Node node1 = gd2.createNode(100, 200);
		Node node2 = gd2.createNode(60, 75);
		PathType testType = new PathType(1,"Default",1);
		Edge edge = gd2.createEdge(node1, node2, true, testType);
		gd2.removeEdge(edge);
		gd2.fillData(model);
		Iterator iter = model.getEdges().iterator();
		boolean found = false;
		while(iter.hasNext()){
			Edge cur = (Edge)iter.next();
			if(cur.equals(edge)){
				found = true;
			}
		}
		assertFalse(found);
		gd2.removeNode(node1);
		gd2.removeNode(node2);
		
	}

	@Test
	public void testRemoveNode() throws SQLException{
		Node test = gd2.createNode(100, 200);
		gd2.removeNode(test);
		gd2.fillData(model);
		assertNull(model.getNode(test.getNodeId()));
	}
	
	@After
	public void tearDown() throws Exception {
		gd1.close();
		gd2.close();
	}

}
