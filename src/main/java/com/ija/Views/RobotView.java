/**
* @file RobotView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the view of the `BaseRobot` model.
*/

package com.ija.Views;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Commands.AddRobotCommand;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.RemoveRobotCommand;
import com.ija.Enums.SimulationState;
import com.ija.Interfaces.Observer;
import com.ija.Models.AutoRobot;
import com.ija.Models.BaseRobot;
import com.ija.Models.ControlledRobot;
import com.ija.Models.GameField;
import com.ija.Models.Point;

import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

public class RobotView extends Group implements Observer {
  private static final Color EDITED_ROBOT_COLOR = Color.web("#020112");

  private BaseRobot robot;
  private GameField gameField;

  private Circle circle;
  private Rectangle rectangle;
  private Group robotView;
  private ActionsWrapper robotActionsWrapper;

  private boolean isDragged = false;

  public RobotView(BaseRobot robot, GameField gameField) {
    super();
    this.circle = new Circle(0, 0, robot.getRadius());
    this.rectangle = new Rectangle(10, 5);
    this.robot = robot;
    this.gameField = gameField;
    this.robotView = new Group();
    this.robotActionsWrapper = new ActionsWrapper(robot, new RemoveRobotCommand(robot, gameField));

    update();

    robot.addObserver(this);

    addStrokeIfControlled();
    setPaintIfEdited(UIData.addedRobot.get());
    setActions(UIData.simulationState.get());
    setUpListeners();

    // Style rectangle for displaying robot movement direction
    rectangle.setLayoutY(-2.5);
    rectangle.setLayoutX(robot.getRadius() - 10);
    rectangle.setFill(Color.WHITE);

    this.robotView.getChildren().addAll(circle, rectangle);
    this.getChildren().add(robotView);
  }

  private void addStrokeIfControlled() {
    if (robot instanceof ControlledRobot) {
      circle.setStroke(Config.CONTROLLED_ROBOT_STROKE_COLOR);
      circle.setStrokeWidth(Config.CONTROLLED_ROBOT_STROKE_WIDTH);
      circle.setStrokeType(Config.CONTROLLED_ROBOT_STROKE_TYPE);
    }
  }

  private void setUpListeners() {
    UIData.simulationState.addListener((observable, oldValue, newValue) -> {
      setActions(newValue);
    });

    UIData.addedRobot.addListener((observable, oldValue, newValue) -> {
      setPaintIfEdited(newValue);
    });
  }

  private void setPaintIfEdited(BaseRobot editedRobot) {
    if (editedRobot == robot) {
      circle.setFill(EDITED_ROBOT_COLOR);
    } else {
      circle.setFill(robot.getPaint());
    }
  }

  /**
   * Set robots action during Simulation.PAUSE and removes them in every other
   * state.
   * 
   * @param gameState
   */
  private void setActions(SimulationState gameState) {
    if (gameState == SimulationState.PAUSE) {
      this.robotView.setCursor(Cursor.HAND);

      this.setOnMouseEntered(e -> this.getChildren().add(this.robotActionsWrapper));
      this.setOnMouseExited(e -> this.getChildren().remove(this.robotActionsWrapper));
      this.setOnDragDetected(e -> isDragged = true);

      this.setOnMouseClicked(e -> {
        if (isDragged == false) {
          swtichRobotType();
        }
        isDragged = false;
      });

      this.setOnMouseDragged(event -> {
        double mouseX = event.getX(); // Mouse X position relative to the model
        double mouseY = event.getY(); // Mouse Y position relative to the model

        // Convert mouse coordinates to parent gameField coordinates
        Point2D pointInParent = this.localToParent(mouseX, mouseY);

        // Set the position relative to the parent pane
        robot.setPos(new Point(pointInParent.getX() - circle.getCenterX(), pointInParent.getY() - circle.getCenterY()));
      });
    } else {
      this.robotView.setCursor(Cursor.DEFAULT);

      // Ensure the action wrapper is removed when the simulation is launched
      this.getChildren().remove(this.robotActionsWrapper);

      // If the game state is not paused, remove the event handlers
      this.setOnMouseEntered(null);
      this.setOnMouseExited(null);
      this.setOnMouseDragged(null);
      this.setOnMouseClicked(null);
      this.setOnDragDetected(null);
    }
  }

  /**
   * Changes robot type from AutoRobot to Controlled robot and vice versa
   */
  private void swtichRobotType() {
    CommandInvoker.executeCommand(new RemoveRobotCommand(robot, gameField));

    if (robot instanceof ControlledRobot) {
      // Replace ControlledRobot with AutoRobot
      AutoRobot autoRobot = BaseRobot.Mapper.mapToAutoRobot(robot);
      CommandInvoker.executeCommand(new AddRobotCommand(autoRobot, gameField));

      if (robot == UIData.addedRobot.get()) {
        UIData.addedRobot.set(autoRobot);
      }
    } else if (robot instanceof AutoRobot) {
      ControlledRobot oldControlledRobot = gameField.getControlledRobot();

      // Remove old ControlledRobot
      if (oldControlledRobot != null) {
        CommandInvoker.executeCommand(new RemoveRobotCommand(oldControlledRobot, gameField));

        AutoRobot newAutoRobot = BaseRobot.Mapper.mapToAutoRobot(oldControlledRobot);
        CommandInvoker.executeCommand(new AddRobotCommand(newAutoRobot, gameField));

        if (oldControlledRobot == UIData.addedRobot.get()) {
          UIData.addedRobot.set(newAutoRobot);
        }
      }

      // Change current AutoRobot to ControlledRobot
      ControlledRobot controlledRobot = BaseRobot.Mapper.mapToControlledRobot(robot);
      CommandInvoker.executeCommand(new AddRobotCommand(controlledRobot, gameField));

      if (robot == UIData.addedRobot.get()) {
        UIData.addedRobot.set(controlledRobot);
      }
    }
  }

  @Override
  public void update() {
    this.setTranslateX(robot.getPos().getX());
    this.setTranslateY(robot.getPos().getY());
    this.robotView.setRotate(robot.getAngle());
    circle.setRadius(robot.getRadius());
    rectangle.setLayoutX(robot.getRadius() - 10);
  }
}