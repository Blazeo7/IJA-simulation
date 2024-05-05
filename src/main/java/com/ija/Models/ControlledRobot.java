/**
* @file ControlledRobot.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents user controlled robot.
*/

package com.ija.Models;

import com.ija.Events;
import com.ija.Utils;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.UpdateParameterCommand;
import com.ija.Enums.Direction;

public class ControlledRobot extends BaseRobot {

  public ControlledRobot() {
  }

  public ControlledRobot(double velocity, double rotationVelocity, Point pos) {
    super(velocity, rotationVelocity, pos);
  }

  public void move() {
    for (Direction direction : Events.ControlledRobotActions) {
      if (direction == Direction.Forward) {
        translate();
      } else {
        rotate(direction);
      }
    }
  }

  /**
   * Move robot forward and save new position in command history
   */
  protected void translate() {
    double deltaX = velocity * Math.cos(Utils.degrees2Radians(angle));
    double deltaY = velocity * Math.sin(Utils.degrees2Radians(angle));

    UpdateParameterCommand<Double> updateParameterCommandX = new UpdateParameterCommand<>(
        (param) -> setPosX(param), getPos().getX(), getPos().getX() + deltaX);

    UpdateParameterCommand<Double> updateParameterCommandY = new UpdateParameterCommand<>(
        (param) -> setPosY(param), getPos().getY(), getPos().getY() + deltaY);

    CommandInvoker.executeCommand(updateParameterCommandX);
    CommandInvoker.executeCommand(updateParameterCommandY);
  }

  /**
   * Rotate robot in provided direction and save new angle in command history
   */
  protected void rotate(Direction direction) {
    double newAngle = getAngle() + direction.getValue() * rotationVelocity;

    UpdateParameterCommand<Double> updateParameterCommand = new UpdateParameterCommand<>(
        (param) -> setAngle(param), getAngle(), newAngle);

    CommandInvoker.executeCommand(updateParameterCommand);
  }

  public void collide() {
  }
}
