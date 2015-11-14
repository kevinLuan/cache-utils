package com.extract.cache.ehcache;

import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.SerializableUtils;

public class EHCacheManager implements com.extract.cache.CacheManager {
  private Cache cache;
  private static CacheManager cacheManager = null;
  private SerializableUtils<Object> serializable = new SerializableUtils<Object>();
  static {
    try {
      CacheLogger.info("init ehcache.xml......");
      cacheManager = new CacheManager(Thread.currentThread().getContextClassLoader().getResource("ehcache.xml"));
      CacheLogger.info("init ehcache.xml success......");
    } catch (Exception e) {
      CacheLogger.error("init ehcache.xml faild......", e);
    }
  }

  public EHCacheManager(String cacheName) {
    this.cache = getCacheIfAbsentAddCache(cacheName);
  }

  public static Cache getCacheIfAbsentAddCache(String name) {
    if (cacheManager.cacheExists(name) == false) {
      synchronized (EHCacheManager.class) {
        if (cacheManager.cacheExists(name) == false) {
          cacheManager.addCache(name);
        }
      }
    }
    return cacheManager.getCache(name);
  }

  @Deprecated
  protected EHCacheManager(String cacheName, String xml) throws UnsupportedEncodingException {
    cacheManager = new CacheManager(new ByteArrayInputStream(xml.getBytes("UTF-8")));
    this.cache = getCacheIfAbsentAddCache(cacheName);
  }

  public boolean add(String key, Object value, boolean isReload) {
    if (isReload) {
      try {
        this.cache.put(new EHElement(key, value));
      } catch (Exception e) {
        CacheLogger.error("add(" + key + "," + value + "," + isReload + ")", e);
        return false;
      }
      return true;
    } else {
      return add(key, value);
    }
  }

  public boolean add(String key, Object value, boolean isReload, int expiry) {
    if (isReload) {
      try {
        this.cache.put(new EHElement(key, value, expiry));
      } catch (Exception e) {
        CacheLogger.error("add(" + key + "," + value + "," + isReload + "," + expiry + ")", e);
        return false;
      }
      return true;
    } else {
      return put(key, value, expiry);
    }
  }

  public boolean add(String key, Object value) {
    try {
      this.cache.put(new Element(key, value));
    } catch (Exception e) {
      CacheLogger.error("add(" + key + "," + value + ")", e);
      return false;
    }
    return true;
  }

  public boolean put(String key, Object value, Integer expiry) {
    try {
      this.cache.put(new Element(key, value, Boolean.valueOf(false), Integer.valueOf(expiry.intValue() / 1000), Integer
          .valueOf(expiry.intValue() / 1000)));
    } catch (Exception e) {
      CacheLogger.error("put(" + key + "," + value + "," + expiry + ")", e);
      return false;
    }
    return true;
  }

  public boolean delete(String key) {
    try {
      this.cache.remove(key);
    } catch (Exception e) {
      CacheLogger.error("delete(" + key + ")", e);
      return false;
    }
    return true;
  }

  public void destroy() {
    this.cache.dispose();
  }

  public boolean flushAll() {
    try {
      this.cache.removeAll();
    } catch (Exception e) {
      CacheLogger.error("flushAll()", e);
      return false;
    }
    return true;
  }

  public Object get(String key) {
    Element e = this.cache.get(key);
    if (e == null) {
      return null;
    }
    return e.getObjectValue();
  }

  public boolean isReload(String key) {
    Element e = this.cache.get(key);
    if (e == null) {
      return true;
    } else if (e instanceof EHElement) {
      return ((EHElement) e).isReload();
    } else {
      return e.isExpired();
    }
  }

  public Map<String, Object> getMulti(String[] keys) {
    Map<String, Object> map = new HashMap<String, Object>();
    for (String key : keys) {
      Element e = this.cache.get(key);
      if (e != null)
        map.put(key, e.getObjectValue());
    }
    return map;
  }

  public Object[] getMultiArray(String[] keys) {
    Object[] o = new Object[keys.length];
    for (int i = 0; i < keys.length; ++i) {
      Element e = this.cache.get(keys[i]);
      if (e != null)
        o[i] = e.getObjectValue();
    }
    return o;
  }

  public boolean keyExists(String key) {
    return this.cache.getKeys().contains(key);
  }

  public boolean replace(String key, Object value) {
    return add(key, value);
  }

  public boolean replace(String key, Object value, boolean isReload) {
    return add(key, value, isReload);
  }

  public boolean replace(String key, Object value, Integer expiry) {
    return put(key, value, expiry);
  }

  public static CacheManager getCacheManager() {
    return cacheManager;
  }

  /**
   * 添加Cache，存储为序列化的方式
   * 
   * @param key
   * @param value
   * @param isReload
   * @param expiry
   * @return
   */
  public boolean addSerializableVal(String key, Serializable value, boolean isReload, int expiry) {
    if (isReload) {
      try {
        this.cache.put(new EHElement(key, serializable.encode(value), expiry));
      } catch (Exception e) {
        CacheLogger.error("addSerializableVal(" + key + "," + value + "," + isReload + "," + expiry + ")", e);
        return false;
      }
      return true;
    } else {
      return put(key, serializable.encode(value), expiry);
    }
  }

  /**
   * 获取序列化结果
   * 
   * @param key
   * @return
   */
  public Object getSerializableVal(String key) {
    Element e = this.cache.get(key);
    if (e == null) {
      return null;
    }
    byte[] bytes = (byte[]) e.getObjectValue();
    return serializable.decode(bytes);
  }
}
