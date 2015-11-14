package com.extract.cache.jedis;

import redis.clients.jedis.Jedis;

/**
 * 抽象jedis管理类
 * 
 * @author SHOUSHEN LUAN
 *
 */
public abstract class AbstractJedisClient {
  /**
   * 获取Jedis连接
   * 
   * @return
   */
  public abstract Jedis getResource();

  /**
   * 释放Jedis链接
   * 
   * @param jedis
   */
  public abstract void release(Jedis jedis);
}
