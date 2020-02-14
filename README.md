1、常用的服务间调用方式讲解
	简介：讲解常用的服务间的调用方式

		RPC:
			远程过程调用，像调用本地服务(方法)一样调用服务器的服务
			支持同步、异步调用
			客户端和服务器之间建立TCP连接，可以一次建立一个，也可以多个调用复用一次链接

			PRC数据包小
				protobuf
				thrift
			rpc：编解码，序列化，链接，丢包，协议


		Rest(Http):
			http请求，支持多种协议和功能
			开发方便成本低

			http数据包大

			java开发：HttpClient，URLConnection
			
2、微服务调用方式之ribbon实战 订单调用商品服务
            	简介：实战电商项目 订单服务 调用商品服务获取商品信息
            		1、创建order_service项目
            		2、开发伪下单接口
            		3、使用ribbon. (类似httpClient,URLConnection) 
            
            			启动类增加注解
            			  @Bean
            			  @LoadBalanced
            			  public RestTemplate restTemplate() {
            			       return new RestTemplate();
            			  }
            		4、根据名称进行调用商品，获取商品详情
            		
3.	ribbon服务间调用负载均衡源码分析
  		1、完善下单接口
  		2、分析@LoadBalanced
  			1）首先从注册中心获取provider的列表
  			2）通过一定的策略选择其中一个节点
  			3）再返回给restTemplate调用
  			
4.调整默认负载均衡策略
自定义负载均衡策略：http://cloud.spring.io/spring-cloud-static/Finchley.RELEASE/single/spring-cloud.html#_customizing_the_ribbon_client_by_setting_properties

	在配置文件yml里面，自定义负载均衡策略
		#自定义负载均衡策略
		product-service:
		  ribbon:
		    NFLoadBalancerRuleClassName: com.netflix.loadbalancer.RandomRule

	策略选择：
		1、如果每个机器配置一样，则建议不修改策略 (推荐)
		2、如果部分机器配置强，则可以改为 WeightedResponseTimeRule
		
5.微服务调用方式之feign  订单调用商品服务
  	简介：改造电商项目 订单服务 调用商品服务获取商品信息
  		Feign： 伪RPC客户端(本质还是用http)
  		官方文档: https://cloud.spring.io/spring-cloud-openfeign/
  
  
  		1、使用feign步骤讲解（新旧版本依赖名称不一样）
  			加入依赖
  				 <dependency>
  			        <groupId>org.springframework.cloud</groupId>
  			        <artifactId>spring-cloud-starter-openfeign</artifactId>
  			    </dependency>
  			启动类增加@EnableFeignClients
  			增加一个接口 并@FeignClient(name="product-service")
  
  		2、注意点：
  			1、路径
  			2、Http方法必须对应
  			3、使用requestBody，应该使用@PostMapping
  			4、多个参数的时候，通过@RequestParam（"id") int id)方式调用
  			
 6.ribbon和feign两个的区别和选择
   			选择feign
   				默认集成了ribbon
   				写起来更加思路清晰和方便
   				采用注解方式进行配置，配置熔断等方式方便
   
   		2、超时配置
   			默认optons readtimeout是60，但是由于hystrix默认是1秒超时
   
   			#修改调用超时时间
   			feign:
   			  client:
   			    config:
   			      default:
   			        connectTimeout: 2000
   			        readTimeout: 2000
   
   			模拟接口响应慢，线程睡眠新的方式
   			  try {
   		            TimeUnit.SECONDS.sleep(1);
   		        } catch (InterruptedException e) {
   		            e.printStackTrace();
   		        }
   		        
7.分布式核心知识之熔断、降级
  	简介：系统负载过高，突发流量或者网络等各种异常情况介绍，常用的解决方案
  
  	1、熔断：
  		保险丝，熔断服务，为了防止整个系统故障，包含子和下游服务
  
  		下单服务 -》商品服务
  				-》用户服务 （出现异常-》熔断）
  
  	2、降级：
  		抛弃一些非核心的接口和数据
  
  		旅行箱的例子：只带核心的物品，抛弃非核心的，等有条件的时候再去携带这些物品
  
  		
  	3、熔断和降级互相交集
  		相同点：
  			1）从可用性和可靠性触发，为了防止系统崩溃
  			2）最终让用户体验到的是某些功能暂时不能用
  
  		不同点
  			1）服务熔断一般是下游服务故障导致的，而服务降级一般是从整体系统负荷考虑，由调用方控制
  
  2、Netflix开源组件断路器Hystrix介绍
  	简介：介绍Hystrix基础知识和使用场景
  	
  	文档地址：
  		https://github.com/Netflix/Hystrix
  		https://github.com/Netflix/Hystrix/wiki
  
  	1、什么是Hystrix？
  		1）hystrix对应的中文名字是“豪猪”
  		
  		2）hystrix	英[hɪst'rɪks] 美[hɪst'rɪks]
  
  	
  	2、为什么要用？
  		在一个分布式系统里，一个服务依赖多个服务，可能存在某个服务调用失败，
  		比如超时、异常等，如何能够保证在一个依赖出问题的情况下，不会导致整体服务失败，
  		通过Hystrix就可以解决
  
  		http://cloud.spring.io/spring-cloud-netflix/single/spring-cloud-netflix.html#_circuit_breaker_hystrix_clients
  
  	3、提供了熔断、隔离、Fallback、cache、监控等功能
  
  
  	4、熔断后怎么处理？
  		出现错误之后可以 fallback 错误的处理信息
  
  		兜底数据
  
  3、Feign结合Hystrix断路器开发
  	简介：讲解SpringCloud整合断路器的使用，用户服务异常情况
  
  	1、加入依赖
  	
  	注意：网上新旧版本问题，所以要以官网为主，不然部分注解会丢失
  	最新版本 2.0
  
          <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
          </dependency>
  
  
  	2、增加注解
  		启动类里面增加注解
  		@EnableCircuitBreaker
  
  		注解越来越多-》 SpringCloudApplication注解
  
  	3、API接口编码
  	  熔断-》降级
  
  		1）最外层api使用，好比异常处理（网络异常，参数或者内部调用问题）
  			api方法上增加 @HystrixCommand(fallbackMethod = "saveOrderFail")
  			
  			编写fallback方法实现，方法签名一定要和api方法签名一致（注意点！！！）
  
  
  
  		
  
  	补充： 修改maven仓库地址
  	pom.xml中修改
  
  	<repositories>
          <repository>
              <id>nexus-aliyun</id>
              <name>Nexus aliyun</name>
              <layout>default</layout>
              <url>http://maven.aliyun.com/nexus/content/groups/public</url>
              <snapshots>
                  <enabled>false</enabled>
              </snapshots>
              <releases>
                  <enabled>true</enabled>
              </releases>
          </repository>
      </repositories>
  
  4、Feign结合Hystrix断路器开发
  	简介：讲解SpringCloud整合断路器的使用，用户服务异常情况
  	1、feign结合Hystrix
  		
  		1）开启feign支持hystrix  (注意，一定要开启，旧版本默认支持，新版本默认关闭)
  			feign:
  			  hystrix:
  			    enabled: true
  
  		2）FeignClient(name="xxx", fallback=xxx.class ), class需要继承当前FeignClient的类
  		
8.熔断降级服务异常报警通知实战
  	简介：完善服务熔断处理，报警机制完善
  
  	1、加入redis依赖
  		<dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-data-redis</artifactId>
          </dependency>
      2、配置redis链接信息
        redis:
  	    database: 0
  	    host: 127.0.0.1
  	    port: 6379
  	    timeout: 2000
  
  	3、使用
  
          //监控报警
          String saveOrderKye = "save-order";
          String sendValue = redisTemplate.opsForValue().get(saveOrderKye);
          final String ip = request.getRemoteAddr();
          new Thread( ()->{
              if (StringUtils.isBlank(sendValue)) {
                  System.out.println("紧急短信，用户下单失败，请离开查找原因,ip地址是="+ip);
                  //发送一个http请求，调用短信服务 TODO
                  redisTemplate.opsForValue().set(saveOrderKye, "save-order-fail", 20, TimeUnit.SECONDS);
              }else{
                  System.out.println("已经发送过短信，20秒内不重复发送");
              }
          }).start();
  		
  
  
  
  
  
  6、Hystrix降级策略和调整
  	Hystrix降级策略和调整
  
  	1、查看默认讲解策略 HystrixCommandProperties
  		1）execution.isolation.strategy   隔离策略
  			THREAD 线程池隔离 （默认）
  			SEMAPHORE 信号量
  				信号量适用于接口并发量高的情况，如每秒数千次调用的情况，导致的线程开销过高，通常只适用于非网络调用，执行速度快
  
  		2）execution.isolation.thread.timeoutInMilliseconds  超时时间
  			默认 1000毫秒
  
  		3）execution.timeout.enabled 是否开启超时限制 （一定不要禁用）
  
  
  		4）execution.isolation.semaphore.maxConcurrentRequests 隔离策略为 信号量的时候，如果达到最大并发数时，后续请求会被拒绝，默认是10
  
  
  	官方文档:
  		https://github.com/Netflix/Hystrix/wiki/Configuration#execution.isolation.strategy
  
  	2、调整策略
  		超时时间调整
  
  	hystrix:
  	  command:
  	    default:
  	      execution:
  	        isolation:
  	          thread:
  	            timeoutInMilliseconds: 4000
  	            
7.断路器Dashboard监控仪表盘
  断路器Dashboard基础使用和查看
  	1、加入依赖
  		 <dependency>
              <groupId>org.springframework.cloud</groupId>
              <artifactId>spring-cloud-starter-netflix-hystrix-dashboard</artifactId>
          </dependency>
  
          <dependency>
              <groupId>org.springframework.boot</groupId>
              <artifactId>spring-boot-starter-actuator</artifactId>
          </dependency>
  
      2、启动类增加注解
      	@EnableHystrixDashboard
  
      	
      3、配置文件增加endpoint
  management:
    endpoints:
      web:
        exposure:
          include: "*"
  
  
      4、访问入口
      	http://localhost:8781/hystrix
  
      	Hystrix Dashboard输入： http://localhost:8781/actuator/hystrix.stream 
  
      
      参考资料
      	默认开启监控配置
      	https://docs.spring.io/spring-boot/docs/current-SNAPSHOT/reference/htmlsingle/#boot-features-security-actuator
  
          配置文件类：
      		spring-configuration-metadata.json
  
  
  
  
  
  
  
  8、断路器监控仪表参数和模拟熔断
断路器监控仪表盘参数和模拟熔断
  
  	1、sse  server-send-event推送到前端
  
  	资料：https://github.com/Netflix/Hystrix/wiki/Dashboard
  	
 