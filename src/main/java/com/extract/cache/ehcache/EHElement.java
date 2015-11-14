package com.extract.cache.ehcache;

import java.io.Serializable;

import net.sf.ehcache.Element;

public class EHElement extends Element {

  private static final long serialVersionUID = 5754710666861444643L;
  private static final long reloadIntervalTime = 1000;
  private long lastReloadTime = System.currentTimeMillis();
  private long expire = -1;

  public EHElement(Serializable key, Serializable value, long expire) {
    super(key, value, 1l);
    this.expire = expire;
  }

  public EHElement(Object key, Object value, long expire) {
    super(key, value, 1l);
    this.expire = expire;
  }

  @Deprecated
  public EHElement(Object key, Object value, long version, long creationTime, long lastAccessTime,
      long nextToLastAccessTime, long lastUpdateTime, long hitCount) {
    this(key, value, version, creationTime, lastAccessTime, lastUpdateTime, hitCount);
  }

  public EHElement(Object key, Object value, long version, long creationTime, long lastAccessTime, long lastUpdateTime,
      long hitCount) {
    super(key, value, version, creationTime, lastAccessTime, lastUpdateTime, hitCount);
  }

  public EHElement(Object key, Object value, long version, long creationTime, long lastAccessTime, long hitCount,
      boolean cacheDefaultLifespan, int timeToLive, int timeToIdle, long lastUpdateTime) {
    super(key, value, version, creationTime, lastAccessTime, hitCount, cacheDefaultLifespan, timeToLive, timeToIdle,
        lastUpdateTime);
  }

  public EHElement(Object key, Object value, Boolean eternal, Integer timeToIdleSeconds, Integer timeToLiveSeconds) {
    super(key, value, eternal, timeToIdleSeconds, timeToLiveSeconds);
  }

  public EHElement(Serializable key, Serializable value) {
    this(key, value, 1L);
  }

  public EHElement(Object key, Object value) {
    this(key, value, 1L);
  }

  public boolean isReload() {
    if (expire == -1) {
      expire = reloadIntervalTime;
    }
    if (System.currentTimeMillis() > lastReloadTime + expire) {
      lastReloadTime = System.currentTimeMillis();
      return true;
    }
    return false;
  }

  public long getExpire() {
    return expire;
  }

  public void setExpire(long expire) {
    this.expire = expire;
  }
}
