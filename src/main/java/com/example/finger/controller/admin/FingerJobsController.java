package com.example.finger.controller.admin;

import com.example.finger.bean.FingerLog;
import com.example.finger.bean.FingerUser;
import com.example.finger.bean.RestResultModule;
import com.example.finger.dao.FingerLogDao;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.entity.MyWebSocket;
import com.example.finger.service.FingerJobsService;
import com.example.finger.service.FingerUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/*
 * 后台-普通用户派单AdminFingerJobsController
 * paddy 2018/12/31
 * */
@Controller
@RequestMapping(value = "appJson/admin")
@Component("AdminFingerJobsController")
public class FingerJobsController {
    private  static Logger logger = LoggerFactory.getLogger(FingerJobsController.class);

    @Resource
    FingerUserDao fingerUserDao;

    @Resource
    private FingerJobsService fingerJobsService;

    /**
     * 获取所以用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerJobsAll")
    public List<FingerUser> getFingerJobsAll(){
        List<FingerUser> fingerUsers = fingerUserDao.getFingerJobsAll();
        return fingerUsers;
    }

    /**
     *  修改状态 : off,等待
     * @param fId
     * @param fsId
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/jobsEditStatus")
    public void jobsEditStatus(@RequestParam(name = "fId",defaultValue = "0",required = true) long fId,
                           @RequestParam(name = "fsId",defaultValue = "0",required = true) long fsId)throws Exception {
        fingerJobsService.jobsEditStatus(fId,fsId);
        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");
    }

    /**
     *  派单
     */
    @Transactional
    @RequestMapping(value = "/addFingerLog")
    @ResponseBody
    public RestResultModule addFingerLog(@RequestBody FingerLog fingerLog)throws Exception {
        RestResultModule module = new RestResultModule();
        if(fingerLog.getfId() > 0){
            fingerJobsService.saveFingerLog(fingerLog,3);
            module.putData("id",fingerLog.getId());
        }

        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");
        return module;
    }

    /**
     *  修改状态
     */
    @ResponseBody
    @RequestMapping(value = "/get1")
    public void get1() throws Exception {
        System.out.println(1);
        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");

    }


}
