/**
* @file RemoveAllObjectsCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the action of removing all robots and obstacles from `GameField`.
*/


package com.ija.Commands;

import java.util.HashSet;

import com.ija.Logger;
import com.ija.Interfaces.Command;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;

public class RemoveAllObjectsCommand implements Command {
  private GameField gameField;
  private HashSet<BaseRobot> robots;
  private HashSet<Obstacle> obstacles;

  public RemoveAllObjectsCommand(GameField gameField) {
    this.gameField = gameField;
    this.robots = new HashSet<BaseRobot>(gameField.getRobots());
    this.obstacles = new HashSet<Obstacle>(gameField.getObstacles());
  }

  @Override
  public void execute() {
    gameField.getObstacles().clear();
    gameField.getRobots().clear();
    Logger.info("All objects removed");
  }

  @Override
  public void undo() {
    gameField.getObstacles().addAll(obstacles);
    gameField.getRobots().addAll(robots);
    Logger.info("All objects imported");
  }
}
