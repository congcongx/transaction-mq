package com.xie.service;

import com.xie.domain.Order;

/**
 * @author xiecong
 * @date 2019/9/27
 */
public interface OrderService {

    void create(Order order,String messageId);

    void sendStorageAndCreate(Order order);
}
