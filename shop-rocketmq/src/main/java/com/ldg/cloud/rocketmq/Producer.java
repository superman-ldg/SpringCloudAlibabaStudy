package com.ldg.cloud.rocketmq;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Producer {

    @Autowired
    private RocketMQTemplate rocketMQTemplate;
    //=============================发送不同类型的消息========================
    //RocketMQ提供了三种方式来发送普通消息
    //1.可靠同步发送   2.可靠异步发送    3.单向发送
    //可靠同步发送：指的是发送方发送消息后，会在收到接收方回响应之后才发送下一个消息
    //可靠异步发送：异步发送多条消息，并对结果处理
    //单向发送：只发送消息，不等待回应

    public void send(){


//        rocketMQTemplate.asyncSend();
//        rocketMQTemplate.syncSend();
//        rocketMQTemplate.syncSendOrderly();
//        rocketMQTemplate
    }

}
