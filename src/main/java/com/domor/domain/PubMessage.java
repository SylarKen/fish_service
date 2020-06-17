package com.domor.domain;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import reactor.util.annotation.Nullable;

import java.util.Date;

/**
 * @desc: 发布消息类
 * @auther: liyuyang
 * @date: 2019/12/19
 **/
@Data
public class PubMessage {

    /**
     * clear,清空各种累计时间
     */
    private String c;



    /**
     * time,设置设备时间
     */
    private String setRtc;

    /**
     * PublishTick,设置发送间隔，单位是秒，默认10秒
     */
    private Integer setPublicTick;

    /**
     * time,设置设备时间
     */
    @JSONField(format = "yyMMdd HHmmss")
    private Date t;

    /**
     * 设置“遥测打开”临界点
     */
    @Nullable
    private Double ycOnCrit;

    /**
     * 设置“遥测关闭”临界点
     */
    @Nullable
    private Double ycOffCrit;

    /**
     * 运行模式,“0”代表非自动模式,“a”代表自动模式
     */
    private String runMode;


    /**
     * 遥控动作,“on”打开,“off”关闭
     */
    private String yk;
}
