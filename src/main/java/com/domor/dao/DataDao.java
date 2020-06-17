package com.domor.dao;

import com.domor.domain.DayData;
import com.domor.domain.Record;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;
import java.util.Map;

/**
 * @desc: 插入mysql数据库 DAO接口
 * @auther: liyuyang
 * @date: 2019/12/13
 **/
@Mapper
public interface DataDao {

    @Cacheable(value = "cache.lastDay")
    DayData getLastDayData(String devCode);

    @Cacheable(value = "cache.lastCls")
    DayData getLastClsData(String devCode);

    void insertStatus(Map<String, Object> map);

    void updateLastStatus(Map<String, Object> map);

    void updateStatus(List<Record> list);

    void insertRecord(List<Record> list);

    @CacheEvict(value = "cache.lastDay", allEntries = true)
    void insertLastDayData(List<Map<String, Object>> list);

    @CacheEvict(value = "cache.lastCls", allEntries = true)
    void insertLastClsData(List<Map<String, Object>> list);

    void insertWarnMessage(Map<String, Object> message);

}
