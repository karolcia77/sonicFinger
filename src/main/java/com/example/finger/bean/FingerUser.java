package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 用户表
 */
@Entity
@Table(name="finger_user")
public class FingerUser {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "f_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "f_name")
    private String name;

    @Column(name = "f_finger")
    private String finger;

    @Column(name = "f_finger_txt")
    private String fingerTxt;

    @Column(name = "f_status",columnDefinition="long default 0")
    private Long status = (long)0;

    @Column(name = "f_fs_id",columnDefinition="long default 1")
    private Long fsId = (long)1;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "f_createdate")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "f_updatedate")
    private Date updateDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "f_jobs_updatedate")
    private Date jobsUpdateDate;

    @Transient
    private String fs_title;  // fsTitle

    @Transient
    private String fc_no;  // fc_no

    public FingerUser(){}
    public FingerUser(long id,String name,long status,Date updateDate){
        this.id = id;
        this.name = name;
        this.status = status;
        this.updateDate = updateDate;
    }
    public FingerUser(long id,String name,String fs_title,Date jobsUpdateDate){
        this.id = id;
        this.name = name;
        this.fs_title = fs_title;
        this.jobsUpdateDate = jobsUpdateDate;
    }
    public FingerUser(long id,String name,String fs_title,Date jobsUpdateDate,String no){
        this.id = id;
        this.name = name;
        this.fs_title = fs_title;
        this.jobsUpdateDate = jobsUpdateDate;
        this.fc_no = no;
    }
    public FingerUser(long id,String fingerTxt){
        this.id = id;
        this.fingerTxt = fingerTxt;
    }


    public String getFc_no() {
        return fc_no;
    }

    public void setFc_no(String fc_no) {
        this.fc_no = fc_no;
    }

    public String getFingerTxt() {
        return fingerTxt;
    }

    public void setFingerTxt(String fingerTxt) {
        this.fingerTxt = fingerTxt;
    }

    public Date getJobsUpdateDate() {
        return jobsUpdateDate;
    }

    public void setJobsUpdateDate(Date jobsUpdateDate) {
        this.jobsUpdateDate = jobsUpdateDate;
    }

    public String getFs_title() {
        return fs_title;
    }

    public void setFs_title(String fs_title) {
        this.fs_title = fs_title;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getFsId() {
        return fsId;
    }

    public void setFsId(Long fsId) {
        this.fsId = fsId;
    }

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

    public String getFinger() {
        return finger;
    }

    public void setFinger(String finger) {
        this.finger = finger;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
