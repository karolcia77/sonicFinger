package com.example.finger.dao;

import com.example.finger.bean.FingerCaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * 派单关系
 */
@Component(value = "fingerCaseRelationDao")
public interface FingerCaseRelationDao extends JpaRepository<FingerCaseRelation,Long> {

    @Query(value = "SELECT fcr_id,fcr_fc_id,fcr_f_id,fcr_createdate,fcr_ind,fcr_finger_startdate,fcr_finger_enddate" +
            " FROM finger_case_relation" +
            " INNER JOIN finger_case ON fc_id = fcr_fc_id" +
            " WHERE fc_status = :status " +
            " AND fcr_ind = 1" +
            " AND fcr_f_id = :fId",nativeQuery = true)
    FingerCaseRelation getStatusAndFid(@Param("fId")long fId, @Param("status")long status);


    /**
     * 指纹签到开始
     * @param d 时间
     * @param fcr_id
     */
    @Modifying
    @Query("update FingerCaseRelation f set f.fingerStartdate = :d  where f.id = :fcr_id")
    void editFingerStart(@Param("d")Date d,@Param("fcr_id")long fcr_id);

    /**
     * 指纹签到结束
     * @param d 时间
     * @param fcr_id
     */
    @Modifying
    @Query("update FingerCaseRelation f set f.ind = 0 , f.fingerEnddate = :d  where f.id = :fcr_id")
    void editFingerEnd(@Param("d")Date d,@Param("fcr_id")long fcr_id);

    @Query(value = "SELECT fcr_id,fcr_fc_id,fcr_f_id,fcr_ind,fcr_createdate,fc_no,fc_description,f_name\n" +
            "FROM finger_case_relation\n" +
            "INNER JOIN finger_case ON  fcr_fc_id = fc_id\n" +
            "INNER JOIN finger_user ON  fcr_f_id = f_id",nativeQuery = true)
    List<FingerCaseRelation> getJobsAllYes();

    @Modifying
    @Query("update FingerCaseRelation f set f.ind = 0  where f.fId = :fId")
    void updateCaseRelationInd(@Param("fId")long fId);


}
