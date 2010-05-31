package edu.washington.cs.cse403.camps.model;

public enum TransportationMethod {
  Walk(1, 1.0),
  Run(2, 0.75),
  Bike(3, 0.5),
  Wheelchair(4, 2);
  
  public final int id;
  public final double costMultiplier;
  
  TransportationMethod(int id, double costMultiplier) {
    this.id = id;
    this.costMultiplier = costMultiplier;
  }
}
