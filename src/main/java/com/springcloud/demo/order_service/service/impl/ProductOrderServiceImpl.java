package com.springcloud.demo.order_service.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.springcloud.demo.order_service.domain.ProductOrder;
import com.springcloud.demo.order_service.service.ProductClient;
import com.springcloud.demo.order_service.service.ProductOrderService;
import com.springcloud.demo.order_service.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/14 6:26
 */
@Service
public class ProductOrderServiceImpl implements ProductOrderService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

/* @Autowired
 private RestTemplate restTemplate;*/

/*    @Autowired
    private LoadBalancerClient loadBalancer;*/

    @Autowired
    private ProductClient productClient;

    @Override
    public ProductOrder save(int userId,int productId) {

        if(userId == 1){
            return null;
        }

     //  Map<String, Object> productMap =  restTemplate.getForObject("http://product-service/api/v1/product/find?id="+productId, Map.class);

        //调用方式二
/*        ServiceInstance instance = loadBalancer.choose("product-service");
        String url = String.format("http://%s:%s/api/v1/product/find?id="+productId, instance.getHost(),instance.getPort());
        RestTemplate restTemplate = new RestTemplate();
        Map<String,Object> productMap = restTemplate.getForObject(url, Map.class);*/

       //调用方式三
     String response = productClient.findById(productId);

        //积分服务 TODO
        logger.info("service save order");
        JsonNode jsonNode = JsonUtils.str2JsonNode(response);

        ProductOrder productOrder = new ProductOrder();
        productOrder.setCreateTime(new Date());
        productOrder.setUserId(userId);
        productOrder.setTradeNo(UUID.randomUUID().toString());
        //productOrder.setProductName(productMap.get("name").toString());
        // productOrder.setPrice(Integer.parseInt(productMap.get("price").toString()));
        productOrder.setProductName(jsonNode.get("name").toString());
        productOrder.setPrice(Integer.parseInt(jsonNode.get("price").toString()));
        return productOrder;
    }
}
