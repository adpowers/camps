package edu.washington.cs.cse403.camps.server;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.awt.*;

public class FindPathManagerTest {
	FindPathManager manager;
	Node[] nodes;
	Edge[] edges;
	PathData[] paths;

	@Before
	public void setUp() throws Exception {
		nodes = new Node[100];
		edges = new Edge[100];
		paths = new PathData[100];
		manager = new FindPathManager();
		PathType test = new PathType(1,"Default",1);
		for(int i=0;i< 100;i++){
			nodes[i]= new Building(i,10*i,10*i,"Buld "+i,"abbrev "+i);
		}
		for(int i=0;i<99;i++){
			edges[i] = new Edge(i,test,nodes[i],nodes[i+1]);
		}
	}

	@Test
	public void testCachingCircular() {
		TransportationMethod method = new TransportationMethod(1,"Test",1);
		for(int i=2;i< 100;i++){
			paths[i] = manager.getPath(nodes[0], nodes[i], method);
		}
		assertNotSame(paths[5],manager.getPath(nodes[0], nodes[5], method));//Should no longer be in cache
		assertNotSame(paths[10],manager.getPath(nodes[0], nodes[10], method));
		assertSame(paths[50],manager.getPath(nodes[0], nodes[50], method));//Should be in cache
		assertSame(paths[80],manager.getPath(nodes[0], nodes[80], method));
	}

}
