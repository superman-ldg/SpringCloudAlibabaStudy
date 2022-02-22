package com.ldg.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.ldg.cloud.openfegin.ProductService;
import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.pojo.Product;
import com.ldg.cloud.service.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * 模拟高并发的场景
 */
@RestController
@Slf4j
public class OrderController02 {
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private ProductService productService;


    /**
     * 基于OpenFegin 的服务调用
     * 模拟网络拥堵
     *
     * @param pid
     * @return
     */
    @GetMapping("/order02/fegin/{pid}")
    public Order orderOpenFegin(@PathVariable("pid") Integer pid) throws InterruptedException {
        log.info(">>>>>客户下单，这时候要调用商品微服务查询商品信息");
        log.info("openfegin调用成功");
        Product product = productService.findProductById(pid);
        log.info(">>>>>查询到的商品信息{}",product);
       //模拟网络延时
        Thread.sleep(100);
        Order order=new Order();
        order.setOid(1L);
        order.setUid(1L);
        order.setUsername("梁登光");
        //orderService.save(order);
        log.info("下单成功");
        return order;
        }

        @RequestMapping("/order/message")
        @SentinelResource(value = "/order/message",blockHandler = "",fallback = "")
        public String message(){
        return "高并发下的问题测试";
        }

    /**
     * 容错方案  隔离 ，超时， 限流， 熔断， 降级
     *
     *隔离：按模块区分服务，依赖性不强，有（线程池隔离和信号量隔离）
     *超时：服务设置最大响应时间，超过最大响应时间就断开连接，释放线程
     *限流： 限制系统的输入和输出流量达到保护系统的目的，一般是达到阈值就开始限流
     * 熔断：切断下游服务的调用（熔断三状态：熔断关闭，熔断开启（直接返回fallbock），熔断半关闭(尝试恢复服务，并允许低流量进入，成功率高则恢复，否则关闭)）
     * 降级：无法恢复正常，就采用兜底的方案
     * 降级有：hystrix,sentinel
     *
     */

    @RequestMapping("/order/message01")
    @SentinelResource(value = "/order/message01",blockHandler = "",fallback = "")
    public String message01(){
        orderService.message();
        return "message01";
    }

    @RequestMapping("/order/message02")
    @SentinelResource(value = "/order/message02",blockHandler = "",fallback = "")
    public String message02(){
        orderService.message();
        return "message02";
    }





}
