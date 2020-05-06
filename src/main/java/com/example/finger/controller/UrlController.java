package com.example.finger.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
 * url跳转页面
 * paddy 2018/12/25
 * */
@Controller
@RequestMapping("appPage")
public class UrlController {

    @RequestMapping("/")
    public String test(){
        return "fingerIndex";
    }

    @RequestMapping("/ok")
    public String ok(){
        return "ok";
    }

    @RequestMapping("/fingerIndex")
    public String fingerIndex(){
        return "fingerIndex";
    }

    @RequestMapping("/fingerSampleIndex")
    public String fingerSampleIndex(){
        return "fingerSampleIndex";
    }



    @RequestMapping("/admin/login")
    public String login(){return "admin/login";}

    @RequestMapping("/admin/fingerUser")
    public String fingerUser(){
        return "admin/fingerUser";
    }

    @RequestMapping("/admin/fingerUserEdit")
    public String fingerUserEdit(){
        return "admin/fingerUserEdit";
    }

    @RequestMapping("/admin/fingerJobs")
    public String fingerJobs(){
        return "admin/fingerJobs";
    }

    @RequestMapping("/admin/fingerJobsYes")
    public String fingerJobsYes(){
        return "admin/fingerJobsYes";
    }

    @RequestMapping("/admin/fingerJobsNo")
    public String fingerJobsNo(){
        return "admin/fingerJobsNo";
    }

    @RequestMapping("/admin/fingerJobsLog")
    public String fingerJobsLog(){
        return "admin/fingerJobsLog";
    }

    @RequestMapping("/admin/fingerRecoding")
    public String fingerRecoding(){
        return "admin/fingerRecoding";
    }

    @RequestMapping("/admin/fingerZoho")
    public String fingerZoho(){
        return "admin/fingerZoho";
    }
}
