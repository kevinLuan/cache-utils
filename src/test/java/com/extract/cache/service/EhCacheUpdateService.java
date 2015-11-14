package com.extract.cache.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.extract.cache.ehcache.EHCacheKey;
import com.extract.cache.ehcache.EHCacheUpdate;
import com.extract.cache.inteceptor.MockBean;

@Service
public class EhCacheUpdateService {
	public static AtomicInteger counted = new AtomicInteger(0);
	public static AtomicInteger counted1 = new AtomicInteger(0);

	@EHCacheKey(cacheName = "name1", key = "key1", params = "0")
	public MockBean getMockBean(int num) {
		System.out.println("geetMockBean(" + num + ")" + counted.incrementAndGet());
		return new MockBean();
	}

	@EHCacheKey(cacheName = "name2", key = "key2", params = "0")
	public MockBean getMockBean1(int num) {
		System.out.println("getMockBean1(" + num + ")" + counted1.incrementAndGet());
		return new MockBean();
	}

	@EHCacheUpdate(cacheNames = { "name1", "name2" }, keys = { "flushAll", "key2" }, params = { "", "0" })
	public void update(int num) {
		System.out.println("update...." + num);
	}
}
