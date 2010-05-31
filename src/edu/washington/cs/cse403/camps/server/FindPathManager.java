package edu.washington.cs.cse403.camps.server;

import java.util.HashMap;
import java.util.List;

import com.google.appengine.repackaged.com.google.common.base.Objects;

import edu.washington.cs.cse403.camps.model.TransportationMethod;

/**
 * This class is a manager for running find path 
 * instances and caching path results.  Only a
 * single instance of this class should be created.
 * @author Ryan
 *
 */
public class FindPathManager {
	private static final int cacheSize = 60;

	//private GraphData data;
	private HashMap pathCache;
	private PathKey[] knownPaths;
	private int index;

	/**
	 * Initilizes a new FindPathManager
	 */
	public FindPathManager(){
		pathCache = new HashMap();
		knownPaths = new PathKey[cacheSize];
		index = 0;
	}
	
	/**
	 * Finds a path between the start and end node using the given transporation method.
	 * Will used cached data if avialable
	 * @param start Start Node
	 * @param end End Node
	 * @param transMethod Transportation Method
	 * @return
	 */
	public PathData getPath(Node start,Node end,TransportationMethod transMethod){
		PathData data = getCachedPath(start,end,transMethod);
		if(data == null){
			/*
			 * Means that the path was not cached, so we need to create an
			 * instance of FindPath, run the find path algorithm, and cache the path
			 */
			FindPath finder = new FindPath(start,end,transMethod);
			List nodes = finder.runFindPath();
			data = new PathData(nodes,finder.getPathCost(),finder.getPathDistance(),finder.getPathTime());
			cachePath(start,end,transMethod,data);
		}
		return data;
	}

	/**
	 * This method increments the index(pointer) of the caching array.  
	 * It is one of three synchronized methods that modifies or reads from the cache
	 */
	private synchronized void incrementIndex(){
		index++;
		if(index >= cacheSize){
			index = 0;
		}
	}

	/**
	 * This method caches a path.  
	 * It is one of three synchronized methods that modifies or reads from the cache
	 * @param start The start node, one part of the three part key
	 * @param end The end node, one part of the three part key
	 * @param trans The transportation method, one part of the three part key
	 * @param data The path data to be cached.
	 */
	private synchronized void cachePath(Node start, Node end, TransportationMethod trans, PathData data){
		PathKey key = new PathKey(start,end,trans);
		PathKey existing = knownPaths[index];
		if(existing != null){
			/*
			 * The array works as a circular list, so the path that was cached the 
			 * longest ago is removed.  We have to remove the corresponding element
			 * from the HashTable, if it exists.
			 */
			pathCache.remove(existing);
		}
		knownPaths[index] = key;//Overwrite array with new key
		pathCache.put(key, data);//Add data to Hash Table
		incrementIndex();
	}

	/**
	 * This method returns the data for a path if it has been cached.
	 * @param start The start node, one part of the three part key
	 * @param end The end node, one part of the three part key
	 * @param trans The transportation method, one part of the three part key
	 * @return The path data or null if not found
	 */
	private synchronized PathData getCachedPath(Node start, Node end, TransportationMethod trans){
		return (PathData)pathCache.get(new PathKey(start,end,trans));
	}

	/**
	 * This class is used to represent the key for a path.
	 * @author Ryan
	 *
	 */
	class PathKey {
		public Node start;
		public Node end;
		public TransportationMethod trans;
		/**
		 * Create a new path key.
		 * @param start The start node
		 * @param end The end node
		 * @param trans The transportation method
		 */
		public PathKey(Node start, Node end, TransportationMethod trans){
			this.start = start;
			this.end = end;
			this.trans = trans;
		}

		public int hashCode() {
		  return Objects.hashCode(start.getID(), end.getID(), trans);
		}

		public boolean equals(Object o){
			if(o instanceof PathKey){
				PathKey p = (PathKey)o;
				if(p.start.equals(this.start) && p.end.equals(this.end) && p.trans.equals(this.trans)){
					return true;
				}
			}
			return false;
		}

	}

}
