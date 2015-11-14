package com.extract.cache.jedis;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.jedis.service.JedisCacheManager;
import com.extract.cache.utils.CacheLogger;
import com.extract.cache.utils.StringUtils;

public class JedisUpdateAnnotationProcess extends AbstactCacheAnnotationProcess {
  private JedisCacheManager jedisCacheManager;

  @Override
  public String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable {
    JedisCacheUpdate jedisCacheUpdate = (JedisCacheUpdate) annotation;
    StringBuilder keyBuilder = new StringBuilder();
    if (StringUtils.isNotEmpty(jedisCacheUpdate.key())) {
      keyBuilder.append(jedisCacheUpdate.key());
    } else {
      keyBuilder.append(mi.getStaticPart().toString());
    }
    appendParameter(keyBuilder, jedisCacheUpdate.params(), mi);
    return keyBuilder.toString();
  }

  @Override
  public Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable {
    JedisCacheUpdate jedisCacheUpdate = (JedisCacheUpdate) annotation;
    String key = generatorKey(mi, jedisCacheUpdate);
    Long res = jedisCacheManager.deleteTry(key);
    CacheLogger.debug("JedisCacheUpdate", "key[" + key + "] res:" + res);
    return mi.proceed();
  }

  public JedisCacheManager getJedisCacheManager() {
    return jedisCacheManager;
  }

  public void setJedisCacheManager(JedisCacheManager jedisCacheManager) {
    this.jedisCacheManager = jedisCacheManager;
  }
}
