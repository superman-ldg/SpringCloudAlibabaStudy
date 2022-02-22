package com.ldg.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.ldg.cloud.openfegin.ProductService;
import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.pojo.Product;
import com.ldg.cloud.service.OrderServiceImpl;
import com.ldg.cloud.service.OrderServiceTx;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class OrderControllerMQ {

    @Autowired
    private OrderServiceImpl orderService;
    @Autowired
    private ProductService productService;

    @Autowired(required = true)
    private RocketMQTemplate rocketMQTemplate;

    @Autowired
    private OrderServiceTx orderServiceTx;

    @GetMapping("/order/mq/{pid}")
    public Order order(@PathVariable("pid") Integer id){
        log.info(">>>>>>>>>客户下单，查看商品信息");
        Product product = productService.findProductById(id);
        if(product==null){
            Order order = new Order();
            order.setUsername("下单失败");
            order.setUid(-1L);
            return order;
        }
        log.info(">>>>>>>>>商品查询结果："+ JSON.toJSONString(product));
        Order order = new Order();
        order.setUid(1L);
        order.setUsername("测试用户");
        orderService.save(order);
        rocketMQTemplate.convertAndSend("order-topic",order);
        return order;
    }

    @GetMapping("/order/tx")
    public String tx(){
        Order order = new Order();
        order.setUsername("测试事务");
        order.setUid(20182131106L);
        int txId = orderServiceTx.createOrderBefore(order);
        orderServiceTx.createOrder(txId,order);
        return "测试事务";
    }

    //=============================发送不同类型的消息========================
    //RocketMQ提供了三种方式来发送普通消息
    //1.可靠同步发送   2.可靠异步发送    3.单向发送
    //可靠同步发送：指的是发送方发送消息后，会在收到接收方回响应之后才发送下一个消息
    //可靠异步发送：异步发送多条消息，并对结果处理
    //单向发送：只发送消息，不等待回应

    //可靠同步
    public void sendSync(){
        SendResult sendResult = rocketMQTemplate.syncSend("", "");
    }

    //可靠异步
    public void  sendAsync(){

        rocketMQTemplate.asyncSend("", "", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {

            }

            @Override
            public void onException(Throwable throwable) {

            }
        });
    }

    //可靠单条
    public void sendOneWay(){
        rocketMQTemplate.sendOneWay("","");
    }


    //===========================顺序消息======================
    public void SyncSendOrderly(){
        rocketMQTemplate.syncSendOrderly("","","");
    }


    //========================事务消息===============================
    //RocketMQ提供事务消息，通过事务消息能够到达分布式事务的最终一致性
    //1.半事务消息：暂时不能投递消息，发送方已经成功地将消息发送到RocketMQ，但是服务端为收到生成者对该消息的二次确认
    //此时该消息被标志成暂不能投递状态，

    //2.消息回值:由于网络闪断，生成者应用重启的原因。导致某条事务消息的二次确认丢失。
    //RecketMQ 服务端通过扫描发现某条消息长期处于半事务消息时，需要主动向消息生成者询问消息的最终状态


    //=====================事务消息发送的步骤==========
    //1.发送方将半事务消息发送到RocketMQ
    //2.RocketMQ服务器将消息持久化之后，向发送方返回ACK确认消息已经发送成功，此时消息为半事务消息
    //3.发送方开始执行本地事务逻辑
    //4.发送方根据本地事务执行结果向服务器提交二次确认，服务器收到commit状态则将半事务消息标志为可投递。订阅方最终收到该消息
    //服务端收到Rollback状态则删除半事务消息，订阅方不再接收该消息


    //===============事务消息回查步骤
    //1.在断网或者应用重启的特殊情况下，上述步骤4可能没有进行二次确认，经过固定时间后服务端对该消息回差
    //2.发送方收到消息后，需要检查对于消息本地事务的执行结果
    //3.发送方根据检查结果进行二次确认。



}
