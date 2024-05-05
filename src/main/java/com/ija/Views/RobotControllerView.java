/**
* @file RobotControllerView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the controller for manipulating with `ControlledRobot`.
*/

package com.ija.Views;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import com.ija.UIData;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.MoveControlledRobotCommand;
import com.ija.Commands.RemoveControlledRobotDirectionCommnand;
import com.ija.Enums.Direction;
import com.ija.Enums.SimulationState;
import com.ija.Interfaces.Command;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RobotControllerView extends Pane {
  private final static double HEIGHT = 170;
  private final static double WIDTH = 250;
  private final static double BUTTON_SIDE_LENGTH = 80;
  private final static double CORNER_RADIUS = 15;

  public RobotControllerView() {
    super();
    this.setPrefWidth(WIDTH);
    this.setPrefHeight(HEIGHT);
    this.getChildren().addAll(forwardButton(), leftButton(), rightButton());
  }

  private StackPane forwardButton() {
    FontIcon forwardIcon = new FontIcon(MaterialDesign.MDI_ARROW_UP);
    StackPane button = buildButton(forwardIcon, Direction.Forward);
    button.setLayoutX(WIDTH / 2 - BUTTON_SIDE_LENGTH / 2);

    return button;
  }

  private StackPane rightButton() {
    FontIcon rightIcon = new FontIcon(MaterialDesign.MDI_ARROW_RIGHT);
    StackPane button = buildButton(rightIcon, Direction.Right);
    button.setLayoutX(WIDTH - BUTTON_SIDE_LENGTH);
    button.setLayoutY(HEIGHT - BUTTON_SIDE_LENGTH);
    return button;
  }

  private StackPane leftButton() {
    FontIcon leftIcon = new FontIcon(MaterialDesign.MDI_ARROW_LEFT);
    StackPane button = buildButton(leftIcon, Direction.Left);
    button.setLayoutY(HEIGHT - BUTTON_SIDE_LENGTH);
    return button;
  }

  private StackPane buildButton(FontIcon icon, Direction direction) {
    icon.setIconSize(60);
    icon.setFill(Color.WHITE);
    Rectangle rectangle = new Rectangle(BUTTON_SIDE_LENGTH, BUTTON_SIDE_LENGTH);
    rectangle.setFill(Color.rgb(80, 80, 80, 0.4));

    rectangle.setArcWidth(CORNER_RADIUS);
    rectangle.setArcHeight(CORNER_RADIUS);

    StackPane button = new StackPane(rectangle, icon);
    StackPane.setAlignment(icon, Pos.CENTER);

    button.setCursor(Cursor.HAND);

    Command moveCommand = new MoveControlledRobotCommand(direction);
    Command stopCommand = new RemoveControlledRobotDirectionCommnand(direction);

    button.setOnMousePressed(e -> {
      if (UIData.simulationState.get() != SimulationState.PAUSE) {
        CommandInvoker.executeCommand(moveCommand);
      }
    });

    button.setOnMouseReleased(e -> {
      if (UIData.simulationState.get() != SimulationState.PAUSE) {
        CommandInvoker.executeCommand(stopCommand);
      }
    });
    
    return button;
  }
}
