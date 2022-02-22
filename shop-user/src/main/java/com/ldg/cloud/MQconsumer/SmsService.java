package com.ldg.cloud.MQconsumer;

import com.alibaba.fastjson.JSON;
import com.ldg.cloud.pojo.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RocketMQMessageListener(
        consumerGroup = "shop-user",
        topic = "order-topic",
        consumeMode = ConsumeMode.CONCURRENTLY,
        messageModel= MessageModel.CLUSTERING
)
public class SmsService implements RocketMQListener<Order> {
    @Override
    public void onMessage(Order order) {
        log.info("接收到一条订单消息{}"+ JSON.toJSONString(order));
    }
}
