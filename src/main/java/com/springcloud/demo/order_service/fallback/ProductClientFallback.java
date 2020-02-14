package com.springcloud.demo.order_service.fallback;

import com.springcloud.demo.order_service.service.ProductClient;
import org.springframework.stereotype.Component;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/15 1:12
 */

/**
 *针对商品服务，错降级处理
 */
@Component
public class ProductClientFallback implements ProductClient {

    @Override
    public String findById(int id) {
        System.out.println("feign 调用product-service finbyid 异常");
        return null;
    }
}
