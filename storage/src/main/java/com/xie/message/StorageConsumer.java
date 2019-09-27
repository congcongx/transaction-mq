package com.xie.message;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author xiecong
 * @date 2019/9/27
 */
@Service
@RocketMQMessageListener(topic = "TX_TOPIC",consumerGroup = "wxConsumerGroup")
public class StorageConsumer implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
        System.out.println("消费:" +message);
    }
}
