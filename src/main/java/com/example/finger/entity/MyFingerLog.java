package com.example.finger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * 派单历史
 */
@Entity
@Table(name="MyFingerLog")
public class MyFingerLog {
    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "fl_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "f_name")
    private String f_name;
    @Column(name = "fc_no")
    private String fc_no;
    @Column(name = "fc_description")
    private String fc_description;
    @Column(name = "fc_status")
    private Long fc_status;
    @Column(name = "fs_title")
    private String fs_title;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "fl_createdate")
    private Date fl_createdate;

    public String getF_name() {
        return f_name;
    }

    public void setF_name(String f_name) {
        this.f_name = f_name;
    }

    public String getFc_no() {
        return fc_no;
    }

    public void setFc_no(String fc_no) {
        this.fc_no = fc_no;
    }

    public String getFc_description() {
        return fc_description;
    }

    public void setFc_description(String fc_description) {
        this.fc_description = fc_description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFc_status() {
        return fc_status;
    }

    public void setFc_status(Long fc_status) {
        this.fc_status = fc_status;
    }

    public String getFs_title() {
        return fs_title;
    }

    public void setFs_title(String fs_title) {
        this.fs_title = fs_title;
    }

    public Date getFl_createdate() {
        return fl_createdate;
    }

    public void setFl_createdate(Date fl_createdate) {
        this.fl_createdate = fl_createdate;
    }
}
