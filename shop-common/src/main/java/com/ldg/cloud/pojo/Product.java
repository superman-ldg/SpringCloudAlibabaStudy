package com.ldg.cloud.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Product implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer pid;
    private String name;
    private Double price;
    private Integer stock;//库存
}
