package com.example.finger.dao;

import com.example.finger.bean.FingerLog;
import com.example.finger.bean.ZohoList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

/**
 * zoholist
 */
@Component(value = "zohoListDao")
public interface ZohoListDao extends JpaRepository<ZohoList,Long> {


}
