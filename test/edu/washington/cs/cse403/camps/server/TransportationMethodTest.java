package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit(4.1) test for TransportationMethod class.
 * @author jcwp5
 *
 */
public class TransportationMethodTest {
	TransportationMethod tm;
	final int ID = 1;
	final String name ="Walk";
	final double cost_multiplier = 2.0;
	@Before
	public void setUp() throws Exception {
		tm = new TransportationMethod(ID, name, cost_multiplier);
	}

	@Test
	@Ignore
	/*
	 * Ignore constructor
	 */
	public void testTransportationMethod() {
		fail("Not yet implemented");
	}

	@Test
	/**
	 * Testing the getID() method for returning the correct method ID
	 */
	public void testGetID() {
		assert(tm.getID()==ID);
	}

	@Test
	/**
	 * Testing the getName() method for returning the correcting method name
	 */
	public void testGetName() {
		assert(tm.getName().equals(name));
	}

	@Test
	/**
	 * Testing the getCostMuliplyer() method for returning the correcting
	 * method cost multiplyer
	 */
	public void testGetCostMultiplyer() {
		assert(tm.getCostMultiplyer() == this.cost_multiplier);
	}

	@Test
	@Ignore
	/**
	 * Don't need a test for this.
	 */
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	/*
	 * Testing the equals(Object o) method.  This method return true if o's ID is the same
	 * as this object's ID
	 */
	public void testEqualsObject() {
		TransportationMethod new_tm = new TransportationMethod(1, "Walk", 2.0);
		assertTrue(tm.equals(new_tm));
		
		new_tm = new TransportationMethod(2, "Walk", 2.0);
		assertFalse(tm.equals(new_tm));
	}

}
