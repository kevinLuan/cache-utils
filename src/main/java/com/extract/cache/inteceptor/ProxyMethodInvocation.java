package com.extract.cache.inteceptor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;

import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;

public class ProxyMethodInvocation implements MethodInvocation {
  private MethodInvocation mi;
  private static final Logger LOGGER = Logger.getLogger(ProxyMethodInvocation.class);

  public ProxyMethodInvocation(MethodInvocation mi) {
    this.mi = mi;
  }

  @Override
  public Object[] getArguments() {
    return mi.getArguments();
  }

  @Override
  public Object proceed() throws Throwable {
    long start = System.currentTimeMillis();
    try {
      return mi.proceed();
    } finally {
      long useTime = System.currentTimeMillis() - start;
      if (useTime > 10) {
        String thisString = mi.getThis().toString();
        if (thisString.indexOf("DAO") != -1) {
          if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("requestTime:" + new Timestamp(System.currentTimeMillis()) + "|Class:" + thisString
                + "|method:" + mi.getMethod().getName() + "|params:" + Arrays.toString(mi.getArguments()) + "|useTime:"
                + useTime);
          } else if (LOGGER.isInfoEnabled()) {
            LOGGER.info("requestTime:" + new Timestamp(System.currentTimeMillis()) + "|Class:" + thisString
                + "|method:" + mi.getMethod().getName() + "|paramsLength:" + mi.getArguments().length + "|useTime:"
                + useTime);
          }
        }
      }
    }
  }

  @Override
  public Object getThis() {
    return mi.getThis();
  }

  @Override
  public AccessibleObject getStaticPart() {
    return mi.getStaticPart();
  }

  @Override
  public Method getMethod() {
    return mi.getMethod();
  }

}
