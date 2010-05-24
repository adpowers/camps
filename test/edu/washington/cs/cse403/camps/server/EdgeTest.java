/**
 * 
 */
package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import java.awt.Point;

import javax.naming.spi.DirStateFactory;

import org.junit.Before;
import org.junit.Test;

import com.sun.jndi.toolkit.dir.DirSearch;

/**
 * @author lundbje
 *
 */
public class EdgeTest {
	Edge directedE;
	Edge bidirectedE;
	Node deStart;
	Node deEnd;
	Node bidirOne;
	Node bidirTwo;
	PathType pNormal;
	PathType pRoad;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		deStart = new Node(1,new Point(1,2) );
		deEnd = new Node(2, new Point(1,3) );
		bidirOne = new Node(3, new Point(2,2) );
		bidirTwo = new Node(4, new Point(2,3) );
		pNormal = new PathType(1,"WalkWay", 5.0);
		pRoad = new PathType(2, "Road", 3.0);
		directedE = new Edge(1,pRoad,deStart,deEnd, false);
		bidirectedE = new Edge(2,pNormal, bidirOne, bidirTwo);
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#Edge(int, com.cse403.server.PathType, com.cse403.server.Node, com.cse403.server.Node, boolean)}.
	 */
	//@Test
	public final void testEdgeIntPathTypeNodeNodeBoolean() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#Edge(int, com.cse403.server.PathType, com.cse403.server.Node, com.cse403.server.Node)}.
	 */
	//@Test
	public final void testEdgeIntPathTypeNodeNode() {
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#getStart()}.
	 */
	@Test
	public final void testGetStart() {
		assertTrue(directedE.getStart() == deStart );
		assertFalse(directedE.getStart() == directedE.getEnd());
		assertTrue(bidirectedE.getStart() == bidirOne );
		assertFalse(bidirectedE.getStart() == bidirectedE.getEnd());
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#getEnd()}.
	 */
	@Test
	public final void testGetEnd() {
	 assertTrue(directedE.getEnd() == deEnd);
	 assertFalse(directedE.getStart() == directedE.getEnd());
	 assertTrue(bidirectedE.getEnd() == bidirTwo);
	 assertFalse(bidirectedE.getStart() == bidirectedE.getEnd());
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#getOtherNode(com.cse403.server.Node)}.
	 */
	@Test
	public final void testGetOtherNode() {
		assertTrue(bidirectedE.getOtherNode(bidirOne) == bidirTwo);
		assertTrue(bidirectedE.getOtherNode(bidirOne) != bidirOne);
		assertTrue(bidirectedE.getOtherNode(bidirTwo) != bidirTwo);
		assertTrue(bidirectedE.getOtherNode(bidirTwo) == bidirOne);
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#getCost(com.cse403.server.TransportationMethod)}.
	 */
	//@Test
	public final void testGetCostTransportationMethod() {
		assertTrue(directedE.getCost() == deStart.calcDistanceTo(deEnd)*pRoad.getCostMultiplyer()); //change
		fail("Not yet implemented"); // TODO
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#isBidirectional()}.
	 */
	@Test
	public final void testIsBidirectional() {
		assertFalse(directedE.isBidirectional());
		assertTrue(bidirectedE.isBidirectional());
	}

	/**
	 * Test method for {@link com.cse403.server.Edge#equals(java.lang.Object)}.
	 */
	@Test
	public final void testEqualsObject() {
		Edge tmp_test = bidirectedE;
		assertTrue(directedE.equals(directedE));
		assertFalse(directedE.equals(deStart));
		assertFalse(directedE.equals(bidirectedE));
		assertTrue(bidirectedE.equals(tmp_test) );
		assertTrue(tmp_test.equals(bidirectedE));
	}



}
