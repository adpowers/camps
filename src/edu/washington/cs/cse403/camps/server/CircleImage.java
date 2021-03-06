package edu.washington.cs.cse403.camps.server;


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
