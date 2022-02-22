package com.ldg.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldg.cloud.pojo.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
