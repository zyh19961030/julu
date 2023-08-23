package com.qu.task;

import com.qu.modules.web.service.IQSingleDiseaseTakeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 *
 *
 * @author: jesse
 * @createDate: 2021/6/28
 */
@Component
@Slf4j
public class ScheduleTask {

    @Autowired
    private IQSingleDiseaseTakeService qSingleDiseaseTakeService;


    // 每天凌晨1点执行
//    @Scheduled(cron = "0 0 1 * * ?")
    // 每半小时执行一次
//    @Scheduled(cron = "0 30 * * * ?")
    // 每30s执行一次
//    @Scheduled(cron = "0/30 * * * * ?")
    // 每3分钟执行一次

    @Scheduled(cron = "0 0 1 * * ?")
    public void getdzbxx() {
        String uuid= UUID.randomUUID().toString();
        log.info("{}",uuid);
        log.info("ScheduleTask start 抓取getdzbxx视图数据：{}-----时间----->{}",uuid,new Date());
        qSingleDiseaseTakeService.getGetdzbxxInsertDrgs();
        log.info("ScheduleTask end 抓取getdzbxx视图数据：{}-----时间----->{}",uuid,new Date());
    }
}