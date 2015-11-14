package com.extract.cache.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.service.EHCacheService;
import com.extract.commons.BaseTest;

public class TestEHCache extends BaseTest {
	@Autowired
	private EHCacheService ehCacheService;

	@Before
	public void before() {
		ehCacheService.updateMockBean();
	}

	@After
	public void after() {
		ehCacheService.updateMockBean(111, new MockBean());
	}

	@Test
	public void test() {

		int i = 0;
		while (i < 10) {
			try {
				ehCacheService.getMockBean();
			} catch (Exception e) {
				e.printStackTrace();
			}
			i++;
		}
		Assert.assertEquals(EHCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test1() {
		int i = 0;
		while (i < 10) {
			ehCacheService.getMockBean();
			ehCacheService.updateMockBean();
			i++;
		}
		Assert.assertEquals(EHCacheService.counted.compareAndSet(10, 0), true);
	}

	@Test
	public void test2() {
		int i = 0;
		while (i < 10) {
			ehCacheService.getMockBean(111, new MockBean());
			i++;
		}
		Assert.assertEquals(EHCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test3() {
		int i = 0;
		while (i < 10) {
			ehCacheService.getMockBean(111, new MockBean());
			ehCacheService.updateMockBean(111, new MockBean());
			i++;
		}
		System.out.println(EHCacheService.counted.get());
		Assert.assertEquals(EHCacheService.counted.compareAndSet(10, 0), true);
	}

	@Test
	public void test4() {
		ehCacheService.getMockBean(1);
		ehCacheService.getMockBean(2);
		ehCacheService.flushAll();
		Assert.assertEquals(ehCacheService.getMockBean(3), 3);
	}

	@Test
	public void test5() {
		int i = 0;
		while (i < 100) {
			ehCacheService.testEmptyCache();
			i++;
		}
		System.out.println(EHCacheService.counted.get());
		Assert.assertEquals(EHCacheService.counted.compareAndSet(1, 0), true);
	}
}
