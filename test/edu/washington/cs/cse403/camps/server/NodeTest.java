package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;
import com.cse403.server.Edge;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import java.awt.Point;

/**
 * JUnit tests for Node.java
 * 
 * @author Justin Lundberg
 */

public class NodeTest {
	Node tNode;
	Node n1;
	Node n2;
	Node n3;
	Node n4;
	
	Edge e1;
	Edge e2;
	Edge e3;
	Edge e4;
	
	PathType mpt;
	
	@Before
	public void setUp() throws Exception {
//		Node tNode = new Node( new Point(0,0) );
//		Node n1 = new Node( new Point(4,5) );
//		Node n2 = new Node( new Point(0,0) );
//		Node n3 = new Node( new Point(0,0) );
		String t = "test";
		mpt = new PathType(5, t,5.0);
		tNode = new Node(0, new Point(0,0) );
		n1 = new Node(1, new Point(4,3) );
		n2 = new Node(2, new Point(-4, -3) );
		n3 = new Node(3, new Point(0,0) );
		n4 =  new Node(5, new Point(0,0) );
		
		e1 = new Edge(1, mpt, tNode, n1);
		e2 = new Edge(2, mpt, tNode, n2);
		e3 = new Edge(3, mpt, tNode, n3);
		e4 = new Edge(4, mpt,n1,n2);
		
		tNode.add(e1);
		tNode.add(e2);
	}
	
	@Test
	public void testAdd() {
		List l = tNode.getNeighbors();
		assertTrue( l.size() == 2 );
		tNode.add(e3);
		l = tNode.getNeighbors();
		assertTrue( l.size() == 3 );
		assertTrue(l.get(2).equals(e3));
	
	}
	@Test
	public void testGetNeigbors() {
		List l = tNode.getNeighbors();
		assertTrue( l.size() == 2 );
		assertTrue( l.get(0).equals(e1) );
		assertTrue( l.get(1).equals(e2) );
	
	}

	@Test
	public void testGetNeighborsIter() {
		Iterator it = tNode.getNeighborsIter();
		Edge ea[] = {e1,e2};
		int i = 0;
		for(Edge currEdge = (Edge) it.next(); it.hasNext(); currEdge = (Edge) it.next()){
			assertTrue("getNeigbhorsIter error" + i, currEdge.equals(ea[i]) ); i++;
			
			}
	}



	@Test
	public void testCalcDistanceTo() {
		assertTrue("tNode, n1",tNode.calcDistanceTo(n1) == 5);
		assertTrue("tNode, n2",tNode.calcDistanceTo(n2) == 5);
		assertTrue("tNode, n3",tNode.calcDistanceTo(n3) == 0);
		assertTrue("n2, n1", n2.calcDistanceTo(n1) == 10);
	}

	@Test
	public void testEqualsObject() {
		assertTrue(tNode.equals(tNode));
		assertTrue( n4.equals(n4) );
		assertFalse( tNode.equals(n4) ); // different Nieghbor Lists
		assertFalse(tNode.equals(n1));
		
	}

}
