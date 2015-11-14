package com.extract.cache;

public interface CacheManager {

  public boolean put(String key, Object value, Integer expiry);

  public boolean delete(String key);

  public boolean keyExists(String key);

  public Object get(String key);
}
