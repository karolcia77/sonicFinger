package com.example.finger.service;

import com.example.finger.dao.FingerUserDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("excelUtilsService")
public class ExcelUtilsService {

    @Resource
    FingerUserDao fingerUserDao;




}
