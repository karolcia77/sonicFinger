package com.example.finger.controller.admin;

import com.example.finger.bean.FingerUser;
import com.example.finger.bean.RestResultModule;
import com.example.finger.bean.User;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.entity.MyWebSocket;
import com.example.finger.service.FingerUserService;
import com.example.finger.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/*
 * 后台-普通用户FingerUserController
 * paddy 2018/12/26
 * */
@Controller
@RequestMapping(value = "appJson/admin")
@Component("AdminFingerUserController")
public class FingerUserController {
    private  static Logger logger = LoggerFactory.getLogger(FingerUserController.class);
    @Resource
    FingerUserDao fingerUserDao;

    @Resource
    private FingerUserService fingerUserService;

    /**
     * 获取所以用户
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerUserAll")
    public List<FingerUser> getFingerUserAll(){
        List<FingerUser> fingerUsers = fingerUserDao.getAll();
        return fingerUsers;
    }


    /**
     * 获取单个对象
     * @param fId
     * @return
     */
    @ResponseBody
    @RequestMapping("/getFingerUserEdit")
    public RestResultModule getFingerUserEdit(
            @RequestParam(name = "fId",defaultValue = "0",required = true) long fId) {
        RestResultModule module = new RestResultModule();
        FingerUser user = null;
        if(fId > 0){
            user = fingerUserDao.findById(fId);
        }
        module.putData("user",user);
        return module;

    }

    /**
     * 添加,编辑统一
     */
    @ResponseBody
    @RequestMapping(value = "/fingerUserUpdate",method= RequestMethod.POST)
    public RestResultModule faqThreeUpdate(@RequestBody FingerUser user){
        RestResultModule module = new RestResultModule();
        Date d = new Date();
        if(null == user.getId()){
            user.setCreateDate(d);
        }
        user.setUpdateDate(d);
        user.setJobsUpdateDate(d);
        fingerUserDao.save(user);
        return module;
    }

    /**
     *  修改状态
     * @param fIds
     * @param status
     */
    @ResponseBody
    @RequestMapping(value = "/fingerUser/editStatus")
    public void editStatus(@RequestParam(name = "fIds",defaultValue = "",required = true) String fIds,
                           @RequestParam(name = "status",defaultValue = "0",required = true) long status) throws Exception{
        String [] fId = fIds.split("-");
        for (String id : fId) {
            fingerUserService.saveStatus(Long.parseLong(id),status);
        }
        MyWebSocket webSocket = new MyWebSocket();
        webSocket.sendInfo("ok");
    }


    /**
     *  删除
     * @param fIds
     */
    @ResponseBody
    @RequestMapping(value = "/fingerUser/delete")
    public void delete(@RequestParam(name = "fIds",defaultValue = "",required = true) String fIds){
        String [] fId = fIds.split("-");
        for (String id : fId) {
            fingerUserService.deleteById(Long.parseLong(id));
        }
    }

    /**
     *  模糊查询(name)
     * @param search
     */
    @ResponseBody
    @RequestMapping(value = "/fingerUser/getSearch")
    public List<FingerUser> getSearch(@RequestParam(name = "search",defaultValue = "",required = true) String search){
        List<FingerUser> fingerUsers = fingerUserDao.getSearch(search);
        return fingerUsers;
    }

}
