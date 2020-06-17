package com.domor.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.cache.annotation.Cacheable;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @desc: 插入mysql数据库 DAO接口
 * @auther: liyuyang
 * @date: 2019/12/13
 **/
@Mapper
public interface CommonDao {

    @Cacheable(value = "cache.deviceInfo")
    List<Map<String, Object>> getAllDevices();

    String getDayEndTime();

    List<String> getClsEndTimes();

    String getDayByDate(Date date);

}
