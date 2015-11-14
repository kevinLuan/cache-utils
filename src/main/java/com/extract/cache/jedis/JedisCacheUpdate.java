package com.extract.cache.jedis;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface JedisCacheUpdate {

  String params() default "";// 参数：0.id,1,2

  String key();

  // boolean isRegex() default false;// 正则匹配
}
