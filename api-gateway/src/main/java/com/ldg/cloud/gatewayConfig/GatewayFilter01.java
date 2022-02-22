package com.ldg.cloud.gatewayConfig;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

//自定义GatewayFilter
@Component
public class GatewayFilter01 implements org.springframework.cloud.gateway.filter.GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String uname = exchange.getRequest().getQueryParams().getFirst("uname");//每次进来后判断带不带uname这个key
        if(uname == null){
            exchange.getResponse().setStatusCode(HttpStatus.NOT_ACCEPTABLE);    //uname为null非法用户
            return exchange.getResponse().setComplete();
        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}

