package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 派单历史
 */
@Entity
@Table(name="finger_log")
public class FingerLog {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "fl_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "fl_f_id",columnDefinition="long default 0")
    private Long fId = (long)0;  // 人ID

    @Column(name = "fl_no")
    private String no;   // 单号

    @Column(name = "fl_description")
    private String description;  // 描述


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fl_createdate")
    private Date createDate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fl_startdate")
    private Date startdate;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fl_enddate")
    private Date enddate;

    @Column(name = "fl_status",columnDefinition="long default 0")
    private Long status = (long)0;  // 状态（2待做,1完成，0未完成）

    @Transient
    private String fs_title;  // fs_title

    @Transient
    private String f_name;  // f_name

    public FingerLog(){}
    public FingerLog(long id, String f_name, String fl_no, String description, String fs_title, Date createDate){
        this.id = id;
        this.f_name = f_name;
        this.no = fl_no;
        this.description = description;
        this.fs_title = fs_title;
        this.createDate = createDate;
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

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public String getFs_title() {
        return fs_title;
    }

    public void setFs_title(String fs_title) {
        this.fs_title = fs_title;
    }

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }
}
