/**
* @file AddAllObjectsCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents Command for adding all obstacles and robots to `GameField`.
*/

package com.ija.Commands;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.GameField;

public class AddAllObjectsCommand implements Command {

  private GameField gameField;

  public AddAllObjectsCommand(GameField gameField) {
    this.gameField = gameField;
  }

  @Override
  public void execute() {
    Logger.info("All objects imported");
  }

  @Override
  public void undo() {
    gameField.getObstacles().clear();
    gameField.getRobots().clear();
    Logger.info("All objects removed");
  }
}
