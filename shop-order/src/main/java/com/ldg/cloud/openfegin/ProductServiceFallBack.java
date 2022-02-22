package com.ldg.cloud.openfegin;

import com.ldg.cloud.pojo.Product;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProductServiceFallBack implements ProductService{
    @Override
    public Product findProductById(int pid) {
        Product product = new Product();
        product.setPid(-1);
        return product;
    }
}
