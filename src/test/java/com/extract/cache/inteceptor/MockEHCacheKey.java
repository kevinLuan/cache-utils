package com.extract.cache.inteceptor;

import java.lang.annotation.Annotation;

import com.extract.cache.ehcache.EHCacheKey;

public class MockEHCacheKey {
	public static EHCacheKey getEhCacheKey() {
		return new EHCacheKey() {
			@Override
			public String cacheName() {
				return "test";
			}

			@Override
			public int expire() {
				return 0;
			}

			@Override
			public boolean isReload() {
				return false;
			}

			@Override
			public String key() {
				return "lss";
			}

			@Override
			public String params() {
				return "0.id ,1, 2";
			}

			@Override
			public Class<? extends Annotation> annotationType() {
				return this.getClass();
			}

			@Override
			public boolean isCacheNull() {
				return false;
			}

		};
	}
}
