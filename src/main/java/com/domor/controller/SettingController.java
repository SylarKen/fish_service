package com.domor.controller;

import com.domor.domain.PubMessage;
import com.domor.domain.Result;
import com.domor.mqtt.MqServer;
import com.domor.service.QuartzService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @desc: 对外接口类，设置硬件参数
 * @auther: liyuyang
 * @date: 2019/12/14
 **/
@Slf4j
@RestController
public class SettingController {

    @Autowired
    private QuartzService quartzService;

    /**
     * 设置数据发送间隔
     *
     * @param interval 间隔（秒）
     * @return Result
     */
    @RequestMapping("/setSendInterval")
    public Result setSendInterval(int interval, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");   //用于ajax post跨域（*，最好指定确定的http等协议+ip+端口号）
        response.setCharacterEncoding("utf-8");
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>设置数据发送间隔");
            List<Map<String, Object>> devices = quartzService.getAllDevices();
            MqServer mqServer = new MqServer();
            for (Map<String, Object> device : devices) {
                String topic = MapUtils.getString(device, "topic");
                if (topic != null && !topic.isEmpty()) {
                    PubMessage pm = new PubMessage();
                    pm.setSetPublicTick(interval);
                    mqServer.publish(topic, pm);
                }
            }
            mqServer.stop();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @RequestMapping("/setYcThreshold")
    public Result setYcThreshold(String ICCID, String threshold_on, String threshold_off, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");   //用于ajax post跨域（*，最好指定确定的http等协议+ip+端口号）
        response.setCharacterEncoding("utf-8");
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>设置遥测 打开/关闭 临界点");
            Double threshold_on_d = Double.parseDouble(threshold_on);
            Double threshold_off_d = Double.parseDouble(threshold_off);
            MqServer mqServer = new MqServer();
            if (ICCID == null || ICCID.equals("")) {
                List<Map<String, Object>> devices = quartzService.getAllDevices();
                for (Map<String, Object> device : devices) {
                    String topic = MapUtils.getString(device, "collectorId");
                    if (topic != null && !topic.isEmpty()) {
                        PubMessage pm = new PubMessage();
                        pm.setYcOnCrit(threshold_on_d);
                        pm.setYcOffCrit(threshold_off_d);
                        mqServer.publish(topic, pm);
                    }
                }
            } else {
                PubMessage pm = new PubMessage();
                pm.setYcOnCrit(threshold_on_d);
                pm.setYcOffCrit(threshold_off_d);
                mqServer.publish(ICCID, pm);
            }
            mqServer.stop();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @RequestMapping("/setRunMode")
    public Result setRunMode(String ICCID, String mode, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");   //用于ajax post跨域（*，最好指定确定的http等协议+ip+端口号）
        response.setCharacterEncoding("utf-8");
        try {
            log.info(">>>>>>>>>>>>>>>>>>>>设置运行模式");
            List<Map<String, Object>> devices = quartzService.getAllDevices();
            MqServer mqServer = new MqServer();
            PubMessage pm = new PubMessage();
            pm.setRunMode(mode.equals("0") ? "0" : "a");
            mqServer.publish(ICCID, pm);
//            for (Map<String, Object> device : devices) {
//                String topic = MapUtils.getString(device, "collectorId");
//                if (topic != null && !topic.isEmpty()) {
//
//
//                }
//            }
            mqServer.stop();
            return Result.success();
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }

    @RequestMapping("/setOnOff")
    public Result setOnOff(String ICCID, String o, HttpServletResponse response) {
        response.addHeader("Access-Control-Allow-Origin", "*");   //用于ajax post跨域（*，最好指定确定的http等协议+ip+端口号）
        response.setCharacterEncoding("utf-8");
        try {
            if (o.equals("on") || o.equals("off")) {
                log.info(">>>>>>>>>>>>>>>>>>>>设置打开关闭");
                MqServer mqServer = new MqServer();
                PubMessage pm = new PubMessage();
                pm.setRunMode("0");
                pm.setYk(o);
                mqServer.publish(ICCID, pm);
                mqServer.stop();
                return Result.success();
            } else {
                log.info(">>>>>>>>>>>>>>>>>>>>设置打开关闭:指令错误");
                return Result.error();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error();
        }
    }
}
