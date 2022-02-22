package com.ldg.cloud.pojo;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "txlog")
public class TxLog {
    @TableId(value = "txlogid")
    private Integer txlogid;
    private String context;
    private Date date;

}
