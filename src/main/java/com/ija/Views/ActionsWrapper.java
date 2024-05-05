/**
* @file ActionsWrapper.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class that privides delete and edit buttons for robots and obstacles 
*/


package com.ija.Views;

import org.kordamp.ikonli.javafx.FontIcon;
import org.kordamp.ikonli.materialdesign.MaterialDesign;

import com.ija.Config;
import com.ija.UIData;
import com.ija.Commands.CommandInvoker;
import com.ija.Enums.ObservableProperty;
import com.ija.Enums.OpenDialog;
import com.ija.Interfaces.Command;
import com.ija.Interfaces.Observer;
import com.ija.Models.BaseRobot;
import com.ija.Models.Obstacle;

import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;

public class ActionsWrapper extends Group implements Observer {

  private static double HEIGHT = 20;
  private static double WIDTH = 34;

  private BaseRobot robot;
  private Obstacle obstacle;
  private Command deleteCommand;

  public ActionsWrapper(BaseRobot robot, Command deleteCommand) {
    super();
    this.robot = robot;
    this.deleteCommand = deleteCommand;

    robot.addObserver(this, ObservableProperty.RADIUS);
    update();

    HBox floatingButtons = getFloatingButtons(UIData.addedRobot, robot);
    this.getChildren().add(floatingButtons);
  }

  public ActionsWrapper(Obstacle obstacle, Command deleteCommand) {
    super();
    this.obstacle = obstacle;
    this.deleteCommand = deleteCommand;
    obstacle.addObserver(this, ObservableProperty.HEIGHT);
    update();

    HBox floatingButtons = getFloatingButtons(UIData.addedObstacle, obstacle);
    this.getChildren().add(floatingButtons);
  }

  private <T> HBox getFloatingButtons(SimpleObjectProperty<T> dialogObj, T wrappedObj) {
    HBox hbox = new HBox();
    hbox.setBackground(
        new Background(new BackgroundFill(Color.rgb(50, 50, 50, 0.2), new CornerRadii(20), Insets.EMPTY)));
    hbox.setPadding(new Insets(5));
    hbox.setSpacing(Config.FLOATING_ACTION_BUTTONS_SPACING);

    Cross cross = new Cross();
    cross.setOnMouseClicked(e -> {
      CommandInvoker.executeCommand(deleteCommand);

      if (dialogObj.get() == wrappedObj) {
        dialogObj.set(null);
        UIData.dialogProperty.set(OpenDialog.NONE);
      }
      e.consume();
    });

    Edit edit = new Edit();
    edit.setOnMouseClicked(e -> {
      e.consume();

      // Already being edited
      if (dialogObj.get() == wrappedObj)
        return;

      dialogObj.set(wrappedObj);
      UIData.dialogProperty.set(wrappedObj instanceof BaseRobot ? OpenDialog.ROBOT_DIALOG : OpenDialog.OBSTACLE_DIALOG);
    });

    hbox.getChildren().addAll(edit, cross);
    return hbox;
  }

  @Override
  public void update() {
    if (robot != null) {
      this.setLayoutY(-robot.getRadius() - HEIGHT);
      this.setLayoutX(-5);
    } else {
      this.setLayoutY(-5 - HEIGHT);
      this.setLayoutX(obstacle.getWidth() / 2 - WIDTH / 2);
    }
  }
}

class Cross extends Group {
  Cross() {
    super();
    Circle circle = new Circle();
    circle.setRadius(10);
    circle.setFill(Color.BLACK);
    circle.setOpacity(0.2);

    Line line = new Line(
        -Config.FLOATING_BUTTON_WIDTH / 2, -Config.FLOATING_BUTTON_HEIGHT / 2,
        Config.FLOATING_BUTTON_WIDTH / 2, Config.FLOATING_BUTTON_HEIGHT / 2);

    Line line2 = new Line(
        Config.FLOATING_BUTTON_WIDTH / 2, -Config.FLOATING_BUTTON_HEIGHT / 2,
        -Config.FLOATING_BUTTON_WIDTH / 2, Config.FLOATING_BUTTON_HEIGHT / 2);

    line.setStroke(Color.RED);
    line2.setStroke(Color.RED);

    this.setCursor(Cursor.HAND);
    this.getChildren().addAll(circle, line, line2);
  }
}

class Edit extends Group {
  Edit() {
    super();
    Circle circle = new Circle();
    circle.setRadius(10);
    circle.setFill(Color.BLACK);
    circle.setOpacity(0.2);

    FontIcon icon = new FontIcon(MaterialDesign.MDI_PENCIL);
    icon.setIconSize(Config.FLOATING_BUTTON_HEIGHT);
    icon.setIconColor(Color.WHITE);

    // Calculate the position of the icon to center it within the circle
    double iconX = circle.getCenterX() - Config.FLOATING_BUTTON_HEIGHT / 2;
    double iconY = circle.getCenterY() + Config.FLOATING_BUTTON_HEIGHT / 2;
    icon.setLayoutX(iconX);
    icon.setLayoutY(iconY);

    this.setCursor(Cursor.HAND);
    this.getChildren().addAll(circle, icon);
  }
}
