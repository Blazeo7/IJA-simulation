/**
* @file UpdateParameterCommand.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the change of robot or obstacle parameter
*/

package com.ija.Commands;

import java.util.function.Consumer;

import com.ija.Interfaces.Command;

public class UpdateParameterCommand<T> implements Command {

  private Consumer<T> settter;
  private T oldValue;
  private T newValue;

  public UpdateParameterCommand(Consumer<T> settter, T oldValue, T newValue) {
    this.settter = settter;
    this.oldValue = oldValue;
    this.newValue = newValue;

  }

  @Override
  public void execute() {
    settter.accept(newValue);
  }

  @Override
  public void undo() {
    settter.accept(oldValue);
  }
}
