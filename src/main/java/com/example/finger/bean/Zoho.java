package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

/**
 * zoho
 */
@Entity
@Table(name="finger_zoho")
public class Zoho {
    @Id
    @Column(name = "zoho_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "zoho_fzl_id",columnDefinition="long default 0")
    private Long fzlid = (long)0;

    @Column(name = "zoho_order",columnDefinition="long default 0")
    private Long ord = (long)0;

    @Column(name = "zoho_number")
    private String number;

    @Column(name = "zoho_mail")
    private String mail;

    @Column(name = "zoho_subject")
    private String subject;

    @Column(name = "zoho_user")
    private String user;

    @Column(name = "zoho_status")
    private String status;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "zoho_createdate")
    private Date createDate;


    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "zoho_enddate")
    private Date enddate;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getFzlid() {
        return fzlid;
    }

    public void setFzlid(Long fzlid) {
        this.fzlid = fzlid;
    }

    public Long getOrd() {
        return ord;
    }

    public void setOrd(Long ord) {
        this.ord = ord;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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
