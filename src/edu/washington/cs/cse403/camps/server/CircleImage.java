package edu.washington.cs.cse403.camps.server;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

/**
 * @author ryanls03
 *
 */
public class CircleImage {
	public static final int BUFFER = 5;
	
	private Node node;
	private int circleSize;
	private int minX;
	private int minY;
	private int maxX;
	private int maxY;
	
	public CircleImage(Node toCircle, int circleSize){
		this.node = toCircle;
		this.circleSize = circleSize;
	}
	
	public void determineSize(){
		int x = node.getLocation().x;
		int y = node.getLocation().y;
		minX = x - circleSize/2 - BUFFER;
		minY = y - circleSize/2 - BUFFER;
		maxX = x + circleSize/2 + BUFFER;
		maxY = y + circleSize/2 + BUFFER;
	}
	
	public void writeImage(OutputStream out) throws IOException{
		determineSize();
		int width = getWidth();
		int height = getHeight();
		BufferedImage buffer =
		    new BufferedImage(width,
		                      height,
		                      BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = buffer.createGraphics();
		g.setPaint(new Color(0,0,0,0));
		g.fillRect(0,0,width,height);
		g.setColor(new Color(255,0,0,128));
		
		BasicStroke pen = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL); 
		g.setStroke(pen);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.drawOval(BUFFER, BUFFER, circleSize, circleSize);
		g.dispose();
		ImageIO.write(buffer, "png", out);
	}
	
	public String getImageUri() {
	  return "http://chart.apis.google.com/chart?"
	    + "cht=lxy" // chart type
	    + "&chs=" + getWidth() + "x" + getHeight()
      + "&chma=8,8,8,8" // chart margin
      + "&chxt=x,y"
      + "&chxs=0,00000000,0,0,_|1,00000000,0,0,_" // hide the axis
      + "&chf=bg,s,00000000" // background color
      + "&chd=t:-1|-1" // who knows
      + "&chfd=0,x,0,10,0.1,sin(x)*48%2B50|1,y,0,10,0.1,cos(y)*48%2B50" // formula to draw circle
      + "&chls=5" // line thickness
      + "&chco=FF000080"; // line color
	}
	
	public int getMinX() {
		return minX;
	}
	public int getMinY() {
		return minY;
	}
	public int getWidth(){
		return maxX-minX;
	}
	public int getHeight(){
		return maxY-minY;
	}
}
