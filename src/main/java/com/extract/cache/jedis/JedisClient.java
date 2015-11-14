package com.extract.cache.jedis;

import redis.clients.jedis.Jedis;

/**
 * 简单jedis链接管理
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class JedisClient extends AbstractJedisClient {
  private String password;
  private String host;
  private int port;

  @Override
  public Jedis getResource() {
    Jedis jedis = new Jedis(host, port);
    jedis.auth(password);
    jedis.connect();
    return jedis;
  }

  @Override
  public void release(Jedis jedis) {
    if (jedis != null) {
      jedis.disconnect();
    }
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

}
