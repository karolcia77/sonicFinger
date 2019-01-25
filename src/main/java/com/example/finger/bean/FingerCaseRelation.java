package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 派单关联表
 */
@Entity
@Table(name="finger_case_relation")
public class FingerCaseRelation {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fcr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "fcr_fc_id")
    private Long fcId;  // 单ID

    @Column(name = "fcr_f_id")
    private Long fId;  // 人ID

    @Column(name = "fcr_ind",columnDefinition="long default 0")
    private Long ind = (long)0;;  // 单状态:0不做,1正在做

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fcr_finger_startdate")
    private Date fingerStartdate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fcr_finger_enddate")
    private Date fingerEnddate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fcr_createdate")
    private Date createDate;

    @Transient
    @Column(name = "fs_title")
    private String fs_title;  // fsTitle

    @Transient
    @Column(name = "f_name")
    private String f_name;  // f_name


    public Date getFingerStartdate() {
        return fingerStartdate;
    }

    public void setFingerStartdate(Date fingerStartdate) {
        this.fingerStartdate = fingerStartdate;
    }

    public Date getFingerEnddate() {
        return fingerEnddate;
    }

    public void setFingerEnddate(Date fingerEnddate) {
        this.fingerEnddate = fingerEnddate;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getFs_title() {
        return fs_title;
    }

    public void setFs_title(String fs_title) {
        this.fs_title = fs_title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getfId() {
        return fId;
    }

    public void setfId(Long fId) {
        this.fId = fId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getFcId() {
        return fcId;
    }

    public void setFcId(Long fcId) {
        this.fcId = fcId;
    }

    public Long getInd() {
        return ind;
    }

    public void setInd(Long ind) {
        this.ind = ind;
    }
}
