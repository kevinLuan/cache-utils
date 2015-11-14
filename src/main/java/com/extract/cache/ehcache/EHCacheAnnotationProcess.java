package com.extract.cache.ehcache;

import java.io.Serializable;
import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.StringUtils;

public class EHCacheAnnotationProcess extends AbstactCacheAnnotationProcess {

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    EHCacheKey ehCacheKey = (EHCacheKey) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    if (StringUtils.isNotEmpty(ehCacheKey.key())) {
      keyBuilder.append(ehCacheKey.key());
    } else {
      keyBuilder.append(mi.getStaticPart().toString());
    }
    appendParameter(keyBuilder, ehCacheKey.params(), mi);
    return keyBuilder.toString();
  }

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    EHCacheKey ehCacheKey = (EHCacheKey) annotation;
    String key = generatorKey(mi, ehCacheKey);
    CacheLogger.debug("EHCacheAnnotationProcess", key);
    EHCacheManager ehCacheManager = new EHCacheManager(ehCacheKey.cacheName());
    if (ehCacheManager.keyExists(key)) {
      if (ehCacheManager.isReload(key)) {
        Object value = mi.proceed();
        if (ehCacheKey.isCacheNull() || value != null) {
          return push(key, value, ehCacheManager, ehCacheKey);
        } else {
          ehCacheManager.delete(key);
        }
        return value;
      } else {
        return getValue(key, ehCacheManager, ehCacheKey);
      }
    }
    Object value = mi.proceed();
    if (ehCacheKey.isCacheNull() || value != null) {
      push(key, value, ehCacheManager, ehCacheKey);
    }
    return value;
  }

  private Object push(String key, Object value, EHCacheManager ehCacheManager, EHCacheKey ehCacheKey) {
    long start = System.currentTimeMillis();
    try {
      ehCacheManager.addSerializableVal(key, (Serializable) value, ehCacheKey.isReload(), ehCacheKey.expire());
    } catch (Exception ex) {
      CacheLogger.error("push(" + key + "," + value + ")", ex);
    } finally {
      CacheLogger.writeSlowRequest(start, "EHCacheAnnotationProcess.push(" + key + "," + value + ")");
    }
    return value;
  }

  private Object getValue(String key, EHCacheManager ehCacheManager, EHCacheKey ehCacheKey) {
    long start = System.currentTimeMillis();
    try {
      return ehCacheManager.getSerializableVal(key);
    } catch (Exception ex) {
      CacheLogger.error("getValue(" + key + ")", ex);
      // 反序列失败时，删除Cache
      ehCacheManager.delete(key);
    } finally {
      CacheLogger.writeSlowRequest(start, "EHCacheAnnotationProcess.getValue(" + key + ")");
    }
    return null;
  }
}
