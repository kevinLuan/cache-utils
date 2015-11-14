package com.extract.cache.memcache;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.StringUtils;

public class MemCacheAnnotationProcess extends AbstactCacheAnnotationProcess {

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    MemCacheKey memCacheKey = (MemCacheKey) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    if (StringUtils.isNotEmpty(memCacheKey.key())) {
      keyBuilder.append(memCacheKey.key());
    } else {
      keyBuilder.append(mi.getStaticPart().toString());
    }
    appendParameter(keyBuilder, memCacheKey.params(), mi);
    return keyBuilder.toString();
  }

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    MemCacheKey memCacheKey = (MemCacheKey) annotation;
    String key = generatorKey(mi, memCacheKey);
    CacheLogger.debug("MemCache", key);
    Object value = MemCacheManager.getInstance().get(key);
    if (value == null) {
      value = mi.proceed();
      if (value != null) {
        MemCacheManager.getInstance().put(key, value, memCacheKey.expire());
      }
    }
    return value;
  }

}
