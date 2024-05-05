/**
* @file UIData.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class that holds Observable wrappers for application data. 
*/

package com.ija;

import com.ija.Enums.Direction;
import com.ija.Enums.SimulationState;
import com.ija.Enums.OpenDialog;
import com.ija.Models.AutoRobot;
import com.ija.Models.BaseRobot;
import com.ija.Models.Obstacle;
import com.ija.Models.Point;

import javafx.beans.property.SimpleObjectProperty;

public class UIData {
  /**
   * Currently open dialog
   */
  public static SimpleObjectProperty<OpenDialog> dialogProperty = new SimpleObjectProperty<>(OpenDialog.NONE);
  
  /**
   * Robot that is being added into simulation
   */
  public static SimpleObjectProperty<BaseRobot> addedRobot = new SimpleObjectProperty<>(
    new AutoRobot(5, 4, new Point(500, 400),Direction.Left)
  );

  /**
   * Obstacle that is being added into simulation
   */
  public static SimpleObjectProperty<Obstacle> addedObstacle = new SimpleObjectProperty<>(
    new Obstacle(new Point(400, 300), 50, 30)
  );

  /**
   * Current state of the simulation
   */
  public static SimpleObjectProperty<SimulationState> simulationState = new SimpleObjectProperty<>(SimulationState.PAUSE);

  public static void selectDialogTab(OpenDialog tab) {
    if (tab == dialogProperty.get()) {
      dialogProperty.set(OpenDialog.NONE);
    } else {
      dialogProperty.set(tab);
    }
  }

}