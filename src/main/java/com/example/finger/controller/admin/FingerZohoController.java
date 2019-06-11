package com.example.finger.controller.admin;

import com.example.finger.bean.FingerRecording;
import com.example.finger.bean.RestResultModule;
import com.example.finger.bean.Zoho;
import com.example.finger.bean.ZohoList;
import com.example.finger.dao.FingerRecordingDao;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.dao.ZohoDao;
import com.example.finger.dao.ZohoListDao;
import com.example.finger.service.ExcelUtilsService;
import com.example.finger.util.ExcelUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 后台-普通用户FingerZohoController
 * paddy 2019/6/11
 * */
@Controller
@RequestMapping(value = "appJson/admin")
@Component("AdminFingerZohoController")
public class FingerZohoController {
    private  static Logger logger = LoggerFactory.getLogger(FingerZohoController.class);


    @Resource
    private ZohoListDao zohoListDao;

    @Resource
    private ZohoDao zohoDao;

    @Resource
    private ExcelUtilsService excelUtilsService;

    /**
     * 获取全部zoho_list
     * @return
     */
    @ResponseBody
    @RequestMapping("/zoho/getZohos")
    public List<ZohoList> getFingerRecordingAll(){
        List<ZohoList> zohoLists = zohoListDao.findAll();
        return zohoLists;
    }


    /**
     * 上传excel
     * @return
     */
    @ResponseBody
    @RequestMapping("/zoho/upload")
    public RestResultModule getFingerRecordingAll(@RequestParam("file") MultipartFile file,
                                                  @RequestParam("title") String title)throws Exception {
        RestResultModule module = new RestResultModule();
        Date date = new Date();
        InputStream inputStream = file.getInputStream();
        List<List<Object>> list = excelUtilsService.getBankListByExcel(inputStream, file.getOriginalFilename());
        inputStream.close();
        ZohoList zohoList = null;
        if(list.size() > 5) {
            zohoList = new ZohoList();
            zohoList.setTitle(title);
            zohoList.setCreateDate(date);
            zohoListDao.save(zohoList);

            Zoho zoho = null;
            for (int i = 3; i < list.size(); i++) {
                if (i == list.size() - 2) {
                    break;
                }
                List<Object> lo = list.get(i);
                //TODO 随意发挥
                System.out.println(lo);
                zoho = new Zoho();
                // 关联的zoho list
                zoho.setFzlid(zohoList.getId());
                // 排序
                zoho.setOrd((long) i - 2);
                // 编号
                zoho.setNumber(lo.get(0).toString());
                // 邮件
                zoho.setMail(lo.get(1).toString());
                // 创建时间
                String fmt = "yyyy-MM-dd hh:mm a";
                SimpleDateFormat sdf = new SimpleDateFormat(fmt, Locale.ENGLISH);
                zoho.setCreateDate(sdf.parse(lo.get(2).toString()));
                // 主题
                zoho.setSubject(lo.get(3).toString());
                // user
                zoho.setUser(lo.get(4).toString());
                // 状态
                zoho.setStatus(lo.get(5).toString());
                // 结束时间
                String s = lo.get(6).toString();
                if (!s.equals("-")) {
                    zoho.setEnddate(sdf.parse(s));
                }
                zohoDao.save(zoho);
            }

        }
        module.putData("fzlid",zohoList.getId());
        return module;
    }

    /**
     * 获取Zoho信息
     * @return
     */
    @ResponseBody
    @RequestMapping("/zoho/getZohoPage")
    public RestResultModule getFingerRecordingAll(@RequestBody Map<String,Object> map){
        RestResultModule module = new RestResultModule();
        int CurrentPage = Integer.parseInt(map.get("CurrentPage").toString());
        int PageSize = Integer.parseInt(map.get("PageSize").toString());
        String startTime = map.get("startTime").toString();
        String endTime = map.get("endTime").toString();
        long fzlid = Long.parseLong(map.get("fzlid").toString());

        //分页
        Pageable pageable = new PageRequest(CurrentPage-1,PageSize);
        Page<Zoho> zohos = null;
        zohos = zohoDao.fingZohoPage(fzlid,startTime,endTime,pageable);

        module.putData("zohos",zohos.getContent());
        module.putData("PageCount",zohos.getTotalElements());

        return module;
    }

}
