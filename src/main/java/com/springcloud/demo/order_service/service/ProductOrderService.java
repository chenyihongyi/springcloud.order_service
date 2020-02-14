package com.springcloud.demo.order_service.service;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/14 6:24
 */

import com.springcloud.demo.order_service.domain.ProductOrder;

/**
 * 订单业务类
 */
public interface ProductOrderService {

    /**
     * 下单接口
     * @param userId
     * @param productId
     * @return
     */
    ProductOrder save(int userId, int productId);

}
