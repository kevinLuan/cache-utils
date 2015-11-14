package com.extract.cache.memcache;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MemCacheKey {

  String params() default "";

  String key() default "";

  int expire() default 1000 * 60;// 单位：毫秒
}
