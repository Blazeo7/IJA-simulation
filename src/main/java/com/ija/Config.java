/**
* @file Config.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class that holds constants. 
*/

package com.ija;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class Config {
  // Main layout
  public static double WINDOW_WIDTH = 1000.0;
  public static double WINDOW_HEIGHT = 600.0;

  // App bar
  public static final double APP_BAR_HEIGHT = 50;
  public static final String APP_BAR_TEXT_COLOR = "#9D9D9D";
  public static final String APP_BAR_ACTIVE_TEXT_COLOR = "#261c1c";

  // Game field
  public static final Color GAME_FIELD_BCKG_COLOR = Color.web("#201818");
  public static final double SECONDS_PER_FRAME = 0.016;

  // Overlays
  public static final Color OVERLAY_BCKG_COLOR = Color.web("#23222280");

  // Controlled robot
  public static final Color CONTROLLED_ROBOT_STROKE_COLOR = Color.web("#5300A6");
  public static final double CONTROLLED_ROBOT_STROKE_WIDTH = 2;
  public static final StrokeType CONTROLLED_ROBOT_STROKE_TYPE = StrokeType.INSIDE;

  // Robot Action Buttons (floating buttons)
  public static final double FLOATING_BUTTON_WIDTH = 6;
  public static final int FLOATING_BUTTON_HEIGHT = 8;
  public static final double FLOATING_ACTION_BUTTON_ROBOT_OFFSET_Y = 10;
  public static final double FLOATING_ACTION_BUTTONS_SPACING = 4;

  // Obstacle view
  public static final Color OBSTACLE_STROKE_COLOR = Color.BLACK;
  public static final double OBSTACLE_STROKE_WIDTH = 2;
  public static final StrokeType OBSTACLE_STROKE_TYPE = StrokeType.INSIDE;
}
