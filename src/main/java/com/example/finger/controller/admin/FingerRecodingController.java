package com.example.finger.controller.admin;

import com.example.finger.bean.FingerRecording;
import com.example.finger.bean.RestResultModule;
import com.example.finger.dao.FingerRecordingDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/*
 * 后台-普通用户FingerRecodingController
 * paddy 2019/5/30
 * */
@Controller
@RequestMapping(value = "appJson/admin")
@Component("AdminFingerRecodingController")
public class FingerRecodingController {
    private  static Logger logger = LoggerFactory.getLogger(FingerRecodingController.class);

    @Resource
    private FingerRecordingDao fingerRecordingDao;

    /**
     * 获取所有打卡用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerRecordingPage")
    public RestResultModule getFingerRecordingAll(@RequestBody Map<String,Object> map){
        RestResultModule module = new RestResultModule();
        int CurrentPage = Integer.parseInt(map.get("CurrentPage").toString());
        int PageSize = Integer.parseInt(map.get("PageSize").toString());
//分页
        Pageable pageable = new PageRequest(CurrentPage-1,PageSize);
        Page<FingerRecording> fingerRecordings = null;
        fingerRecordings = fingerRecordingDao.fingByCreateDate(pageable);

        module.putData("fingerRecordings",fingerRecordings.getContent());
        module.putData("PageCount",fingerRecordings.getTotalElements());

        return module;
    }



}
