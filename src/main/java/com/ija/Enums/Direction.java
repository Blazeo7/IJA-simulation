/**
* @file Direction.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents robot movement direction.
*/

package com.ija.Enums;

public enum Direction {
  Right(1),
  Left(-1),
  Forward(0);

  private int value;

  private Direction(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
