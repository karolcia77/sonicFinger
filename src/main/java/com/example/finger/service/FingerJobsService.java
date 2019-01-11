package com.example.finger.service;

import com.example.finger.bean.FingerCase;
import com.example.finger.bean.FingerCaseRelation;
import com.example.finger.bean.FingerLog;
import com.example.finger.dao.*;
import com.example.finger.entity.MyWebSocket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service("fingerJobsService")
public class FingerJobsService {

    @Resource
    FingerUserDao fingerUserDao;

    @Resource
    FingerLogDao fingerLogDao;

    @Resource
    FingerCaseDao fingerCaseDao;

    @Resource
    FingerCaseRelationDao fingerCaseRelationDao;

    @Transactional
    public void jobsEditStatus(long fId,long fsId){
        Date d = new Date();
        fingerUserDao.jobsEditStatus(fId,fsId,d);
    }

    @Transactional
    public void saveFingerCase(FingerCase fingerCase){
        fingerCaseDao.save(fingerCase);
    }

    @Transactional
    public void saveFingerLog(FingerLog fingerLog,long fsId,Date d){
        fingerLog.setFsId(fsId);
        fingerLogDao.save(fingerLog);
        fingerUserDao.jobsEditStatus(fingerLog.getfId(),fsId,d);
    }

    @Transactional
    public void saveFingerCaseRelation(FingerCaseRelation relation){
        fingerCaseRelationDao.save(relation);
    }

    /**
     * 按人ID修改case关联当做状态: 关
     * @param fId
     */
    @Transactional
    public void updateCaseRelationInd(long fId){
        fingerCaseRelationDao.updateCaseRelationInd(fId);
    }


    /**
     * 录入指纹 ,操作
     * @param fId 人ID
     * @param d 时间
     */
    @Transactional
    public void saveFingerLogFering(long fId,Date d) throws Exception{
        // 获取当前跟单case,人ID,case状态0
        FingerCaseRelation relation = fingerCaseRelationDao.getStatusAndFid(fId,0);
        if(null != relation){
            if(relation.getFingerInd() == 0){
                // 登记
                // 修改当前case指纹状态
                fingerCaseRelationDao.editFingerInd(1,relation.getId());
                // 修改人跟单状态:跟单
                fingerUserDao.jobsEditStatus(relation.getfId(),4,d);
            }else{
                // 结束登记
                // 修改当前case指纹状态
                fingerCaseRelationDao.editFingerInd(0,relation.getId());
                // 修改当前case,完成状态
                fingerCaseDao.editStatus(relation.getFcId());
                // 修改人跟单状态:等待
                fingerUserDao.jobsEditStatus(relation.getfId(),2,d);
            }


            MyWebSocket webSocket = new MyWebSocket();
            webSocket.sendInfo("ok");
        }
    }


}