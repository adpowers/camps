package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;

/**
 * Junit(4.1) tests for PathType class.  
 * @author jcwp5
 *
 */
public class PathTypeTest {
	PathType pt;
	final int pathType_ID = 1;
	final String pathType_name = "Default";
	final double costMultiplier = 1;
	@Before
	public void setUp() throws Exception {
		pt = new PathType(pathType_ID, pathType_name, costMultiplier);
		pt.addMethod(new TransportationMethod(1, "Wheelchair", 1.0));
		pt.addMethod(new TransportationMethod(2, "Walk", 2.0));
	}

	@Test
	@Ignore
	/**
	 * Ignore constructor
	 */
	public void testPathType() {
		fail("Not yet implemented");
	}

	@Test
	/**
	 * Testing the getID()function for returning the correct ID.
	 */
	public void testGetID() {
		assert(pt.getID() == pathType_ID);
	}

	@Test
	/**
	 * Testing the getName() function for return the correct name.
	 */
	public void testGetName() {
		assert(pt.getName().equals(this.pathType_name));
	}

	@Test
	/**
	 * Testing the getCostMultiplyer() funciton for return the correct cost multiplyer
	 */
	public void testGetCostMultiplyer() {
		assert(pt.getCostMultiplyer() == this.costMultiplier * 2000);
	}

	@Test
	/**
	 * Testing the getCostMultiplyer(TransportationMethod tm) method.  Ensure that it returnt he
	 * correct cost for a given transportation method that's allowed for this path type.  If the 
	 * method isn't allowed, the highest cost should returned (Double.MAX_VALUE)
	 */
	public void testGetCostMultiplyerTransportationMethod() {
		double total_cost = pt.getCostMultiplyer(new TransportationMethod(1, "Wheelchair", 1.0));
		assert(total_cost == 1.0 * 2000);
		
		total_cost = pt.getCostMultiplyer(new TransportationMethod(2, "Walk", 2.0));
		assert(total_cost == 2.0 * 2000);
		
		double false_total_cost = pt.getCostMultiplyer(new TransportationMethod(2, "hello", 1.0));
		assert(total_cost == Double.MAX_VALUE);
	}

	@Test
	/**
	 * Testing the getTransportationMethods() function for return the correct number
	 * of transportation methods for this pathtype.
	 */
	public void testGetTransportationMethods() {
		ArrayList method_lists = pt.getTransportationMethods();
		assert(method_lists.size() == 2);
	}

	@Test
	/**
	 * Testing addMethod function.  If a method's already in the list of method, there shouldn't
	 * be any chages to the list. Otherwise the method will add to the list.
	 */
	public void testAddMethod() {
		ArrayList method_lists = pt.getTransportationMethods();
		pt.addMethod(new TransportationMethod(2, "Walk", 2.0));
		assert(method_lists.size() == 2);
		
		pt.addMethod(new TransportationMethod(3, "Run", 3.0));
		assert(method_lists.size() ==3);
	}

	@Test
	/**
	 * Make sure the correct string is returned
	 */
	public void testToString() {
		String s = "Path Type " + this.pathType_name + " with cost " + this.costMultiplier;
		assert(pt.toString().equals(s));
	}

}
