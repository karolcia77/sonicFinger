package com.example.finger.service;

import com.example.finger.bean.User;
import com.example.finger.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Service("userService")
public class UserService {
    @Resource
    private UserMapper userMapper;

    public boolean login(HttpSession session, String loginId, String password) {
        User user = userMapper.login(loginId,password);
        if (user != null){
            session.setAttribute("userSession",user);
            return true;
        }else{
            return false;
        }

    }

}
