package edu.washington.cs.cse403.camps.admin;

//import java.net.*;
//import java.io.*;

import javax.swing.JOptionPane;

/**Main way to run the map pathfinding project*/
public class Runner2 {
    
    /**main method to run the project
     * @param args Type "default" (no quotes) to load the default list of nodes and edges*/
    public static void main (String[] args) {
        
        
        //Loader l = new Loader(m);
        GraphData data = new GraphData();
        MapModel m = new MapModel(data);
        String host = JOptionPane.showInputDialog("Enter the host name","localhost");
        String port = JOptionPane.showInputDialog("Enter the port number","3306");
        String password = JOptionPane.showInputDialog("Enter the database password");
        if(data.connect(host,Integer.parseInt(port),"cse403c-wi07_test","cse403c-wi07",password)){
        	data.fillData(m);
        }else{
        	System.exit(0);
        }
        
        
        /*
        // String arg1 = args[0];
        if(true) {
        	//URL dataURL = m.getThisResource("map3.txt");
        	try{
        		URL dataURL = new URL("http://students.washington.edu/jarfalk/cs/map3.txt");
        		//File dataFile = new File((InputStream)dataURL.getContent());
        		Reader reader = new InputStreamReader((InputStream)dataURL.getContent());
        		l.processReader(reader);
        	}
        	catch(MalformedURLException e ){
        		ErrorHandler.foundError("Problem reading from the URL http://students.washington.edu/jarfalk/cs/map3.txt");
        	}
        	catch(IOException e){
        		ErrorHandler.foundError("problem reading the default data file. You might not be connected to the internet.");
        	}

        } else {
        	l.processFile();
        }
        */
        m.setImage("UniversityMap.png");
        MapController control = new MapController(m);
    }
    
}