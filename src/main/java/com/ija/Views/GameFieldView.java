/**
* @file GameFieldView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents view of the `GameField` model
*/

package com.ija.Views;

import java.util.HashMap;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Utils;
import com.ija.Commands.CommandInvoker;
import com.ija.Enums.SimulationState;
import com.ija.Interfaces.Observer;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;

import javafx.animation.AnimationTimer;
import javafx.collections.SetChangeListener;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class GameFieldView extends Pane implements Observer {
  private HashMap<Obstacle, ObstacleView> obstacles = new HashMap<>();
  private HashMap<BaseRobot, RobotView> robots = new HashMap<>();
  private GameField gamefield;

  private AnimationTimer timer;
  private Label time;

  public GameFieldView(GameField gameField) {
    super();
    this.gamefield = gameField;
    this.time = new Label();

    setUpTimeLabel();

    this.setPrefWidth(gamefield.getWidth());
    this.setPrefHeight(gamefield.getHeight());
    this.getChildren().add(time);

    gameField.addObserver(this);

    setUpRobotListener();
    setUpObstacleListener();
    setUpGameStateListener();

    Background background = new Background(new BackgroundFill(Config.GAME_FIELD_BCKG_COLOR, null, null));
    setBackground(background);
  }

  private void setUpTimeLabel() {
    this.time.setLayoutX(Config.WINDOW_WIDTH - 50);
    this.time.setLayoutY(20);
    this.time.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 20));
    this.time.setStyle("-fx-text-fill: red;");
  }

  public void update() {
    this.setPrefWidth(gamefield.getWidth());
    this.setPrefHeight(gamefield.getHeight());

    this.getChildren().removeAll(getObstacles().values());
    this.getChildren().removeAll(getRobots().values());

    this.getRobots().clear();
    this.getObstacles().clear();

    for (BaseRobot robot : gamefield.getRobots()) {
      this.addRobot(robot);
    }
    for (Obstacle obstacle : gamefield.getObstacles()) {
      this.addObstacle(obstacle);
    }

  }

  private void setUpRobotListener() {
    this.gamefield.getRobots().addListener((SetChangeListener<BaseRobot>) change -> {
      if (change.wasAdded()) {
        addRobot(change.getElementAdded());
      }
      if (change.wasRemoved()) {
        removeRobot(change.getElementRemoved());
      }
    });
  }

  private void setUpObstacleListener() {
    this.gamefield.getObstacles().addListener((SetChangeListener<Obstacle>) change -> {
      if (change.wasAdded()) {
        addObstacle(change.getElementAdded());
      }
      if (change.wasRemoved()) {
        removeObstacle(change.getElementRemoved());
      }
    });
  }

  private void setUpGameStateListener() {
    UIData.simulationState.addListener((observable, oldValue, newValue) -> {
      if (newValue == SimulationState.PAUSE) {
        stopTimer();
      } else if (newValue == SimulationState.PLAY) {
        stopTimer();
        timer = new AnimationTimer() {
          @Override
          public void handle(long arg0) {
            performGameAction(arg0, () -> gamefield.moveRobots());
          }
        };
        timer.start();
      } else {
        stopTimer();
        timer = new AnimationTimer() {
          @Override
          public void handle(long arg0) {
            performGameAction(arg0, () -> {
              CommandInvoker.executeHistoryCommand();
              gamefield.moveRobotsBackward();
            });
          }
        };
        timer.start();
      }
    });
  }

  private void performGameAction(long arg0, Runnable action) {
    updateClock();
    action.run();
    Utils.Counter.tick();
  }

  private void stopTimer() {
    if (this.timer != null) {
      timer.stop();
    }
  }

  private void updateClock() {
    long frame = Utils.Counter.get();
    time.setText(Utils.double2String(frame * Config.SECONDS_PER_FRAME, 2));
  }

  public void addObstacle(Obstacle obstacle) {
    ObstacleView obstacleView = new ObstacleView(obstacle, gamefield);
    getObstacles().put(obstacle, obstacleView);
    this.getChildren().add(obstacleView);
  }

  public void removeObstacle(Obstacle obstacle) {
    ObstacleView obstacleView = getObstacles().remove(obstacle);
    assert obstacleView == null;
    this.getChildren().remove(obstacleView);
  }

  public void addRobot(BaseRobot robot) {
    RobotView robotView = new RobotView(robot, gamefield);
    getRobots().put(robot, robotView);
    this.getChildren().add(robotView);
  }

  public void removeRobot(BaseRobot robot) {
    RobotView robotView = getRobots().remove(robot);
    assert robotView == null;
    this.getChildren().remove(robotView);
  }

  public HashMap<Obstacle, ObstacleView> getObstacles() {
    return this.obstacles;
  }

  public HashMap<BaseRobot, RobotView> getRobots() {
    return this.robots;
  }
}
