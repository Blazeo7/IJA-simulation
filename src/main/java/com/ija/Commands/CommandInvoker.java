/**
* @file CommandInvoker.java 
* @author Matúš Moravčík (xmorav48)
* @brief This class serves as a wrapper for executing commands. It maintains a history of all 
*        executed commands, enabling the ability to redo them.
*/

package com.ija.Commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import com.ija.UIData;
import com.ija.Utils;
import com.ija.Enums.SimulationState;
import com.ija.Interfaces.Command;

import javafx.util.Pair;

public class CommandInvoker {
  private static Stack<Pair<Long, ArrayList<Command>>> history = new Stack<>();

  public static void executeCommand(Command command) {
    long id = Utils.Counter.get();
    Pair<Long, ArrayList<Command>> topmostPair;

    if (UIData.simulationState.get() == SimulationState.REPLAY) {
      command.execute();
      return;
    }

    if (history.isEmpty()) {
      topmostPair = new Pair<>(id, new ArrayList<>());
      history.push(topmostPair);
    } else {
      topmostPair = history.peek();

      if (topmostPair.getKey() < id) {
        topmostPair = new Pair<>(id, new ArrayList<>());
        history.push(topmostPair);
      }
    }

    topmostPair.getValue().add(command);
    command.execute();
  }

  public static void executeHistoryCommand() {
    long id = Utils.Counter.get();

    if (history.isEmpty())
      return;

    // No action for this frame
    if (history.peek().getKey() != id)
      return;

    if (UIData.simulationState.get() == SimulationState.REPLAY) {
      List<Command> commands = history.pop().getValue();

      for (int i = commands.size() - 1; i >= 0; i--) {
        Command command = commands.get(i);
        command.undo();
      }
    }
  }
}
