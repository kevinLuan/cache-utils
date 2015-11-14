package com.extract.cache.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.service.JedisCacheService;
import com.extract.commons.BaseTest;

public class TestJeditCache extends BaseTest {
	@Autowired
	private JedisCacheService jedisCacheService;

	@Before
	public void before() {
		JedisCacheService.counted.set(0);
		jedisCacheService.updateMockBean(111, new MockBean());
		jedisCacheService.updateMockBean();
	}

	@After
	public void after() {
	}

	@Test
	public void test() {
		int i = 0;
		while (i < 10) {
			jedisCacheService.getMockBean();
			i++;
		}
		Assert.assertEquals(JedisCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test1() {
		System.out.println(JedisCacheService.counted.get());
		int i = 0;
		while (i < 10) {
			jedisCacheService.getMockBean();
			jedisCacheService.updateMockBean();
			i++;
		}
		Assert.assertEquals(JedisCacheService.counted.compareAndSet(10, 0), true);
	}

	@Test
	public void test2() {
		int i = 0;
		while (i < 10) {
			jedisCacheService.getMockBean(111, new MockBean());
			i++;
		}
		Assert.assertEquals(JedisCacheService.counted.compareAndSet(1, 0), true);
	}

	@Test
	public void test3() {
		int i = 0;
		while (i < 10) {
			jedisCacheService.getMockBean(111, new MockBean());
			jedisCacheService.updateMockBean(111, new MockBean());
			i++;
		}
		System.err.println(JedisCacheService.counted.get());
		Assert.assertEquals(JedisCacheService.counted.compareAndSet(10, 0), true);
	}
}
