/**
* @file FileExplorer.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class that handle file IO operations. 
*/

package com.ija;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class FileExplorer {
  public static Stage stage;

  public static void init(Stage primaryStage) {
    stage = primaryStage;
  }

  public static File openDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Open");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter(".json", "*.json"));
    return fileChooser.showOpenDialog(stage);
  }

  public static File saveDialog() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Save");
    fileChooser.setInitialFileName("state");
    fileChooser.getExtensionFilters().addAll(new ExtensionFilter(".json", "*.json"));
    return fileChooser.showSaveDialog(stage);
  }

  public static void writeToFile(File file, String text) {
    // Write StringBuilder content to file using UTF-8 encoding
    try (BufferedWriter writer = new BufferedWriter(
        new OutputStreamWriter(new FileOutputStream(file), "UTF-8"))) {
      writer.write(text);
    } catch (IOException e) {
      Logger.fail("IOException: " + e.getMessage());
    } catch (NullPointerException e) {
      Logger.info("File not selected");
    }
  }
}
