/**
* @file GameField.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents field where robots and obstacles are situated.
*/

package com.ija.Models;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ija.Observable;
import com.ija.Utils;
import com.ija.Commands.AddAllObjectsCommand;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.RemoveAllObjectsCommand;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

public class GameField extends Observable implements Serializable {
  private double width;
  private double height;
  private ObservableSet<BaseRobot> robots = FXCollections.observableSet();
  private ObservableSet<Obstacle> obstacles = FXCollections.observableSet();
  private ControlledRobot controlledRobot = null;

  public GameField() {
  }

  public GameField(double width, double height) {
    this.width = width;
    this.height = height;
  }

  /**
   * Commands all robots to move and handles collision of each robot after
   * movement
   */
  public void moveRobots() {
    for (BaseRobot robot : getRobots()) {
      robot.move();

      handleCollisions(robot);
    }
  }

  /**
   * Commands all robots to move backward
   */
  public void moveRobotsBackward() {
    for (BaseRobot robot : getRobots()) {
      if (robot instanceof ControlledRobot)
        continue;

      ((AutoRobot) robot).moveBackward();
    }
  }

  /**
   * Hadles collision between robot and simulation border, other robot and
   * obstacle
   * 
   * @param robot Robot that potential collisions are being resolved.
   */
  public void handleCollisions(BaseRobot robot) {
    Point pos = robot.getPos();

    // Top border
    if (pos.getY() - robot.getCollisionRadius() < 0) {
      robot.setPosY(robot.getCollisionRadius());
      robot.collide();
    }

    // Bottom border
    if (pos.getY() + robot.getCollisionRadius() > getHeight()) {
      robot.setPosY(getHeight() - robot.getCollisionRadius());
      robot.collide();
    }

    // Left border
    if (pos.getX() - robot.getCollisionRadius() < 0) {
      robot.setPosX(robot.getCollisionRadius());
      robot.collide();
    }

    // Right border
    if (pos.getX() + robot.getCollisionRadius() > getWidth()) {
      robot.setPosX(getWidth() - robot.getCollisionRadius());
      robot.collide();
    }

    // colsition with other robots
    for (BaseRobot other : this.getRobots()) {
      if (other == robot)
        continue;

      if (handleCollision(robot, other)) {
        robot.collide();
      }
    }

    // collistion with obstacles
    for (Obstacle obstacle : this.getObstacles()) {
      if (handleCollision(robot, obstacle)) {
        robot.collide();
      }
    }
  }

  /**
   * Handles collision between a `BaseRobot` and an `Obstacle`.
   * If the robot collides with the obstacle, it moves the robot back to a safe
   * distance away from the obstacle.
   * 
   * @param robot    The BaseRobot involved in the collision.
   * @param obstacle The Obstacle involved in the collision.
   * @return True if a collision occurred, otherwise false.
   */
  protected boolean handleCollision(BaseRobot robot, Obstacle obstacle) {
    // Nearest point on the rectangle to the circle
    double Nx = Math.max(obstacle.getPos().getX(),
        Math.min(obstacle.getPos().getX() + obstacle.getWidth(), robot.getPos().getX()));
    double Ny = Math.max(obstacle.getPos().getY(),
        Math.min(obstacle.getPos().getY() + obstacle.getHeight(), robot.getPos().getY()));

    double xDist = Nx - robot.getPos().getX();
    double yDist = Ny - robot.getPos().getY();

    // Distance between center of the circle and the nearest rectangle point
    double distance = Utils.euclidean(xDist, yDist);

    if (distance < robot.getCollisionRadius()) {
      robot.collide();
      double overlap = robot.getRadius() + robot.collisionDistDetection - distance;

      robot.setPos(
          robot.getPos().add(
              -((xDist) / distance) * overlap,
              -((yDist) / distance) * overlap));

      return true; // collision
    }
    return false;
  }

  /**
   * Handles collision between two robots.
   * If the robots collide with each other, the `robot` is moved back.
   * 
   * @param robot The first BaseRobot involved in the collision.
   * @param other The second BaseRobot involved in the collision.
   * @return True if a collision occurred, otherwise false.
   */
  public boolean handleCollision(BaseRobot robot, BaseRobot other) {
    double sx = other.getPos().getX() - robot.getPos().getX();
    double sy = other.getPos().getY() - robot.getPos().getY();
    double dist = Utils.euclidean(sx, sy); // distance between the centers of the circles

    double circleCenterDist = robot.getCollisionRadius() + other.getRadius();
    double intercept = circleCenterDist - dist;

    if (intercept > 0) {
      robot.setPos(
          robot.getPos().add(
              -intercept * (sx / dist),
              -intercept * (sy / dist)));

      return true; // collision
    }
    return false;
  }

  /**
   * Adds a robot to the simulation.
   * 
   * @param robot The robot to be added.
   */
  public void addRobot(BaseRobot robot) {
    getRobots().add(robot);

    // Add controlled robot
    if (robot instanceof ControlledRobot) {
      setControlledRobot((ControlledRobot) robot);
    }
  }

  /**
   * Removes a robot to the simulation.
   * 
   * @param robot The robot to be removed.
   */
  public void removeRobot(BaseRobot robot) {
    if (robot == null)
      return;

    getRobots().remove(robot);

    // Remove controlled robot
    if (robot instanceof ControlledRobot) {
      setControlledRobot(null);
    }
  }

  /**
   * Adds an obstacle to the simulation.
   * 
   * @param obstacle The obstacle to be added.
   */
  public void addObstacle(Obstacle obstacle) {
    obstacles.add(obstacle);
  }

  /**
   * Removes an obstacle to the simulation.
   * 
   * @param obstacle The robot to be removed.
   */
  public void removeObstacle(Obstacle obstacle) {
    if (obstacle == null)
      return;
    getObstacles().remove(obstacle);
  }

  /**
   * Recursivelly serialize `GameField`
   * 
   * @return Json representation of the serialized `GameField`.
   * @throws JsonProcessingException
   */
  public String serialize() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

    // Enable pretty printing
    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

    // Create an ObjectWriter with custom pretty printing
    ObjectWriter objectWriter = objectMapper.writer().withDefaultPrettyPrinter();

    return objectWriter.writeValueAsString(this);
  }

  /**
   * Update all properties of the `GameField` object according to serialized
   * `GameField` in json file.
   * 
   * @param file Json file that contains valid `GameField` representation
   * @throws IOException
   */
  public void deserialize(File file) throws IOException {
    CommandInvoker.executeCommand(new RemoveAllObjectsCommand(this));

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.readerForUpdating(this).readValue(file);

    CommandInvoker.executeCommand(new AddAllObjectsCommand(this));
    notifyObservers();
  }

  public double getWidth() {
    return width;
  }

  public double getHeight() {
    return height;
  }

  public ObservableSet<BaseRobot> getRobots() {
    return robots;
  }

  public ObservableSet<Obstacle> getObstacles() {
    return obstacles;
  }

  public void setWidth(double width) {
    this.width = width;
  }

  public void setHeight(double height) {
    this.height = height;
  }

  public void setRobots(List<BaseRobot> robots) {
    this.robots.clear();
    this.robots.addAll(robots);
  }

  public void setObstacles(List<Obstacle> obstacles) {
    this.obstacles.clear();
    this.obstacles.addAll(obstacles);
  }

  public ControlledRobot getControlledRobot() {
    return controlledRobot;
  }

  public void setControlledRobot(ControlledRobot controlledRobot) {
    this.controlledRobot = controlledRobot;
  }
}
