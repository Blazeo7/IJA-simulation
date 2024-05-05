/**
* @file MoveControlledRobotCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the action of changing a direction by controlled robot.
*/

package com.ija.Commands;

import com.ija.Events;
import com.ija.Logger;
import com.ija.Enums.Direction;
import com.ija.Interfaces.Command;

public class MoveControlledRobotCommand implements Command {
  private Direction direction;

  public MoveControlledRobotCommand(Direction direction){
    this.direction = direction;
  }

  @Override
  public void execute() {
    Events.ControlledRobotActions.add(direction);
    Logger.action("Added direction: " + direction);
  }

  @Override
  public void undo() {
    Events.ControlledRobotActions.remove(direction);
    Logger.action("Removed direction: " + direction);
  }
}
