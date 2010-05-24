package edu.washington.cs.cse403.camps.server;

import java.util.List;

public class PathData {
	public double pathDistance;
	public double pathCost;
	public double pathTime;
	public List nodes;
	public PathData(List nodes, double cost, double distance, double time){
		this.nodes = nodes;
		this.pathDistance = distance;
		this.pathTime = time;
		this.pathCost = cost;
	}
	public boolean isNull(){
		return nodes.isEmpty();
	}
}

