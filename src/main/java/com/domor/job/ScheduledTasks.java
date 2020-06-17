package com.domor.job;

import com.domor.domain.PubMessage;
import com.domor.domain.Record;
import com.domor.mqtt.MqServer;
import com.domor.service.QuartzService;
import com.domor.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc: 定时任务
 * @auther: liyuyang
 * @date: 2019/12/14
 **/
@Slf4j
//@Component
public class ScheduledTasks {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private QuartzService quartzService;

    /**
     * 设备实时记录定时插入关系库
     */
    @Scheduled(fixedRate = 30 * 1000, initialDelay = 10 * 1000)
    public void saveRealTimeRecord(){
        log.info(">>>>>>>>>>>>>>>>>>>>执行实时数据插入任务");
        List<Record> records = new ArrayList<>();
        List<Map<String, Object>> devices = quartzService.getAllDevices();
        for (Map<String, Object> device : devices) {
            String devCode = MapUtils.getString(device, "devCode");
            Record record = (Record)redisUtil.hget("data.record", devCode);
            if (!StringUtils.isEmpty(record)) {
                records.add(record);
            }
        }
        if (records.size() > 0) {
            quartzService.insertRecord(records);
        }
    }

    /**
     * 设备校时,每天1点运行
     */
    @Scheduled(cron="0 0 1 * * ?")
    public void setDeviceTime(){
        log.info(">>>>>>>>>>>>>>>>>>>>执行设备校时任务");
        List<Map<String, Object>> devices = quartzService.getAllDevices();
        MqServer mqServer = new MqServer();
        for (Map<String, Object> device : devices) {
            String topic = MapUtils.getString(device, "topic");
            if (topic != null && !topic.isEmpty()) {
                PubMessage pm = new PubMessage();
                pm.setT(new Date());
                mqServer.publish(topic, pm);
            }
        }
        mqServer.stop();
    }

}
