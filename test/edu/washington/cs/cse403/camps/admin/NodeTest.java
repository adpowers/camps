package edu.washington.cs.cse403.camps.admin;

import static org.junit.Assert.*;
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
		String t = "test";
		mpt = new PathType(5, t,5.0);
		tNode = new Node(0,0,0);
		n1 = new Node(1,4,3);
		n2 = new Node(2,-4, -3);
		n3 = new Node(3,0,0);
		n4 =  new Node(0,0,0);
		
		e1 = new Edge(1, tNode, n1,mpt,true);
		e2 = new Edge(2, tNode, n2,mpt,true);
		e3 = new Edge(3, tNode, n3,mpt,false);
		e4 = new Edge(4, n1,n2,mpt,false);
	}
	
	@Test
	public void testAdd() {
		assertEquals(3,tNode.getExits().size());
		tNode.add(e4);//Should not add because it isn't valid
		assertEquals(3,tNode.getExits().size());
		tNode.add(e3);//Should not add because it should already exist.
		assertEquals(3,tNode.getExits().size());
		tNode.remove(e3);
		assertEquals(2,tNode.getExits().size());
		tNode.add(e3);//should work
		assertEquals(3,tNode.getExits().size());
		assertEquals(e3,tNode.getExit(n3));
	
	}
	@Test
	public void testGetExits() {
		List exits = tNode.getExits();
		assertEquals(3, exits.size());
		exits = n3.getExits();
		assertEquals(0, exits.size());//0 because e3 is directional
		exits = n1.getExits();
		assertEquals(2, exits.size());
		Iterator iter = exits.iterator();
		while(iter.hasNext()){
			Object cur = iter.next();
			assertTrue(cur instanceof Edge);
		}
	
	}
	
	@Test
	public void testGetExit() {
		Edge exit = tNode.getExit(n3);
		assertEquals(e3,exit);
		exit = tNode.getExit(n2);
		assertEquals(e2,exit);
		exit = tNode.getExit(n1);
		assertEquals(e1,exit);
		exit = n1.getExit(tNode);
		assertEquals(e1,exit);
		exit = n2.getExit(tNode);
		assertEquals(e2,exit);
		exit = n1.getExit(n2);
		assertEquals(e4,exit);
		exit = n3.getExit(tNode);
		assertNull(exit);
		exit = n2.getExit(n1);
		assertNull(exit);	
	}

	@Test
	public void testEqualsObject() {
		assertTrue(tNode.equals(tNode));
		assertTrue( n4.equals(n4) );
		assertTrue( n4.equals(new Node(0,0,0)) );
		assertFalse( tNode.equals(n4) ); // different Nieghbor Lists
		assertFalse(tNode.equals(n1));
		
	}

}
