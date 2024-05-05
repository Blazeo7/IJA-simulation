/**
* @file ObservableProperty.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents properties that can be observed by `Observer`.
*/

package com.ija.Enums;

public enum ObservableProperty {
  POSITION("Position"),
  POSITION_X("Position X"),
  POSITION_Y("Position Y"),
  ANGLE("Angle"),
  PAINT("Paint"),
  DIRECTION("Direction"),
  ROTATION_VELOCITY("Rotation velocity"),
  RADIUS("Radius"),
  VELOCITY("Velocity"),
  COLLISION_DIST("Collision dist"),
  ROTATION_SIZE("Rotation size"),
  WIDTH("Width"),
  HEIGHT("Height"),
  ACTION("Action");

  private final String value;

  ObservableProperty(String value) {
      this.value = value;
  }

  public String getValue() {
      return value;
  }

  @Override
  public String toString() {
      return value;
  }
}