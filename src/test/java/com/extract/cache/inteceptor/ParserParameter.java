package com.extract.cache.inteceptor;

import junit.framework.Assert;

import org.junit.Test;

import com.extract.cache.ehcache.EHCacheAnnotationProcess;
import com.extract.cache.ehcache.EHCacheKey;

public class ParserParameter {
	@Test
	public void test() {
		try {
			EHCacheKey ehCacheKey = MockEHCacheKey.getEhCacheKey();
			Object result= new EHCacheAnnotationProcess().invoke(new MockMethodInvocation(), ehCacheKey);
			Assert.assertEquals(result,"Success");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
