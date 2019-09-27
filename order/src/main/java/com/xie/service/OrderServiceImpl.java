package com.xie.service;

import com.alibaba.fastjson.JSONObject;
import com.xie.domain.Order;
import com.xie.domain.SendMsg;
import com.xie.mapper.OrderMapper;
import com.xie.mapper.SendMsgMapper;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xiecong
 * @date 2019/9/27
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private SendMsgMapper sendMsgMapper;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    @Override
    @Transactional
    public void create(Order order,String messageId) {
        SendMsg sendMsg  = new SendMsg();
        sendMsg.setMessageId(messageId);
        sendMsgMapper.insert(sendMsg);
        orderMapper.insert(order);

    }

    @Override
    public void sendStorageAndCreate(Order order) {
        Map<String,Object> map = new HashMap<>();
        map.put("productId",1);
        map.put("residue",20);
        Message msg = MessageBuilder.withPayload(JSONObject.toJSONString(map))
                .setHeader(RocketMQHeaders.TRANSACTION_ID, "product_1").build();
        SendResult sendResult = rocketMQTemplate.sendMessageInTransaction("tx_group","TX_TOPIC",msg,order);
        System.out.println(sendResult);
    }
}
