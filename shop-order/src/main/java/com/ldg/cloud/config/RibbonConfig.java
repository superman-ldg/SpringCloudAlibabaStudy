package com.ldg.cloud.config;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RibbonConfig {
    @Bean
    @LoadBalanced //基于Ribbon的负载均衡
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }

}
