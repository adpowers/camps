package edu.washington.cs.cse403.camps.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.washington.cs.cse403.camps.model.TransportationMethod;


/** 
 * CampsServlet The main servlet for the CAMPS program.  It responds to get requests from the client code
 * @author Ryan Adams
 */

public class CampsServlet extends HttpServlet {

  private static final Logger log = Logger.getLogger(CampsServlet.class.getName());
  
  public final static int circleSize = 100;//pixels

  private GraphData data;
  private FindPathManager manager;


  public void init() throws ServletException {
    data = new GraphData();
    manager = new FindPathManager();

    //pathCache = new ArrayList[cacheSize];
  }
  
  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    String method = req.getParameter("method");
    if(method != null){

      res.setContentType("text/html");
      PrintWriter out = res.getWriter();
      if (method.equals("getpathinfo")){
        /*
 GetPathInfo(int first, int last, int transportationType) returns
{   "time" : 2.1,
    "x" : 513,
    "y" : 20
}
         */
        String node1 = req.getParameter("node1");
        String node2 = req.getParameter("node2");
        String transType = req.getParameter("trans_type");
        //make sure all of the input parameters were specified
        if(node1 != null && node2 != null && transType != null){
          //Get the Java Objects for the respective ids
          Node start = data.getNodeById(Integer.parseInt(node1));
          Node end = data.getNodeById(Integer.parseInt(node2));
          TransportationMethod transMethod = data.getTransportationMethodById(Integer.parseInt(transType));
          //If the start is a valid node, but the end is not then we just need to draw a circle around the start
          if(start != null && end == null){
            //Create a new circle image object
            CircleImage ci = new CircleImage(start, circleSize);
            //determine the size of the image that would be generated
            ci.determineSize();
            int minx = ci.getMinX();
            int miny = ci.getMinY();
            int width = ci.getWidth();
            int height = ci.getHeight();
            //Return the location and size of the circle, and -1 for the time
            out.print("{ \"time\" : -1, ");
            out.print("\"x\" : "+minx+", ");
            out.print("\"y\" : "+miny+", ");
            out.print("\"w\" : "+width+", ");
            out.print("\"h\" : "+height+", ");
            out.print("\"imageUrl\" : \""+ci.getImageUri()+"\"");
            out.print(" }");
          }else{
            //Find a Path
            PathData pathData;
            if(start == null || end == null || transMethod == null){
              pathData = new PathData(new ArrayList(),-1,-1,-1);
            }else{
              pathData = manager.getPath(start, end, transMethod);
            }
            //Create a new PathImage object that will draw the path
            PathImage pi = new PathImage(pathData);
            //determine the size of the image that would be generated
            pi.determineSize();
            int minx = pi.getMinX();
            int miny = pi.getMinY();
            int width = pi.getWidth();
            int height = pi.getHeight();
            double time = pathData.pathTime;
            String timeFormated = String.valueOf(time);
            int index = timeFormated.indexOf('.');
            if(index != -1 && timeFormated.length() >= index+2){
              timeFormated = timeFormated.substring(0, index+2);
            }
            //Return the location and size of the path, and the time it takes to travel the path
            out.print("{ \"time\" : "+timeFormated+", ");
            out.print("\"x\" : "+minx+", ");
            out.print("\"y\" : "+miny+", ");
            out.print("\"w\" : "+width+", ");
            out.print("\"h\" : "+height+", ");
            out.print("\"imageUrl\" : \""+pi.getImageUri()+"\"");
            out.print(" }");
          }
        }else{
          //One or more required parameters were not entered
          res.sendError(405, "You must provide appropriate parameters.");
        }


      }else if (method.equals("autocomplete")) {
        // ?method=autocomplete&value=EE
        String autoCompleteValue = req.getParameter("value");

        Iterator iter = data.getMatchingBuildings(autoCompleteValue).iterator();
        out.print("{ results: [");
        while(iter.hasNext()){
          Building cur = (Building)iter.next();
          out.print("\""+cur.getPrimaryName()+" ("+cur.getAbbreviation()+")\"");
          if(iter.hasNext()){
            out.print(", ");
          }
        }
        out.print("] }");

        // find a list of buildings that match autoCompleteValue;
        // put these into a JSON object containing an array of strings
        /*
 { results: ["A", "B", "C"]
 }
         */

        //out.println("Autocomplete value " + autoCompleteValue);

      } else if (method.equals("getnode")) {
        //find a building that best matches this value and return a JSON object with its ID and name;
        /*
 {   
 id : 153, name : "Electrical Engineering"
    }
         */
        // ?method=getnode&value=EE
        String requestedNode = req.getParameter("value");

        String primary = requestedNode;
        String abbrev = null;
        int index = requestedNode.indexOf("(");
        if(index != -1){
          if(index > 0){
            primary = requestedNode.substring(0,index-1);
          }
          int index2 = requestedNode.indexOf(")");
          if(index2 != -1){
            abbrev = requestedNode.substring(index+1,index2);
          }
        }
        Building foundNode = data.getBuildingByName(primary);
        out.print("{ ");
        if(foundNode != null){
          out.print("\"id\" : "+foundNode.getID()+", ");
          out.print("\"name\" : \""+foundNode.getPrimaryName()+"\"");
        }else{
          if(abbrev != null && (foundNode = data.getBuildingByName(abbrev)) != null){
            out.print("\"id\" : "+foundNode.getID()+", ");
            out.print("\"name\" : \""+foundNode.getPrimaryName()+"\"");
          }else{
            foundNode = data.getBuildingByName(primary.toUpperCase());
            if(foundNode != null){
              out.print("\"id\" : "+foundNode.getID()+", ");
              out.print("\"name\" : \""+foundNode.getPrimaryName()+"\"");
            }else{
              out.print("\"id\" : -1, ");
              out.print("\"name\" : \"\"");
            }

          }
        }
        out.print(" }");

      } else if (method.equals("gettime")) {
        // DON'T WRITE THIS METHOD YET, THE METHOD MIGHT CHANGE SOON

        // ?method=gettime&startnode=12&endnode=315&transportationtype=2
        int start = Integer.parseInt(req.getParameter("startnode"));
        int end = Integer.parseInt(req.getParameter("endnode"));
        int transportationType = Integer.parseInt(req.getParameter("transportationtype"));

        // return the # of minutes to go from start to end using that transportation type;
        /*
 { time: 3.3 }
         */

        out.println("The time it takes to blank from node " + start +" to node " + end + " is a billion");

      } else if (method.equals("gettransportationtypes")) {
        // ?method=gettransportationtypes

        // return an array of objects containing the id and name of each transportation type
        /*
{ types: [{id: 1, name: "Walk"}, {id: 2, name: "Jog"}, {id: 3, name: "Crawl"}] }
         */

        Iterator iter = data.getTransportationMethods().iterator();

        out.print("{ \"types\" : [");

        while(iter.hasNext()) {
          TransportationMethod t = (TransportationMethod)iter.next();
          out.print("{ \"id\": " + t.id + ", \"name\": \"" + t.name() + "\"}");
          if (iter.hasNext()) {
            out.print(", ");
          }
        }

        out.println("] }");
      }
    } else {// No Method was specified
      res.sendError(405, "You must call an appropriate method.");
    }
  }
}
