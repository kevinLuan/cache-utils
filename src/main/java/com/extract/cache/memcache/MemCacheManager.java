package com.extract.cache.memcache;

import java.sql.Timestamp;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

import com.danga.MemCached.MemCachedClient;
import com.danga.MemCached.SockIOPool;
import com.extract.cache.CacheManager;
import com.extract.cache.utils.StringUtils;
import com.schooner.MemCached.MemcachedItem;

public class MemCacheManager implements CacheManager {

  private static MemCachedClient MEM_CACHE_CLIENT = new MemCachedClient();
  private static MemCacheManager INSTANCE = new MemCacheManager();
  private static AtomicBoolean isInit = new AtomicBoolean();
  private static final Logger LOGGER = Logger.getLogger(MemCacheManager.class);
  private String servers;
  private String weights;
  private Integer initConn = 5;
  private Integer minConn = 5;
  private Integer maxConn = 100;
  private Integer maxIdle = 1000 * 60 * 60 * 6;
  private Integer maintSleep = 30;
  private Boolean nagle = false;
  private Integer socketTO = 3000;
  private Integer socketConnectTO = 0;
  private String poolName;

  private MemCacheManager() {}

  protected void init() {
    if (isInit.compareAndSet(false, true)) {
      LOGGER.warn("++++++++++++++++++++++++++++++++++>>>>init memcache...");
      SockIOPool pool;
      if (StringUtils.isNotEmpty(poolName)) {
        pool = SockIOPool.getInstance(poolName);
      } else {
        pool = SockIOPool.getInstance();
      }
      pool.setServers(servers.split(","));
      pool.setWeights(parserWeights(weights));
      pool.setInitConn(initConn);
      pool.setMinConn(minConn);
      pool.setMaxConn(maxConn);
      pool.setMaxIdle(maxIdle);
      pool.setMaintSleep(maintSleep);
      pool.setNagle(nagle);
      pool.setSocketTO(socketTO);
      pool.setSocketConnectTO(socketConnectTO);
      pool.initialize();
      LOGGER.warn("++++++++++++++++++++++++++++++++++>>>>init memcache successs...");
    }
  }

  public boolean isExists(String key) {
    return MEM_CACHE_CLIENT.keyExists(key);
  }

  public long incr(String key) {
    if (MEM_CACHE_CLIENT.keyExists(key)) {
      return MEM_CACHE_CLIENT.incr(key);
    } else {
      MEM_CACHE_CLIENT.set(key, "0");
      return 0;
    }
  }

  private Integer[] parserWeights(String weightStr) {
    try {
      if (StringUtils.isNotEmpty(weightStr)) {
        String[] strs = weightStr.split(",");
        Integer[] weights = new Integer[strs.length];
        for (int i = 0; i < strs.length; i++) {
          weights[i] = Integer.parseInt(strs[i]);
        }
        return weights;
      }
    } catch (Exception e) {
      LOGGER.warn("parser weights '" + weightStr + "' error", e);
    }
    return null;
  }

  public boolean flushAll() {
    return MEM_CACHE_CLIENT.flushAll();
  }

  public static MemCacheManager getInstance() {
    if (isInit.get()) {
      return INSTANCE;
    } else {
      LOGGER.warn("Please initialize MemCache.init");
      return null;
    }
  }

  public MemcachedItem gets(String key) {
    return MEM_CACHE_CLIENT.gets(key);
  }

  public boolean add(String key, Object value) {
    return MEM_CACHE_CLIENT.add(key, value);
  }

  public boolean add(String key, Object value, Date expiry) {
    return MEM_CACHE_CLIENT.add(key, value, expiry);
  }

  public boolean set(String key, Object value) {
    return MEM_CACHE_CLIENT.set(key, value);
  }

  public boolean set(String key, Object value, Date expiry) {
    return MEM_CACHE_CLIENT.set(key, value, expiry);
  }

  public boolean set(String key, Object value, long seconds) {
    Date date = new Date();
    return this.set(key, value, new Date(date.getTime() + seconds * 1000));
  }

  /**
   * 替换 如果KEY存在则替换成功，不存在则替换失败
   * 
   * @param key
   * @param value
   * @return
   */
  public boolean replace(String key, Object value) {
    return MEM_CACHE_CLIENT.replace(key, value);
  }

  public boolean replace(String key, Object value, Date expiry) {
    return MEM_CACHE_CLIENT.replace(key, value, expiry);
  }

  public Object get(String key) {
    return MEM_CACHE_CLIENT.get(key);
  }

  @Override
  public boolean delete(String key) {
    return MEM_CACHE_CLIENT.delete(key);
  }

  @Override
  public boolean keyExists(String key) {
    return MEM_CACHE_CLIENT.keyExists(key);
  }

  @Override
  public boolean put(String key, Object value, Integer expiry) {
    return MEM_CACHE_CLIENT.add(key, value, new Timestamp(System.currentTimeMillis() + expiry));
  }

  public Integer getInitConn() {
    return initConn;
  }

  public void setInitConn(Integer initConn) {
    this.initConn = initConn;
  }

  public Integer getMinConn() {
    return minConn;
  }

  public void setMinConn(Integer minConn) {
    this.minConn = minConn;
  }

  public Integer getMaxConn() {
    return maxConn;
  }

  public void setMaxConn(Integer maxConn) {
    this.maxConn = maxConn;
  }

  public Integer getMaxIdle() {
    return maxIdle;
  }

  public void setMaxIdle(Integer maxIdle) {
    this.maxIdle = maxIdle;
  }

  public Integer getMaintSleep() {
    return maintSleep;
  }

  public void setMaintSleep(Integer maintSleep) {
    this.maintSleep = maintSleep;
  }

  public Boolean getNagle() {
    return nagle;
  }

  public void setNagle(Boolean nagle) {
    this.nagle = nagle;
  }

  public Integer getSocketTO() {
    return socketTO;
  }

  public void setSocketTO(Integer socketTO) {
    this.socketTO = socketTO;
  }

  public Integer getSocketConnectTO() {
    return socketConnectTO;
  }

  public void setSocketConnectTO(Integer socketConnectTO) {
    this.socketConnectTO = socketConnectTO;
  }

  public String getPoolName() {
    return poolName;
  }

  public void setPoolName(String poolName) {
    this.poolName = poolName;
  }

  public String getServers() {
    return servers;
  }

  public void setServers(String servers) {
    this.servers = servers;
  }

  public String getWeights() {
    return weights;
  }

  public void setWeights(String weights) {
    this.weights = weights;
  }

  public MemCachedClient getMemCachedClient() {
    return MEM_CACHE_CLIENT;
  }

}
