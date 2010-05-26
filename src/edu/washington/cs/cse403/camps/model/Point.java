package edu.washington.cs.cse403.camps.model;

public class Point {
  public final int x;
  public final int y;
  
  public Point(int x, int y) {
    this.x = x;
    this.y = y;
  }
  
  public Point(Point point) {
    this.x = point.x;
    this.y = point.y;
  }
  
  public double distance(Point other) {
    int xDiff = other.x - this.x;
    int yDiff = other.y - this.y;
    return Math.sqrt(xDiff * xDiff + yDiff + yDiff);
  }
}
