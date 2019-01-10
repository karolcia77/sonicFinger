package com.example.finger.dao;

import com.example.finger.bean.FingerCase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 派单数据
 */
@Component(value = "fingerCaseDao")
public interface FingerCaseDao extends JpaRepository<FingerCase,Long> {

    @Modifying
    @Query("update FingerCase f set f.status = 1  where f.id = :id")
    void editStatus(@Param("id")long id);


    @Query(value = "SELECT f.* " +
            " FROM finger_case f" +
            " WHERE NOT EXISTS (SELECT fcr_fc_id FROM finger_case_relation WHERE fc_id = fcr_fc_id)"
            ,nativeQuery = true)
    List<FingerCase> getJobsAllNo();

}
