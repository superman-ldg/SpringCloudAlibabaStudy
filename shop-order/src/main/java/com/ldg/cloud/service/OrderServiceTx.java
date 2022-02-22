package com.ldg.cloud.service;

import com.ldg.cloud.dao.OrderDao;
import com.ldg.cloud.dao.TxLogDao;
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
    private OrderDao orderDao;
    @Autowired
    private TxLogDao txLogDao;
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
        //本地事务
        orderDao.insert(order);
        //记录日志到数据库，回查使用
        TxLog txLog = new TxLog();
        txLog.setTxlogid(txId);
        txLog.setContext("事务测试");
        txLog.setDate(new Date());
        txLogDao.insert(txLog);
    }

}
