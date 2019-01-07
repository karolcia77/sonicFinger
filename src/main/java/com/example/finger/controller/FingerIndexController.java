package com.example.finger.controller;

import com.example.finger.bean.FingerUser;
import com.example.finger.bean.RestResultModule;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.service.FingerUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/*
 * FingerIndexController
 * paddy 2019/1/2
 * */
@Controller
@RequestMapping(value = "appJson")
@Component("AdminFingerIndexController")
public class FingerIndexController {
    private  static Logger logger = LoggerFactory.getLogger(FingerIndexController.class);
    @Resource
    FingerUserDao fingerUserDao;

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


}
