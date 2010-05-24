package edu.washington.cs.cse403.camps.admin;

import java.util.*;
/**This pathfinding algorithm tries to get closer to its destination in every step
 */
public class CloserTo{
  private ArrayList visitedEdges = new ArrayList();
  
  /**Figure out a path between the two nodes
   * @param start where to start
   * @param end where to end
   * @return the list of edges contained in the path
   */
  public ArrayList pathfind(Node start, Node end){
    visitedEdges.clear();
    moveCloser(start, end);
    return visitedEdges;
  }
  
  /**Recursively tries to get closer*/
  private void moveCloser(Node start, Node end){
    if (start == end){
      return;
    }
    else {
      ArrayList exitPaths = start.getExits();
      if(exitPaths.size() == 0){
        ErrorHandler.foundError("Pathfinding failed because of a dead end.");
      }
      Iterator it = exitPaths.iterator();
      double distanceToEnd = Double.MAX_VALUE;
      
      //just initial setup to intitialize makesMostProgress
      Edge makesMostProgress = (Edge)it.next();
      distanceToEnd = makesMostProgress.getEndingNode().distanceTo(end); 
      
      //compare the other options for leaving the start node
      while(it.hasNext()){
        Edge temp = (Edge)(it.next());
        Node possibleDestination = temp.getEndingNode();
        if (possibleDestination.distanceTo(end) < distanceToEnd && possibleDestination.getExits().size() > 0){
          distanceToEnd = possibleDestination.distanceTo(end);
          makesMostProgress = temp;
        }
      }
      
      visitedEdges.add(makesMostProgress);
      moveCloser(makesMostProgress.getEndingNode(), end);
    }
  }
}