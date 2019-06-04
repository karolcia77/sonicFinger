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


        for (FingerRecording recording:fingerRecordings.getContent()) {
            if(null !=  recording.getSeconds()){
                recording.setSeconds(secToTime(Integer.parseInt(recording.getSeconds())));
            }
            if(null !=  recording.getLastseconds()){
                recording.setLastseconds(secToTime(Integer.parseInt(recording.getLastseconds())));
            }
        }
        module.putData("fingerRecordings",fingerRecordings.getContent());
        module.putData("PageCount",fingerRecordings.getTotalElements());

        return module;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if(time < 60){
                second = time % 60;
                timeStr =  unitFormat(second) + "秒";
            }else if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + "分" + unitFormat(second) + "秒";
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + "时" + unitFormat(minute) + "分" + unitFormat(second) + "秒";
            }
        }
        return timeStr;
    }
    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

}
