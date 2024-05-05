/**
* @file App.java 
* @author Matúš Moravčík (xmorav48)
* @brief Handles launching application and registration of keyboard listeners 
*/

package com.ija;

import com.ija.Commands.CommandInvoker;
import com.ija.Commands.MoveControlledRobotCommand;
import com.ija.Commands.RemoveControlledRobotDirectionCommnand;
import com.ija.Enums.Direction;
import com.ija.Enums.SimulationState;
import com.ija.Views.MainLayout;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class App extends Application {

  public static void run() {
    launch();
  }

  @Override
  public void start(Stage primaryStage) {
    MainLayout mainLayout = new MainLayout();
    Scene scene = new Scene(mainLayout);
    registerKeyboardListeners(scene);

    primaryStage.setScene(scene);
    primaryStage.setTitle("Application title");
    primaryStage.show();
  }

  private void registerKeyboardListeners(Scene scene) {

    scene.addEventFilter(KeyEvent.KEY_PRESSED, e -> {
      // Handle A,D,W keys
      Direction direction = getDirection(e.getCode());
      if (direction != null && !Events.ControlledRobotActions.contains(direction)
          && UIData.simulationState.get() == SimulationState.PLAY) {
        CommandInvoker.executeCommand(new MoveControlledRobotCommand(direction));
        return;
      }

      // Handle SPACE
      if (e.getCode() == KeyCode.SPACE) {
        if (UIData.simulationState.get() == SimulationState.PLAY) {
          UIData.simulationState.set(SimulationState.PAUSE);
        } else {
          UIData.simulationState.set(SimulationState.PLAY);
        }
      }
    });

    scene.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
      // Handle A,D,W keys
      Direction direction = getDirection(e.getCode());
      if (direction != null && UIData.simulationState.get() == SimulationState.PLAY) {
        CommandInvoker.executeCommand(new RemoveControlledRobotDirectionCommnand(direction));
        return;
      }
    });
  }

  private Direction getDirection(KeyCode code) {
    switch (code) {
      case A:
        return Direction.Left;
      case D:
        return Direction.Right;
      case W:
        return Direction.Forward;
      default:
        return null;
    }

  }
}