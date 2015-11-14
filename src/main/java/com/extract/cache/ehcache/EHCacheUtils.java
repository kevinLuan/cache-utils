package com.extract.cache.ehcache;

public class EHCacheUtils {

  public static Object getValue(String key, Object... params) {
    return getValue(EHCacheUpdate.DEF_CACHE_NAME, key, params);
  }

  public static Object getValue(String cacheName, String key, Object... params) {
    EHCacheManager ehCacheManager = new EHCacheManager(cacheName);
    StringBuilder builder = new StringBuilder(20);
    builder.append(key);
    for (int i = 0; i < params.length; i++) {
      if (params[i] == null) {
        builder.append("_NULL");
      } else {
        builder.append("_" + params[i]);
      }
    }
    return ehCacheManager.get(builder.toString());
  }

  public static boolean setValue(Object value, String key, Object... params) {
    return setValue(EHCacheUpdate.DEF_CACHE_NAME, value, key, params);
  }

  public static boolean setValue(String cacheName, Object value, String key, Object... params) {
    EHCacheManager ehCacheManager = new EHCacheManager(cacheName);
    StringBuilder builder = new StringBuilder(20);
    builder.append(key);
    for (int i = 0; i < params.length; i++) {
      if (params[i] == null) {
        builder.append("_NULL");
      } else {
        builder.append("_" + params[i]);
      }
    }
    return ehCacheManager.add(builder.toString(), value);
  }
}
