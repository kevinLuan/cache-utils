package com.extract.cache.test;

import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extract.cache.ehcache.Dump;
import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.service.EHCacheService;
import com.extract.commons.BaseTest;

public class EHCacheTest extends BaseTest {
  @Autowired
  private EHCacheService ehCacheService;

  @Test
  public void testSeriable() {
    MockBean mockBean = new MockBean();
    mockBean.setId(1);
    mockBean.setName("kevin");
    for (int u = 0; u < 50; u++) {
      MockBean bean = ehCacheService.testSeriable(mockBean);
      Assert.assertEquals("kevin", bean.getName());
      bean.setName("xx");
    }
    System.out.println(Dump.dump());
  }

  @Test
  public void testNoSeriable() {
    MockBean mockBean = new MockBean();
    mockBean.setId(1);
    mockBean.setName("kevin");
    for (int u = 0; u < 50; u++) {
      MockBean bean = ehCacheService.testNoSeriable(mockBean);
      Assert.assertEquals("kevin", bean.getName());
      bean.setName("xx");
    }
    System.out.println(Dump.dump());
  }

  @Test
  public void testSeriableDisk() {
    MockBean mockBean = new MockBean();
    mockBean.setName("kevin");
    long start = System.currentTimeMillis();
    for (int u = 0; u < 1000; u++) {
      mockBean.setId(new Random().nextInt(1000));
      MockBean bean = ehCacheService.testDiskSeriable(mockBean);
      bean.setName("xx");
    }
    System.out.println("use time:" + (System.currentTimeMillis() - start));
    // System.out.println(Dump.dump());
  }
}
