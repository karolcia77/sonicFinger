package com.example.finger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="MyCaseRelation")
public class MyCaseRelation {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fcr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "f_name")
    private String name;  // 人name

    @Column(name = "fc_no")
    private String no;   // 单号

    @Column(name = "fc_description")
    private String description;  // 描述

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fc_createdate")
    private Date fcCreateDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fc_startdate")
    private Date fcStartdate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fc_enddate")
    private Date fcEnddate;

    @Column(name = "fc_status",columnDefinition="long default 0")
    private Long status = (long)0;  // 状态（2待做,1完成，0未完成）


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fcr_finger_startdate")
    private Date fcrStartdate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fcr_finger_enddate")
    private Date fcrEnddate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }


    public Date getFcCreateDate() {
        return fcCreateDate;
    }

    public void setFcCreateDate(Date fcCreateDate) {
        this.fcCreateDate = fcCreateDate;
    }

    public Date getFcStartdate() {
        return fcStartdate;
    }

    public void setFcStartdate(Date fcStartdate) {
        this.fcStartdate = fcStartdate;
    }

    public Date getFcEnddate() {
        return fcEnddate;
    }

    public void setFcEnddate(Date fcEnddate) {
        this.fcEnddate = fcEnddate;
    }

    public Date getFcrStartdate() {
        return fcrStartdate;
    }

    public void setFcrStartdate(Date fcrStartdate) {
        this.fcrStartdate = fcrStartdate;
    }

    public Date getFcrEnddate() {
        return fcrEnddate;
    }

    public void setFcrEnddate(Date fcrEnddate) {
        this.fcrEnddate = fcrEnddate;
    }
}
