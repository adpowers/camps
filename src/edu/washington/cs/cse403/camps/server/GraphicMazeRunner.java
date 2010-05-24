package edu.washington.cs.cse403.camps.server;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

/**
 * 
 * @author Justin Lundberg
 * this is a direct transfer of Ryans Runner.java
 *
 */
public class GraphicMazeRunner extends JPanel {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
GraphData data = new GraphData();
		
		
		//Node start = data.getNodeById(76);
		//Node end = data.getNodeById(110);
		//Node start = data.getNodeById(394);
		//Node end = data.getNodeById(226);
		//Node start = data.getNodeById(219);
		//Node end = data.getNodeById(232);

//Node start = data.getBuildingByName("CSE"); // why the different pathes?
//Node end = data.getBuildingByName("CDH");
//Node end2 = data.getBuildingByName("MGH");

Node start = data.getBuildingByName("MGH");
Node end = data.getBuildingByName("CDH");
Node end2 = data.getBuildingByName("MGH");
TransportationMethod trans = data.getTransportationMethodById(1);
		FindPath find = new FindPath(start,end,trans);
		FindPath find2 = new FindPath(end,end2,trans);
		//FindPath find = new FindPath(start,end,null);
		LinkedList temp = (LinkedList) find.runFindPath();
		temp.addAll(find2.runFindPath());
		
		
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
		System.out.println("Distance:"+find.getPathDistance());
		System.out.println("Time:"+find.getPathTime());
		System.out.println("Distance:"+find2.getPathDistance());
		System.out.println("Time:"+find2.getPathTime());
	

		int xRange = maxX - minX;
		int yRange = maxY - minY;
		
		JFrame frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GraphicMazeRunner panel = new GraphicMazeRunner(minX, minY, maxX, maxY, temp);
		JScrollPane scrollerH = new JScrollPane(panel);
		JScrollPane scrollerV = new JScrollPane(scrollerH);
		scrollerH.setHorizontalScrollBar(new JScrollBar() );
		scrollerV.setVerticalScrollBar(new JScrollBar());
		scrollerV.setPreferredSize(new Dimension(500,500));
		frame.getContentPane().add(scrollerV);
	
		frame.pack();
		frame.setVisible(true);

	}
	
	public static int scale(int in, int min,int max){
		//return min + range - (in / min - max);
		//return min + (in / max - min);
		return (in / max - min);
	}
	List pathList;
	int minX, minY, maxX, maxY;
	
	public GraphicMazeRunner(int minX, int minY, int maxX, int maxY, List pathList) {
		super();
		this.minX = minX;
		this.minY = minY;
		this.maxX = maxX;
		this.maxY = maxY;
		
		int mult=1;
		//this.setBounds(min, minY, mult*maxX, mult*maxY);
		this.setBounds(0,0,maxX + 10, maxY+10);
		this.pathList = pathList;		
		//this.setPreferredSize(new Dimension(mult*maxX - minX, mult*maxY - minY));
		this.setPreferredSize(new Dimension(maxX+50, maxY+50));
		this.repaint();
	
		
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Rectangle range = null;
		range = this.getBounds(range);
		Dimension dim = this.getSize();
		
//		System.out.println(range.toString());		
//		System.out.println("range x: " + range.x + " - " + (range.width+range.x));
//		System.out.println("range y: " + range.y + " - " + (range.width+range.y));
//		
//		System.out.println("this.get: " + this.getWidth() + " - " + this.getHeight());
		
		Iterator iter = pathList.iterator();
		int prevX = -1, prevY = -1;
		g.setColor(Color.RED);
		int radious = 30;
		while(iter.hasNext()){
			Node cur = (Node)iter.next();
			int x = cur.getLocation().x;
			int y = cur.getLocation().y;
			//System.out.println("x: " + x + " y: " + y);
			//x = GraphicMazeRunner.scale(x, range.x, range.width+ range.x);
			//y =GraphicMazeRunner.scale(y, range.y, range.height+ range.y);
			//x = x - minX;
			//y = y - minY;
			
			x += 15;
			y += 15;
			
			g.drawOval(x-radious/2, y-radious/2, radious, radious);
			if( ( range.contains(prevX, prevY) && range.contains(x, y) ) ){
				g.setColor(Color.GREEN);
				g.drawLine(prevX, prevY, x, y);
				//System.out.println("drew line: " + "(x: " + x + ", y: " + y + ") + (prevX: "+ prevX + ",prevY: " + prevY + " )" );
			}
			prevX = x; prevY =y;
			g.setColor(Color.BLUE);
		}
		
		//System.out.print("\n\nEND paintComponent \n\n");
			
	}
	
	
}
	



