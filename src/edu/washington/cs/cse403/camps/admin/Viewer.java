package edu.washington.cs.cse403.camps.admin;

import java.awt.*;
import javax.swing.*;
/**Viewers register themselves with MapModel and are notified of changes to the model.*/
public interface Viewer{

  /**Notifies the viewer that something has changed in the model.*/
  public void notifyViewer();   
}