/**
* @file DirectionArrows.java 
* @author Matúš Moravčík (xmorav48)
* @brief This class represents the arrows used in `CreationRobotOverlay` for updating 
*        the direction of robot rotation.
*/

package com.ija.Views.OvelayWidgets;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import com.ija.Config;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.UpdateParameterCommand;
import com.ija.Enums.Direction;
import com.ija.Enums.ObservableProperty;
import com.ija.Interfaces.Observer;
import com.ija.Models.BaseRobot;

import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class DirectionArrows extends HBox implements Observer {

  private static final Color ACTIVE_DIRECTION_BUTTON_COLOR = Config.CONTROLLED_ROBOT_STROKE_COLOR;
  private static final Color DEFAULT_DIRECTION_BUTTON_COLOR = Color.web("#313131");
  private static final int ICON_SIZE = 25;

  private BaseRobot robot;

  private FontIcon leftIcon;
  private FontIcon rightIcon;

  public DirectionArrows(BaseRobot robot) {
    super();
    this.robot = robot;
    this.leftIcon = new FontIcon(MaterialDesign.MDI_ARROW_LEFT);
    this.rightIcon = new FontIcon(MaterialDesign.MDI_ARROW_RIGHT);

    leftIcon.setIconSize(ICON_SIZE);
    rightIcon.setIconSize(ICON_SIZE);

    leftIcon.setCursor(Cursor.HAND);
    rightIcon.setCursor(Cursor.HAND);

    this.robot.addObserver(this, ObservableProperty.DIRECTION);

    leftIcon.setOnMouseClicked(e -> {
      if (this.robot.getDirection() == Direction.Left)
        return;

      UpdateParameterCommand<Direction> updateParameterCommand = new UpdateParameterCommand<>(
          (param) -> this.robot.setDirection(param), // setter
          this.robot.getDirection(), // oldValue
          Direction.Left); // newValue

      CommandInvoker.executeCommand(updateParameterCommand);
    });

    rightIcon.setOnMouseClicked(e -> {
      if (this.robot.getDirection() == Direction.Right)
        return;

      UpdateParameterCommand<Direction> updateParameterCommand = new UpdateParameterCommand<>(
          (param) -> this.robot.setDirection(param), // setter
          this.robot.getDirection(), // oldValue
          Direction.Right); // newValue

      CommandInvoker.executeCommand(updateParameterCommand);
    });

    update();

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(leftIcon, rightIcon);
  }

  @Override
  public void update() {
    if (this.robot.getDirection() == Direction.Left) {
      leftIcon.setIconColor(ACTIVE_DIRECTION_BUTTON_COLOR);
      rightIcon.setIconColor(DEFAULT_DIRECTION_BUTTON_COLOR);
    } else if (this.robot.getDirection() == Direction.Right) {
      rightIcon.setIconColor(ACTIVE_DIRECTION_BUTTON_COLOR);
      leftIcon.setIconColor(DEFAULT_DIRECTION_BUTTON_COLOR);
    }
  }
}
