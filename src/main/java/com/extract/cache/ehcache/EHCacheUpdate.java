package com.extract.cache.ehcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface EHCacheUpdate {
  String DEF_CACHE_NAME = "activity_common_cache";
  String FLUSH_ALL = "flushAll";

  String[] params() default "";

  String[] cacheNames() default {DEF_CACHE_NAME};

  String[] keys();

  boolean flushAll() default false;
}
