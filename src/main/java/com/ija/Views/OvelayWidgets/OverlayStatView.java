/**
* @file OverlayStatView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents `TextField` that reflect changes in Observable and updates it as user is typing. 
*/

package com.ija.Views.OvelayWidgets;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.ija.Utils;
import com.ija.Commands.CommandInvoker;
import com.ija.Commands.UpdateParameterCommand;
import com.ija.Interfaces.Observer;

import javafx.scene.control.TextField;

public class OverlayStatView extends TextField implements Observer {
  private boolean updatedByItself = false;
  private Supplier<Double> propertyGetter;
  private String defualtTextFieldStyle = """
      -fx-background-color: transparent;
      -fx-padding: 0;
      -fx-font-size: 14px;
      -fx-alignment: center;
      -fx-text-fill: white;
      -fx-font-weight: bold;
      """;

  public OverlayStatView(Supplier<Double> getter, Consumer<Double> setter, Double minValue, Double maxValue) {
    super(Utils.double2String(getter.get(), 1));

    this.setStyle(defualtTextFieldStyle);
    this.propertyGetter = getter;
    this.setPrefWidth(100);
    this.textProperty().addListener((observable, oldValue, newValue) -> {
      try {
        Double newPropertyValue = Double.parseDouble(newValue);

        // valid value => update Observable
        if (newPropertyValue <= maxValue && newPropertyValue >= minValue) {
          updatedByItself = true;
          this.setStyle(defualtTextFieldStyle);

          // Update if a new value provided
          if (Math.abs(getter.get() - newPropertyValue) - 0.009 > 0) {
            UpdateParameterCommand<Double> updateParameterCommand = new UpdateParameterCommand<>(
                setter, getter.get(), newPropertyValue);

            CommandInvoker.executeCommand(updateParameterCommand);
          }

          this.setText(Utils.trimStart(newValue));
        }

        // invalid value
        else {
          this.setStyle(defualtTextFieldStyle + "-fx-text-fill: red;");
        }
      } catch (NumberFormatException e) {
        this.setText(oldValue);
        updatedByItself = true;
      }

    });
  }

  @Override
  public void update() {
    if (!updatedByItself) {
      this.setText(Utils.trimStart(Utils.double2String(propertyGetter.get(), 1)));
    }
    updatedByItself = false;
  }
}
