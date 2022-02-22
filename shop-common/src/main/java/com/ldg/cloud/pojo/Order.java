package com.ldg.cloud.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName(value = "uorder")
public class Order implements Serializable {
    @TableId(type = IdType.AUTO)
    private Long oid;
    private Long uid;
    private String username;
}
