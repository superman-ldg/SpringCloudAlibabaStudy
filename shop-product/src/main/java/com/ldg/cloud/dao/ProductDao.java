package com.ldg.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldg.cloud.pojo.Product;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ProductDao extends BaseMapper<Product> {

}
