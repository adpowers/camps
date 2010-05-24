package edu.washington.cs.cse403.camps.admin;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;


/**
 * JUnit tests for Building.java 
 * @author Ryan
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
		b1 = new Building(1,1,1438,8273,"Husky Union Building","HUB");
		b2 = new Building(2,2,1,4000,"McCarty Hall","MCC");
		b3 = new Building(20,40,298832,0,"Lander-Terry Halls","LTH");
		b4 = new Building(-1,-1,-12,3,"","");
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
	 * Test that getBuildingID() method works
	 */
	@Test
	public void testGetBuildingID() {
		assertTrue(b1.getBuildingID() == 1);
		assertTrue(b3.getBuildingID() == 40);
		assertTrue(b4.getBuildingID() == -1);
	}
	

}
