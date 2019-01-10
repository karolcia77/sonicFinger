package com.example.finger.dao;

import com.example.finger.entity.MyFingerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 派单历史
 */
@Component(value = "myFingerLogDao")
public interface MyFingerLogDao extends JpaRepository<MyFingerLog,Long> {

    @Query(value = "SELECT" +
            " fl_id," +
            " f_name," +
            " fc_no," +
            " fc_description," +
            " IFNULL(fc_status,-1) as fc_status," +
            " fl_createdate," +
            " fs_title" +
            " FROM finger_log" +
            " LEFT JOIN finger_case ON fc_id = fl_fc_id" +
            " INNER JOIN finger_user ON f_id = fl_f_id" +
            " INNER JOIN finger_status ON fs_id = fl_fs_id"+
            " ORDER BY fl_createdate DESC"
            ,nativeQuery = true)
    List<MyFingerLog> getAll();


}
