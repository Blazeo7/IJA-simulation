/**
* @file ObstacleView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the view of the `Obstacle` model.
*/


package com.ija.Views;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.RemoveObstacleCommand;
import com.ija.Commands.UpdateParameterCommand;
import com.ija.Enums.SimulationState;
import com.ija.Interfaces.Observer;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ObstacleView extends Group implements Observer {
  private static final Color EDITED_OBSTACLE_COLOR = Color.web("#E24A2D");

  private Obstacle obstacle;

  private Rectangle rectangle;
  private ActionsWrapper actionsWrapper;

  public ObstacleView(Obstacle obstacle, GameField gameField) {
    super();
    this.obstacle = obstacle;
    this.rectangle = new Rectangle(0, 0, obstacle.getWidth(), obstacle.getHeight());
    setUpStyle(obstacle);
    this.actionsWrapper = new ActionsWrapper(obstacle, new RemoveObstacleCommand(obstacle, gameField));

    obstacle.addObserver(this);
    update();

    setActions(UIData.simulationState.get());
    setPaintIfEdited(UIData.addedObstacle.get());

    setUpListeners();

    this.getChildren().add(rectangle);
  }

  private void setUpStyle(Obstacle obstacle) {
    this.rectangle.setFill(obstacle.getPaint());
    this.rectangle.setStroke(Config.OBSTACLE_STROKE_COLOR);
    this.rectangle.setStrokeWidth(Config.OBSTACLE_STROKE_WIDTH);
    this.rectangle.setStrokeType(Config.OBSTACLE_STROKE_TYPE);
  }

  private void setUpListeners() {
    UIData.simulationState.addListener((observable, oldValue, newValue) -> {
      setActions(newValue);
    });
    UIData.addedObstacle.addListener((observable, oldValue, newValue) -> {
      setPaintIfEdited(newValue);
    });
  }

  private void setPaintIfEdited(Obstacle editedObstacle) {
    if (editedObstacle == obstacle) {
      this.rectangle.setFill(EDITED_OBSTACLE_COLOR);
    } else {
      this.rectangle.setFill(obstacle.getPaint());
    }
  }

  private void setActions(SimulationState gameState) {
    if (gameState == SimulationState.PAUSE) {
      this.rectangle.setCursor(Cursor.HAND);

      this.setOnMouseEntered(e -> this.getChildren().add(this.actionsWrapper));
      this.setOnMouseExited(e -> this.getChildren().remove(this.actionsWrapper));

      setOnMouseDragged(event -> {
        double mouseX = event.getX(); // Mouse X position relative to the model
        double mouseY = event.getY(); // Mouse Y position relative to the model

        // Convert mouse coordinates to parent gameField coordinates
        Point2D pointInParent = this.localToParent(mouseX, mouseY);

        // Set the position relative to the parent pane
        // Set the position relative to the parent pane
        UpdateParameterCommand<Double> updateParameterCommandX = new UpdateParameterCommand<>(
            (param) -> obstacle.setPosX(param),
            obstacle.getPos().getX(),
            pointInParent.getX() - obstacle.getWidth() / 2);

        CommandInvoker.executeCommand(updateParameterCommandX);

        UpdateParameterCommand<Double> updateParameterCommandY = new UpdateParameterCommand<>(
            (param) -> obstacle.setPosY(param),
            obstacle.getPos().getY(),
            pointInParent.getY() - obstacle.getHeight() / 2);

        CommandInvoker.executeCommand(updateParameterCommandY);

        // obstacle.setPos(
        // new Point(pointInParent.getX() - obstacle.getWidth() / 2,
        // pointInParent.getY() - obstacle.getHeight() / 2));
      });
    } else {
      this.rectangle.setCursor(Cursor.DEFAULT);

      // If the game state is not paused, remove the event handlers
      setOnMouseEntered(null);
      setOnMouseExited(null);
      setOnMouseDragged(null);
    }
  }

  @Override
  public void update() {
    this.setTranslateX(obstacle.getPos().getX());
    this.setTranslateY(obstacle.getPos().getY());
    this.rectangle.setRotate(obstacle.getAngle());
    this.rectangle.setWidth(obstacle.getWidth());
    this.rectangle.setHeight(obstacle.getHeight());
  }
}
