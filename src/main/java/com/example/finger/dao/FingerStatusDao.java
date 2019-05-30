package com.example.finger.dao;

import com.example.finger.bean.FingerStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * 状态类;
 */
@Component(value = "fingerStatusDao")
public interface FingerStatusDao extends JpaRepository<FingerStatus,Long> {

    FingerStatus findAllById(long id);

}
