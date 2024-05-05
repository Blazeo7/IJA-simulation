/**
* @file RobotActionCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the change of the `AutoRobot` action
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Enums.RobotAction;
import com.ija.Interfaces.Command;
import com.ija.Models.AutoRobot;
import com.ija.Models.Point;

public class RobotActionCommand implements Command {
  private RobotAction action;
  private RobotAction oldAction;
  private AutoRobot robot;

  private Point lastPos;
  private double lastAngle;

  public RobotActionCommand(AutoRobot robot, RobotAction oldAction, RobotAction action) {
    this.action = action;
    this.oldAction = oldAction;
    this.robot = robot;

    this.lastAngle = robot.getAngle();
    this.lastPos = new Point(robot.getPos().getX(), robot.getPos().getY());
  }

  @Override
  public void execute() {
    Logger.info("Action " + action);
    robot.setAction(action);
  }

  @Override
  public void undo() {
    Logger.info("Action: " + oldAction);
    robot.setAngle(lastAngle);
    robot.setPos(lastPos);
    robot.setAction(oldAction);
  }
}
