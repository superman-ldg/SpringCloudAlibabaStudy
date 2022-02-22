package com.ldg.cloud.tx;

import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.pojo.TxLog;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Random;

@Service
public class OrderServiceTx {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    public int createOrderBefore(Order order){
        Integer txId = new Random().nextInt(100);
        rocketMQTemplate.sendMessageInTransaction(
                "order-topic",
                MessageBuilder.withPayload(order)
                        .setHeader("txId",txId).build(),
                order
        );
        return txId;
    }

    @Transactional
    public void createOrder(Integer txId,Order order){


    }

}
