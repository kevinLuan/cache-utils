package com.extract.cache.jedis;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.jedis.service.JedisCacheManager;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.StringUtils;

public class JedisAnnotationProcess extends AbstactCacheAnnotationProcess {

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    JedisCacheKey jedisCacheKey = (JedisCacheKey) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    if (StringUtils.isNotEmpty(jedisCacheKey.key())) {
      keyBuilder.append(jedisCacheKey.key());
    } else {
      keyBuilder.append(mi.getStaticPart().toString());
    }
    appendParameter(keyBuilder, jedisCacheKey.params(), mi);
    return keyBuilder.toString();
  }

  private JedisCacheManager jedisCacheManager;

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    JedisCacheKey jedisCacheKey = (JedisCacheKey) annotation;
    String key = generatorKey(mi, jedisCacheKey);
    CacheLogger.debug("JedisCache", key);
    Object value = jedisCacheManager.getTry(key);
    if (value == null) {
      value = mi.proceed();
      if (value != null) {
        jedisCacheManager.putTry(key, value, jedisCacheKey.expire());
      }
    }
    return value;
  }

  public JedisCacheManager getJedisCacheManager() {
    return jedisCacheManager;
  }

  public void setJedisCacheManager(JedisCacheManager jedisCacheManager) {
    this.jedisCacheManager = jedisCacheManager;
  }

}
