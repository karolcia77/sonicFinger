package com.example.finger.controller.admin;

import com.example.finger.bean.User;
import com.example.finger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/*
 * 后台-管理员用户UserController
 * paddy 2018/12/25
 * */
@Controller
@RequestMapping(value = "appJson/admin")
@Component("AdminUserController")
public class UserController {
    private  static Logger logger = LoggerFactory.getLogger(UserController.class);

    @Resource
    private UserService userService;


    @ResponseBody
    @RequestMapping(value = "/userlogin",method= RequestMethod.POST)
    public boolean userlogin(HttpSession request, @RequestBody User user){
        System.out.println(user.getLoginId());
        System.out.println(user.getPassword());
        boolean falg  = userService.login(request,user.getLoginId(),user.getPassword());
        return falg;
    }

    @ResponseBody
    @RequestMapping(value = "/cleanlogin")
    public boolean cleanlogin(HttpSession session,HttpServletRequest request, HttpServletResponse response)throws Exception {
        System.out.println("cleanlogin");
        session.setAttribute("userSession",null);
        response.sendRedirect(request.getContextPath()+"/appPage/admin/login");
        return true;
    }


}
