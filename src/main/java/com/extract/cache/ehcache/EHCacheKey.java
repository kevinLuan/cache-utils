package com.extract.cache.ehcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EHCacheKey {
  String params() default "";

  String cacheName() default "common_cache";

  String key() default "";

  int expire() default 1000 * 60;// 单位：毫秒

  boolean isReload() default false;

  boolean isCacheNull() default false;
}
