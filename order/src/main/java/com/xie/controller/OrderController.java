package com.xie.controller;

import com.xie.domain.Order;
import com.xie.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author xiecong
 * @date 2019/9/27
 */
@RestController
@RequestMapping("order")
public class OrderController {

    @Autowired
    private OrderService orderServiceImpl;

    /**
     * 创建订单
     * @param order
     * @return
     */
    @GetMapping("create")
    public String create(Order order){
        try {
            orderServiceImpl.sendStorageAndCreate(order);
        } catch (Exception e) {
            return "error";
        }
        return "Create order success";
    }
}
