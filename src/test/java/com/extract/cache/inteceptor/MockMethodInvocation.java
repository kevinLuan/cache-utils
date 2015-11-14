package com.extract.cache.inteceptor;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;

public class MockMethodInvocation implements MethodInvocation {
	
	public Object getThis() {
		return this;
	}

	public Object[] getArguments() {
		MockBean miniConfig = new MockBean();
		miniConfig.setId(100);
		return new Object[] { miniConfig, 1, "LSS" };
	}

	public Method getMethod() {
		try {
			return Method.class.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	public AccessibleObject getStaticPart() {
		try {
			return AccessibleObject.class.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Object proceed() throws Throwable {
		return "Success";
	}
}
