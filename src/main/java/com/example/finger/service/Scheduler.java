package com.example.finger.service;


import com.example.finger.controller.FingerZohoController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {
    @Resource
    private FingerZohoController fingerZohoController;


    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    //每隔2秒执行一次
    @Scheduled(fixedRate = 10000)
    public void testTasks() {
        try {
            System.out.println("定时任务（获取zoho数据）执行时间：" + dateFormat.format(new Date()));
            fingerZohoController.test();
        }catch (Exception e){
            System.out.println(e);
        }


    }

    //每天14：30执行
    @Scheduled(cron = "0 30 14 ? * *")
    public void testTasks1() {
        System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));
    }
}
