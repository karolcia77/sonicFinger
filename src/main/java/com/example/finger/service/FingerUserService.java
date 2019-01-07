package com.example.finger.service;

import com.example.finger.dao.FingerUserDao;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Date;

@Service("fingerUserService")
public class FingerUserService {

    @Resource
    FingerUserDao fingerUserDao;


    @Transactional
    public void saveStatus(long fId,long status){
        fingerUserDao.editStatus(fId,status,new Date());
    }

    // delete,id
    @Modifying
    @Transactional
    public void deleteById(long id){
        fingerUserDao.deleteById(id);
    }

}
