package com.example.finger.dao;

import com.example.finger.bean.FingerCaseRelation;
import com.example.finger.bean.FingerRecording;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 打卡
 */
@Component(value = "fingerRecordingDao")
public interface FingerRecordingDao extends JpaRepository<FingerRecording,Long> {

    @Query(value = "SELECT r.*" +
            " FROM finger_recording r" +
            " WHERE fr_f_id = :fId" +
            " AND fr_enddate IS NULL" +
            " ORDER BY fr_createdate" ,nativeQuery = true)
    List<FingerRecording> getAllByEnddateByNull(@Param("fId")long fId);

    @Query(value = "SELECT new FingerRecording(r.id,r.lasttime,r.lastseconds,r.createDate,r.enddate,r.seconds,u.name) " +
            " FROM FingerRecording r,FingerUser u" +
            " WHERE r.fid = u.id" +
            " ORDER BY r.createDate desc"
            ,countQuery="SELECT COUNT(r)" +
            " FROM FingerRecording r,FingerUser u" +
            " WHERE r.fid = u.id" +
            " ORDER BY r.createDate desc")
    Page<FingerRecording> fingByCreateDate(Pageable pageable);
}
