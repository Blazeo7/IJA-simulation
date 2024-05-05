/**
* @file AddObstacleCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents Command for adding `Obstacle` into `GameField`.
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;

public class AddObstacleCommand implements Command {
  private Obstacle obstacle;
  private GameField gameField;

  public AddObstacleCommand(Obstacle obstacle, GameField gameField) {
    this.obstacle = obstacle;
    this.gameField = gameField;
  }

  public void execute() {
    gameField.addObstacle(obstacle);
    Logger.info(obstacle + " added");
  }

  public void undo() {
    gameField.removeObstacle(obstacle);
    Logger.info(obstacle + " removed");
  }
}
