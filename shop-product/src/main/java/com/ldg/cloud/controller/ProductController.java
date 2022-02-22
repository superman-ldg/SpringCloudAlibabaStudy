package com.ldg.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.fastjson.JSON;
import com.ldg.cloud.pojo.Product;
import com.ldg.cloud.service.ProductServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ProductController {
    @Autowired
    private ProductServiceImpl productService;

    @GetMapping("/product/{pid}")
    @SentinelResource(value = "/product/{pid}")
    public Product product(@PathVariable("pid") Integer id){
        Product product = productService.findById(id);
        log.info("查询到商品："+ JSON.toJSONString(product));
        return product;
    }

}
