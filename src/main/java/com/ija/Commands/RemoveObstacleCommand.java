/**
* @file RemoveObstacleCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the action of removing `Obstacle` from `GameField`
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;

public class RemoveObstacleCommand implements Command {

  private GameField gameField;
  private Obstacle obstacle;

  public RemoveObstacleCommand(Obstacle obstacle, GameField gameField) {
    this.gameField = gameField;
    this.obstacle = obstacle;
  }

  @Override
  public void execute() {
    gameField.removeObstacle(obstacle);
    Logger.info(obstacle + " removed");
  }

  @Override
  public void undo() {
    gameField.addObstacle(obstacle);
    Logger.info(obstacle + " added");
  }
}
