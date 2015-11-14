package com.extract.cache.jedis;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

/**
 * Jedis链接池实现类
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class JedisClientPool extends AbstractJedisClient {

  private static JedisPool pool;
  private static final Logger LOGGER = Logger.getLogger(JedisClientPool.class);

  public JedisClientPool(JedisPoolConfig jpc, String host, int port, int timeout, String password) {
    pool = new JedisPool(jpc, host, port, timeout, password);
  }

  public JedisClientPool(JedisPoolConfig jpc, String host, int port) {
    pool = new JedisPool(jpc, host, port);
  }

  public Jedis getResource() {
    Jedis jedis = null;
    try {
      jedis = pool.getResource();
    } catch (JedisConnectionException e) {
      LOGGER.error("get jedis resource failed...", e);
    }
    return jedis;
  }

  public void release(Jedis jedis) {
    if ((null != jedis) && pool != null)
      pool.returnResource(jedis);
  }

}
