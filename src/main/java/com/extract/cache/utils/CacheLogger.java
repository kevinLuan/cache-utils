package com.extract.cache.utils;

import java.sql.Timestamp;

import org.apache.log4j.Logger;

/**
 * CACHE处理处理
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class CacheLogger {
  private static final Logger LOGGER = Logger.getLogger(CacheLogger.class);

  private static String getTime() {
    return "requestTime:" + new Timestamp(System.currentTimeMillis());
  }

  public static void info(String msg) {
    if (LOGGER.isInfoEnabled()) {
      LOGGER.info(getTime() + "|cache:info|" + msg);
    }
  }

  public static void warn(String msg) {
    LOGGER.warn(getTime() + "|cache:warn|" + msg);
  }

  public static void error(Object msg, Throwable e) {
    LOGGER.error(getTime() + "|" + msg, e);
  }

  public static void error(Object msg) {
    LOGGER.error(getTime() + "|" + msg);
  }

  public static void faild(Object msg) {
    LOGGER.fatal(getTime() + "|" + msg);
  }

  public static void writeSlowRequest(long start, String prefix) {
    if (System.currentTimeMillis() - start > 10) {
      LOGGER
          .warn(getTime() + "|cache:jedis.slow|prefix:" + prefix + "|useTime:" + (System.currentTimeMillis() - start));
    }
  }

  public static void debug(String prefix, String key) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug(getTime() + "|prefix:" + prefix + "|key:" + key);
    }
  }
}
