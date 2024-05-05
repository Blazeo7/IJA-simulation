/**
* @file RobotAction.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents an action which the robot is performing.
*/

package com.ija.Enums;

public enum RobotAction {
  TRANSLATE,
  STARTING_TRANSLATE, // transition from rotating to translating
  ROTATE;
}
