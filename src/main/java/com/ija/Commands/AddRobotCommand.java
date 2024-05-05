/**
* @file AddRobotCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents Command for adding `Robot` into `GameField`.
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;

public class AddRobotCommand implements Command {
  private BaseRobot robot;
  private GameField gameField;

  public AddRobotCommand(BaseRobot robot, GameField gameField) {
    this.robot = robot;
    this.gameField = gameField;
  }

  public void execute() {
    gameField.addRobot(robot);
    Logger.info(robot + " added");
  }

  public void undo() {
    gameField.removeRobot(robot);
    Logger.info(robot + " removed");
  }
}
