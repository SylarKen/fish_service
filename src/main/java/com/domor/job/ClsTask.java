package com.domor.job;

import com.domor.service.QuartzService;
import com.domor.utils.CommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
* @Desc: 动态定时任务类(班次结束后执行任务)
* @Author: liyuyang
* @Date: 2019/12/14
**/
@Slf4j
//@Component
public class ClsTask implements SchedulingConfigurer {

    @Autowired
    private QuartzService service;

    private static String cron;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info(">>>>>>>>>>>>>>>>>>>>执行班次统计数据插入任务");
                    service.insertClsData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },
        new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext) {
                log.info(">>>>>>>>>>>>>>>>>>>>加载clsTask cron参数");
                List<String> etims = service.getClsEndTimes();
                cron = CommonUtil.getCron(etims);
                if ("".equals(cron) || cron == null) return null;
                CronTrigger trigger = new CronTrigger(cron);// 定时任务触发，可修改定时任务的执行周期
                Date nextExecDate = trigger.nextExecutionTime(triggerContext);
                return nextExecDate;
            }
        });
    }

}
