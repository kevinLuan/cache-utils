package com.extract.cache.inteceptor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.AbstactCacheAnnotationProcess;
import com.extract.cache.utils.CacheLogger;

public class CacheInteceptor implements MethodInterceptor {

  public CacheInteceptor(Map<String, AbstactCacheAnnotationProcess> registreMAP) throws ClassNotFoundException {
    if (isInit.compareAndSet(false, true)) {
      for (Map.Entry<String, AbstactCacheAnnotationProcess> entry : registreMAP.entrySet()) {
        try {
          REGISTER_MAP.put(Class.forName(entry.getKey()), entry.getValue());
        } catch (ClassNotFoundException e) {
          CacheLogger.error("CacheInteceptor()errorMsg:" + e.getMessage() + "|key:" + entry.getKey() + "|value:"
              + entry.getValue());
          throw e;
        }
      }
      CacheLogger.warn("--------------event Annotation Process---------------");
      for (Map.Entry<Object, AbstactCacheAnnotationProcess> entry : REGISTER_MAP.entrySet()) {
        CacheLogger.warn("Annotation:" + entry.getKey() + "|AnnotationProcess:" + entry.getValue());
      }
      CacheLogger.warn("--------------event Annotation Process done.---------------");
    } else {
      CacheLogger.warn("-----alreay init cache -----");
    }
  }

  private static Map<Object, AbstactCacheAnnotationProcess> REGISTER_MAP =
      new HashMap<Object, AbstactCacheAnnotationProcess>();
  private static AtomicBoolean isInit = new AtomicBoolean(false);

  @SuppressWarnings("unchecked")
  @Override
  public Object invoke(MethodInvocation mi) throws Throwable {
    try {
      mi = new ProxyMethodInvocation(mi);
      for (Map.Entry<Object, AbstactCacheAnnotationProcess> entry : REGISTER_MAP.entrySet()) {
        Annotation annotation = mi.getMethod().getAnnotation((Class<Annotation>) entry.getKey());
        if (annotation != null) {
          return entry.getValue().invoke(mi, annotation);
        }
      }
    } catch (Exception e) {
      CacheLogger.error("invoke()errorMsg:" + e.getMessage() + "|method:" + mi.getMethod().getName()
          + "|getStaticPart:" + mi.getStaticPart() + "|getArguments:" + Arrays.toString(mi.getArguments()));
      throw e;
    }
    return mi.proceed();
  }
}
