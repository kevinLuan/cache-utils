package com.extract.cache.service;

import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.stereotype.Service;

import com.extract.cache.inteceptor.MockBean;
import com.extract.cache.jedis.JedisCacheKey;
import com.extract.cache.jedis.JedisCacheUpdate;

@Service
public class JedisCacheService {
	public static AtomicInteger counted = new AtomicInteger(0);

	@JedisCacheKey(key = "test", expire = 1000)
	public MockBean getMockBean() {
		System.out.println("getMockBean()...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@JedisCacheUpdate(key = "test")
	public void updateMockBean() {
	}

	@JedisCacheKey(key = "test1", expire = 1000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}

	@JedisCacheUpdate(key = "test1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}
}
