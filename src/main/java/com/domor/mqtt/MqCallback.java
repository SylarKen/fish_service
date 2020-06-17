package com.domor.mqtt;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.domor.StartSpringBoot;
import com.domor.domain.DayData;
import com.domor.domain.Record;
import com.domor.service.DataService;
import com.domor.service.QuartzService;
import com.domor.utils.SpringUtils;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 发布或接受消息后的回调类
 * @Author: liyuyang
 * @Date: 2019/12/11
 **/
@Slf4j
@Component
public class MqCallback implements MqttCallback {

    private QuartzService quartzService= SpringUtils.getBean("quartzService");
    /**
     * 注入Service
     */
    private DataService service = SpringUtils.getBean("dataService");

    /**
     * 服务连接中断后回调
     *
     * @param throwable
     */
    @Override
    public void connectionLost(Throwable throwable) {
        StartSpringBoot.mqClient.stop();
        StartSpringBoot.mqClient.reConnect();
        log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:服务连接成功！");
        StartSpringBoot.mqClient.subscribe();
        log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub:订阅主题成功！");
    }

    /**
     * 消息发布成功后回调
     *
     * @param token
     */
    @Override
    public void deliveryComplete(IMqttDeliveryToken token) {
        log.info(">>>>>>>>>>>>>>>>>>>>mqtt-pub：消息发布成功回调" + token.isComplete());
    }

    /**
     * 订阅主题收到消息后回调
     *
     * @param topic
     * @param message 此方法不能抛出异常，否则mqtt服务会断开
     */
    @Override
    public void messageArrived(String topic, MqttMessage message) {
        String content = new String(message.getPayload());
        log.info(">>>>>>>>>>>>>>>>>>>>mqtt-sub: 接收到数据" + topic + ":" + content);
        // 数据处理保存
        try {
            String ts[] = topic.split("/");
            String msg_dev = ts[2];
            String msg_type = ts[1];
            String devCode = service.getDevCode(msg_dev);
            if (devCode != null && !devCode.isEmpty()) {
                if (msg_type.equals("status")) {
                } else if (msg_type.equals("data")) {
                    Record record = JSON.parseObject(content, Record.class);
                    List<Record> records = new ArrayList<>();
                    record.setICCID(msg_dev);
                    records.add(record);
                    quartzService.insertRecord(records);
                }
//                record.setDevCode(devCode);
//                record.setDp((record.getDc0()/1000)*record.getDv0()+(record.getDc1()/1000)*record.getDv1()+(record.getDc2()/1000)*record.getDv2());

//                DayData lastDay = service.getLastDayData(devCode);
//                DayData lastCls = service.getLastClsData(devCode);
//                // 处理实时数据, 放入缓存
//                service.handlerRealTimeDate(record, lastDay, lastCls);
//
//                // 如果是变位数据，则插入变位表
//                if (msg_type != null && msg_type.equals("status")) {
//                    service.handlerChangeStatusDate(record);
//                }
            } else {
                log.warn(">>>>>>>>>>>>>>>>>>>>mqtt-sub: 接收数据主题无对应设备，已忽略！" + topic);
            }
        } catch (JSONException e) {
            log.warn(">>>>>>>>>>>>>>>>>>>>mqtt-sub: 接收数据格式错误！" + topic + ":" + content);
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
