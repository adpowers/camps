package edu.washington.cs.cse403.camps.server;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit tests for Building.java 
 * @author Charley 
 */
public class BuildingTest {
	Building b1;
	Building b2;
	Building b3;
	Building b4;
	
	/**
	 * Initiates variables used in this test class to avoid redundancy
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		b1 = new Building(3,1438,8273,"Husky Union Building","HUB");
		b2 = new Building(5,1,4000,"McCarty Hall","MCC");
		b3 = new Building(29,298832,0,"Lander-Terry Halls","LTH");
		b4 = new Building(-1,-12,3,"","");
	}
	
	/**
	 * Test that the compareTo method works when Buildings are equal
	 */
	@Test
	public void testCompareToForEquals() {
		assertTrue(b1.compareTo(b1) == 0); // equivalence testing; doesn't test b2,b3
		assertFalse(b1.compareTo(b4) == 0); 
	}
	
	/**
	 * Test that the compareTo method works when first Building's name comes after alphabetically 
	 */
	@Test
	public void testCompareToForLessThan() {
		assertTrue(b1.compareTo(b2) < 0);
		assertTrue(b1.compareTo(b3) < 0);
		assertTrue(b4.compareTo(b1) < 0);
		assertFalse(b3.compareTo(b2) > 0);
	}
	
	/**
	 * Test that the compareTo method works when second Building's name comes before alphabetically 
	 */
	@Test
	public void testCompareToForGreaterThan() {
		assertTrue(b2.compareTo(b3) > 0);
		assertTrue(b2.compareTo(b4) > 0);
		assertTrue(b3.compareTo(b1) > 0);
		assertFalse(b2.compareTo(b2) < 0);
	}	
	
	/**
	 * Test that the compareTo method catches non-building objects
	 */
	@Test
	public void testCompareToForNonBuildings() {
		assertEquals(b2.compareTo(new Integer(3)), -1);
	}	

	/**
	 * Test that getAbbreviation() method works
	 */
	@Test
	public void testGetAbbreviation() {
		assertTrue(b1.getAbbreviation().equals("HUB"));
		assertTrue(b4.getAbbreviation().equals(""));
	}
	
	/**
	 * Test that getPrimaryName() method works
	 */
	@Test
	public void testGetPrimaryName() {
		assertEquals("Husky Union Building", b1.getPrimaryName());
		assertEquals("", b4.getPrimaryName());
	}	
	
	/**
	 * Test that toString() method works
	 */
	@Test
	public void testToString() {
		assertEquals("Building Husky Union Building (HUB) at 1438,8273", b1.toString());
		assertEquals("Building  () at -12,3", b4.toString());
	}	
	

}
