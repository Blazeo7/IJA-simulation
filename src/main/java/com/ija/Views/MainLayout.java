/**
* @file MainLayout.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the layout of the application
*/


package com.ija.Views;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Commands.AddObstacleCommand;
import com.ija.Commands.AddRobotCommand;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.RemoveObstacleCommand;
import com.ija.Commands.RemoveRobotCommand;
import com.ija.Enums.Direction;
import com.ija.Models.AutoRobot;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;
import com.ija.Models.Point;

import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;

public class MainLayout extends Pane {

  private static final double ROBOT_CONTROLLER_MARGIN = 30;

  private CreationRobotOverlay creationRobotOverlay;
  private CreationObstacleOverlay creationObstacleOverlay;

  private GameField gameField;
  private GameFieldView gameFieldView;

  public MainLayout() {
    super();

    registerDialogListeners();

    setUpWindow();
    createLayout();
  }

  private void registerDialogListeners() {
    UIData.dialogProperty.addListener((observable, oldValue, newValue) -> {
      switch (oldValue) {
        case ROBOT_DIALOG:
          hideCreationRobotOverlay();
          break;
        case OBSTACLE_DIALOG:
          hideCreationObstacleOverlay();
          break;
        default:
          break;
      }

      switch (newValue) {
        case ROBOT_DIALOG:
          showRobotOverlay();
          break;
        case OBSTACLE_DIALOG:
          showObstacleOverlay();
          break;
        default:
          break;
      }
    });
  }

  private void setUpWindow() {
    this.setPrefSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
    Background background = new Background(new BackgroundFill(Color.web("#181818"), null, null));
    this.setBackground(background);
  }

  public void showRobotOverlay() {
    BaseRobot robot = UIData.addedRobot.get();
    if (gameField.getRobots().contains(robot) == false) {
      CommandInvoker.executeCommand(new AddRobotCommand(robot, gameField));
    }

    creationRobotOverlay = new CreationRobotOverlay(gameField);
    this.getChildren().add(creationRobotOverlay);
  }

  public void hideCreationRobotOverlay() {
    BaseRobot robotAdded = UIData.addedRobot.get();

    // Robot was created
    if (robotAdded == null || !gameField.getRobots().contains(robotAdded)) {
      UIData.addedRobot.set(new AutoRobot(5, 3, new Point(400, 500), Direction.Left));
    }

    // Robot was not created
    else {
      CommandInvoker.executeCommand(new RemoveRobotCommand(UIData.addedRobot.get(), gameField));

      // Ensure that the next time the dialog is opened, the robot type is always set
      // to AutoRobot
      UIData.addedRobot.set(BaseRobot.Mapper.mapToAutoRobot(UIData.addedRobot.get()));
    }

    this.getChildren().remove(creationRobotOverlay);
    creationRobotOverlay = null;
  }

  public void showObstacleOverlay() {
    Obstacle obstacle = UIData.addedObstacle.get();
    if (gameField.getObstacles().contains(obstacle) == false) {
      CommandInvoker.executeCommand(new AddObstacleCommand(obstacle, gameField));
    }
    creationObstacleOverlay = new CreationObstacleOverlay(obstacle, gameField);
    this.getChildren().add(creationObstacleOverlay);
  }

  public void hideCreationObstacleOverlay() {
    Obstacle obstacleAdded = UIData.addedObstacle.get();

    if (obstacleAdded == null || !gameField.getObstacles().contains(obstacleAdded)) {
      UIData.addedObstacle.set(new Obstacle(new Point(400, 300), 50, 30));
    } else {
      CommandInvoker.executeCommand(new RemoveObstacleCommand(UIData.addedObstacle.get(), gameField));
    }

    this.getChildren().remove(creationObstacleOverlay);
    creationObstacleOverlay = null;
  }

  private void createLayout() {
    this.gameField = new GameField(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT - Config.APP_BAR_HEIGHT);
    gameFieldView = new GameFieldView(gameField);

    gameFieldView.setLayoutY(Config.APP_BAR_HEIGHT);

    AppBar appBar = new AppBar(gameField);
    RobotControllerView robotController = new RobotControllerView();

    robotController.setLayoutX(Config.WINDOW_WIDTH - robotController.getPrefWidth() - ROBOT_CONTROLLER_MARGIN);
    robotController.setLayoutY(Config.WINDOW_HEIGHT - robotController.getPrefHeight() - ROBOT_CONTROLLER_MARGIN);

    this.getChildren().addAll(gameFieldView, appBar, robotController);
    gameFieldView.toBack();
  }

}
