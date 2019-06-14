package com.example.finger.dao;

import com.example.finger.bean.Zoho;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

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


    /**
     * 以下是zoho对比
     * @return
     */
    @Query(value = " SELECT z.* FROM finger_zoho z" +
            " WHERE 1=1" +
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    List<Zoho> getzohoList(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = " SELECT z.zoho_user FROM finger_zoho z" +
            " WHERE 1=1" +
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"+
            " GROUP BY z.zoho_user"
            , nativeQuery = true)
    List<String> getUserLists(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT COUNT(*) FROM finger_zoho z WHERE z.zoho_status = :status AND z.zoho_user = :name"+
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    String getStatusCount(@Param("status") String status,@Param("name") String name,@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT MAX(z.zoho_createdate) FROM finger_zoho z WHERE 1=1"+
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    String getMaxDate(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT MIN(z.zoho_createdate) FROM finger_zoho z WHERE 1=1"+
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    String getMinDate(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT u.f_name,r.fr_createdate,r.fr_enddate FROM finger_recording r,finger_user u"+
            " WHERE r.fr_f_id = u.f_id AND r.fr_enddate IS NOT NULL"+
            " AND u.f_id = :f_id"+
            " AND if(:startTime != '',r.fr_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',r.fr_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    List<Object[]>  getRecodings(@Param("f_id")String f_id,@Param("startTime") String startTime, @Param("endTime") String endTime);

    @Query(value = "SELECT MAX(z.zoho_createdate) FROM finger_zoho z WHERE 1=1"+
            " AND if(:fzlid > 0,z.zoho_fzl_id = :fzlid,1=1)"+
            " AND if(:startTime != '',z.zoho_createdate > :startTime,1=1)"+
            " AND if(:endTime != '',z.zoho_createdate <= :endTime,1=1)"
            , nativeQuery = true)
    String getFingerMaxDate(@Param("fzlid") long fzlid,@Param("startTime") String startTime, @Param("endTime") String endTime);



}
