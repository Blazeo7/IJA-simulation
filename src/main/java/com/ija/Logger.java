/**
* @file Logger.java 
* @author Matúš Moravčík (xmorav48)
* @brief Class for logging changes on application. 
*/


package com.ija;

public class Logger {
  public static void success(String msg) {
    System.err.println("\u001B[32m[Success] " + msg + "\u001B[0m");
  }

  public static void fail(String msg) {
    System.err.println("\u001B[31m[Fail] " + msg + "\u001B[0m");
  }

  public static void info(String msg) {
    System.err.println("\u001B[33m[Info] " + msg + "\u001B[0m");
  }
  
  public static void state(String msg) {
    System.err.println("\u001B[36m[State] " + msg + "\u001B[0m");
  }

  public static void action(String msg) {
    System.err.println("\u001B[35m[Action] " + msg + "\u001B[0m");
  }
}
