package com.example.finger.controller.admin;

import com.example.finger.bean.FingerRecording;
import com.example.finger.bean.FingerUser;
import com.example.finger.bean.RestResultModule;
import com.example.finger.dao.FingerRecordingDao;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Resource
    private FingerUserDao fingerUserDao;

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
        String startTime = map.get("startTime").toString();
        String endTime = map.get("endTime").toString();
        //分页
        Pageable pageable = new PageRequest(CurrentPage-1,PageSize);
        Page<FingerRecording> fingerRecordings = null;
        fingerRecordings = fingerRecordingDao.fingByCreateDatePage(startTime,endTime,pageable);

        for (FingerRecording recording:fingerRecordings.getContent()) {
            recording.setFname(fingerUserDao.getUserNameById(recording.getFid()));
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

    /**
     * 报表
     * @param response
     * @param start
     * @param end
     */
    @RequestMapping("/getReportRecodings")
    public void getReportRecodings(HttpServletResponse response,
                                               @RequestParam(value="startTime",defaultValue = "") String start,
                                               @RequestParam(value="endTime",defaultValue = "") String end){
        List<FingerRecording> recodings = fingerRecordingDao.getReportRecodings(start,end);
        try {
            Date now = new Date( );
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy.MM.dd hh:mm:ss");
            // 告诉浏览器用什么软件可以打开此文件
            response.setHeader("content-Type", "application/vnd.ms-excel");
            // 下载文件的默认名称
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode("IT-Recoding"+ ft.format(now) + ".xls", "utf-8"));
            OutputStream out = response.getOutputStream();
            String[] headers = { "ID", "名称" ,"上次结束时间","上次等待分钟","创建时间","结束时间","用时"};
            ExcelUtils eeu = new ExcelUtils();
            HSSFWorkbook workbook = new HSSFWorkbook();
            List<List<Object>> data = new ArrayList<List<Object>>();
            for (FingerRecording r:recodings) {
                List rowData = new ArrayList();
                rowData.add(r.getId());
                rowData.add(fingerUserDao.getUserNameById(r.getFid()));
                rowData.add(r.getLasttime());
                if(null !=  r.getLastseconds()){
                    rowData.add(secToTime(Integer.parseInt(r.getLastseconds())));
                }else{
                    rowData.add("");
                }
                rowData.add(r.getCreateDate());
                rowData.add(r.getEnddate());
                if(null !=  r.getSeconds()){
                    rowData.add(secToTime(Integer.parseInt(r.getSeconds())));
                }else{
                    rowData.add("");
                }
                data.add(rowData);
            }
            eeu.exportExcel(workbook, 0, "sheet1", headers, data, out);
            //原理就是将所有的数据一起写入，然后再关闭输入流。
            workbook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
