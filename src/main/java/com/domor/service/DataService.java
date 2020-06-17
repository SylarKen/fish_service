package com.domor.service;

import com.domor.dao.CommonDao;
import com.domor.dao.DataDao;
import com.domor.domain.DayData;
import com.domor.domain.DevBeat;
import com.domor.domain.Record;
import com.domor.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @desc: 数据处理保存服务层
 * @auther: liyuyang
 * @date: 2019/12/12
 **/
@Slf4j
@Service
public class DataService {

    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private DataDao dataDao;

    @Autowired
    private CommonDao commonDao;

    /**
     * 根据订阅主题获取设备编号
     * @param topic
     * @return
     */
    public String getDevCode(String topic){
        List<Map<String, Object>> devices = commonDao.getAllDevices();
        for (Map<String, Object> device : devices) {
            String devCode = MapUtils.getString(device, "collectorCode");
            String devTopic = MapUtils.getString(device, "collectorId");
            if (devTopic != null && devTopic.equals(topic)) {
                return devCode;
            }
        }
        return null;
    }

    public DayData getLastDayData(String devCode) {
        return dataDao.getLastDayData(devCode);
    }

    public DayData getLastClsData(String devCode) {
        return dataDao.getLastClsData(devCode);
    }

    /**
     * 处理实时数据，使用线程池异常调用
     * @param record 接收到的消息
     * @param lastDay 上一天的数据
     * @param lastCls 上一班次的数据
     */
    @Async("asyncServiceExecutor")
    public void handlerRealTimeDate(Record record, DayData lastDay, DayData lastCls){
//        String devCode = record.getDevCode();
//
//        // 1. 缓存设备最新数据，用于插入关系库
//        redisUtil.hset("data.record" , devCode, record, 300);
//
//        // 2. 缓存设备历史数据（1小时）,用于前台读取
//        Map<String, Object> temp = new HashMap<>();
//        temp.put("devCode", devCode);
//        temp.put("devStatus", record.getDs());
//        temp.put("devPower", record.getDp());
//        temp.put("devCurrent0", record.getDc0());
//        temp.put("devCurrent1", record.getDc1());
//        temp.put("devCurrent2", record.getDc2());
//        temp.put("devVoltage0", record.getDv0());
//        temp.put("devVoltage1", record.getDv1());
//        temp.put("devVoltage2", record.getDv2());
//        temp.put("devTime", record.getT());
//        redisUtil.lSet("data.history." + devCode, temp, 60 * 60);
//
//        // 2. 缓存设备实时信息,用于前台读取
//        temp.put("runTimeAcc", record.getRta());
//        temp.put("onTimeAcc", record.getOta());
//        temp.put("downTimeAcc", record.getSta());
//        temp.put("runTimeDay", record.getRta() - (lastDay == null ? 0 : lastDay.getRunTimeAcc()));
//        temp.put("onTimeDay", record.getOta() - (lastDay == null ? 0 : lastDay.getOnTimeAcc()));
//        temp.put("downTimeDay", record.getSta() - (lastDay == null ? 0 : lastDay.getDownTimeAcc()));
//        temp.put("runTimeCls", record.getRta() - (lastCls == null ? 0 : lastCls.getRunTimeAcc()));
//        temp.put("onTimeCls", record.getOta() - (lastCls == null ? 0 : lastCls.getOnTimeAcc()));
//        temp.put("downTimeCls", record.getSta() - (lastCls == null ? 0 : lastCls.getDownTimeAcc()));
//        // 缓存时限1分钟，即1分钟没接收到数据，删除该设备，认为设备离线
//        redisUtil.hset("data.temp" , devCode, temp, 60);
//
//        // 3. 针对特殊设备处理并告警
//        handlerDeviceBeat(record);
    }

    /**
     * 校验设备节拍数据，并预警
     * @param record
     */
    private void handlerDeviceBeat(Record record){
//        String nowBeats = record.getDb();
//        if (nowBeats != null && nowBeats.length()>0) {
//            long nowTime = record.getT().getTime();
//            String devCode = record.getDevCode();
//            List<Object> oldBeats = redisUtil.lGetAll("data.beat." + devCode);
//            // 如果上次节拍不为空，并且长度相同，则比较后处理
//            if (oldBeats != null && oldBeats.size() == nowBeats.length()) {
//                for (int i = 0; i < nowBeats.length(); i++) {
//                    int nowBeat = Integer.parseInt(String.valueOf(nowBeats.charAt(i)));
//
//                    DevBeat oldDevBeat = (DevBeat) oldBeats.get(i);
//                    int oldBeat = oldDevBeat.getBeat();
//                    long oldTime = oldDevBeat.getTime();
//
//                    // 如果节拍为1超过15分钟，插入告警消息表
//                    long interval = nowTime - oldTime;
//                    if (nowBeat == oldBeat && nowBeat == 1 && interval > 15 * 60 * 1000) {
//                        log.info(">>>>>>>>>>>>>>>>>>>>设备节拍超时告警："+devCode);
//                        int bi = i + 1;
//                        Map<String, Object> message = new HashMap<>();
//                        message.put("devCode", devCode);
//                        message.put("content", "第"+bi+"节点超过15分钟未移动，请检查！");
//                        dataDao.insertWarnMessage(message);
//                    }
//
//                    // 如果节拍变化，更新节拍的缓存数据
//                    if (nowBeat != oldBeat) {
//                        DevBeat devBeat = new DevBeat(nowBeat,nowTime);
//                        redisUtil.lUpdateIndex("data.beat." + devCode, i, devBeat);
//                    }
//                }
//            // 如果上次节拍为空，插入节拍数据到缓存
//            } else {
//                List<Object> list = new ArrayList<>();
//                for (int i = 0; i < nowBeats.length(); i++) {
//                    int nowBeat = Integer.parseInt(String.valueOf(nowBeats.charAt(i)));
//                    list.add(new DevBeat(nowBeat,nowTime));
//                }
//                redisUtil.lSetAll("data.beat." + devCode, list);
//            }
//        }
    }

    /**
     * 处理变位数据
     * 不能使用线程异步调用，否则影响插入顺序，并且更新报错
     * @param record
     */
    @Transactional
    public void handlerChangeStatusDate(Record record){
//        String devCode = record.getDevCode();
//        Map<String, Object> data = new HashMap<>();
//        data.put("devCode", devCode);
//        data.put("devStatus", record.getDs());
//        data.put("devStatusChange", record.getDsc());
//        data.put("devTime", record.getF());
//        dataDao.updateLastStatus(data);
//        dataDao.insertStatus(data);
    }

}
