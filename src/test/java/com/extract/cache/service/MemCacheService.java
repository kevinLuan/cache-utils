package com.extract.cache.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.memcache.MemCacheKey;
import com.extract.cache.memcache.MemCacheUpdate;

@Service
public class MemCacheService {
	public static AtomicInteger counted = new AtomicInteger(0);

	@MemCacheKey(key = "test", expire = 1000)
	public MockBean getMockBean() {
		System.out.println("getMockBean()...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@MemCacheUpdate(key = "test")
	public void updateMockBean() {
	}

	@MemCacheKey(key = "test1", expire = 1000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@MemCacheUpdate(key = "test1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}
}
