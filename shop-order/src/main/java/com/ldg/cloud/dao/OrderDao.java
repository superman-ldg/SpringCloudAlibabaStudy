package com.ldg.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldg.cloud.pojo.Order;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderDao extends BaseMapper<Order> {
}
