package com.extract.cache.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.service.MemCacheService;
import com.extract.commons.BaseTest;

public class TestMemCache extends BaseTest {
	@Autowired
	private MemCacheService memCacheService;

	@After
	public void After() {
		MemCacheService.counted.set(0);
		memCacheService.updateMockBean();
		memCacheService.updateMockBean(111, new MockBean());
	}

	@Test
	public void test() {
		int i = 0;
		while (i < 10) {
			memCacheService.getMockBean();
			i++;
		}
		Assert.assertEquals(MemCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test1() {
		int i = 0;
		while (i < 10) {
			memCacheService.getMockBean();
			memCacheService.updateMockBean();
			i++;
		}
		Assert.assertEquals(MemCacheService.counted.compareAndSet(10, 0), true);
	}

	@Test
	public void test2() {
		int i = 0;
		while (i < 10) {
			memCacheService.getMockBean(111, new MockBean());
			i++;
		}
		Assert.assertEquals(MemCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test3() {
		int i = 0;
		while (i < 10) {
			memCacheService.getMockBean(111, new MockBean());
			memCacheService.updateMockBean(111, new MockBean());
			i++;
		}
		Assert.assertEquals(MemCacheService.counted.compareAndSet(10, 0), true);
	}
}
