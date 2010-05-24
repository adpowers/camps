package edu.washington.cs.cse403.camps.server;

import java.util.*;

public class Runner {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		GraphData data = new GraphData();
		
		//Node start = data.getNodeById(76);
		//Node end = data.getNodeById(110);
		Node start = data.getNodeById(394);
		Node end = data.getNodeById(226);
		//Node start = data.getNodeById(219);
		//Node end = data.getNodeById(232);
		
		FindPath find = new FindPath(start,end);
		List temp = find.runFindPath();
		//find.run();
		//java.util.List temp = find.getPath();
		if(temp != null && temp.size() > 0){
			Iterator iter = temp.iterator();
			while(iter.hasNext()){
				System.out.println(iter.next());
			}
		}else{
			System.out.println("Nothing Found");
		}
		int minX = Integer.MAX_VALUE;
		int minY = Integer.MAX_VALUE;
		int maxX = 0;
		int maxY = 0;
		Iterator iter = temp.iterator();
		while(iter.hasNext()){
			Node cur = (Node)iter.next();
			int x = cur.getLocation().x;
			int y = cur.getLocation().y;
			if(x < minX){
				minX = x;
			}
			if(x > maxX){
				maxX = x;
			}
			if(y < minY){
				minY = y;
			}
			if(y > maxY){
				maxY = y;
			}
		}
		System.out.println("X:"+minX);
		System.out.println("Y:"+minY);

	}

}
