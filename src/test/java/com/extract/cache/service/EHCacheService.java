package com.extract.cache.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.extract.cache.ehcache.EHCacheKey;
import com.extract.cache.ehcache.EHCacheUpdate;
import com.extract.cache.inteceptor.MockBean;

@Service
public class EHCacheService {
	public static AtomicInteger counted = new AtomicInteger(0);

	@EHCacheKey(key = "test", cacheName = "cacheName", expire = 2000)
	public MockBean getMockBean() {
		System.out.println("getMockBean()...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@EHCacheUpdate(keys = "test", cacheNames = "cacheName")
	public void updateMockBean() {
	}

	@EHCacheKey(key = "test1", cacheName = "cacheName1", expire = 2000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@EHCacheUpdate(keys = "test1", cacheNames = "cacheName1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}

	@EHCacheUpdate(cacheNames = "cacheName1", flushAll = true, keys = "")
	public void flushAll() {
	}

	@EHCacheKey(key = "num",params="0", cacheName = "cacheName1")
	public int getMockBean(int num) {
		return num;
	}

	@EHCacheKey(isCacheNull = true)
	public MockBean testEmptyCache() {
		System.out.println("testEmptyCache()...................." + counted.addAndGet(1));
		return null;
	}
	@EHCacheKey(params="0.id")
	public MockBean testSeriable(MockBean bean){
		return bean;
	}
	
	@EHCacheKey(params="0.id")
	public MockBean testNoSeriable(MockBean bean){
		return bean;
	}
	
	@EHCacheKey(key="disk", params="0.id")
	public MockBean testDiskSeriable(MockBean bean){
		return bean;
	}
}
