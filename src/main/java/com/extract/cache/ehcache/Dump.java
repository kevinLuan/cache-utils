package com.extract.cache.ehcache;

import java.util.List;

public class Dump {
  public static String dump() {
    String cacheName[] = EHCacheManager.getCacheManager().getCacheNames();
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < cacheName.length; i++) {
      List<?> keyList = EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).getKeys();
      builder.append("name:" + cacheName[i]).append("\n");
      builder.append("Size:" + EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).getSize()).append("\n");
      builder.append("InMemorySize:" + EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).calculateInMemorySize())
          .append("\n");
      builder.append(
          "calculateOffHeapSize:" + EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).calculateOffHeapSize())
          .append("\n");
      builder.append("getDiskStoreSize:" + EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).getDiskStoreSize())
          .append("\n");
      builder.append(
          "getOffHeapStoreSize:" + EHCacheManager.getCacheIfAbsentAddCache(cacheName[i]).getOffHeapStoreSize()).append(
          "\n");
      builder.append("getActiveConfigurationText").append("\n");
      builder.append(EHCacheManager.getCacheManager().getActiveConfigurationText(cacheName[i])).append("\n");
      builder.append("-------------------keys----------------------").append("\n");
      for (Object key : keyList) {
        builder.append(key).append("\n");
      }
      builder.append("\n\n");
    }
    return builder.toString();
  }
}
