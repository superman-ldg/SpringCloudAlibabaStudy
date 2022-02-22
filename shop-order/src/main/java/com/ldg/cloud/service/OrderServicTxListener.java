package com.ldg.cloud.service;

import com.ldg.cloud.dao.TxLogDao;
import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.pojo.TxLog;
import org.apache.rocketmq.spring.annotation.RocketMQTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionListener;
import org.apache.rocketmq.spring.core.RocketMQLocalTransactionState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Service;

@RocketMQTransactionListener
public class OrderServicTxListener implements RocketMQLocalTransactionListener {
    @Autowired
    private TxLogDao txLogDao;
    @Autowired
    private OrderServiceTx orderServiceTx;

    //执行本地事务
    @Override
    public RocketMQLocalTransactionState executeLocalTransaction(Message message, Object o) {
        try {
            orderServiceTx.createOrder((Integer) message.getHeaders().get("txId"), (Order) o);
            System.out.println(">>>>>>>>>>>>>>>>>>>成功提交");
            return RocketMQLocalTransactionState.COMMIT;
        }catch (Exception e){
            System.out.println(">>>>>>>>>>>>>>>>>>>回滚");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }

    //消息回查
    @Override
    public RocketMQLocalTransactionState checkLocalTransaction(Message message) {
        TxLog txlog = txLogDao.selectById((Integer) message.getHeaders().get("txId"));
        if(txlog==null){
            System.out.println(">>>>>>>>>>>>>>>>>>>消息回查提交");
            return RocketMQLocalTransactionState.COMMIT;
        }else{
            System.out.println(">>>>>>>>>>>>>>>>>>>消息回查回滚");
            return RocketMQLocalTransactionState.ROLLBACK;
        }
    }
}
