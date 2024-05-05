/**
* @file AppBar.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents app bar with simulation controlling buttons
*/


package com.ija.Views;

import java.io.File;
import java.io.IOException;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import com.ija.Config;
import com.ija.FileExplorer;
import com.ija.Logger;
import com.ija.UIData;
import com.ija.Utils;
import com.ija.Enums.SimulationState;
import com.ija.Enums.OpenDialog;
import com.ija.Models.GameField;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class AppBar extends HBox {
  private GameField gameField;

  public AppBar(GameField gameField) {
    super();
    this.gameField = gameField;

    this.setStyle("-fx-background-color: rgba(13, 13, 13, 0.6);; -fx-padding: 8 30 8 30; -fx-spacing: 30;");
    this.setPrefHeight(Config.APP_BAR_HEIGHT);
    this.setPrefWidth(Config.WINDOW_WIDTH);

    HBox obstacleTab = buildDialogTab("Obstacles", new FontIcon(MaterialDesign.MDI_SQUARE_INC),
        OpenDialog.OBSTACLE_DIALOG);
    HBox robotTab = buildDialogTab("Robots", new FontIcon(MaterialDesign.MDI_ROBOT), OpenDialog.ROBOT_DIALOG);
    HBox.setHgrow(robotTab, Priority.ALWAYS);

    FontIcon pauseButton = pauseButton();
    FontIcon replayButton = replayButton();

    HBox loadButton = loadButton();
    HBox saveButton = saveButton();

    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(obstacleTab, robotTab, replayButton, pauseButton, loadButton, saveButton);
  }

  private HBox buildDialogTab(String name, FontIcon icon, OpenDialog dialog) {
    icon.setIconSize(20);
    icon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));

    Label label = buildAppbarButtonText(name);

    UIData.dialogProperty.addListener((observable, oldValue, newValue) -> {
      if (newValue == dialog) {
        label.setTextFill(Config.CONTROLLED_ROBOT_STROKE_COLOR);
        icon.setFill(Config.CONTROLLED_ROBOT_STROKE_COLOR);
      } else {
        label.setTextFill(Color.web(Config.APP_BAR_TEXT_COLOR));
        icon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));
      }
    });

    HBox tab = new HBox();

    tab.getChildren().addAll(label, icon);
    tab.setAlignment(Pos.CENTER_LEFT);

    tab.setOnMouseEntered(e -> tab.setCursor(Cursor.HAND));
    tab.setOnMouseExited(e -> tab.setCursor(Cursor.DEFAULT));

    tab.setOnMouseClicked(e -> {
      UIData.selectDialogTab(dialog);
      UIData.simulationState.set(SimulationState.PAUSE);
    });

    return tab;
  }

  private FontIcon pauseButton() {
    FontIcon pauseIcon = new FontIcon(
        UIData.simulationState.get() == SimulationState.PLAY ? MaterialDesign.MDI_PAUSE : MaterialDesign.MDI_PLAY);
    pauseIcon.setIconSize(24);
    pauseIcon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));

    pauseIcon.setCursor(Cursor.HAND);

    UIData.simulationState.addListener((observable, oldValue, newValue) -> {
      Logger.state(newValue.toString());

      if (newValue == SimulationState.PLAY) {
        pauseIcon.setIconCode(MaterialDesign.MDI_PAUSE);
      } else {
        pauseIcon.setIconCode(MaterialDesign.MDI_PLAY);
      }
    });

    pauseIcon.setOnMouseClicked(e -> {
      if (UIData.simulationState.get() == SimulationState.PLAY) {
        UIData.simulationState.set(SimulationState.PAUSE);
      } else {
        UIData.simulationState.set(SimulationState.PLAY);
      }
    });

    return pauseIcon;
  }

  private FontIcon replayButton() {
    FontIcon replayIcon = new FontIcon(MaterialDesign.MDI_SKIP_PREVIOUS);
    replayIcon.setIconSize(24);
    replayIcon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));

    replayIcon.setCursor(Cursor.HAND);

    replayIcon.setOnMousePressed(e -> {
      Utils.Counter.reverse();
      UIData.simulationState.set(SimulationState.REPLAY);
    });

    replayIcon.setOnMouseReleased(e -> {
      Utils.Counter.reverse();
      UIData.simulationState.set(SimulationState.PAUSE);
    });

    return replayIcon;

  }

  private HBox saveButton() {
    FontIcon icon = new FontIcon(MaterialDesign.MDI_FLOPPY);
    icon.setIconSize(20);
    icon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));

    HBox row = new HBox();
    row.getChildren().addAll(buildAppbarButtonText("Save"), icon);
    row.setAlignment(Pos.CENTER_LEFT);

    row.setOnMouseEntered(e -> row.setCursor(Cursor.HAND));
    row.setOnMouseExited(e -> row.setCursor(Cursor.DEFAULT));
    row.setOnMouseClicked((e) -> export());

    row.setAlignment(Pos.CENTER_RIGHT);

    return row;
  }

  private HBox loadButton() {
    FontIcon icon = new FontIcon(MaterialDesign.MDI_DOWNLOAD);
    icon.setIconSize(20);
    icon.setFill(Color.web(Config.APP_BAR_TEXT_COLOR));

    Label label = buildAppbarButtonText("Load");

    HBox row = new HBox();
    row.getChildren().addAll(label, icon);
    row.setAlignment(Pos.CENTER_LEFT);

    row.setOnMouseEntered(e -> row.setCursor(Cursor.HAND));
    row.setOnMouseExited(e -> row.setCursor(Cursor.DEFAULT));
    row.setOnMouseClicked((e) -> importFile());

    HBox.setHgrow(row, Priority.ALWAYS);
    row.setAlignment(Pos.CENTER_RIGHT);

    return row;
  }

  private Label buildAppbarButtonText(String text) {
    Label label = new Label(text);
    label.setStyle("-fx-font-size: 20px; -fx-text-fill:" + Config.APP_BAR_TEXT_COLOR + "; -fx-font-weight: bold;");
    label.setPadding(new Insets(0, 5, 0, 0));
    return label;
  }

  private void export() {
    File file = FileExplorer.saveDialog();
    try {
      if (file == null) {
        return;
      }
      FileExplorer.writeToFile(file, gameField.serialize());

      Logger.success("Export");
    } catch (Exception e) {
      Logger.fail("Export: " + e.getMessage());
    }
  }

  private void importFile() {
    File file = FileExplorer.openDialog();
    UIData.simulationState.set(SimulationState.PAUSE);

    // No file selected
    if (file == null)
      return;

    try {
      gameField.deserialize(file);
      Logger.success("Import");
      UIData.dialogProperty.set(OpenDialog.NONE);
    } catch (IOException e) {
      Logger.fail("Import:" + e.getMessage());
    }
  }
}
