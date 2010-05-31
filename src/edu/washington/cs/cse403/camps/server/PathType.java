package edu.washington.cs.cse403.camps.server;

import java.util.HashSet;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.collect.Lists;
import com.google.appengine.repackaged.com.google.common.collect.Sets;

import edu.washington.cs.cse403.camps.model.TransportationMethod;


/**
 * Represents the type of a path edge. Example: Stairs. We currently have two types of paths - 'Default' 
 * and 'Stairs'. There is also an arraylist of associated transportation methods. PathType 
 */

public class PathType {
  private int id;  // the id key associated w/ this type of path
  private String name;  // the string name describing this type of path
  private double costMultiplier;  // the cost associated w/ taking this path. The costMultiplier must be >= 1. 
  private HashSet<TransportationMethod> transportationMethods; // associated transportation methods for this path type
  private static final double transportCostDefault = Double.MAX_VALUE / 100; // 4.0 * pathCost * 1/220  = min/ft

  /**
   * Constructor - assigns the ID, name, and costMultiplier
   */
  public PathType(int id, String name, double costMultiplier){
    this.id = id;
    this.costMultiplier = costMultiplier;
    this.name = name;
    this.transportationMethods = Sets.newHashSet();
  }

  public int getId(){
    return id;
  }
  
  public String getName(){
    return name;
  }

  public double getCostMultiplyer(){
    return getCostMultiplyer(null);
  }

  public double getCostMultiplyer(TransportationMethod trans){
    if (transportationMethods.contains(trans)) {
      return costMultiplier * trans.costMultiplier;
    } else {
      return transportCostDefault;
    }
  }

  public List<TransportationMethod> getTransportationMethods(){
    return Lists.newArrayList(transportationMethods);
  }

  public void addMethod(TransportationMethod method){
    if(!this.transportationMethods.contains(method))
      this.transportationMethods.add(method);
  }
  
  public String toString(){
    return "Path Type "+name+" with cost "+costMultiplier;
  }
}
