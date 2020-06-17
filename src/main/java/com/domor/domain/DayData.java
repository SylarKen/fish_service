package com.domor.domain;

import lombok.Data;

import java.util.Date;

/**
 * @desc: 设备日数据或班次数据
 * @auther: liyuyang
 * @date: 2019/12/16
 **/
@Data
public class DayData {

    /**
     * 日期
     */
    private String date;

    /**
     * 班次
     */
    private String cls;

    /**
     * 设备编号
     */
    private String devCode;

    /**
     * 运行时间，单位：秒
     */
    private int runTime;

    /**
     * 设备上电时间，单位：秒
     */
    private int onTime;

    /**
     * 设备关闭时间，单位：秒
     */
    private int downTime;

    /**
     * 设备能耗
     */
    private float energy;

    /**
     * 运行累计时间，单位：秒
     */
    private int runTimeAcc;

    /**
     * 设备上电时间，单位：秒
     */
    private int onTimeAcc;

    /**
     * 设备关闭时间，单位：秒
     */
    private int downTimeAcc;

    /**
     * 设备累计能耗
     */
    private float energyAcc;

    /**
     * 设备时间
     */
    private Date devTime;

    /**
     * 插入时间
     */
    private Date createTime;

}
