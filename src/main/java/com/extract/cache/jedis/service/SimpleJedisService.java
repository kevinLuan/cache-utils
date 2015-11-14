package com.extract.cache.jedis.service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import com.extract.cache.jedis.AbstractJedisClient;
import com.extract.cache.utils.CacheLogger;

public class SimpleJedisService {
  private AbstractJedisClient jedisManager;

  public boolean setIfNotExists(String key, String value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return false;
    }
    try {
      return (jedis.setnx(key, value) == 1);
    } finally {
      jedis.expire(key, seconds);
      jedisManager.release(jedis);
    }
  }

  public String atomicGetAndSet(String key, String value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      String result = jedis.getSet(key, value);
      jedis.expire(key, seconds);
      return result;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public List<String> getManyKey(String... keys) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.mget(keys);
    } finally {
      jedisManager.release(jedis);
    }
  }

  private String[] uniq(String[] keys, String[] values) {
    String[] keysvalues = new String[keys.length + values.length];
    for (int i = 0; i < keys.length; i++) {
      keysvalues[i * 2] = keys[i];
      keysvalues[i * 2 + 1] = values[i];
    }
    return keysvalues;
  }

  public void setManyKey(String[] keys, String[] values, int seconds) {
    if (keys.length != values.length) {
      return;
    }
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return;
    }
    try {
      jedis.mset(uniq(keys, values));
    } finally {
      for (int i = 0; i < keys.length; i++) {
        jedis.expire(keys[i], seconds);
      }
      jedisManager.release(jedis);
    }
  }

  public Long incrOneByKey(String key, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.incr(key);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public Long decrOneByKey(String key, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.decr(key);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public Long incrBy(String key, int value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long resLong = jedis.incrBy(key, value);
      jedis.expire(key, seconds);
      return resLong;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public Long decrBy(String key, int value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.decrBy(key, value);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public String getKey(String key) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.get(key);
    } finally {
      jedisManager.release(jedis);
    }
  }

  public void setByKey(String key, String value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return;
    }
    try {
      jedis.set(key, value);
      jedis.expire(key, seconds);
    } finally {
      jedisManager.release(jedis);
    }
  }

  public AbstractJedisClient getJedisManager() {
    return jedisManager;
  }

  public void setJedisManager(AbstractJedisClient jedisManager) {
    this.jedisManager = jedisManager;
  }

  public void mapSet(String key, String field, String value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return;
    }
    try {
      jedis.hset(key, field, value);
      jedis.expire(key, seconds);
    } finally {
      jedisManager.release(jedis);
    }
  }

  public Long mapIncrBy(String key, String field, Long value, int seconds) {
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.hincrBy(key, field, value);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
    }
  }

  public String mapGet(String key, String field) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.hget(key, field);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "mapGet.key:" + key + "|field:" + field);
    }
  }

  public Map<String, String> mapGetAll(String key) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.hgetAll(key);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "mapGetAll.key:" + key);
    }
  }

  public Long delete(String... keys) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return -1l;
    }
    try {
      return jedis.del(keys);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "delete.key:" + Arrays.toString(keys));
    }
  }

  public long deleteKeys(String pattern) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return 0;
    }
    try {
      Set<String> set = jedis.keys(pattern);
      if (set.size() == 0)
        return 0;
      String[] keys = new String[set.size()];
      set.toArray(keys);
      return jedis.del(keys);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "deleteKeys:" + pattern);
    }
  }

  public Set<String> getKeys(String pattern) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.keys(pattern);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "getKeys:" + pattern);
    }
  }

  public Boolean isExists(String key) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.exists(key);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "isExists:" + key);
    }
  }

  public Long zAdd(String key, int score, String member, int seconds) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.zadd(key, score, member);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "zAdd:" + key + "|member:" + member + "|score:" + score);
    }
  }

  public Double zScore(String key, String member) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.zscore(key, member);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "zScore:" + key + "|member:" + member);
    }
  }

  public Double zIncrBy(String key, int score, String member, int seconds) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Double res = jedis.zincrby(key, score, member);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "zIncrBy:" + key + "|member:" + member + "|score:" + score);
    }
  }

  public Long zCount(String key, int min, int max) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.zcount(key, min, max);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "zCount:" + key + "|min:" + min + "|max:" + max);
    }
  }

  public Set<Tuple> zRevRangeWithScore(String key, int start, int end) {
    long startTime = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.zrevrangeWithScores(key, start, end);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(startTime, "zRange:" + key + "|start:" + start + "|end:" + end);
    }
  }

  // 从大到小顺序排列，获取member排名
  public Long zRevRank(String key, String member) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.zrevrank(key, member);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "zRevRank:" + key + "|member:" + member);
    }
  }

  public Long append(String key, String value, int seconds) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      Long res = jedis.append(key, value);
      jedis.expire(key, seconds);
      return res;
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "append|key:" + key + "|value:" + value);
    }
  }

  public Set<String> keys(String pattern) {
    long startTime = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    if (jedis == null) {
      return null;
    }
    try {
      return jedis.keys(pattern);
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(startTime, "keys:" + pattern);
    }
  }
}
