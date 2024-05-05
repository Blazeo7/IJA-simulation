/**
* @file Command.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents an action that can be undone.
*/

package com.ija.Interfaces;

public interface Command {
  void execute();
  void undo();
}
