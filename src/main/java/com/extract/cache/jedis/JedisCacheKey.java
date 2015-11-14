package com.extract.cache.jedis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JedisCacheKey {

  String params() default "";

  String key() default "";

  int expire() default 1000 * 60;// 单位：毫秒
}
