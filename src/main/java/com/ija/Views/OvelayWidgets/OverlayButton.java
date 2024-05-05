/**
* @file OverlayButton.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents creation ovelay submit button. 
*/

package com.ija.Views.OvelayWidgets;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

public class OverlayButton extends Button {
  public OverlayButton(String text, Runnable callback) {
    super(text);
    this.setPrefWidth(Double.MAX_VALUE);
    this.setOnAction((event) -> callback.run());

    this.setStyle("""
      -fx-background-color: #12031480;
      -fx-text-fill: white;
      -fx-font-size: 14px;
      -fx-font-weight: bold;-fx-border-radius: 10px;
      -fx-background-radius: 0 0 10 10px;
        """);

    // Set cursor to hand when hovering over the button
    this.setOnMouseEntered(e -> setCursor(Cursor.HAND));
    this.setOnMouseExited(e -> setCursor(Cursor.DEFAULT));
  }
}
