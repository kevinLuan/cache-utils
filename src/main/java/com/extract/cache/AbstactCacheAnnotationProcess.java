package com.extract.cache;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import org.aopalliance.intercept.MethodInvocation;

import com.extract.cache.utils.StringUtils;

public abstract class AbstactCacheAnnotationProcess {

  public abstract String generatorKey(MethodInvocation mi, Annotation annotation) throws Throwable;

  public abstract Object invoke(MethodInvocation mi, Annotation annotation) throws Throwable;

  protected void appendParameter(StringBuilder builder, String params, MethodInvocation mi) throws Throwable {
    String[] strs = params.replaceAll("\\s*", "").split(",");
    for (int i = 0; i < strs.length; i++) {
      if (StringUtils.isNotEmpty(strs[i])) {
        if (strs[i].indexOf(".") == -1) {
          if (mi.getArguments()[Integer.parseInt(strs[i])] == null) {
            builder.append("_NULL");
          } else {
            builder.append("_" + mi.getArguments()[Integer.parseInt(strs[i])].toString());
          }
        } else {
          String[] el = strs[i].split("\\.");
          if (el.length == 2) {
            int index = Integer.parseInt(el[0]);
            Object obj = mi.getArguments()[index];
            builder.append("_" + parserParameter(obj, el[1]));
          } else {
            throw new IllegalArgumentException("parser “" + strs[i] + "” error...");
          }
        }
      }
    }
  }

  protected Object parserParameter(Object obj, String fieldStr) throws Throwable {
    if (StringUtils.isNotEmpty(fieldStr)) {
      Field filField = obj.getClass().getDeclaredField(fieldStr);
      if (filField != null) {
        filField.setAccessible(true);
        return filField.get(obj);
      }
    }
    throw new IllegalArgumentException("fieldStr is empty |fieldStr:" + fieldStr);
  }
}
