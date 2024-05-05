/**
* @file Point.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents 2D point.
*/

package com.ija.Models;

import java.util.Locale;

public class Point {
  private double x;
  private double y;

  public Point(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Point(){}

  @Override
  public String toString() {
    return String.format(Locale.US,"[%.1f, %.1f]", getX(), getY());
  }

  public static Point fromString(String str) {
    str = str.replaceAll("\\[|\\]", ""); // Remove square brackets
    String[] parts = str.split(", ");
    double x = Double.parseDouble(parts[0]);
    double y = Double.parseDouble(parts[1]);
    return new Point(x, y);
  }

  public Point add(double x, double y) {
    this.setX(getX() + x);
    this.setY(getY() + y);
    return this;
  }


  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }
}
