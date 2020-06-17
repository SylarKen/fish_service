package com.domor.service;

import com.domor.dao.CommonDao;
import com.domor.dao.DataDao;
import com.domor.domain.Record;
import com.domor.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@EnableScheduling
public class QuartzService {

    @Autowired
    private DataDao dataDao;

    @Autowired
    private CommonDao commonDao;

    @Autowired
    private RedisUtil redisUtil;

    public List<Map<String, Object>> getAllDevices() {
        return commonDao.getAllDevices();
    }

    public String getDayEndTime() {
        return commonDao.getDayEndTime();
    }

    public List<String> getClsEndTimes() {
        return commonDao.getClsEndTimes();
    }

    @Async("asyncServiceExecutor")
    public void insertRecord(List<Record> records) {
        dataDao.insertRecord(records);
        dataDao.updateStatus(records);
    }

    public void insertDayData() {
        List<Map<String, Object>> datas = new ArrayList<>();
        Date date = new Date(System.currentTimeMillis() - 1000 * 60 * 10);
        String day = commonDao.getDayByDate(date);
        Map<Object, Object> temp = redisUtil.hmget("data.temp");
        for (Object key : temp.keySet()) {
            Map<String, Object> data = (Map) temp.get(key);
            data.put("date", day);
            datas.add(data);
        }
        if (datas != null && !datas.isEmpty()) {
            dataDao.insertLastDayData(datas);
        }
    }

    public void insertClsData() {
        List<Map<String, Object>> datas = new ArrayList<>();
        Map<Object, Object> temp = redisUtil.hmget("data.temp");
        for (Object key : temp.keySet()) {
            Map<String, Object> data = (Map) temp.get(key);
            datas.add(data);
        }
        if (datas != null && !datas.isEmpty()) {
            dataDao.insertLastClsData(datas);
        }
    }

}
