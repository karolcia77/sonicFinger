package com.example.finger.mapper;

import com.example.finger.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper{
    User login(@Param("loginId") String loginId, @Param("password") String password);
}
