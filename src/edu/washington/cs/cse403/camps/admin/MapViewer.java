package edu.washington.cs.cse403.camps.admin;

import java.awt.*;
import java.awt.image.*;
import javax.imageio.*;
import javax.imageio.stream.*;

import javax.swing.*;
import java.util.*;
/**A basic viewer that draws circles on buildings and lines for edges from data in a MapModel*/
public class MapViewer extends JPanel implements Viewer{
	private MapModel model;

	//drawing options
	private final int circleSize = 10;
	private boolean showPicture = true;
	private boolean showNonBuildings = true;
	private Color edgeColor;
	private Color visitedEdgeColor;
	private Color nodeColor;
	private Color startNodeColor;
	private Color endNodeColor;

	private int xCoordOffset, yCoordOffset = 0; //for scrolling
	private final int maxScrollChange = 150; //solves a jumpy problem from mouse motion

	/**Create a new viewer.  Defaults to 500 by 500 pixels
	 * @param model the MapModel to watch*/
	public MapViewer(MapModel model){
		this(model, 500, 500);
	}

	/**Create a new viewer
	 * @param model the MapModel to watch
	 * @param width width of the viewer
	 * @param height height of the viewer*/
	public MapViewer(MapModel model, int width, int height) {
		setPreferredSize(new Dimension(width, height));
		this.model = model;
		edgeColor = Color.green;
		visitedEdgeColor = Color.red;
		nodeColor = Color.blue;
		startNodeColor = Color.green;
		endNodeColor = Color.red;
	}

	/**Notifies this viewer of model changes, updates accordingly.*/
	public void notifyViewer() {
		repaint();
	}

	/**Draws this view's representation of the model
	 * @param g the context to draw on*/
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if(showPicture){
			BufferedImage i = model.getImage();

			if(i != null){
				g.drawImage(i, 0+xCoordOffset, 0+yCoordOffset, null);//g.drawImage(i, 0, 0, 451, 527, 0, 0, i.getWidth(this), i.getHeight(this), this);//getWidth(), getHeight(), 
			}
		}

		//draw nodes
		Collection nodes = model.getNodes();
		Iterator it = nodes.iterator();
		while(it.hasNext()) {
			g.setColor(nodeColor);
			Node cur = (Node)it.next();
			if(cur == model.getStart()){
				g.setColor(startNodeColor);
			}
			if(cur == model.getEnd()){
				g.setColor(endNodeColor);
			}
			/*
      if(cur.getName().charAt(0) != 'p'){
        drawNode(g, cur);
      } else if (showNonBuildings){
        drawNode(g, cur);
      }
			 */
			drawNode(g, cur);
		}

		//draw edges
		Collection edges = model.getEdges();
		it = edges.iterator();
		g.setColor(edgeColor);
		while(it.hasNext()) {
			drawEdge(g, (Edge)it.next());
		}
		/*
    //draw visited edges on top of everything else
    ArrayList visitedEdges = model.getVisitedEdges();
    it = visitedEdges.iterator();
    g.setColor(visitedEdgeColor);
    while(it.hasNext()) {
      drawEdge(g, (Edge)it.next());
    }
		 */
	}

	/**draws this viewer's interpretation of a node on the screen and shifts offsets
	 * @param g the context to draw on
	 * @param cur the current node being drawn*/
	private void drawNode(Graphics g, Node cur){
		Rectangle bounds = this.getBounds();
		int x = cur.getxLocation();
		int y = cur.getyLocation();

		if(x < -1*xCoordOffset || y < -1*yCoordOffset || x > -1*xCoordOffset+bounds.width || y > -1*yCoordOffset+bounds.height){
			return;
		}
		//System.out.println("x,y,width,height"+xCoordOffset+","+yCoordOffset+","+bounds.width+","+bounds.height);
		if(showNonBuildings && !(cur instanceof Building)){
			g.drawOval(x-circleSize/2+xCoordOffset, y-circleSize/2+yCoordOffset, circleSize, circleSize);
			g.drawOval(x-circleSize/2+xCoordOffset+2, y-circleSize/2+yCoordOffset+2, circleSize-4, circleSize-4);
			g.drawOval(x-1+xCoordOffset, y-1+yCoordOffset, 2, 2);
		}else if(cur instanceof Building ){
			g.fillOval(x-circleSize/2+xCoordOffset, y-circleSize/2+yCoordOffset, circleSize, circleSize);
			g.drawLine(x-(int)(circleSize/2*Math.sin(Math.PI/2))+xCoordOffset, y+(int)(circleSize/2*Math.sin(Math.PI/2))+yCoordOffset, x+(int)(circleSize/2*Math.sin(Math.PI/2))+xCoordOffset, y-(int)(circleSize/2*Math.sin(Math.PI/2))+yCoordOffset);
			g.drawLine(x-(int)(circleSize/2*Math.sin(Math.PI/2))+xCoordOffset, y-(int)(circleSize/2*Math.sin(Math.PI/2))+yCoordOffset, x+(int)(circleSize/2*Math.sin(Math.PI/2))+xCoordOffset, y+(int)(circleSize/2*Math.sin(Math.PI/2))+yCoordOffset);
			g.drawString(((Building)cur).getAbbreviation(), x+xCoordOffset-5, y+yCoordOffset-5);
		}
	}

	/**draws this viewer's interpretation of an edge on the screen and shifts offsets
	 * @param g the context to draw on
	 * @param e the Edge to draw*/
	private void drawEdge(Graphics g, Edge e){
		Rectangle bounds = this.getBounds();
		Node start = e.getStartingNode();
		Node end = e.getEndingNode();
		int x1 = start.getxLocation();
		int y1 = start.getyLocation();
		int x2 = end.getxLocation();
		int y2 = end.getyLocation();
		if((x1 < -1*xCoordOffset && x2 < -1*xCoordOffset) || (y1 < -1*yCoordOffset && y2 < -1*yCoordOffset) || (x1 > -1*xCoordOffset+bounds.width && x2 > -1*xCoordOffset+bounds.width) || (y1 > -1*yCoordOffset+bounds.height && y2 > -1*yCoordOffset+bounds.height)){
			return;
		}
		g.drawLine(x1+xCoordOffset,y1+yCoordOffset,x2+xCoordOffset,y2+yCoordOffset);

		int arcSize = 40;
		double angle;
		int x3;
		int y3;
		if(x2 != x1){
			double m = (double)(y2-y1)/(double)(x2-x1);
			//double b = y1-m*x1;
			//MIDPOINT
			x3 = (x2-x1)/2+x1;
			y3 = (y2-y1)/2+y1;
			double theta = Math.atan(m)*180/Math.PI;
			if(x1>x2){
				theta += 180;
			}
			angle = 160-theta;//160 is 180 - the angle away from the line. i.e. 20, which is why arcSize is 40;

		}else{
			if(y2>y1){
				angle = 70;
			}else{
				angle = 70+180;
			}
			x3 = x1;
			y3 = (y2-y1)/2+y1;
		}
		g.fillArc(x3+xCoordOffset-10,y3+yCoordOffset-10,20,20,(int)angle,arcSize);
		if(e.isBidirectional()){
			g.fillArc(x3+xCoordOffset-10,y3+yCoordOffset-10,20,20,(int)angle-180,arcSize);
		}
	}

	/**Tell the viewer to show the background image, if one is specified
	 * @param showPicture if this viewer should show the background image*/
	public void setShowPicture(boolean showPicture){
		this.showPicture = showPicture;
		repaint();
	}

	/**Tell the viewer to show nodes that are not buildings.  These nodes begin with 'p'
	 * @param showNonBuildings if this viewer should draw nodes if the node isn't a building*/
	public void setShowNonBuildings(boolean showNonBuildings){
		this.showNonBuildings = showNonBuildings;
		repaint();
	}
	/**Sets the X offset
	 * @param deltaX the amount to change the offset by */
	public void changeXOffset(int deltaX){
		if(deltaX > maxScrollChange || deltaX < -maxScrollChange){ //prevents jumpy behavior from mouse motion releases then restarts...
			return;
		}
		xCoordOffset = xCoordOffset + deltaX;
		repaint();
	}
	/**Sets the Y offset
	 * @param deltaY the amount to change the offset by */
	public void changeYOffset(int deltaY){
		if(deltaY > maxScrollChange || deltaY < -maxScrollChange){
			return;
		}
		yCoordOffset = yCoordOffset +deltaY;
		repaint();
	}

	/**Gets the X offset
	 * @return returns the X offset*/
	public int getXOffset(){
		return xCoordOffset;
	}

	/**Gets the Y offset
	 * @return returns the Y offset*/
	public int getYOffset(){
		return yCoordOffset;
	}
	/**Change the color of edges
	 * @param c the new color*/
	public void setEdgeColor(Color c){
		edgeColor = c;
		repaint();
	}
	/**Change the color of visited edges
	 * @param c the new color*/
	public void setVisitedEdgeColor(Color c){
		visitedEdgeColor = c;
		repaint();
	}
	/**Change the color of nodes
	 * @param c the new color*/
	public void setNodeColor(Color c){
		nodeColor = c;
		repaint();
	}
	/**Change the color of the starting node
	 * @param c the new color*/
	public void setStartNodeColor(Color c){
		startNodeColor = c;
		repaint();
	}
	/**Change the color of the ending node
	 * @param c the new color*/
	public void setEndNodeColor(Color c){
		endNodeColor = c;
		repaint();
	}
	/**Get the edge color
	 * @return the current color of edges*/
	public Color getEdgeColor(){
		return edgeColor;
	}
	/**Get the visited edge color
	 * @return the current color of visited edges*/
	public Color getVisitedEdgeColor(){
		return visitedEdgeColor;
	}

	/**Get the node color
	 * @return the current color of nodes*/
	public Color getNodeColor(){
		return nodeColor;
	}

	/**Get the starting node color
	 * @return the current color of the starting node*/
	public Color getStartNodeColor(){
		return startNodeColor;
	}

	/**Get the end node color
	 * @return the current color of the ending node*/
	public Color getEndNodeColor(){
		return endNodeColor;
	}

	/**Information about this viewer
	 * @return a String representation of this viewer*/
	public String toString(){
		return "Viewing " + model;
	}
}