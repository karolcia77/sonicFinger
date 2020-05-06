package com.example.finger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finger.bean.*;
import com.example.finger.dao.*;
import com.example.finger.entity.ZohoAnalysis;
import com.example.finger.service.ExcelUtilsService;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * 普通用户FingerZohoController
 * paddy 2019/12/10
 * */
@Controller
@RequestMapping(value = "appJson")
@Component("FingerZohoController")
public class FingerZohoController {
    private static Logger logger = LoggerFactory.getLogger(FingerZohoController.class);
    @Resource
    private FingerUserDao fingerUserDao;
    @Resource
    private Zoho_TicketDao zoho_ticketDao;


    /**
     * 获取全部zoho_list
     *
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/zoho/getZohos")
    public String test() throws Exception {
        // contacts,assignee,departments,team,isRead
        String uri = "https://desk.zoho.com/api/v1/tickets?include=assignee&limit=99";
        //  模拟请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        headers.set("Authorization", "c750ba9df2f935ec4c0e7abd23169875");
        headers.set("orgId", "48175427");
        HttpEntity<String> entity = new HttpEntity<String>(headers);
        RestTemplate restTemplate = new RestTemplate();
        String strbody = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
        System.out.println(strbody);

        JSONObject jsonobject = JSON.parseObject(strbody);
        JSONArray json = jsonobject.getJSONArray("data");
        System.out.println("json数量=" + json.size());
        for (int i = 0; i < json.size(); i++) {
            JSONObject j = json.getJSONObject(i);
            Zoho_Ticket ticket = new Zoho_Ticket();
            ticket.setTicketID(j.getString("id"));
            ticket.setTicketNumber(j.getString("ticketNumber"));
            ticket.setEmail(j.getString("email"));
            ticket.setSubject(j.getString("subject"));
            ticket.setStatus(j.getString("status"));
            ticket.setStatusType(j.getString("statusType"));
            ticket.setCreatedTime(getW3cTimeConvertString2Date(j.getString("createdTime")));
            ticket.setWebUrl(j.getString("webUrl"));
            JSONObject js = j.getJSONObject("assignee");
            if (js != null) {
                ticket.setAssignee_id(js.getString("id"));
                ticket.setAssignee_firstName(js.getString("firstName"));
                ticket.setAssignee_lastName(js.getString("lastName"));
                ticket.setAssignee_email(js.getString("email"));
            }
            if (zoho_ticketDao.chekTickeinfo(ticket.getTicketID()) > 0) {
                zoho_ticketDao.editTicket(ticket);
            } else {
                zoho_ticketDao.save(ticket);
            }
        }
        return "OK";
    }

    public static Date getW3cTimeConvertString2Date(String date) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINESE);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date parse = format.parse(date);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // return sdf.format(parse);
        return parse;
    }


    /**
     * 获取全部zoho_list
     *
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/conversion")
    public String conversion(@RequestParam(name = "title", defaultValue = "", required = true) String title) throws Exception {
        String zh = ZhConverterUtil.convertToSimple(title);
        String hk = ZhConverterUtil.convertToTraditional(title);
        String result = "";
        result += "简体：" + zh;
        result += "<br>";
        result += "繁体：" + hk;
        System.out.println(zh);
        return result;
    }


    /**
     *  zoho
     *  oauth
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/zoho/oauth")
    public RestResultModule oauth(@RequestParam(name = "code", defaultValue = "", required = false) String code,
                                  @RequestParam(name = "access_token", defaultValue = "", required = false) String access_token,
                                  @RequestParam(name = "refresh_token", defaultValue = "", required = false) String refresh_token){
        RestResultModule module = new RestResultModule();
        System.out.println("收到/zoho/oauth-----------------------------");
        System.out.println("code:"+code);
        System.out.println("access_token:"+access_token);
        System.out.println("refresh_token:"+refresh_token);
        return module;
    }

    /**
     *  zoho
     *  更新：access_token
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/zoho/oauth/getaccess_token")
    public RestResultModule getaccess_token(@RequestBody(required = false) String json,
                                  @RequestParam(name = "access_token", defaultValue = "", required = false) String access_token){
        RestResultModule module = new RestResultModule();
        System.out.println("收到/zoho/getaccess_token-----------------------------");
        System.out.println("json:"+json);
        System.out.println("access_token:"+access_token);
        return module;
    }


    public static void main(String args[]) throws Exception{
      /*  String url = "https://accounts.zoho.com/oauth/v2/token";
        url += "?refresh_token=1000.b3148a31297801c67ec2ea0f74029cf3.5529f93e84a0f626cdc35b6fd5d38640";
        url += "&client_id=1000.AXXI2WZ4VE9TRG3CHUBE9VLANVSDRH";
        url += "&client_secret=8e5e427f0ceaad05c2ede20e2056c01ee70b7cabb9";
        url += "&scope=Desk.tickets.ALL,Desk.tasks.ALL,Desk.settings.ALL,Desk.basic.READ,Desk.basic.CREATE,Desk.events.ALL";
        url += "&redirect_uri=https://e2398957.ngrok.io/finger/appJson/zoho/oauth";
        url += "&grant_type=refresh_token";

        //  模拟请求
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
       *//* headers.set("Authorization", "c750ba9df2f935ec4c0e7abd23169875");
        headers.set("orgId", "48175427");*//*
        HttpEntity<String> entity = new HttpEntity<String>(headers);

        RestTemplate restTemplate = new RestTemplate();
        String strbody = restTemplate.exchange(url, HttpMethod.POST, entity, String.class).getBody();
        System.out.println(strbody);*/

        String s = "HI Paddy，\n \t\t\n \t\t目前正在更加你的问题。 <blockquote style=\\\"border-left: 2.0px solid blue;padding-left: 3.0px;\\\"> <meta /><style>div.zm_1181483996715872233_parse_4320222674990126279 { line-height: 1.5 }\\r\\ndiv.zm_1181483996715872233_parse_4320222674990126279 p { margin-top: 0px; margin-bottom: 0px }\\r\\ndiv.zm_1181483996715872233_parse_4320222674990126279 { font-size: 10.5pt; font-family: &quot;Microsoft YaHei UI&quot;; color: rgb(0, 0, 0); line-height: 1.5 }</style><div class=\\\" zm_1181483996715872233_parse_4320222674990126279\\\">\\n<div><span></span>test 3</div>\\n<div><br /></div><hr style=\\\"width: 210.0px;min-height: 1.0px;\\\" color=\\\"#b5c4df\\\" align=\\\"left\\\" />\\n<div><span><div style=\\\"margin: 10.0px;\\\"><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><a name=\\\"OLE_LINK1\\\" rel=\\\"noreferrer\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Thanks &amp; Regards</span></a></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Paddy pong</span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\"></span><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Senior Software Engineer&nbsp;</span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Sonic Teleservices Co. Ltd.</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Mob:&nbsp;</span><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">+86 13286960470</span><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">&nbsp;&nbsp;</span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Tel: 194</span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 0.0cm 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">Fax:&nbsp;&nbsp;</span><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">+86 2066826300</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~<br />The information in this email is confidential.</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">If you are not the intended recipient, you should not read, use or disseminate or otherwise divulge that information.</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">If you have received this email in error, please notify the sender and delete it immediately from your system.</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">This email transmission and the attachment cannot be guaranteed to be secure or error free as information could be intercepted, corrupted, lost, destroyed, arrived late, incomplete or contain viruses.</span><span style=\\\"font-family: Calibri, sans-serif;\\\"></span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\">We and the sender therefore accept no liability for any error or omission in the content of this message which may arise as a result of email transmission.<br />~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~<br />&quot;SAVE PAPER - THINK BEFORE YOU PRINT!&quot;</span></p><p class=\\\"MsoNormal\\\" style=\\\"font-family: verdana;font-size: 13.3333px;line-height: normal;margin: 6.0pt 0.0cm 0.0pt;\\\"><span style=\\\"font-size: 10.0pt;font-family: Calibri, sans-serif;\\\"><img style=\\\"max-width: 100.0%;height: auto;\\\" src=\\\"/api/v1/threads/81154000046456379/inlineImages/edbsn04b985d2c927c466fcec2bd53a1e5d6e58524605cb1e418917a5a4f28a94ca8952a9411044d1bfa60826f31201db0a6e014c5fcc24d951f0bfdacff5aa00ee61?et=1716daeb023&ha=28fbb192c6bb62167d3fa69a418d6f7826284d2ed624c23349832111d7ef4fc1&f=1.png\\\" /></span></p></div></span></div>\\n</div> </blockquote>   ";
        System.out.println(s);


    }


}
