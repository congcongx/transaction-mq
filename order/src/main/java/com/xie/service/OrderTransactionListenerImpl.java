package com.xie.service;

import com.xie.domain.Order;
import com.xie.domain.SendMsg;
import com.xie.mapper.SendMsgMapper;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author xiecong
 * @date 2019/9/27
 */
@RocketMQTransactionListener(txProducerGroup = "tx_group")
public class OrderTransactionListenerImpl implements RocketMQLocalTransactionListener {

    @Autowired
    private OrderService orderService;

    @Autowired
    private SendMsgMapper sendMsgMapper;

    private final static String rocketmq = "rocketmq_";

    private ConcurrentHashMap<String, Integer> localTrans = new ConcurrentHashMap<String, Integer>();

    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        String transId = (String)msg.getHeaders().get(rocketmq+RocketMQHeaders.TRANSACTION_ID);
        System.out.println("message: " + msg);
        System.out.printf("#### executeLocalTransaction is executed, msgTransactionId=%s %n",
                transId);
        Order order = (Order)arg;
        int status;
        try{
            orderService.create(order,transId);
            System.out.printf("    # COMMIT # Simulating msg %s related local transaction exec succeeded! ### %n", msg.getPayload());
            return RocketMQLocalTransactionState.COMMIT;
        } catch (Exception e) {
            status = 1;
            System.out.println("异常");
        }
        if (status == 1) {
            // Return local transaction with failure(rollback) , in this case,
            // this message will not be checked in checkLocalTransaction()
            System.out.printf("    # ROLLBACK # Simulating %s related local transaction exec failed! %n", msg.getPayload());
            return RocketMQLocalTransactionState.ROLLBACK;
        }
        System.out.printf("    # UNKNOW # Simulating %s related local transaction exec UNKNOWN! \n");
        return RocketMQLocalTransactionState.UNKNOWN;
    }

    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message msg) {
        String transId = (String)msg.getHeaders().get(rocketmq+RocketMQHeaders.TRANSACTION_ID);
        System.out.println("check transId: " + transId);
        System.out.println("执行回调检查");
        SendMsg sendMsg = sendMsgMapper.selectById(transId);
        if(sendMsg != null) {
            System.out.println("回查: COMMIT: " + transId);
            return RocketMQLocalTransactionState.COMMIT;
        }
        return RocketMQLocalTransactionState.UNKNOWN;
    }
}
