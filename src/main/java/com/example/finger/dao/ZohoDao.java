package com.example.finger.dao;

import com.example.finger.bean.Zoho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

/**
 * zoho
 */
@Component(value = "zohoDao")
public interface ZohoDao extends JpaRepository<Zoho,Long> {

    @Query(value = "SELECT z.*"+
            " FROM finger_zoho z"+
            " WHERE 1=1" +
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"+
            " ORDER BY z.zoho_fzl_id,z.zoho_order"
            ,countQuery="SELECT COUNT(*)" +
            " FROM finger_zoho z"+
            " WHERE 1=1" +
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"+
            " ORDER BY z.zoho_fzl_id,z.zoho_order"
            , nativeQuery = true)
    Page<Zoho> fingZohoPage(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime, Pageable pageable);



}
