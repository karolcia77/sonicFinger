package com.example.finger.controller;

import com.example.finger.bean.FingerStatus;
import com.example.finger.bean.FingerUser;
import com.example.finger.dao.FingerStatusDao;
import com.example.finger.dao.FingerUserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/*
 * FingerIndexController
 * paddy 2019/1/2
 * */
@Controller
@RequestMapping(value = "appJson")
@Component("FingerIndexController")
public class FingerIndexController {
    private  static Logger logger = LoggerFactory.getLogger(FingerIndexController.class);
    @Resource
    FingerUserDao fingerUserDao;
    @Resource
    FingerStatusDao fingerStatusDao;

    /**
     * 获取所以用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerIndex")
    public List<FingerUser> getFingerIndex(){
        List<FingerUser> fingerUsers = fingerUserDao.getAllStatusBy1();
        for (FingerUser u:fingerUsers) {
            FingerStatus status = fingerStatusDao.findAllById(u.getFsId());
            u.setFs_title(status.getTitle());
            System.out.println(u.getName()+","+u.getFs_title());
        }


        return fingerUsers;
    }


}
