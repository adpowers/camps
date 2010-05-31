package edu.washington.cs.cse403.camps.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.EventPreview;
import com.google.gwt.user.client.WindowResizeListener;

import com.google.gwt.user.client.Window;

import com.google.gwt.user.client.DOM; 
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.LoadListener;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * CAMPS is the entry point for the CAMPS service program.
 * Remaining DESCRIPTION goes here...... 
 * @author Dale Olson
 */
/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Camps implements EntryPoint {  // EntryPoint is GWT specified keyword for entrypoint to programs
  public static final int CONTROLPANELWIDTH = 300;


  //panels to contain content
  final MapPanel map = new MapPanel();
  final FocusPanel mapwrap = new FocusPanel();
  final AbsolutePanel control = new AbsolutePanel();

  //andrews panel
  final JSInitializer jsinit = new JSInitializer(map);

  //control buttons
  final Image zoomin = new Image("images/zoom-in.png");
  final Image zoomout = new Image("images/zoom-out.png");

  //pointer to main container panel
  private AbsolutePanel rootptr = new AbsolutePanel();

  boolean over=false;      //mouse pointer over map?

  /**
   * This is the entry point method, defined by GWT (Google Web Toolkit).
   */
  public void onModuleLoad() {

    jsinit.setup();

    rootptr=RootPanel.get();

    /* SET UP CONTROL PANEL */
    control.setStyleName("control");

    /* Zoom Button Event Handlers */
    zoomout.addClickListener(new ClickListener() {public void onClick(Widget sender) {
      map.Zoom(0.5);
      zoomin.setUrl("images/zoom-in.png");
      zoomin.setTitle("Zoom In...");
      if(!map.canZoom(0.5)) {
        zoomout.setUrl("images/zoom-out2.png");
        zoomout.setTitle("No further zooming out is allowed.");
      }
    }});
    zoomin.addClickListener(new ClickListener() {public void onClick(Widget sender) {
      map.Zoom(2.0);
      zoomout.setUrl("images/zoom-out.png");
      zoomout.setTitle("Zoom Out...");
      if(!map.canZoom(2.0)) {
        zoomin.setUrl("images/zoom-in2.png");
        zoomin.setTitle("No further zooming in is allowed.");
      }
    }});

    mapwrap.setPixelSize(rootptr.getOffsetWidth()-CONTROLPANELWIDTH, rootptr.getOffsetHeight());
    map.setPixelSize(rootptr.getOffsetWidth()-CONTROLPANELWIDTH, rootptr.getOffsetHeight());


    Window.enableScrolling(false);    //disable window scrollbars

    /* Ensure proper positioning of zoom controls */
    LoadListener zoomPlacer = new LoadListener(){
      public void onLoad(Widget sender){
        int w = Window.getClientWidth();
        int h = Window.getClientHeight();
        rootptr.setWidgetPosition(zoomin, w - zoomin.getOffsetWidth(),
            h - zoomin.getOffsetHeight());
        rootptr.setWidgetPosition(zoomout, w
            - (zoomin.getOffsetWidth() * 2), h
            - zoomout.getOffsetHeight());
      }
      public void onError(Widget sender){}
    };
    zoomin.addLoadListener(zoomPlacer);
    zoomout.addLoadListener(zoomPlacer);

    /* Add all widgets to the rootpanel, size and draw */
    //map.setCenter(1000, 1000);    //SET DESIRED CENTER
    //map.Zoom(0.5);  // make zoom level a bit nicer
    map.draw();   // in case there are undrawn tiles
    mapwrap.add(map);
    rootptr.add(mapwrap, CONTROLPANELWIDTH,0);
    rootptr.add(zoomin);
    rootptr.add(zoomout);
    zoomin.setTitle("Zoom In...");
    zoomout.setTitle("Zoom Out...");

    /* Defer a command for later execution to size panels and widgets */
    final Command placeWidgets = new Command() {
      public void execute() {
        int w = Window.getClientWidth();
        int h = Window.getClientHeight();
        MapPanel.WINDOWWIDTH = w - CONTROLPANELWIDTH;
        MapPanel.WINDOWHEIGHT = h;
        mapwrap.setPixelSize(MapPanel.WINDOWWIDTH,
            MapPanel.WINDOWHEIGHT);
        map.setPixelSize(MapPanel.WINDOWWIDTH, MapPanel.WINDOWHEIGHT);
        rootptr.setWidgetPosition(mapwrap, CONTROLPANELWIDTH, 0);

        // rootptr.setWidgetPosition(control,Window.getClientWidth()-CONTROLSIZE,MapPanel.WINDOWHEIGHT-CONTROLSIZE);

        rootptr.setWidgetPosition(zoomin, w - zoomin.getOffsetWidth(),
            h - zoomin.getOffsetHeight());
        rootptr.setWidgetPosition(zoomout, w
            - (zoomin.getOffsetWidth() * 2), h
            - zoomout.getOffsetHeight());

        map.draw();
      }
    };
    DeferredCommand.add(placeWidgets);

    /* BUG FIX #24 STOP DEFAULT BEHAVIOR OF IMG SELECTION IN FF */
    DOM.addEventPreview(new EventPreview() {
      public boolean onEventPreview(Event event) {
        switch (DOM.eventGetType(event)) {
        case Event.ONMOUSEDOWN:
        case Event.ONMOUSEMOVE:
        case Event.ONMOUSEUP:
          // Tell the browser not to act on the event.
          if(over)
            DOM.eventPreventDefault(event); 
        }
        // But DO allow the event to fire.
        return true;
      } 
    });
    /* END BUG #24 FIX */

    /* create event handlers to navigate map (focuspanel does this) */
    zoomin.addMouseListener(new MouseListener() {

      public void onMouseEnter(Widget sender) {
        if (map.canZoom(2.0)) ((Image)sender).setUrl("images/zoom-in1.png");
      }
      public void onMouseMove(Widget sender, int x, int y) {  }
      public void onMouseUp(Widget sender, int x, int y) {  }
      public void onMouseLeave(Widget sender) {
        if (map.canZoom(2.0)) ((Image)sender).setUrl("images/zoom-in.png");
      }
      public void onMouseDown (Widget sender, int x, int y) {  }
    });
    zoomout.addMouseListener(new MouseListener() {

      public void onMouseEnter(Widget sender) {
        if (map.canZoom(0.5))((Image)sender).setUrl("images/zoom-out1.png");
      }
      public void onMouseMove(Widget sender, int x, int y) {  }
      public void onMouseUp(Widget sender, int x, int y) {  }
      public void onMouseLeave(Widget sender) {
        if (map.canZoom(0.5)) ((Image)sender).setUrl("images/zoom-out.png");
      }
      public void onMouseDown (Widget sender, int x, int y) {  }
    });


    mapwrap.addMouseListener(new MouseListener() {
      private int clickx, clicky;
      public void onMouseEnter(Widget sender) {
        if (!DOM.compare(DOM.getCaptureElement(),mapwrap.getElement())){
          map.clicked=false;
          DOM.setCapture(mapwrap.getElement());
        }
        over=true;
      }
      public void onMouseMove(Widget sender, int x, int y) {
        if (map.clicked) {
          map.move(clickx-x, clicky-y);
          clickx=x;
          clicky=y;
        }
      }
      public void onMouseUp(Widget sender, int x, int y) {
        if (map.clicked) {
          if (x<0 || y<0 || x>MapPanel.WINDOWWIDTH || y>MapPanel.WINDOWHEIGHT){
            DOM.releaseCapture(sender.getElement()); 
          }
          map.move(clickx-x, clicky-y);
          map.clicked = false;
          map.draw();
        }
      }
      public void onMouseLeave(Widget sender) {
        if (!map.clicked){
          DOM.releaseCapture(sender.getElement()); 
        }
        over=false;
      }
      public void onMouseDown (Widget sender, int x, int y) {
        if (map.clicked){
          map.move(clickx-x, clicky-y);
          clickx=x;
          clicky=y;
        } else {
          DOM.setCapture(mapwrap.getElement());
          clickx=x;
          clicky=y;
          map.clicked=true;
        }

      }
    });

    /* Add window listener to adjust window if someone screws with it */
    WindowResizeListener wrsl = new WindowResizeListener(){
      public void onWindowResized(int w, int h) {
        placeWidgets.execute();
      }
    };
    Window.addWindowResizeListener(wrsl);
  }

  /* Whew! Done Loading. */

}
