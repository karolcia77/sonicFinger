package com.example.finger.service;

import com.example.finger.bean.FingerLog;
import com.example.finger.dao.FingerLogDao;
import com.example.finger.dao.FingerUserDao;
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

    @Transactional
    public void jobsEditStatus(long fId,long fsId){
        fingerUserDao.jobsEditStatus(fId,fsId,new Date());
    }

    @Transactional
    public void saveFingerLog(FingerLog fingerLog,long fsId){
        Date d = new Date();
        fingerLog.setCreateDate(d);
        fingerLogDao.save(fingerLog);
        fingerUserDao.jobsEditStatus(fingerLog.getfId(),fsId,d);
    }


}
