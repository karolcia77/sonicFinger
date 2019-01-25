package com.example.finger.dao;

import com.example.finger.entity.MyCaseRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 派单关系
 */
@Component(value = "myCaseRelationDao")
public interface MyCaseRelationDao extends JpaRepository<MyCaseRelation,Long> {

    @Query(value = "SELECT fcr_id,f_name,fc_no,fc_description,fc_startdate,fc_enddate,fc_createdate,fc_status,fcr_finger_startdate,fcr_finger_enddate" +
            " FROM finger_case_relation" +
            " INNER JOIN finger_case ON fcr_fc_id = fc_id" +
            " INNER JOIN finger_user ON fcr_f_id = f_id" +
            " ORDER BY fc_createdate DESC",nativeQuery = true)
    List<MyCaseRelation> getJobsAllYes();


}
