##分布式Cache工具

####author:kevin Luan
	email: kevin_Luan@126.com


####打包成JAR 包命令

	安装包本地仓库
	mvn clean compile install -Dmaven.test.skip=true
	打包成jar包安装到本地仓库并发布到远程私服
	mvn clean compile install deploy -Dmaven.test.skip=true	
 
####Running the samples
	运行单元测试事例
	com.extract.cache.test.*.java

####Importing into eclipse

	mvn eclipse:eclipse	

####功能描述
	目前实现了redis,memcache,ehcache Api的封装
	实现原理：基于spring BeanNameProxy实现
	org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator
	根据被代理的类名称中需要使用Cache的业务方式上使用注解完成.
	#Jedis事例
	@JedisCacheKey(key = "test1", expire = 1000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}
	@JedisCacheUpdate(key = "test1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}
	#MEMCache事例	
	@MemCacheKey(key = "test1", expire = 1000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}
	@MemCacheUpdate(key = "test1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}
	#EHCache事例
	@EHCacheKey(key = "test1", cacheName = "cacheName1", expire = 2000, params = "0,1.id")
	public MockBean getMockBean(int num, MockBean bean) {
		System.out.println("getMockBean(num,bean)...................." + counted.addAndGet(1));
		return new MockBean();
	}
	@EHCacheUpdate(keys = "test1", cacheNames = "cacheName1", params = "0,1.id")
	public void updateMockBean(int num, MockBean bean) {
	}
	
		
