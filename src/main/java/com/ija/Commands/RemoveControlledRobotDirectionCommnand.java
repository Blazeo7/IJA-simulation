/**
* @file RemoveControlledRobotDirectionCommnand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the action of removing the movement direction of `ControlledRobot`
*/

package com.ija.Commands;

import com.ija.Events;
import com.ija.Logger;
import com.ija.Enums.Direction;
import com.ija.Interfaces.Command;

public class RemoveControlledRobotDirectionCommnand implements Command {
  private Direction direction;

  public RemoveControlledRobotDirectionCommnand(Direction direction){
    this.direction = direction;
  }

  @Override
  public void execute() {
    Events.ControlledRobotActions.remove(direction);
    Logger.action("Removed direction: " + direction);
  }
  
  @Override
  public void undo() {
    Events.ControlledRobotActions.add(direction);
    Logger.action("Added direction: " + direction);
  }
}
