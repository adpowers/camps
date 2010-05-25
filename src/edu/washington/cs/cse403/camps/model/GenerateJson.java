package edu.washington.cs.cse403.camps.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenerateJson {
  
  public static void main(String[] args) throws Exception {
    Map<String, PathType> pathTypeMap = Maps.newHashMap();
    pathTypeMap.put("1", PathType.Default);
    pathTypeMap.put("2", PathType.Stairs);
    
    String baseDir = "/Users/Andrew/Documents/workspace/Camps";

    // NAMES.TSV
    Map<String, String> locationNames = Maps.newHashMap();
    Map<String, String> locationAbbreviations = Maps.newHashMap();
    
    BufferedReader br = getReader(new File(baseDir, "names.tsv"));
    String line = br.readLine();
    while (line != null) {
      String[] parts = line.split("\t");
      if (parts.length != 3) {
        throw new RuntimeException(line);
      }
      locationNames.put(parts[0], parts[1]);
      locationAbbreviations.put(parts[0], parts[2]);
      line = br.readLine();
    }
    
    br.close();
    
    // NODES.TSV
    List<Node> nodes = Lists.newArrayList();
    
    br = getReader(new File(baseDir, "nodes.tsv"));
    line = br.readLine();
    while (line != null) {
      String[] parts = line.split("\t");
      if (parts.length != 4) {
        throw new RuntimeException(line);
      }
      Node node = new Node();
      node.nodeId = Integer.parseInt(parts[0]);
      node.x = Integer.parseInt(parts[1]);
      node.y = Integer.parseInt(parts[2]);
      node.name = locationNames.get(parts[3]);
      node.abbreviation = locationNames.get(parts[3]);
      nodes.add(node);
      
      line = br.readLine();
    }
    
    br.close();
    
    // EDGES.TSV
    List<Edge> edges = Lists.newArrayList();
    
    br = getReader(new File(baseDir, "edges.tsv"));
    line = br.readLine();
    while (line != null) {
      String[] parts = line.split("\t");
      if (parts.length != 6) {
        throw new RuntimeException(line);
      }
      Edge edge = new Edge();
      
      edges.add(edge);
      edge.edgeId = Integer.parseInt(parts[0]);
      edge.startNodeId = Integer.parseInt(parts[1]);
      edge.endNodeId = Integer.parseInt(parts[2]);
      edge.bidirectional = ("1".equals(parts[3]));
      edge.pathType = pathTypeMap.get(parts[4]);
      line = br.readLine();
    }
    
    br.close();
    
    NodeList nodeList = new NodeList();
    nodeList.nodes = nodes;
    
    EdgeList edgeList = new EdgeList();
    edgeList.edges = edges;
    
    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
    String nodeJson = gson.toJson(nodeList);
    String edgeJson = gson.toJson(edgeList);
    
    BufferedWriter bw = getWriter(new File(baseDir, "nodes.json"));
    bw.write(nodeJson);
    bw.close();
    
    bw = getWriter(new File(baseDir, "edges.json"));
    bw.write(edgeJson);
    bw.close();
  }
  
  private static BufferedReader getReader(File file) throws Exception {
    return new BufferedReader(new FileReader(file));
  }
  
  public static BufferedWriter getWriter(File file) throws Exception {
    return new BufferedWriter(new FileWriter(file));
  }
}
