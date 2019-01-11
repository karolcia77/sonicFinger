package com.example.finger.controller.admin;

import com.example.finger.bean.*;
import com.example.finger.dao.*;
import com.example.finger.entity.MyCaseRelation;
import com.example.finger.entity.MyFingerLog;
import com.example.finger.entity.MyWebSocket;
import com.example.finger.service.FingerJobsService;
import com.example.finger.service.FingerUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import zk.jni.JavaToBiokey;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    MyFingerLogDao myFingerLogDao;
    @Resource
    FingerCaseDao fingerCaseDao;
    @Resource
    private FingerJobsService fingerJobsService;

    @Resource
    private MyCaseRelationDao myCaseRelationDao;

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
    public RestResultModule addFingerLog(@RequestBody FingerCase fingerCase)throws Exception {
        RestResultModule module = new RestResultModule();
        Date d = new Date();
        fingerCase.setCreateDate(d);
        // 添加case
        fingerJobsService.saveFingerCase(fingerCase);
        if(fingerCase.getfId() > 0){ // 人id
            if(fingerCase.getId() > 0){
                //添加关系
                FingerCaseRelation relation = new FingerCaseRelation();
                relation.setFcId(fingerCase.getId());
                relation.setfId(fingerCase.getfId());
                relation.setCreateDate(d);
                relation.setInd((long)1);  // 值1 是正在做
                // 更新其他正在操作的
                fingerJobsService.updateCaseRelationInd(fingerCase.getfId());
                fingerJobsService.saveFingerCaseRelation(relation);
                fingerUserDao.jobsEditStatus(fingerCase.getfId(),3,d);
              /* // 添加历史
                FingerLog log = new FingerLog();
                log.setFcId(fingerCase.getId());
                log.setfId(fingerCase.getfId());
                log.setCreateDate(d);
                fingerJobsService.saveFingerLog(log,3,d);*/
            }
        }
        module.putData("id",fingerCase.getId());

        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");
        return module;
    }

    /**
     *  派单,人ID,单ID
     * @param fId
     * @param fcId
     */
    @Transactional
    @ResponseBody
    @RequestMapping(value = "/addCaseRelation")
    public RestResultModule addCaseRelation(@RequestParam(name = "fId",defaultValue = "0",required = true) long fId,
                               @RequestParam(name = "fcId",defaultValue = "0",required = true) long fcId)throws Exception {
        RestResultModule module = new RestResultModule();
        Date d = new Date();
        //添加关系
        FingerCaseRelation relation = new FingerCaseRelation();
        relation.setFcId(fcId);
        relation.setfId(fId);
        relation.setCreateDate(d);
        relation.setInd((long)1);  // 值1 是正在做
        // 更新其他正在操作的
        fingerJobsService.updateCaseRelationInd(fId);
        fingerJobsService.saveFingerCaseRelation(relation);
        fingerUserDao.jobsEditStatus(fId,3,d);

        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");
        return module;
    }



    /**
     * yes: 获取所有
     * @return
     */
    @ResponseBody
    @RequestMapping("/getJobsAllYes")
    public List<MyCaseRelation> getJobsAllYes(){
        List<MyCaseRelation> relation = myCaseRelationDao.getJobsAllYes();
        return relation;
    }

    /**
     * no: 获取所有
     * @return
     */
    @ResponseBody
    @RequestMapping("/getJobsAllNo")
    public RestResultModule getJobsAllNo(){
        RestResultModule module = new RestResultModule();
        List<FingerCase> fingerCases = fingerCaseDao.getJobsAllNo();
        List<FingerUser> fingerUsers = fingerUserDao.getAllStatus();
        module.putData("fingerCases",fingerCases);
        module.putData("fingerUsers",fingerUsers);
        return module;
    }

    /**
     * log: 获取所有
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerJobsLogAll")
    public List<MyFingerLog> getFingerJobsLogAll(){
        List<MyFingerLog> myFingerLogs = myFingerLogDao.getAll();
        return myFingerLogs;
    }


    /**
     * editfpComparison : 传指纹比较
     * @return
     */
    @ResponseBody
    @RequestMapping("/editfpComparison")
    public RestResultModule editfpComparison(@RequestBody FingerUser fingerUser) throws Exception{
        Date d = new Date();
        logger.info("------"+d+"指纹录入:"+fingerUser.getFingerTxt());
        RestResultModule module = new RestResultModule();
        List<FingerUser> fingerUsers = fingerUserDao.getAllfpComparison();
        for (FingerUser u :fingerUsers) {
            String rev = u.getFingerTxt();
            if(!rev.equals(" ")){
                String revNew = rev.replace("[","").replace("]","");
                if (JavaToBiokey.NativeToProcess(fingerUser.getFingerTxt(), revNew)){
                    logger.info("------"+d+"匹配指纹成功:"+u.getId());
                    fingerJobsService.saveFingerLogFering(u.getId(),d);
                    break;
                }
            }
        }
        module.putData("fingerUsers",fingerUsers);
        return module;
    }

    /**
     *  test:修改状态
     */
    @ResponseBody
    @RequestMapping(value = "/get1")
    public void get1() throws Exception {
        System.out.println("进来测试:");
    /*    MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");*/


        System.out.println(System.getProperty("java.library.path"));
        String reg = "1TVFTUjIyAAAEEhAFBQUHCc7QAAAcE2kAAAAAgyEgiBKiAacPlQABAIIdnAHcAH4PagBlEmYN6ACJAA0PLhKrAaoPSADAAZcdEQGWACsONgA8EqwMzQAYAEYPZBJMAIMOhAB3AJodXgGdALIPeAB5EmQPqQBZADAMNxKXAbMPRACKAMIdAABwAM4PYQAbEvkOoQA9Ab0PAhMXAFMOtQCeAfAdlwGIAM0PZABvEucMcgDfAEoPyBJqAUgOngDAAX0dcAEyANkPUAAnE/cOIwBXAH8PAxMHAFMMGQDiAMEdLAmp3xIhWOopGLPgtfpCBkfzxGWAgSogmv2fgkJ1qPtygM/6FgYnAf6ecoBWeeZzdz8K4i9neo/e2VeB9g7jj7PzfPgBCHvu5hdWAxMP/QzLAbaAsJMYEaa8IAtOidYD9w2j/FoCFgk3ia4jhmX6I8cHuYf3hcMQ8AG335Nj830/ERZyASuC6G9qhZJs8OoY8hMfC8filOfG4M5Wk4TO7EYDExab4gbnvZFXfbcHvvW69PsZcAGv37/XMgzfEcLuzLf4/cQJBBIDMVlMFw+e9VYh1l1f/06bOqskIQADRB4qBMUsBEicDQA4AFoBwIJDgiUARABXVXhw7cCVw4DDb0fAf9CGxRQAVACnosXSwkSEwsDCBosDEmABXpNcDsWHBHvCgWd4eAbFngRlx8HAwwUAHwAN7P7ZDAAOAoZ4+9FSbgMAGQWJwwgSYhNTwVyABYgZEswVj6lpwkl6ldSDocPBSQnF1R0dwf/9/f4kwQDJCIGOBAAbLviECxMNL6mZfsUFwcDSwcMRAO83WcPFgJONkMQLAKs4SNLDcMHClhbFp0r7/P3A+fjAm/767PzB/f/9wOMDBNtTgcMJAJmlU6XUyMLDDQELoEkyVMHB+10GAMtrOYHBDQAUazAFc4BuBQG8b3Cn3gDPYEf//vz9/D76+u38//7///w7/vns//v9wP3A3gEPb0j///9HwDhU++3+/ztE/0beAQiBSP/+PlP+Ov/7UsH8///AwDv/xewLARSXKXivhQ4SNp0peIOJzgEPvVIqV8AzDsUMvznDdcFsb8HdARSuXVVTPsDA7v867f9LBwAMyvWZaB4ADsta/0eQ/kkeABDZXP/COcFP0zMHAAveJwbAxNECAZ3fdP3QAQTsZUZM//5bBf/60j/BChAXC9N0emUGEZ8Icf6F/w0DAgtiPkr/yBEGC2ZL/8D+wzr+XRYRWVt9MgrUAiJ1RcH/ZAwQyynz63PD/8PAasIQED0XwMDCagjVDjU7n8GNBRChhXTEKgQR5kNtRcEQ511sRVJCABmGAQYTAccAWl8AxACmEoXUAAAZRZcAQFAAAAAAABbFAAQSAzIAAAAAxQBBUA==";
        String ver = "TVFTUjIyAAAEEhAFBQUHCc7QAAAcE2kAAAAAgyEgiBKiAacPlQABAIIdnAHcAH4PagBlEmYN6ACJAA0PLhKrAaoPSADAAZcdEQGWACsONgA8EqwMzQAYAEYPZBJMAIMOhAB3AJodXgGdALIPeAB5EmQPqQBZADAMNxKXAbMPRACKAMIdAABwAM4PYQAbEvkOoQA9Ab0PAhMXAFMOtQCeAfAdlwGIAM0PZABvEucMcgDfAEoPyBJqAUgOngDAAX0dcAEyANkPUAAnE/cOIwBXAH8PAxMHAFMMGQDiAMEdLAmp3xIhWOopGLPgtfpCBkfzxGWAgSogmv2fgkJ1qPtygM/6FgYnAf6ecoBWeeZzdz8K4i9neo/e2VeB9g7jj7PzfPgBCHvu5hdWAxMP/QzLAbaAsJMYEaa8IAtOidYD9w2j/FoCFgk3ia4jhmX6I8cHuYf3hcMQ8AG335Nj830/ERZyASuC6G9qhZJs8OoY8hMfC8filOfG4M5Wk4TO7EYDExab4gbnvZFXfbcHvvW69PsZcAGv37/XMgzfEcLuzLf4/cQJBBIDMVlMFw+e9VYh1l1f/06bOqskIQADRB4qBMUsBEicDQA4AFoBwIJDgiUARABXVXhw7cCVw4DDb0fAf9CGxRQAVACnosXSwkSEwsDCBosDEmABXpNcDsWHBHvCgWd4eAbFngRlx8HAwwUAHwAN7P7ZDAAOAoZ4+9FSbgMAGQWJwwgSYhNTwVyABYgZEswVj6lpwkl6ldSDocPBSQnF1R0dwf/9/f4kwQDJCIGOBAAbLviECxMNL6mZfsUFwcDSwcMRAO83WcPFgJONkMQLAKs4SNLDcMHClhbFp0r7/P3A+fjAm/767PzB/f/9wOMDBNtTgcMJAJmlU6XUyMLDDQELoEkyVMHB+10GAMtrOYHBDQAUazAFc4BuBQG8b3Cn3gDPYEf//vz9/D76+u38//7///w7/vns//v9wP3A3gEPb0j///9HwDhU++3+/ztE/0beAQiBSP/+PlP+Ov/7UsH8///AwDv/xewLARSXKXivhQ4SNp0peIOJzgEPvVIqV8AzDsUMvznDdcFsb8HdARSuXVVTPsDA7v867f9LBwAMyvWZaB4ADsta/0eQ/kkeABDZXP/COcFP0zMHAAveJwbAxNECAZ3fdP3QAQTsZUZM//5bBf/60j/BChAXC9N0emUGEZ8Icf6F/w0DAgtiPkr/yBEGC2ZL/8D+wzr+XRYRWVt9MgrUAiJ1RcH/ZAwQyynz63PD/8PAasIQED0XwMDCagjVDjU7n8GNBRChhXTEKgQR5kNtRcEQ511sRVJCABmGAQYTAccAWl8AxACmEoXUAAAZRZcAQFAAAAAAABbFAAQSAzIAAAAAxQBBUA==";
        String test="fali";
        if (JavaToBiokey.NativeToProcess(reg, ver)){
            test="success";
        }
        System.out.println(test);

    }

}
