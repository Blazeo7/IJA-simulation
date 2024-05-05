/**
* @file BaseRobot.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents common features of both Autonomous and Controlled robots..
*/

package com.ija.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.ija.Observable;
import com.ija.Utils;
import com.ija.Enums.Direction;
import com.ija.Enums.ObservableProperty;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY)
@JsonSubTypes({
    @JsonSubTypes.Type(value = AutoRobot.class, name = "AutoRobot"),
    @JsonSubTypes.Type(value = ControlledRobot.class, name = "ControlledRobot") })
public abstract class BaseRobot extends Observable {
  protected double velocity;
  protected double rotationVelocity;
  protected double angle = 0;
  protected double rotationSize = 90;
  protected Point pos;
  protected double radius = 30;
  protected Paint paint = Color.BLACK;
  protected Direction direction = Direction.Right;
  protected double collisionDistDetection = 3;

  public BaseRobot() {
  }

  public BaseRobot(double velocity, double rotationVelocity, double angle, Point pos, double radius,
      Paint paint, Direction direction, double collisionDistDetection) {
    this.velocity = velocity;
    this.rotationVelocity = rotationVelocity;
    this.angle = angle;
    this.pos = pos;
    this.radius = radius;
    this.paint = paint;
    this.direction = direction;
    this.collisionDistDetection = collisionDistDetection;
  }

  public BaseRobot(double velocity, double rotationVelocity, Point pos) {
    this.velocity = velocity;
    this.rotationVelocity = rotationVelocity;
    this.pos = pos;
  }

  /**
   * Action that is taken when collision occured
   */
  public abstract void collide();

  
  /**
   * Robot type specific move
   */
  public abstract void move();

  /**
   * Translates the object's position based on its velocity and angle.
   */
  protected void translate() {
    double deltaX = velocity * Math.cos(Utils.degrees2Radians(angle));
    double deltaY = velocity * Math.sin(Utils.degrees2Radians(angle));
    setPos(getPos().add(deltaX, deltaY));
  }

  /**
 * Rotates the object based on the given direction.
 * The rotation is calculated using the current rotation velocity
 * and the specified direction (Left or Right),
 * and the object's angle is updated accordingly.
 * 
 * @param direction The direction of rotation (Left or Right).
 */
  protected void rotate(Direction direction) {
    double newAngle = getAngle() + direction.getValue() * rotationVelocity;
    setAngle(newAngle);
  }

  /**
   * Maps one robot type to another
   */
  public static class Mapper {
    public static AutoRobot mapToAutoRobot(BaseRobot robot) {
      if (robot instanceof AutoRobot) {
        return (AutoRobot) robot;
      }

      // Map either data that are not fully relevant for controlled robot to ensure
      // backward compatibility with auto robot
      AutoRobot autoRobot = new AutoRobot();
      autoRobot.setAngle(robot.getAngle());
      autoRobot.setCollisionDistDetection(robot.getCollisionDistDetection());
      autoRobot.setDirection(robot.getDirection());
      autoRobot.setPaint(robot.getPaint());
      autoRobot.setPos(robot.getPos());
      autoRobot.setRadius(robot.getRadius());
      autoRobot.setRotationSize(robot.getRotationSize());
      autoRobot.setRotationVelocity(robot.getRotationVelocity());
      autoRobot.setVelocity(robot.getVelocity());

      return autoRobot;
    }

    public static ControlledRobot mapToControlledRobot(BaseRobot robot) {
      if (robot instanceof ControlledRobot) {
        return (ControlledRobot) robot;
      }

      // Map either data that are not fully relevant for controlled robot to ensure
      // backward compatibility with auto robot
      ControlledRobot controlledRobot = new ControlledRobot();
      controlledRobot.setAngle(robot.getAngle());
      controlledRobot.setCollisionDistDetection(robot.getCollisionDistDetection());
      controlledRobot.setDirection(robot.getDirection());
      controlledRobot.setPaint(robot.getPaint());
      controlledRobot.setPos(robot.getPos());
      controlledRobot.setRadius(robot.getRadius());
      controlledRobot.setRotationSize(robot.getRotationSize());
      controlledRobot.setRotationVelocity(robot.getRotationVelocity());
      controlledRobot.setVelocity(robot.getVelocity());
      controlledRobot.setVelocity(robot.getVelocity());

      return controlledRobot;
    }
  }

  @Override
  public String toString() {
    String str = super.toString();
    return str.substring(str.lastIndexOf(".") + 1);
  }

  public double getVelocity() {
    return velocity;
  }

  public double getAngle() {
    return angle;
  }

  public Point getPos() {
    return pos;
  }

  public double getRadius() {
    return radius;
  }

  public double getRotationSize() {
    return rotationSize;
  }

  @JsonIgnore
  public Paint getPaint() {
    return paint;
  }

  public double getRotationVelocity() {
    return rotationVelocity;
  }

  public Direction getDirection() {
    return direction;
  }

  @JsonIgnore
  public double getCollisionRadius() {
    return getRadius() + getCollisionDistDetection();
  }

  public double getCollisionDistDetection() {
    return collisionDistDetection;
  }

  public void setVelocity(double velocity) {
    this.velocity = velocity;
    notifyObservers(ObservableProperty.VELOCITY);
  }

  public void setRotationVelocity(double rotationVelocity) {
    this.rotationVelocity = rotationVelocity;
    notifyObservers(ObservableProperty.ROTATION_VELOCITY);
  }

  public void setAngle(double angle) {
    this.angle = (angle % 360 + 360) % 360;
    notifyObservers(ObservableProperty.ANGLE);
  }

  public void setPos(Point pos) {
    this.pos = pos;
    notifyObservers(ObservableProperty.POSITION, ObservableProperty.POSITION_X, ObservableProperty.POSITION_Y);
  }

  public void setPosX(double x) {
    this.getPos().setX(x);
    notifyObservers(ObservableProperty.POSITION_X, ObservableProperty.POSITION);
  }

  public void setPosY(double y) {
    this.getPos().setY(y);
    notifyObservers(ObservableProperty.POSITION_Y, ObservableProperty.POSITION);
  }

  public void setRadius(double radius) {
    this.radius = radius;
    notifyObservers(ObservableProperty.RADIUS);
  }

  public void setPaint(Paint paint) {
    this.paint = paint;
    notifyObservers(ObservableProperty.PAINT);
  }

  public void setDirection(Direction direction) {
    this.direction = direction;
    notifyObservers(ObservableProperty.DIRECTION);
  }

  public void setCollisionDistDetection(double collisionDistDetection) {
    this.collisionDistDetection = collisionDistDetection;
    notifyObservers(ObservableProperty.COLLISION_DIST);
  }

  public void setRotationSize(double rotationSize) {
    this.rotationSize = rotationSize;
    notifyObservers(ObservableProperty.ROTATION_SIZE);
  }
}
