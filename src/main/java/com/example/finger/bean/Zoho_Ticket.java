package com.example.finger.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.Date;

/**
 * zoho - Ticket
 */
@Entity
@DynamicInsert
@Table(name="zoho_Ticket")
public class Zoho_Ticket {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)  //自增
    private Long id;

    @Column(name = "ticketID")
    private String ticketID;

    @Column(name = "ticketNumber")
    private String ticketNumber;

    @Column(name = "email")
    private String email;

    @Column(name = "subject")
    private String subject;

    @Column(name = "status")
    private String status;

    @Column(name = "statusType")
    private String statusType;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "createdTime")
    private Date createdTime;

    @Column(name = "webUrl")
    private String webUrl;

    @Column(name = "assignee_id")
    private String assignee_id;

    @Column(name = "assignee_firstName")
    private String assignee_firstName;

    @Column(name = "assignee_lastName")
    private String assignee_lastName;

    @Column(name = "assignee_email")
    private String assignee_email;


    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getAssignee_email() {
        return assignee_email;
    }

    public void setAssignee_email(String assignee_email) {
        this.assignee_email = assignee_email;
    }

    public String getAssignee_id() {
        return assignee_id;
    }

    public void setAssignee_id(String assignee_id) {
        this.assignee_id = assignee_id;
    }

    public String getAssignee_firstName() {
        return assignee_firstName;
    }

    public void setAssignee_firstName(String assignee_firstName) {
        this.assignee_firstName = assignee_firstName;
    }

    public String getAssignee_lastName() {
        return assignee_lastName;
    }

    public void setAssignee_lastName(String assignee_lastName) {
        this.assignee_lastName = assignee_lastName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusType() {
        return statusType;
    }

    public void setStatusType(String statusType) {
        this.statusType = statusType;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }
}
