package com.ldg.cloud.service;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ldg.cloud.dao.OrderDao;
import com.ldg.cloud.pojo.Order;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService{
    @Autowired
    private OrderDao orderDao;

    @Override
    public void save(Order order) {
        orderDao.insert(order);
    }

    @SentinelResource(value = "messageImpl")
    @GlobalTransactional//开启seata的全局事务控制
    public void message(){
        System.out.println("message===>>>>测试限流");
    }
}
