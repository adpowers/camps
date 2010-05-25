package edu.washington.cs.cse403.camps.model;

public enum TransportationMethod {
  Walk(1.0),
  Run(0.75),
  Bike(0.5),
  Wheelchair(2);
  
  public final double costMultiplier;
  
  TransportationMethod(double costMultiplier) {
    this.costMultiplier = costMultiplier;
  }
}
