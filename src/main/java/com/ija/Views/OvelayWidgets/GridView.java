/**
* @file GridView.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents the kye value pairs in creation overlays.
*/

package com.ija.Views.OvelayWidgets;

import com.ija.Observable;
import com.ija.Enums.ObservableProperty;

import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GridView extends GridPane {
  private static String fontColor = "#9E9E9E";

  private Observable observable;
  private int rowIndex = 0;

  public GridView(Observable observable) {
    super();
    this.observable = observable;

    this.setPadding(new Insets(10));
    this.setHgap(5);
    this.setVgap(12);
  }

  public void setObservable(Observable observable) {
    this.observable = observable;
  }

  public void addStat(ObservableProperty name, OverlayStatView statValue) {
    observable.addObserver(statValue, name);

    Label label = statName(name.getValue());
    GridPane.setConstraints(label, 0, rowIndex);
    GridPane.setConstraints(statValue, 1, rowIndex);
    GridPane.setHalignment(statValue, HPos.CENTER);

    this.getChildren().addAll(label, statValue);
    rowIndex++;
  }

  public void addCustomStat(String name, Node child) {
    Label label = statName(name);
    GridPane.setConstraints(label, 0, rowIndex);
    GridPane.setConstraints(child, 1, rowIndex);
    GridPane.setHalignment(child, HPos.CENTER);

    this.getChildren().addAll(label, child);
    rowIndex++;
  }

  private Label statName(String name) {
    Label label = new Label(name);
    label.setStyle("-fx-font-size: 14px; -fx-text-fill:" + fontColor + "; -fx-font-weight: bold;");
    return label;
  }
}