package com.extract.cache.ehcache;

import java.lang.annotation.Annotation;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.utils.CacheLogger;

public class EHCacheUpdateAnnotationProcess extends AbstactCacheAnnotationProcess {

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    EHCacheUpdate ehCacheKey = (EHCacheUpdate) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    keyBuilder.append(ehCacheKey.keys()[0]);
    appendParameter(keyBuilder, ehCacheKey.params()[0], mi);
    return keyBuilder.toString();
  }

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    EHCacheUpdate ehCacheUpdate = (EHCacheUpdate) annotation;
    long start = System.currentTimeMillis();
    try {
      if (ehCacheUpdate.cacheNames().length > 1 || ehCacheUpdate.keys().length > 1) {
        if (ehCacheUpdate.cacheNames().length != ehCacheUpdate.keys().length) {
          throw new IllegalArgumentException("params extent differ. cacheNames:"
              + Arrays.toString(ehCacheUpdate.cacheNames()) + "|key:" + Arrays.toString(ehCacheUpdate.keys()));
        }
        for (int i = 0; i < ehCacheUpdate.cacheNames().length; i++) {
          String cacheName = ehCacheUpdate.cacheNames()[i];
          EHCacheManager ehCacheManager = new EHCacheManager(cacheName);
          if (ehCacheUpdate.keys()[i].equals(EHCacheUpdate.FLUSH_ALL)) {
            ehCacheManager.flushAll();
            CacheLogger.debug("EHCacheUpdateAnnotationProcess", cacheName + ".flushAll()");
          } else {
            StringBuilder keyBuilder = new StringBuilder(20);
            keyBuilder.append(ehCacheUpdate.keys()[i]);
            appendParameter(keyBuilder, ehCacheUpdate.params()[i], mi);
            ehCacheManager.delete(keyBuilder.toString());
            CacheLogger.debug("EHCacheUpdateAnnotationProcess.delete", cacheName + "." + keyBuilder.toString());
          }
        }
      } else {
        EHCacheManager ehCacheManager = new EHCacheManager(ehCacheUpdate.cacheNames()[0]);
        if (ehCacheUpdate.flushAll()) {
          ehCacheManager.flushAll();
          CacheLogger.debug("EHCacheUpdateAnnotationProcess", ehCacheUpdate.cacheNames()[0] + ".flushAll()");
        } else {
          String key = generatorKey(mi, ehCacheUpdate);
          ehCacheManager.delete(key);
          CacheLogger.debug("EHCacheUpdateAnnotationProcess.delete", ehCacheUpdate.cacheNames()[0] + "." + key);
        }
      }
    } catch (Exception ex) {
      CacheLogger.error("EHCacheUpdateAnnotationProcess.invoke", ex);
    } finally {
      CacheLogger.writeSlowRequest(start, "EHCacheUpdateAnnotationProcess.invoke");
    }
    return mi.proceed();
  }

}
