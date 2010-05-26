package edu.washington.cs.cse403.camps.server;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import com.google.appengine.repackaged.com.google.common.base.Join;
import com.google.appengine.repackaged.com.google.common.collect.Lists;

/**
 * PathImage Draws an image based on a Path
 * 
 * @author Ryan Adams
 */


public class PathImage {
  private PathData pathData;
  private int minX;
  private int minY;
  private int maxX;
  private int maxY;

  public PathImage(PathData pathData){
    this.pathData = pathData;
  }

  /**
   * Determines the coordinates of an image (TopLeft, TopRight, BottomLeft, BottomRight) that
   * can just fit all of the points in the path.
   */
  public void determineSize(){
    if(!pathData.isNull()){
      minX = Integer.MAX_VALUE;
      minY = Integer.MAX_VALUE;
      maxX = 0;
      maxY = 0;
      Iterator iter = pathData.nodes.iterator();
      while (iter.hasNext()){
        Node cur = (Node)iter.next();
        if (!cur.isBuilding() || pathData.nodes.size() <= 3) {
          int x = cur.getLocation().x;
          int y = cur.getLocation().y;
          if (x < minX){
            minX = x;
          }
          if (x > maxX){
            maxX = x;
          }
          if (y < minY){
            minY = y;
          }
          if (y > maxY){
            maxY = y;
          }
        }
      }
      /* Adjust for width of lines */
      if (minX > 25) {
        minX = minX - 25;
      }
      if (minY > 25) {
        minY = minY - 25;
      }
      maxX = maxX + 25;
      maxY = maxY + 25;
    } else{
      minX = 0;
      minY = 0;
      maxX = 10;
      maxY = 10;
    }
  }

  /**
   * Writes out an image representing the path to the provided output stream.  This image contains a transparent
   * background and is meant to serve as an overlay onto a map.
   * @param out stream to write to
   * @throws IOException
   */
  public void writeImage(OutputStream out) throws IOException{
    determineSize();
    int width = getWidth();
    int height = getHeight();
    if(width < 5)
      width = 10;
    if(height < 5)
      height = 10;
    BufferedImage buffer =
      new BufferedImage(width,
          height,
          BufferedImage.TYPE_INT_ARGB);
    Graphics2D g = buffer.createGraphics();
    g.setPaint(new Color(0,0,0,0));
    g.fillRect(0,0,width,height);
    Random rand = new Random();

    Color[] colors = new Color[6];
    colors[0] = new Color(255,255,0,128);
    colors[1] = new Color(255,0,255,128);
    colors[2] = new Color(0,255,255,128);
    colors[3] = new Color(0,99,255,128);
    colors[4] = new Color(0,0,255,128);
    colors[5] = new Color(255,0,0,128);
    Color color = colors[rand.nextInt(6)];

    Color white = new Color(255,255,255,40);//last arg is alpha
    Color white2 = new Color(255,255,255,40);//last arg is alpha

    BasicStroke outline = new BasicStroke(20,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
    BasicStroke outline2 = new BasicStroke(25,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL);
    BasicStroke pen = new BasicStroke(5,BasicStroke.CAP_ROUND,BasicStroke.JOIN_BEVEL); 

    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    Polygon poly = new Polygon();
    Iterator iter = pathData.nodes.iterator();
    while(iter.hasNext()){
      Node cur = (Node)iter.next();
      if(!cur.isBuilding() || pathData.nodes.size() <= 3){
        poly.addPoint(cur.getLocation().x - minX, cur.getLocation().y - minY);
      }
    }
    color = new Color(255,0,0,164);
    g.setStroke(outline);
    g.setColor(white);
    g.drawPolyline(poly.xpoints, poly.ypoints, poly.npoints);
    g.setStroke(outline2);
    g.setColor(white2);
    g.drawPolyline(poly.xpoints, poly.ypoints, poly.npoints);
    g.setStroke(pen);
    g.setColor(color);
    g.drawPolyline(poly.xpoints, poly.ypoints, poly.npoints);
    g.dispose();
    ImageIO.write(buffer, "png", out);
  }

  public String getImageUri() {
    List<Integer> xPoints = Lists.newArrayList();
    List<Integer> yPoints = Lists.newArrayList();

    for (Node node : pathData.nodes) {
      if (!node.isBuilding() || pathData.nodes.size() <= 3) {
        xPoints.add((int)((node.getLocation().x - minX) * 100 / getWidth()));
        yPoints.add(100 - (int)((node.getLocation().y - minY) * 100 / getHeight()));
      }
    }

    return "http://chart.apis.google.com/chart?"
    + "cht=lxy" // chart type
    + "&chs=" + getWidth() + "x" + getHeight()
    + "&chxt=x,y"
    + "&chxs=0,00000000,0,0,_|1,00000000,0,0,_"
    + "&chf=bg,s,00000000"
    + "&chd=t:" + Join.join(",", xPoints) + "|" + Join.join(",", yPoints)
    + "&chls=5"
    + "&chco=FF000080";
  }

  /**
   * @return the min x coordinate of the image relative to the base map
   */
  public int getMinX() {
    return minX;
  }
  /**
   * @return the min y coordinate of the image relative to the base map
   */
  public int getMinY() {
    return minY;
  }
  /**
   * @return the width of the image
   */
  public int getWidth(){
    return maxX-minX;
  }
  /**
   * @return the height of the image
   */
  public int getHeight(){
    return maxY-minY;
  }
  /**
   * @return the time in minutes that it takes to travel from 
   * the start node to the end node along the path
   */
  public double getTime() {
    return pathData.pathCost;
  }

  /**
   * @return the nodes contained in this path
   */
  public List<Node> getNodes() {
    return Lists.newArrayList(pathData.nodes);
  }
}
