/**
* @file CreationRobotOverlay.java 
* @author Matúš Moravčík (xmorav48)
* @brief Represents overlay for creating `BaseRobot`
*/

package com.ija.Views;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Enums.ObservableProperty;
import com.ija.Enums.OpenDialog;
import com.ija.Models.BaseRobot;
import com.ija.Models.GameField;
import com.ija.Views.OvelayWidgets.DirectionArrows;
import com.ija.Views.OvelayWidgets.GridView;
import com.ija.Views.OvelayWidgets.OverlayButton;
import com.ija.Views.OvelayWidgets.OverlayStatView;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;

public class CreationRobotOverlay extends VBox {
  private static CornerRadii cornerRadii = new CornerRadii(10);

  private BaseRobot robot;
  private GameField gameField;
  private GridView gridView;

  public CreationRobotOverlay(GameField gameField) {
    super();
    this.robot = UIData.addedRobot.get();
    this.gameField = gameField;
    this.gridView = new GridView(robot);

    registerRobotListener();
    setOverlayStyle();
    setStats();

    OverlayButton button = new OverlayButton("Add robot", () -> {
      UIData.addedRobot.set(null);
      UIData.dialogProperty.set(OpenDialog.NONE);
    });

    this.getChildren().addAll(gridView, button);
  }

  private void setOverlayStyle() {
    this.setBackground(new Background(new BackgroundFill(Config.OVERLAY_BCKG_COLOR, cornerRadii, Insets.EMPTY)));
    this.setMaxWidth(240);
    this.setPrefHeight(USE_COMPUTED_SIZE);
    this.setLayoutX(10);
    this.setLayoutY(Config.APP_BAR_HEIGHT + 5);
  }

  private void setStats() {
    this.gridView.getChildren().clear();

    this.gridView.addStat(ObservableProperty.POSITION_X, new OverlayStatView(
        () -> robot.getPos().getX(),
        (param) -> robot.setPosX(param),
        0.0, gameField.getWidth()));

    this.gridView.addStat(ObservableProperty.POSITION_Y, new OverlayStatView(
        () -> robot.getPos().getY(),
        (param) -> robot.setPosY(param),
        0.0, gameField.getHeight()));

    this.gridView.addStat(ObservableProperty.RADIUS, new OverlayStatView(
        () -> robot.getRadius(),
        (param) -> robot.setRadius(param),
        10.0, 60.0));

    this.gridView.addStat(ObservableProperty.VELOCITY, new OverlayStatView(
        () -> robot.getVelocity(),
        (param) -> robot.setVelocity(param),
        1.0, 20.0));

    this.gridView.addStat(ObservableProperty.ROTATION_VELOCITY, new OverlayStatView(
        () -> robot.getRotationVelocity(),
        (param) -> robot.setRotationVelocity(param),
        1.0, 20.0));

    this.gridView.addStat(ObservableProperty.ANGLE, new OverlayStatView(
        () -> robot.getAngle(),
        (param) -> robot.setAngle(param),
        0.0, 360.0));

    this.gridView.addStat(ObservableProperty.COLLISION_DIST, new OverlayStatView(
        () -> robot.getCollisionDistDetection(),
        (param) -> robot.setCollisionDistDetection(param),
        0.0, 15.0));

    this.gridView.addStat(ObservableProperty.ROTATION_SIZE, new OverlayStatView(
        () -> robot.getRotationSize(),
        (param) -> robot.setRotationSize(param),
        1.0, 359.0));

    this.gridView.addCustomStat("Direction", new DirectionArrows(this.robot));
  }

  private void registerRobotListener() {
    UIData.addedRobot.addListener((observable, oldValue, newValue) -> {
      if (newValue == null)
        return;

      this.robot = newValue;
      this.gridView = new GridView(newValue);
      setStats();
      this.getChildren().set(0, gridView);
    });
  }
}
