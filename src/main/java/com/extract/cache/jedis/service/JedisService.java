package com.extract.cache.jedis.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import redis.clients.jedis.Jedis;

import com.extract.cache.jedis.AbstractJedisClient;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.SerializableUtils;

public class JedisService<T extends Object> {
  protected SerializableUtils<T> serializable = new SerializableUtils<T>();
  protected AbstractJedisClient jedisManager;

  public T get(String key) {
    long start = System.currentTimeMillis();
    T t = null;
    Jedis jedis = jedisManager.getResource();
    try {
      if (null != jedis) {
        t = serializable.decode(jedis.get(key.getBytes()));
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.get(" + key + ")");
    }
    return t;
  }

  public void set(String key, T o, int seconds) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null) {
        jedis.set(key.getBytes(), serializable.encode(o));
        jedis.expire(key.getBytes(), seconds);
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.set(" + key + ")");
    }
  }

  public T hget(String key, String field) {
    long start = System.currentTimeMillis();
    T t = null;
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null) {
        t = serializable.decode(jedis.hget(key.getBytes(), field.getBytes()));
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.hget(" + key + ")");
    }
    return t;
  }

  public void hset(String key, String field, T o) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null) {
        jedis.hset(key.getBytes(), field.getBytes(), serializable.encode(o));
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.hset(" + key + ")");
    }
  }

  public List<T> hmget(String key, String... fields) {
    long start = System.currentTimeMillis();
    List<T> list = new ArrayList<T>();
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null) {
        byte[][] b = new byte[fields.length][];
        for (int i = 0; i < b.length; i++) {
          b[i] = fields[i].getBytes();
        }
        List<byte[]> gets = jedis.hmget(key.getBytes(), b);
        for (int i = 0; i < gets.size(); i++) {
          list.add(serializable.decode(gets.get(i)));
        }
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.hmget(" + key + ")");
    }
    return list;
  }

  public void hmset(String key, Map<String, T> hash) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    try {
      if (null != jedis && !hash.isEmpty()) {
        Map<byte[], byte[]> m = new HashMap<byte[], byte[]>();
        Iterator<Entry<String, T>> itr = hash.entrySet().iterator();
        while (itr.hasNext()) {
          Entry<String, T> next = itr.next();
          m.put(next.getKey().getBytes(), serializable.encode(next.getValue()));
        }
        jedis.hmset(key.getBytes(), m);
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.hmset(" + key + ")");
    }
  }

  public long lpush(String key, T o, int seconds) {
    long start = System.currentTimeMillis();
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null && key != null) {
        long size = jedis.lpush(key.getBytes(), serializable.encode(o));
        jedis.expire(key.getBytes(), seconds);
        return size;
      } else {
        return 0l;
      }
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.lpush(" + key + ")");
    }
  }

  public List<T> getListAll(String key) {
    return lrange(key, 0, -1);
  }

  public List<T> lrange(String key, int start, int stop) {
    Jedis jedis = jedisManager.getResource();
    try {
      if (jedis != null && key != null) {
        List<byte[]> bl = jedis.lrange(key.getBytes(), start, stop);
        List<T> tl = new ArrayList<T>();
        for (byte[] b : bl) {
          tl.add(serializable.decode(b));
        }
        return tl;
      }
      return null;
    } finally {
      jedisManager.release(jedis);
      CacheLogger.writeSlowRequest(start, "JedisService.lrange(" + key + ")");
    }
  }

  /*
   * public JedisLock acquireLock(String lockKey) { return this.acquireLock(lockKey,
   * DEFAULT_LOCK_TIMEOUT, DEFAULT_LOCK_EXPIRE); }
   * 
   * public JedisLock acquireLock(String lockKey, int timeoutMsecs) { return
   * this.acquireLock(lockKey, timeoutMsecs, DEFAULT_LOCK_EXPIRE); }
   * 
   * public JedisLock acquireLock(String lockKey, int timeoutMsecs, int expireMsecs) { Jedis jedis =
   * jedisPool.getResource(); JedisLock lock = null; try { lock = new JedisLock(lockKey,
   * timeoutMsecs, expireMsecs); try { lock.acquire(jedis); } catch (InterruptedException e) { } }
   * finally { jedisPool.release(jedis); } return lock; }
   * 
   * public void releaseLock(JedisLock lock) { Jedis jedis = jedisPool.getResource(); try {
   * lock.release(jedis); } finally { jedisPool.release(jedis); } }
   */

  public AbstractJedisClient getJedisManager() {
    return jedisManager;
  }

  public void setJedisManager(AbstractJedisClient jedisManager) {
    this.jedisManager = jedisManager;
  }
}
