package com.ldg.cloud.openfegin;

import com.ldg.cloud.pojo.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "service-product",fallback = ProductServiceFallBack.class)  //声明调用者的名称
public interface ProductService {
    //Fegin+GetMapping 就是一个完整的请求接口

    @GetMapping("/product/{pid}")
    public Product findProductById(@PathVariable("pid") int pid);

}
