/**
* @file RemoveRobotCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the action of removing `Robot` from `GameField`
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;

public class RemoveRobotCommand implements Command {

  private BaseRobot robot;
  private GameField gameField;

  public RemoveRobotCommand(BaseRobot robot, GameField gameField) {
    this.robot = robot;
    this.gameField = gameField;
  }

  @Override
  public void execute() {
    gameField.removeRobot(robot);
    Logger.info(robot + " removed");
  }

  @Override
  public void undo() {
    gameField.addRobot(robot);
    Logger.info(robot + " added");
  }
}
