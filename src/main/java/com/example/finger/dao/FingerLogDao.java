package com.example.finger.dao;

import com.example.finger.bean.FingerLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * 派单历史
 */
@Component(value = "fingerLogDao")
public interface FingerLogDao extends JpaRepository<FingerLog,Long> {


}
