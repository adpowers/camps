package edu.washington.cs.cse403.camps.client;

public class JSInitializer {

  MapPanel map;

  /**
   * Basic constructor.
   * @param map	Your MapPanel which will accept new overlays.
   */
  public JSInitializer(MapPanel map) {
    this.map = map;
  }

  /**
   * Sets up the wrappers that allow JavaScript to make calls to your MapPanel.
   * This must be called when constructing the page, before the user searches.
   */
  public native void setup() /*-{
		var map = this.@edu.washington.cs.cse403.camps.client.JSInitializer::map;
		$doc.addOverlayWrapper = function(key, url, x, y, w, h) {
			map.@edu.washington.cs.cse403.camps.client.MapPanel::addOverlay(Ljava/lang/String;Ljava/lang/String;IIII)(key,url,x,y,w,h);
    }
    $doc.removeOverlayWrapper = function(key) {
      map.@edu.washington.cs.cse403.camps.client.MapPanel::removeOverlay(Ljava/lang/String;)(key);
    }
  }-*/;
}