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
		
5.微服务调用方式之feign 实战 订单调用商品服务
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