package com.example.finger.service;

import com.example.finger.bean.FingerCase;
import com.example.finger.bean.FingerCaseRelation;
import com.example.finger.bean.FingerLog;
import com.example.finger.bean.FingerRecording;
import com.example.finger.dao.*;
import com.example.finger.entity.MyWebSocket;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

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

    @Resource
    private FingerRecordingDao fingerRecordingDao;

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
            if(null == relation.getFingerStartdate()){
                // 登记
                // 修改当前case指纹状态
                fingerCaseRelationDao.editFingerStart(d,relation.getId());
                // 修改人跟单状态:跟单
                fingerUserDao.jobsEditStatus(relation.getfId(),4,d);
            }else{
                // 结束登记
                // 修改当前case指纹状态
                fingerCaseRelationDao.editFingerEnd(d,relation.getId());
                // 修改当前case,完成状态
                fingerCaseDao.editStatus(relation.getFcId());
                // 修改人跟单状态:等待
                fingerUserDao.jobsEditStatus(relation.getfId(),2,d);
            }


            MyWebSocket webSocket = new MyWebSocket();
            webSocket.sendInfo("ok");
        }
    }

    /**
     * 计算秒数
     * @param sourcetime
     * @param currentime
     * @return
     */
    public static String getIntvalSecond(Date sourcetime ,Date currentime) {
        long l = currentime.getTime() - sourcetime.getTime();
        return String.valueOf((int) l / 1000);
    }

    /**
     * 打卡
     * @param fId 人ID
     * @param d 时间
     */
    @Transactional
    public void saveFingerRecording(long fId,Date d,Date josD) throws Exception{
        // 获取个人打卡结束时间为空的记录
        List<FingerRecording> recordings = fingerRecordingDao.getAllByEnddateByNull(fId);
        FingerRecording recording = null;
        int fsId = 4;  // 跟单
        if (recordings.size() > 0) {
            // 有 -> 取第一个
            recording = recordings.get(0);
            recording.setEnddate(d);
            // 获取秒数
            recording.setSeconds(getIntvalSecond(recording.getCreateDate(),recording.getEnddate()));
            fsId = 2; // 等待
        }else{
            // 否
            recording = new FingerRecording();
            recording.setFid(fId);
            recording.setCreateDate(d);
            // 获取上次等待时间和秒数
            recording.setLasttime(josD);
            recording.setLastseconds(getIntvalSecond(josD,d));
        }
        // add
        fingerRecordingDao.save(recording);
        // 修改人跟单状态:跟单
        fingerUserDao.jobsEditStatus(fId,fsId,d);
        System.out.println("打卡成功");
        // 日志
        FingerLog log = new FingerLog();
        log.setfId(fId);
        log.setFsId((long)fsId);
        log.setCreateDate(d);
        fingerLogDao.save(log);

        // 通知前台啦
        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");

    }

}
