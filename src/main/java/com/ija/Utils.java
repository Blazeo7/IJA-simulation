/**
* @file Utils.java 
* @author Matúš Moravčík (xmorav48)
* @brief Utility class. 
*/

package com.ija;

public class Utils {

  public static double euclidean(double x, double y) {
    return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
  }

  public static double degrees2Radians(double degrees) {
    return (2 * Math.PI / 360) * degrees;
  }

  public static String double2String(double value, int precision) {
    return String.format("%." + precision + "f", value).replace(",", ".");
  }

  public static String trimStart(String input) {
    int startIndex = 0;
    // Find the index of the first non-whitespace character
    while (startIndex < input.length() && Character.isWhitespace(input.charAt(startIndex))) {
      startIndex++;
    }
    // Return the substring starting from the first non-whitespace character
    return input.substring(startIndex);
  }

  public static class Counter {
    private static long cnt = 0;
    private static int step = 1;

    public static void tick() {
      cnt = Math.max(0L, cnt + step);
    }

    public static long get() {
      return cnt;
    }

    public static void reverse() {
      step *= -1;
    }
  }
}
