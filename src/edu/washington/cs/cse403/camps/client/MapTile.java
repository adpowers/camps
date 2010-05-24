package edu.washington.cs.cse403.camps.client;

import java.lang.*;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.DOM;
/** 
 * MapTiles are contained within the MapPanel
 * @author Dale Olson
 */

public class MapTile extends Image {
	/***** MapTile CLASS VARIABLE DECLARATIONS *****/
	public static final String BLANKIMGLOC = "images/blank.gif";
	
	private String url;
	public static int WIDTH=400,HEIGHT=400;
	
	private boolean loaded=false;
	/***** MapTile CLASS FUNCTION DECLARATIONS *****/
	
	//constructor
	public MapTile(String image_url){
		this.setPixelSize(WIDTH, HEIGHT);
		url = image_url; //dont load yet
		this.setStyleName("map");
		this.setTitle("Click + Drag to scroll the map.");
		this.addLoadListener(new LoadListener() {
	        public void onLoad(Widget sender) {
				sender.setVisible(true);
				loaded=true;
			}
	        public void onError(Widget sender) {
	        	//Window.alert("There was an error loading "+((Image)sender).getUrl());
	        }
	    });
	}
	
	public void load(){
		if (!loaded){
			setUrl(url);
			//setVisible(false);
		}
	}
	public boolean isLoaded(){
		if (loaded) return true;
		else return false;
	}
	public int getHeight(){
		return HEIGHT;
	}
	public int getWidth(){
		return WIDTH;
	}
	public static int getStaticHeight() {
	  return HEIGHT;
	}
	public static int getStaticWidth() {
	  return WIDTH;
	}
}


