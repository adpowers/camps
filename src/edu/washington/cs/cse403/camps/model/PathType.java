package edu.washington.cs.cse403.camps.model;

public enum PathType {
  Default(1.0),
  Stairs(1.5);
  
  public final double costMultiplier;
  
  PathType(double costMultiplier) {
    this.costMultiplier = costMultiplier;
  }
}
