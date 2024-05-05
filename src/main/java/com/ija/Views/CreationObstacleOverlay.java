/**
* @file CreationObstacleOverlay.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents overlay for creating `Obstacle`
*/

package com.ija.Views;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Enums.ObservableProperty;
import com.ija.Enums.OpenDialog;
import com.ija.Models.GameField;
import com.ija.Models.Obstacle;
import com.ija.Views.OvelayWidgets.GridView;
import com.ija.Views.OvelayWidgets.OverlayButton;
import com.ija.Views.OvelayWidgets.OverlayStatView;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;

public class CreationObstacleOverlay extends VBox {
  private static CornerRadii cornerRadii = new CornerRadii(10);

  private Obstacle obstacle;
  private GameField gameField;
  private GridView gridView;

  public CreationObstacleOverlay(Obstacle obstacle, GameField gameField) {
    super();
    this.obstacle = obstacle;
    this.gameField = gameField;
    this.gridView = new GridView(obstacle);

    registerRobotListener();
    setOverlayStyle();
    setStats();

    OverlayButton button = new OverlayButton("Add obstacle", () -> {
      UIData.addedObstacle.set(null);
      UIData.dialogProperty.set(OpenDialog.NONE);
    });

    this.getChildren().addAll(gridView, button);
  }

  private void setOverlayStyle() {
    this.setBackground(new Background(new BackgroundFill(Config.OVERLAY_BCKG_COLOR, cornerRadii, Insets.EMPTY)));
    this.setMaxWidth(200);
    this.setPrefHeight(USE_COMPUTED_SIZE);
    this.setLayoutX(10);
    this.setLayoutY(Config.APP_BAR_HEIGHT + 5);
  }

  private void setStats() {
    this.gridView.addStat(ObservableProperty.POSITION_X, new OverlayStatView(
        () -> obstacle.getPos().getX(),
        (param) -> obstacle.setPosX(param),
        0.0, gameField.getWidth()));

    this.gridView.addStat(ObservableProperty.POSITION_Y, new OverlayStatView(
        () -> obstacle.getPos().getY(),
        (param) -> obstacle.setPosY(param),
        0.0, gameField.getHeight()));

    this.gridView.addStat(ObservableProperty.WIDTH, new OverlayStatView(
        () -> obstacle.getWidth(),
        (param) -> obstacle.setWidth(param),
        10.0, 150.0));

    this.gridView.addStat(ObservableProperty.HEIGHT, new OverlayStatView(
        () -> obstacle.getHeight(),
        (param) -> obstacle.setHeight(param),
        10.0, 150.0));

    this.gridView.addStat(ObservableProperty.ANGLE, new OverlayStatView(
        () -> obstacle.getAngle(),
        (param) -> obstacle.setAngle(param),
        0.0, 360.0));
  }

  private void registerRobotListener() {
    UIData.addedObstacle.addListener((observable, oldValue, newValue) -> {
      if (newValue == null)
        return;

      this.obstacle = newValue;
      this.gridView = new GridView(newValue);
      setStats();
      this.getChildren().set(0, gridView);
    });
  }
}
