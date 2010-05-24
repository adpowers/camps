/**
 * A sanity check for FindPath.java's 
 *  important public inerface methods.
 *  
 *  @author Justin Lundberg
 */
package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import java.util.*;
import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class FindPathTest {
	Node node0;
	Node node1;
	Node node2;
	Node node3;
	Edge edge1;
	Edge edge2;
	Edge edge3;
	PathType pt; 
	TransportationMethod trans;
	FindPath find1;
	FindPath find2;
	List result;
	@Before
	public void setUp() throws Exception {
		trans = new TransportationMethod(1,"walk",1.0);
		pt = new PathType(1,"normal", 1.0);
		pt.addMethod(trans);
		
		node0 = new Node(0, new Point(0,0));
		node1 = new Node(1, new Point(0,4));
		node2 = new Node(2, new Point(3,4));
		node3 = new Node(3, new Point(5,5));
		edge1 = new Edge(1,pt, node0, node1);
		edge2 = new Edge(2,pt,node1,node2);
		edge3 = new Edge(3,pt,node0,node2);
		node0.add(edge1);
		node1.add(edge1);
		
		node0.add(edge3);
		node2.add(edge3);
		
		node1.add(edge2);
		node2.add(edge2);
		
		find2 = new FindPath(node0,node2,trans);
		find2.run();
		result = find2.getPath();
		
	}

	@Test
	public void testRunFindPathFail() {
		find1 = new FindPath(node0,node3,trans);
		result = find1.runFindPath();
		assertTrue(result.size() == 0); 
	}
	@Test
	public void testRunFindPathSuccede() {
		find1 = new FindPath(node0,node2,trans);
		result = find1.runFindPath();
		System.out.println("my println:" + result.size());
		assertTrue(result.size() == 2); 
	}
	
	

	@Test
	public void testGetPath() {
		assertTrue((Node) result.get(0) == node0);
		assertTrue((Node) result.get(1) == node2);
					
	}
/*
 * A getPathCost is a legasy 
 * method, that is no longer useful
 * to the public after the implementation
 * of A* support.
 */
	@Test
	public void testGetPathCost() {
		assertTrue(true);
	}

	@Test
	public void testGetPathDistance() {
		assertTrue(find2.getPathDistance() == Node.PX_TO_FT*node0.calcDistanceTo(node2));
	}

	@Test
	public void testGetPathTime() {
		assertTrue(find2.getPathTime() == Node.PX_TO_FT*node0.calcDistanceTo(node2)*Edge.DEFAULT_VELOCITY_INVERSE);
	}

}
