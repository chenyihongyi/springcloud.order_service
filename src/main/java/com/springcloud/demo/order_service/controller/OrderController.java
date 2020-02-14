package com.springcloud.demo.order_service.controller;

import com.springcloud.demo.order_service.service.ProductOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Elvis
 * @Description:
 * @Date: 2020/2/14 6:35
 */
@RestController
@RequestMapping("api/v1/order")
public class OrderController {

    @Autowired
    private ProductOrderService productOrderService;

    @RequestMapping("save")
    public Object save(@RequestParam("user_id") int userId, @RequestParam("product_id")int productId){
        return productOrderService.save(userId, productId);
    }

}
