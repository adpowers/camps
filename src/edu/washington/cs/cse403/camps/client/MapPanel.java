package edu.washington.cs.cse403.camps.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Window;

/**
 * MapPanel serves as the container for the right hand portion
 * of the CAMPS ui. It serves as the primary "view" part of the
 * program.
 *
 * Window that the MapPanel displays is at tile index (winx, winy)
 * and zoom is a scalar that operates on the native tile size
 * (MapTile.NATIVESIZE).
 *
 * @author Dale Olson
 */
public class MapPanel extends AbsolutePanel{
  public static int WINDOWWIDTH = 800;
  public static int WINDOWHEIGHT = 800;
   
  public static final int MAPTILESACROSS = 16;
  public static final int MAPTILESDOWN = 11;
   
  private static final String IMG_PREFIX = "images/map/UniversityMapSlice_";
   
  private ArrayList tiles = new ArrayList();        // holds the tiles
  private HashMap<String, Overlay> overlays = new HashMap<String, Overlay>();    // holds the overlays
  private HashMap<String, Integer> refCount = new HashMap<String, Integer>();    // holds the count of duplicate overlays
 
  public boolean clicked = false;
 
  private double zoomfactor = 1.0;
 
  TileWindow win;
 
  public MapPanel() {
    super();
    //center=new Coordinate(WINDOWWIDTH/2,WINDOWHEIGHT/2);
    DOM.setAttribute(this.getElement(), "oncontextmenu", "return false;");
    win = new TileWindow(new Coordinate(2200,1700), new Coordinate(3000,2500));
   
    for (int i=0; i < MAPTILESACROSS * MAPTILESDOWN; i++) {
      tiles.add(new MapTile(IMG_PREFIX + (i+1) + ".png"));
    }
    this.setPixelSize(WINDOWWIDTH, WINDOWHEIGHT);
  }
   
  /**
   * Requests image at [address] from server and adds it to ArrayList "overlays"
   * Displays overlay along with next draw() command at (x,y) ( w.r.t. pixels from top left of
   * complete map img)
   *     @author Dale
   *     @version 0.1
   *     @param key      Key used for lookups, not necessarily the URL.
   *     @param address    String containing actual link to overlay image
   *     @param x        Destination x coord of the overlay ( w.r.t. complete map)
   *     @param y        Destination y coord of the overlay (w.r.t. complete map)
   *     @return            void
   */
  public void addOverlay(String key, String address, int x, int y, int width, int height){
    //need to determine proper zoom factor!!
     
    Integer count = refCount.get(key);
    if (count == null) {
      if (!overlays.containsKey(key)){
        Overlay olay=new Overlay(address, x - 150, y - 90, width, height); //convert between client tiles & map server is using
        this.add(olay);
        win.setCenter ((int)((x+(width/2))*zoomfactor)-150,(int)((y+(height/2))*zoomfactor)-90);
        overlays.put(key, olay);
        olay.setVisible(true);
        draw();
      }
      refCount.put(key, 1);
    } else {
      refCount.put(key, count.intValue() + 1);
    }
  }
 
  /**
   * Removes overlay at index specified.
   *     @author Dale
   *     @version 0.1
   *     @param index    Index (passed back while adding overlay), to remove from arraylist
   *     @return            True if the overlay existed and was removed properly
   */
  public boolean removeOverlay(String key){
    if (!refCount.containsKey(key)) {
      return false;
    }
    
    Integer count = refCount.get(key);
     
    if (count.intValue() == 1) {   
      Overlay olay = overlays.get(key);
      olay.removeFromParent();
      overlays.remove(key);
      refCount.remove(key);
    } else {
      refCount.put(key, count.intValue() - 1);
    }
    return true;
  }
 
  /* Helper Class describing an overlay */
  private class Overlay extends Image{
    private Coordinate loc = new Coordinate(0,0);
    private int height;
    private int width;
    
    public Overlay(String img, int x, int y, int w, int h){
      super(img);
      set(x,y,w,h);
      this.setStyleName("overlay");
      this.setVisible(false);
      this.addLoadListener(new LoadListener() {
        public void onLoad(Widget sender) {
          //do nothing
        }
        public void onError(Widget sender) {
          error("There was an error loading "+((Image)sender).getUrl());
          sender.removeFromParent();
          overlays.remove(((Overlay)sender).getUrl());
        }
      });
    }
    
    public int getWidth() { return width;}
    public int getHeight() { return height;}
    public int getX(){return (int)(loc.X());}
    public int getY(){return (int)(loc.Y());}
    public void set(int x, int y, int w, int h){
      width=w;
      height=h;
      loc.set(x,y);
    }
  }
 
  /**
   * Forces the MapPanel to draw itself with respect to the center coordinate
   * specified by the window (win.getCenter()).
   *     @author Dale
   *     @version 0.8
   */
  public void draw (){
    MapTile temptile;
    /*    calculate offset from (0,0) in the mappanel */
    int offsetx = win.getCenter().X()-(win.TL.getTileX()*MapTile.getStaticWidth())-WINDOWWIDTH/2;
    int offsety = win.getCenter().Y()-(win.TL.getTileY()*MapTile.getStaticHeight())-WINDOWHEIGHT/2;

    /* calculate max of the window in terms of tiles */
    int winmaxY=Math.min(win.TL.getTileY()+win.height()+1,MAPTILESDOWN);
    int winmaxX=Math.min(win.TL.getTileX()+win.width()+1,MAPTILESACROSS);
     
    int tx,ty;    //temporary
     
    /* REMOVE UNNEEDED TILES */
    for (int y=0; y<win.TL.getTileY (); y++){    //0-tl
      for (int x=0; x< MAPTILESACROSS; x++){    //all the way across
        temptile=(MapTile)tiles.get((y*MAPTILESACROSS)+x);    //get tile corresponding to window loc
        if ( temptile.isAttached()) {
          temptile.removeFromParent();
        }
      }
    }       
    for (int y=winmaxY; y<MAPTILESDOWN; y++){        //bl-bottom
      for (int x=0; x< MAPTILESACROSS; x++){            //all the way
        temptile=(MapTile)tiles.get((y*MAPTILESACROSS)+x);    //get tile corresponding to window loc
        if (temptile.isAttached()) {
          temptile.removeFromParent();
        }
      }
    }
    for (int y=0; y<MAPTILESDOWN; y++){   
      for (int x=0; x< win.TL.getTileX(); x++){
        temptile=(MapTile)tiles.get((y*MAPTILESACROSS)+x);    //get tile corresponding to window loc
        if (temptile.isAttached()) {
          temptile.removeFromParent();
        }
      }
    }
    for (int y=0; y<MAPTILESDOWN; y++){   
      for (int x=winmaxX; x< MAPTILESACROSS; x++){
        temptile=(MapTile)tiles.get((y*MAPTILESACROSS)+x);    //get tile corresponding to window loc
        if (temptile.isAttached()) {
          temptile.removeFromParent();
        }
      }
    }
     
    /* LOAD AND POSITION TILES WITHIN WINDOW */
    for (int y=win.TL.getTileY(); y<winmaxY; y++){   
      for (int x=win.TL.getTileX(); x<winmaxX; x++){           
        //error("adding tile (" + x + ", " + y + " to window at " + (x - win.TL.getTileX()-offsetx) + ", " + (x - win.TL.getTileY()-offsety));
        temptile=(MapTile)tiles.get((y*MAPTILESACROSS)+x);    //get tile corresponding to window loc
        
        tx=((x- win.TL.getTileX())*MapTile.getStaticWidth())-offsetx;
        ty=((y - win.TL.getTileY())*MapTile.getStaticHeight())-offsety;
        
        if (!temptile.isLoaded()) {
          temptile.load();
        }
        
        temptile.setPixelSize(MapTile.getStaticWidth(), MapTile.getStaticHeight());
        if (!temptile.isAttached()) {
          add(temptile,tx,ty);
        }
        this.setWidgetPosition(temptile, tx, ty);
      }
    }
     
    /* ADD AND POSITION OVERLAYS */
    Iterator i = overlays.keySet().iterator();    //get keyset of strings corresponding to overlays
    Overlay olay;
    while (i.hasNext()){
      olay=((Overlay)(overlays.get((String)i.next())));
      this.setWidgetPosition(olay,
          (int)(olay.getX()*zoomfactor)-win.TL.X(),(int)(olay.getY()*zoomfactor)- win.TL.Y());
      olay.setPixelSize((int)(olay.getWidth()*zoomfactor), (int)(olay.getHeight()*zoomfactor));     
    }
  }
  
  /**
   * Checks to see if a zoom can be performed
   *     @author Dale
   *     @param factor  Fraction to scale the map by
   *     @return      True if zoom could be completed, false otherwise
   *     @version 0.8
   */
  public boolean canZoom(double factor){
    //Window.alert("zoomfactor=" + zoomfactor);
    if (zoomfactor*factor<0.25 || zoomfactor*factor > 2) {
      return false;
    }
    return true;
  }

  /**
   * Zooms map objects by factor
   *     @author Dale
   *     @param factor  Fraction to scale the map by
   *     @version 0.8
   */
  public void Zoom(double factor){
    if (!canZoom(factor)) {
      return;  //abort if zoom factor not allowed
    }
    
    MapTile.WIDTH= (int)(MapTile.WIDTH*factor);
    MapTile.HEIGHT=(int)(MapTile.HEIGHT*factor);
    
    win.setCenter((int)(win.getCenter().X()*factor), (int)(win.getCenter().Y()*factor));
    zoomfactor=(zoomfactor*factor);
    this.draw();     
  }

  /* Helper Class, describing the viewing window centered over the map tiles */
  private class TileWindow{
    public Coordinate TL;    //top left corned   
    public Coordinate BR;    //bottom right corner
     
    // takes 2 coordinates designating the top left and bottom right corners of window
    public TileWindow(Coordinate topleft, Coordinate bottomright){
      TL=new Coordinate(topleft.X(),topleft.Y());
      BR=new Coordinate(bottomright.X(),bottomright.Y());
    }
     
    //returns width of window in tiles
    private int width(){
      return (BR.getTileX()-TL.getTileX());
    }       
    public void setCenter(int x, int y){
      TL.set (x-WINDOWWIDTH/2, y-WINDOWHEIGHT/2);
      BR.set(x+WINDOWWIDTH/2, y+WINDOWHEIGHT/2);
    }
    public Coordinate getCenter(){
      Coordinate result=new Coordinate(TL.X()+(WINDOWWIDTH/2), TL.Y()+ WINDOWHEIGHT/2);
      return result;
    }
     
    // returns height of window in tiles
    private int height(){
      return BR.getTileY()-TL.getTileY();
    }
    
    public void move(int offsetx, int offsety){
      //boolean fastdraw = true;    //flagged true if offset doesnt have to be modified
      int cx=getCenter().X();
      int cy=getCenter().Y();
         
          /* set top left corner */
          if ((cx+offsetx < 0)) {
            offsetx = 0;
          } else if (cx+offsetx > (MAPTILESACROSS*MapTile.getStaticWidth())) {
            offsetx=0;
          }
          
          if ((cy+offsety < 0)) {
            offsety = 0;
          } else if (cy+offsety > (MAPTILESDOWN*MapTile.getStaticHeight())) {
            offsety=0;
          }

          // now change BR corner to form the window
          TL.set(TL.X()+offsetx, TL.Y()+offsety);
          BR.set (TL.X()+WINDOWWIDTH,TL.Y()+WINDOWHEIGHT);

          draw();
      }
  }

  /* Helper Class, describing a coordinate relative to (0,0) in the absolute
   * image pixels. Provides translation routines to the TileWindow class and
   * tile-space (pixels-->tile conversion routines)
   */
  private class Coordinate {
    private int x,y;    //absolute x,y coordinates
    
    public Coordinate(int x, int y){
      set(x,y);
    }
    
    public int getTileX(){ return getTileX(MapTile.getStaticWidth());}
    public int getTileX(int tilewidth){
      int result=(x/tilewidth);
      if (result>MAPTILESACROSS){
        return MAPTILESACROSS;
      } else if(result<0){
        return 0;
      } else{
        return result;
      }
    }
     
    public int getTileY(){ return getTileY(MapTile.getStaticHeight());}
    public int getTileY(int tileheight){
      int result=(y/tileheight);
      if (result>MAPTILESDOWN){
        return MAPTILESDOWN;
      } else if(result<0){
        return 0;
      } else{
        return result;
      }
    }
    public void set(int x, int y){
      this.x=x;
      this.y=y;
    }
    public int X(){
      return x;
    }
    public int Y(){
      return y;
    }
  }
 
  /**
   * Moves the image on the MapPanel by the offset amount. If Coordinate
   * center is outside of the boundes of the request, the center will be
   * set to the maximum/minimum allowed size. This function call should
   * usually be followed by a call to draw(), but does not necessarily
   * need to be called every time (increase speed and "choppiness")
   *     @author Dale
   *     @version 0.1
   *     @param offsetx        X offset from the current center
   *     @param offsetx        Y offset from the current center
   *  @param forcedraw    True to force the map to completely redraw, not just shift visible tiles
   */
  public void move (int offsetx, int offsety){
    win.move(offsetx, offsety);
  }
  
  /**
   * Wrapper method that changes the current center of the map (calls 
   * TileWindow method). Should be followed by a call to draw().
   * @param x
   * @param y
   */
  public void setCenter(int x, int y){
    win.setCenter(x, y);
  }
  
  // a wrapper used for debugging
  private void error(String error){
    Window.alert(error);
  }
}