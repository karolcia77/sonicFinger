package com.example.finger.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.util.Date;

public class ZohoAnalysis {
    private static final long serialVersionUID = 1L;


    private String name; // 用户

    private String openCount;   // open数量

    private String closedCount;     // closed数量/打卡数量

    private String ave;   // 平均时间数

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenCount() {
        return openCount;
    }

    public void setOpenCount(String openCount) {
        this.openCount = openCount;
    }

    public String getClosedCount() {
        return closedCount;
    }

    public void setClosedCount(String closedCount) {
        this.closedCount = closedCount;
    }

    public String getAve() {
        return ave;
    }

    public void setAve(String ave) {
        this.ave = ave;
    }
}
