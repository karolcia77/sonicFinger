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
        return "index";
    }

    @RequestMapping("/fingerIndex")
    public String fingerIndex(){
        return "fingerIndex";
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

    @RequestMapping("/admin/forms")
    public String forms(){
        return "admin/forms";
    }
}
