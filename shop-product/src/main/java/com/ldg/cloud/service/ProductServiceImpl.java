package com.ldg.cloud.service;

import com.ldg.cloud.dao.ProductDao;
import com.ldg.cloud.pojo.Product;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductDao productDao;

    @Override
    @GlobalTransactional//开启seata的全局事务控制
    public Product findById(Integer id) {
        return productDao.selectById(id);
    }
}
