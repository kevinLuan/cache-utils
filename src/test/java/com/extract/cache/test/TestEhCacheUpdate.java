package com.extract.cache.test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.extract.cache.ehcache.Dump;
import com.extract.cache.service.EhCacheUpdateService;
import com.extract.commons.BaseTest;

public class TestEhCacheUpdate extends BaseTest {
	@Autowired
	private EhCacheUpdateService ehCacheUpdateService;
	static {
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	@Before
	public void before() {
	}

	@After
	public void after() {
		ehCacheUpdateService.counted.set(0);
	}

	@Test
	public void test() {
		int i = 0;
		while (i < 10) {
			ehCacheUpdateService.getMockBean(1);
			ehCacheUpdateService.getMockBean1(1);
			i++;
		}
		Assert.assertSame(ehCacheUpdateService.counted.get()==ehCacheUpdateService.counted1.get(), true);
	}

	@Test
	public void test1() {
		int i = 0;
		while (i < 10) {
			ehCacheUpdateService.getMockBean(i);
			ehCacheUpdateService.getMockBean1(i);
			ehCacheUpdateService.update(i);
			i++;
		}
		System.out.println(Dump.dump());;
		Assert.assertSame(ehCacheUpdateService.counted.get()==ehCacheUpdateService.counted1.get(), true);
	}
}
