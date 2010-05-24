/**
 * 
 */
package edu.washington.cs.cse403.camps.admin;

import static org.junit.Assert.*;

import java.awt.Point;

import javax.naming.spi.DirStateFactory;

import org.junit.Before;
import org.junit.Test;

import com.sun.jndi.toolkit.dir.DirSearch;

/**
 * EdgeTest - A test of the Edge class
 * @author Ryan
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
	 * Initilize Teset
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		deStart = new Node(1,10,20);
		deEnd = new Node(2,0,30);
		bidirOne = new Node(3,2,2);
		bidirTwo = new Node(4,2,3);
		pNormal = new PathType(1,"WalkWay", 5.0);
		pRoad = new PathType(2, "Road", 3.0);
		directedE = new Edge(1,deStart,deEnd,pRoad,false);
		bidirectedE = new Edge(2,bidirOne,bidirTwo,pNormal,true);
	}

	/**
	 * Test method for {@link com.cse403.admin.Edge#getStartingNode()}.
	 */
	@Test
	public final void testGetStartingNode() {
		assertTrue(directedE.getStartingNode() == deStart );
		assertFalse(directedE.getStartingNode() == deEnd);
		assertTrue(bidirectedE.getStartingNode() == bidirOne );
		assertFalse(bidirectedE.getStartingNode() == bidirTwo);
	}

	/**
	 * Test method for {@link com.cse403.admin.Edge#getEndingNode()}.
	 */
	@Test
	public final void testGetEndingNode() {
	 assertTrue(directedE.getEndingNode() == deEnd);
	 assertFalse(directedE.getEndingNode() == deStart);
	 assertTrue(bidirectedE.getEndingNode() == bidirTwo);
	 assertFalse(bidirectedE.getEndingNode() == bidirOne);
	}
	
	/**
	 * Test method for {@link com.cse403.admin.Edge#isBidirectional()}.
	 */
	@Test
	public final void testIsBidirectional() {
		assertFalse(directedE.isBidirectional());
		assertTrue(bidirectedE.isBidirectional());
	}

	/**
	 * Test method for {@link com.cse403.admin.Edge#equals(java.lang.Object)}.
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
	
	/**
	 * Test method for {@link com.cse403.admin.Edge#getPathType()}.
	 */
	@Test
	public final void testGetPathType() {
		 assertTrue(directedE.getPathType().equals(pRoad));
		 assertTrue(bidirectedE.getPathType().equals(pNormal));
	}
	 



}
