package com.extract.cache.jedis.service;

import java.util.Arrays;
import java.util.Set;

import redis.clients.jedis.Jedis;

import com.extract.cache.jedis.AbstractJedisClient;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.SerializableUtils;

public class JedisCacheManager implements com.extract.cache.CacheManager {
  protected SerializableUtils<Object> ou = new SerializableUtils<Object>();
  protected AbstractJedisClient jedisManager;

  @Override
  public boolean put(String key, Object value, Integer expiry) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return false;
    try {
      return jedis.set(key.getBytes(), ou.encode(value)) != null;
    } finally {
      jedis.expire(key.getBytes(), expiry / 1000);
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "put.key:" + key);
    }
  }

  public boolean putTry(String key, Object value, Integer expiry) {
    try {
      put(key, value, expiry);
    } catch (Exception ex) {
      CacheLogger.error("putTry(" + key + "," + value + "," + expiry + ")faild", ex);
    }
    return false;
  }

  @Override
  public boolean delete(String key) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return false;
    try {
      return jedis.del(key.getBytes()) > 0;
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "delete.key:" + key);
    }
  }

  public Long deleteTry(String... key) {
    try {
      return delete(key);
    } catch (Exception ex) {
      CacheLogger.error("deleteTry(" + key + ")", ex);
    }
    return null;
  }

  public boolean keyExists(String key) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return false;
    try {
      return jedis.exists(key.getBytes());
    } finally {
      jedisManager.release(jedis);
    }
  }

  public Object get(String key) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return null;
    try {
      return ou.decode(jedis.get(key.getBytes()));
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "get.key:" + key);
    }

  }

  public Object getTry(String key) {
    try {
      return get(key);
    } catch (Exception ex) {
      CacheLogger.error("getTry(" + key + ")", ex);
      return null;
    }
  }

  public Set<String> keys(String pattern) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return null;
    try {
      return jedis.keys(pattern);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisCacheManager.keys(" + pattern + ")");
    }
  }

  public Set<byte[]> keys(byte pattern[]) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null)
      return null;
    try {
      return jedis.keys(pattern);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisCacheManager.keys(" + pattern + ")");
    }
  }

  public Long delete(Set<String> set) {
    if (set == null || set.size() == 0)
      return -1l;
    String[] keys = new String[set.size()];
    set.toArray(keys);
    return delete(keys);
  }

  public Long deleteTry(Set<String> set) {
    try {
      return delete(set);
    } catch (Exception ex) {
      CacheLogger.error("deleteTry", ex);
      return null;
    }
  }

  public Long delete(String... keys) {
    long start = System.currentTimeMillis();
    if (keys == null || keys.length == 0) {
      return -1l;
    }
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.del(keys);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisCacheManager.delete(" + Arrays.toString(keys) + ")");
    }
  }

  public Long delete(byte[]... keys) {
    long start = System.currentTimeMillis();
    if (keys == null || keys.length == 0) {
      return -1l;
    }
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.del(keys);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisCacheManager.delete(" + Arrays.toString(keys) + ")");
    }
  }

  public AbstractJedisClient getJedisManager() {
    return jedisManager;
  }

  public void setJedisManager(AbstractJedisClient jedisManager) {
    this.jedisManager = jedisManager;
  }
}
