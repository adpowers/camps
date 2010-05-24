package edu.washington.cs.cse403.camps.admin;

import javax.swing.*;

/**ErrorHandler shows dialog boxes of errors.
 * @author Sean and Jarret
 */
public class ErrorHandler {
  
  /**A generic error handling method that displays a notification to the user.
   * @param type a String explaining the error to the user.
   */
  public static void foundError(String type) {
     JOptionPane.showMessageDialog(null, type, "Error!", JOptionPane.ERROR_MESSAGE);
  }
}