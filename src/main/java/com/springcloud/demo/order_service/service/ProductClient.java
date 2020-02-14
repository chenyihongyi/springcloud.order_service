package com.springcloud.demo.order_service.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/14 9:22
 */

/**
 * 商品服务客户端
 */
@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/api/v1/product/find")
    String findById(@RequestParam(value = "id") int id);
}
