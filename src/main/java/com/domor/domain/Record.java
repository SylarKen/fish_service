package com.domor.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

import java.util.Date;

/**
 * @desc: 设备信息实体类
 * @auther: liyuyang
 * @date: 2019/12/12
 **/
@Data
public class Record {

    /**
     * 设备编号
     */
    private String ICCID;

    /**
     * db 增氧机工作状态,“0”代表关闭,“1”代表开机
     */
    private String db;

    /**
     * rm 运行模式,“0”代表非自动模式,“a”代表自动模式
     */
    private String rm;

    /**
     * on 打开临界点
     */
    private float on;

    /**
     * off 关闭临界点
     */
    private float off;

    /**
     * temp0 温度1 单位℃
     */
    private float tem0;
    /**
     * temp1 温度2 单位℃
     */
    private float tem1;
    /**
     * ph PH值
     */
    private float ph1;

    /**
     * o2 溶氧度
     */
    private float o2;

    /**
     * time，时间
     */
    @JSONField(format = "yyMMdd HHmmss")
    private Date t;
    /**
     * time，变位时间
     */
    @JSONField(format = "yyMMdd HHmmss")
    private Date f;

    /**
     * time，时间
     */
    private Date createTime;

}
