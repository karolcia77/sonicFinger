package com.example.finger.controller.admin;

import com.example.finger.bean.*;
import com.example.finger.dao.FingerRecordingDao;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.dao.ZohoDao;
import com.example.finger.dao.ZohoListDao;
import com.example.finger.entity.ZohoAnalysis;
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
    private FingerUserDao fingerUserDao;

    @Resource
    private FingerRecordingDao fingerRecordingDao;

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
    public RestResultModule getFingerRecordingAll(@RequestBody Map<String,Object> map) throws Exception{
        RestResultModule module = new RestResultModule();
        int CurrentPage = Integer.parseInt(map.get("CurrentPage").toString());
        int PageSize = Integer.parseInt(map.get("PageSize").toString());
        String startTime = map.get("startTime").toString();
        String endTime = map.get("endTime").toString();
        long fzlid = Long.parseLong(map.get("fzlid").toString());

        // 列表
        Pageable pageable = new PageRequest(CurrentPage-1,PageSize);
        Page<Zoho> zohos = null;
        zohos = zohoDao.fingZohoPage(fzlid,startTime,endTime,pageable);

        List<ZohoAnalysis> zohoAnalyses = new ArrayList<>(); // zoho
        List<ZohoAnalysis> fingerAnalyses = new ArrayList<>(); // 打卡
        ZohoAnalysis analysis = null;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        // 数据分析
        List<String> userLists = zohoDao.getUserLists(fzlid,startTime,endTime);
        // zoho
        String zoho_maxDate = zohoDao.getMaxDate(fzlid,startTime,endTime); // 最大时间
        String zoho_minDate = zohoDao.getMinDate(fzlid,startTime,endTime); // 最小时间
        List<Zoho> zohoList = zohoDao.getzohoList(fzlid,startTime,endTime);
        for (String s:userLists) {
            analysis = new ZohoAnalysis();
            analysis.setName(s);
            analysis.setOpenCount(zohoDao.getStatusCount("Open",s,fzlid,startTime,endTime));
            analysis.setClosedCount(zohoDao.getStatusCount("Closed",s,fzlid,startTime,endTime));
            // 平均Closed时间
            long diff = 0;
            for (Zoho z:zohoList) {
                if(z.getUser().equals(s) && z.getStatus().equals("Closed") && z.getEnddate() != null){
                    diff += getDateDiff(z.getEnddate(),z.getCreateDate());
                }
            }
            if(diff > 0){
                analysis.setAve(getDateDiff1(diff / Long.parseLong(analysis.getClosedCount())));
            }
            zohoAnalyses.add(analysis);
        }

        // 打卡
        long finger_maxDate = 0; // 最大时间
        long finger_minDate = System.currentTimeMillis(); // 最小时间
        String ids = "";
        for (String s : userLists) {
            analysis = new ZohoAnalysis();
            String id = fingerUserDao.getByZohonameID(s);
            if(null == id){
                continue;
            }
            List<Object[]>  recodings = zohoDao.getRecodings(id,zoho_minDate,zoho_maxDate);
            if(recodings.size() == 0){
                continue; // 没有打卡人
            }
            // 平均Closed时间
            long diff = 0;
            for (int i = 0; i < recodings.size(); i++) {
                Object[] lo = recodings.get(i);
                analysis.setName(lo[0].toString());
                Date date1 = simpleDateFormat.parse(lo[1].toString());
                Date date2 = simpleDateFormat.parse(lo[2].toString());
                diff += getDateDiff(date2,date1);
                if(date1.getTime() > finger_maxDate){
                    finger_maxDate = date1.getTime();
                }
                if(date1.getTime() < finger_minDate){
                    finger_minDate = date1.getTime();
                }

            }
            if(diff > 0){
                analysis.setAve(getDateDiff1(diff / recodings.size()));
            }
            analysis.setClosedCount(String.valueOf(recodings.size()));
            fingerAnalyses.add(analysis);
        }



        module.putData("zohos",zohos.getContent());
        module.putData("PageCount",zohos.getTotalElements());

        module.putData("zohoAnalyses",zohoAnalyses);
        module.putData("zoho_maxDate",zoho_maxDate);
        module.putData("zoho_minDate",zoho_minDate);

        module.putData("fingerAnalyses",fingerAnalyses);
        module.putData("finger_maxDate",simpleDateFormat.format(finger_maxDate));
        module.putData("finger_minDate",simpleDateFormat.format(finger_minDate));
        return module;
    }



    public static long getDateDiff(Date endDate, Date nowDate) {
        long diff = endDate.getTime() - nowDate.getTime();
        return diff;
    }

    public static String getDateDiff1(long diff) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        long nm = 1000 * 60;
        // 计算差多少天
        long day = diff / nd;
// 计算差多少小时
        long hour = diff % nd / nh;
// 计算差多少分钟
        long min = diff % nd % nh / nm;
        return day + "天" + hour + "小时" + min + "分钟";
    }



    public static void main(String args[]) throws Exception{
        Date endDate = new Date();
        System.out.println( endDate.getTime());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date1 = simpleDateFormat.parse("2019-06-14 14:53:00");
        Date date2 = simpleDateFormat.parse("2019-06-14 14:53:01");
        System.out.println( date1.getTime());
        System.out.println( date2.getTime());

    }


}
