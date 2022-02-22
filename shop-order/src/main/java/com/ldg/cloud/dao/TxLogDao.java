package com.ldg.cloud.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ldg.cloud.pojo.TxLog;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TxLogDao extends BaseMapper<TxLog> {
}
