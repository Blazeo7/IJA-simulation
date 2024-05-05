/**
* @file AutoRobot.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents autonomous robot.
*/

package com.ija.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ija.Utils;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.RobotActionCommand;
import com.ija.Enums.Direction;
import com.ija.Enums.RobotAction;

public class AutoRobot extends BaseRobot {

  @JsonIgnore
  private RobotAction action = RobotAction.TRANSLATE;

  // Angle that robot needs to rotate yet in order to start transition
  private double remainingRotationSize = 0;

  public AutoRobot() {
  }

  public AutoRobot(double velocity, double rotationVelocity, Point pos, Direction direction) {
    super(velocity, rotationVelocity, pos);
    this.direction = direction;
  }

  public void move() {
    if (getAction() == RobotAction.TRANSLATE || getAction() == RobotAction.STARTING_TRANSLATE) {
      changeAction(RobotAction.TRANSLATE);
      translate();
    }
    // Rotate robot
    else {
      // Set rotation angle to the expected angle after rotation
      if (remainingRotationSize <= rotationVelocity) {
        setAngle(getAngle() + direction.getValue() * remainingRotationSize);
        changeAction(RobotAction.STARTING_TRANSLATE);
        remainingRotationSize = 0;
      }
      // Rotate at rotationVelocity speed
      else {
        rotate(direction);
        remainingRotationSize -= rotationVelocity;
      }
    }
  }

  /**
   * Move robot in opposite direction
   */
  public void moveBackward() {
    if (getAction() == RobotAction.TRANSLATE || getAction() == RobotAction.STARTING_TRANSLATE) {
      translateBackward();
    }
    // Rotate robot
    else {
      rotateBackward();
      remainingRotationSize += rotationVelocity;
    }
  }

  private void translateBackward() {
    double deltaX = -velocity * Math.cos(Utils.degrees2Radians(angle));
    double deltaY = -velocity * Math.sin(Utils.degrees2Radians(angle));
    setPos(getPos().add(deltaX, deltaY));
  }

  private void rotateBackward() {
    double newAngle = getAngle() - direction.getValue() * rotationVelocity;
    setAngle(newAngle);
  }

  /**
   * Change robot action via command
   */
  private void changeAction(RobotAction nextAction) {
    if (nextAction == getAction())
      return;

    RobotActionCommand command = new RobotActionCommand(this, getAction(), nextAction);
    CommandInvoker.executeCommand(command);
  }

  /**
   * React to collision by changing action to rotation and setting rotation angle
   */
  public void collide() {
    if (getAction() == RobotAction.TRANSLATE) {
      changeAction(RobotAction.ROTATE);
      remainingRotationSize = rotationSize;
    }
  }

  // Set the action of the robot
  public void setAction(RobotAction action) {
    this.action = action;
  }

  public RobotAction getAction() {
    return this.action;
  }

}
