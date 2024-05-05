/**
* @file Obstacle.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents rectangular obstacle.
*/

package com.ija.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ija.Observable;
import com.ija.Enums.ObservableProperty;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class Obstacle extends Observable {
  private Point pos;
  private double angle = 0;
  private double width;
  private double height;

  public Obstacle() {
  }

  @JsonIgnore
  private Paint paint = Color.RED;

  public Obstacle(Point pos, double width, double height, double angle, Paint paint) {
    this.pos = pos;
    this.angle = angle;
    this.width = width;
    this.height = height;
    this.paint = paint;
  }

  public Obstacle(Point pos, double width, double height) {
    this.pos = pos;
    this.width = width;
    this.height = height;
  }

  @Override
  public String toString() {
    String str = super.toString();
    return str.substring(str.lastIndexOf(".") + 1);
  }

  public Point getPos() {
    return pos;
  }

  public double getAngle() {
    return angle;
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public Paint getPaint() {
    return paint;
  }

  public void setPos(Point pos) {
    this.pos = pos;
    notifyObservers(ObservableProperty.POSITION);
    notifyObservers(ObservableProperty.POSITION_X);
    notifyObservers(ObservableProperty.POSITION_Y);
  }

  public void setPosX(double x) {
    pos.setX(x);
    notifyObservers(ObservableProperty.POSITION_X);
  }

  public void setPosY(double y) {
    pos.setY(y);
    notifyObservers(ObservableProperty.POSITION_Y);
  }

  public void setAngle(double angle) {
    this.angle = angle;
    notifyObservers(ObservableProperty.ANGLE);
  }

  public void setWidth(double width) {
    this.width = width;
    notifyObservers(ObservableProperty.WIDTH);
  }

  public void setHeight(double height) {
    this.height = height;
    notifyObservers(ObservableProperty.HEIGHT);
  }

  public void setPaint(Paint paint) {
    this.paint = paint;
  }
}
