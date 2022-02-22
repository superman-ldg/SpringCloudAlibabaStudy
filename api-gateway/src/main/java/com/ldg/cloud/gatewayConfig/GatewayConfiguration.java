package com.ldg.cloud.gatewayConfig;

import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.SentinelGatewayFilter;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.GatewayCallbackManager;
import com.alibaba.csp.sentinel.adapter.gateway.sc.exception.SentinelGatewayBlockExceptionHandler;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
public class GatewayConfiguration {

    private final List<ViewResolver> viewResolvers;
    private final ServerCodecConfigurer serverCodecConfigurer;

    public GatewayConfiguration(List<ViewResolver> viewResolvers,ServerCodecConfigurer serverCodecConfigurer){
        this.viewResolvers=viewResolvers;
        this.serverCodecConfigurer=serverCodecConfigurer;
    }

    //=========================网关限流=================================

    //初始化一个限流过滤器
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public GlobalFilter sentinelGatewayFilter(){
        return new SentinelGatewayFilter();
    }

    //配置初始化的限流参数
    @PostConstruct
    public void initGatewayRules(){
        Set<GatewayFlowRule> rules=new HashSet<>();
        rules.add(new GatewayFlowRule("product_route") //路由id
                .setCount(1) //限流阈值
                .setIntervalSec(1) //统计时间窗口，单位是秒。默认是1秒
        );
        GatewayRuleManager.loadRules(rules);
    }

    //配置限流的异常处理
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SentinelGatewayBlockExceptionHandler sentinelGatewayBlockExceptionHandler(){
        return new SentinelGatewayBlockExceptionHandler(viewResolvers,serverCodecConfigurer);
    }

    //自定义限流异常页面
    @PostConstruct
    public void initBlockHandlers(){
        BlockRequestHandler blockRequestHandler = new BlockRequestHandler(){

            @Override
            public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
                Map map=new HashMap<>();
                map.put("Code",0);
                map.put("message","接口被限流了");

                return ServerResponse.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON_UTF8)
                        .body(BodyInserters.fromObject(map));
            }
        };
        GatewayCallbackManager.setBlockHandler(blockRequestHandler);
    }


    //=========================自定义API分组 (更细力度的限流)=================================

    //配置限流参数
    @PostConstruct
    public void initGatewayRules02(){
        Set<GatewayFlowRule> rules=new HashSet<>();
        rules.add(new GatewayFlowRule("product_api01") //路由id
                .setCount(1) //限流阈值
                .setIntervalSec(1) //统计时间窗口，单位是秒。默认是1秒
        );
        rules.add(new GatewayFlowRule("product_api02") //路由id
                .setCount(1) //限流阈值
                .setIntervalSec(1) //统计时间窗口，单位是秒。默认是1秒
        );
        GatewayRuleManager.loadRules(rules);
    }

    //自定义API分组
    @PostConstruct
    public  void initCustomizedApis(){
        Set<ApiDefinition> definitions=new HashSet<>();
        ApiDefinition api1=new ApiDefinition("product_api01")
                .setPredicateItems(new HashSet<ApiPredicateItem>());
        //-------自定义------
    }


}
