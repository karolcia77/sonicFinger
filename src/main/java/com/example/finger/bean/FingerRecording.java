package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 打卡表
 */
@Entity
@Table(name="finger_recording")
public class FingerRecording {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fr_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fr_last_time")
    private Date lasttime;

    @Column(name = "fr_last_seconds")
    private String lastseconds;

    @Column(name = "fr_seconds")
    private String seconds;

    @Column(name = "fr_f_id",columnDefinition="long default 0")
    private Long fid = (long)0;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fr_createdate")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fr_enddate")
    private Date enddate;

    @Transient
    private String fname;  // 人名


    public FingerRecording(){}
    public FingerRecording(long id,Date lasttime,String lastseconds,Date createDate,Date enddate,String seconds,String fname){
        super();
        this.id = id;
        this.createDate = createDate;
        this.enddate = enddate;
        this.seconds = seconds;
        this.fname = fname;
        this.lasttime = lasttime;
        this.lastseconds = lastseconds;
    }


    public Date getLasttime() {
        return lasttime;
    }

    public void setLasttime(Date lasttime) {
        this.lasttime = lasttime;
    }

    public String getLastseconds() {
        return lastseconds;
    }

    public void setLastseconds(String lastseconds) {
        this.lastseconds = lastseconds;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeconds() {
        return seconds;
    }

    public void setSeconds(String seconds) {
        this.seconds = seconds;
    }

    public Long getFid() {
        return fid;
    }

    public void setFid(Long fid) {
        this.fid = fid;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }
}
