package com.extract.cache.utils;

public class StringUtils {

  public static boolean isNotEmpty(String str) {
    if (str != null && str.trim().length() > 0) {
      return true;
    }
    return false;
  }
}
