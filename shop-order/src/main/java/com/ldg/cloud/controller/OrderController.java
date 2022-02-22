package com.ldg.cloud.controller;

import com.ldg.cloud.openfegin.ProductService;
import com.ldg.cloud.pojo.Order;
import com.ldg.cloud.pojo.Product;
import com.ldg.cloud.service.OrderServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@RestController
@Slf4j
public class OrderController {
    @Autowired
    private OrderServiceImpl orderService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ProductService productService;

    @GetMapping("/order/prod/{pid}")
    public Order order(@PathVariable("pid") Integer pid){
        log.info(">>>>>客户下单，这时候要调用商品微服务查询商品信息");
        Product product = restTemplate.getForObject("http://localhost:8081/product/" + pid, Product.class);
        if(product.getStock()<=0)
        return null;
        else{
            Order order=new Order();
            order.setOid(1L);
            order.setUid(1L);
            order.setUsername("梁登光");
            orderService.save(order);
            log.info("下单成功");
            return order;
        }
    }


    /**
     * 获取服务列表和自定义负载均衡
     * @param pid
     * @return
     */

    @GetMapping("/order/nacos/{pid}")
    public Order orderDiscover(@PathVariable("pid") Integer pid){
        log.info(">>>>>客户下单，这时候要调用商品微服务查询商品信息");

        //获取所有对于名称的服务列表
        List<ServiceInstance> instances = discoveryClient.getInstances("service-product");
        //自定义负载均衡
        int index=new Random().nextInt(instances.size());


        ServiceInstance service=instances.get(index);
        String host = service.getHost();
        int port = service.getPort();
        String serviceId = service.getServiceId();
        Map<String, String> metadata = service.getMetadata();
        String scheme = service.getScheme();

        log.info("获取到的url："+host+":"+port);
        Product product = restTemplate.getForObject("http://" + host + ":" + port + "/product/" + pid, Product.class);
        System.out.println(product);
        if(product.getStock()<=0)
            return null;
        else{
            Order order=new Order();
            //order.setOid(1L);
            order.setUid(1L);
            order.setUsername("梁登光");
            orderService.save(order);
            log.info("下单成功");
            log.info("nacos下单成功");
            return order;
        }
    }

    /**
     * 基于ribbon的负载均衡
     * ribbon的负载均衡有
     * 1.选择最小并发请求的
     * 2.随机
     * 3.轮询
     * 4.重试
     * 5.权重
     * 可在配置文件中修改
     * @param pid
     * @return
     */

    @GetMapping("/order/ribbon/{pid}")
    public Order orderRibbon(@PathVariable("pid") Integer pid){
        log.info(">>>>>客户下单，这时候要调用商品微服务查询商品信息");
        String name="service-product";
        Product product = restTemplate.getForObject("http://" +name+"/product/" + pid, Product.class);
        System.out.println(product);
        if(product.getStock()<=0)
            return null;
        else{
            Order order=new Order();
            //order.setOid(1L);
            order.setUid(1L);
            order.setUsername("梁登光");
            orderService.save(order);
            log.info("下单成功");
            log.info("nacos下单成功");
            return order;
        }
    }

    /**
     * 基于OpenFegin 的服务调用
     *
     * @param pid
     * @return
     */
    @GetMapping("/order/fegin/{pid}")
    public Order orderOpenFegin(@PathVariable("pid") Integer pid){
        log.info(">>>>>客户下单，这时候要调用商品微服务查询商品信息");
        log.info("openfegin调用成功");
        Product product = productService.findProductById(pid);
        System.out.println(product);
        if(product.getStock()<=0)
            return null;
        else{
            Order order=new Order();
            //order.setOid(1L);
            order.setUid(1L);
            order.setUsername("梁登光");
            orderService.save(order);
            log.info("下单成功");
            log.info("nacos下单成功");
            return order;
        }
    }







}
