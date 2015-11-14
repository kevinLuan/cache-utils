package com.extract.cache.memcache;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.StringUtils;

public class MemCacheUpdateAnnotationProcess extends AbstactCacheAnnotationProcess {

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    MemCacheUpdate memCacheUpdate = (MemCacheUpdate) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    if (StringUtils.isNotEmpty(memCacheUpdate.key())) {
      keyBuilder.append(memCacheUpdate.key());
    } else {
      keyBuilder.append(mi.getStaticPart().toString());
    }
    appendParameter(keyBuilder, memCacheUpdate.params(), mi);
    return keyBuilder.toString();
  }

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    MemCacheUpdate memCacheUpdate = (MemCacheUpdate) annotation;
    String key = generatorKey(mi, memCacheUpdate);
    CacheLogger.debug("MemCacheUpdate", key);
    MemCacheManager.getInstance().delete(key);
    return mi.proceed();
  }

}
