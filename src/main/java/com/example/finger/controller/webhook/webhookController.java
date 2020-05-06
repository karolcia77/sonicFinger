package com.example.finger.controller.webhook;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.finger.bean.RestResultModule;
import com.example.finger.bean.Webhook_Ticket;
import com.example.finger.bean.Whatsapp_Webhook;
import com.example.finger.bean.Zoho_Ticket;
import com.example.finger.dao.FingerUserDao;
import com.example.finger.dao.Webhook_TicketDao;
import com.example.finger.dao.Whatsapp_WebhookDao;
import com.example.finger.dao.Zoho_TicketDao;
import com.github.houbb.opencc4j.util.ZhConverterUtil;
import com.nexmo.jwt.Jwt;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

/*
 * API
 * */
@Controller
@RequestMapping(value = "webhook")
@Component("webhookController")
public class webhookController {
    private  static Logger logger = LoggerFactory.getLogger(webhookController.class);

    @Resource
    private Webhook_TicketDao webhook_ticketDao;

    @Resource
    private Whatsapp_WebhookDao whatsapp_webhookDao;

    /**
     * 获取
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/zoho/get")
    public RestResultModule conversion(@RequestBody(required=false) String json)
            throws Exception {
        RestResultModule module = new RestResultModule();
        System.out.println("收到-----------------------------");
        System.out.println(json);
        logger.info("收到-----------------------------");
        logger.info(json);
        if(json == null){
            module.putData("json","null");
            return module;
        }
        try{
            JSONArray jsonArray = JSONArray.parseArray(json);
            JSONObject j = jsonArray.getJSONObject(0);
            JSONObject j_payload = j.getJSONObject("payload");

            Webhook_Ticket ticket = new Webhook_Ticket();
            ticket.setJson(json);
            ticket.setPayload(j.getString("payload"));
            Date date = new Date();
            date.setTime(Long.parseLong(j.getString("eventTime")));
            ticket.setEventTime(date);
            ticket.setEventType(j.getString("eventType"));
            ticket.setTicketNumber(j_payload.getString("ticketNumber"));
            ticket.setEmail(j_payload.getString("email"));
            ticket.setStatus(j_payload.getString("status"));
            ticket.setSubject(j_payload.getString("subject"));

            String summary = "";
            String ticketId = "";
            if(ticket.getEventType().equalsIgnoreCase("Ticket_Thread_Add")){
                System.out.println("Ticket_Thread_Add");
                summary = j_payload.getString("summary");
                ticketId = j_payload.getString("ticketId");
            }else{
                System.out.println("Ticket_Update");
                JSONObject j_payload_firstThread = j_payload.getJSONObject("firstThread");
                summary = j_payload_firstThread.getString("summary");
                ticketId = j_payload_firstThread.getString("ticketId");
                JSONObject j_payload_cf = j_payload.getJSONObject("cf");
                ticket.setCf_handled_by(j_payload_cf.getString("cf_handled_by"));
            }
            ticket.setSummary(summary);
            ticket.setTicketId(ticketId);

            webhook_ticketDao.save(ticket);

        }catch (Exception e){
            System.out.println(e);
            logger.error(e.toString());
        }
        return module;
    }




    /**
     * 获取whatsapp/in
     * @return
     */
    @Transactional
    @RequestMapping("/whatsapp/in")
    public String whatsappin(@RequestBody(required=false) String json)
            throws Exception {
        try{
            logger.info("收到whatsapp/in-----------------------------");
            logger.info(json);
        }catch (Exception e){
            System.out.println(e);
            logger.error(e.toString());
        }
        return "ok";
    }

    /**
     * 获取whatapp/update
     * @return
     */
    @Transactional
    @RequestMapping("/whatsapp/update")
    public String whatsappupdate(@RequestBody(required=false) String json)
            throws Exception {
        try{
            logger.info("收到whatsapp/update-----------------------------");
            logger.info(json);

        }catch (Exception e){
            System.out.println(e);
            logger.error(e.toString());
        }
        return "ok";
    }



    /**
     * sonic
     * 获取sonicwhatsapp/inbound
     * @return
     */
    @Transactional
    @RequestMapping("/sonicwhatsapp/inbound")
    public String whatsappinbound(@RequestBody(required=false) String json)
            throws Exception {
        try{
            logger.info("收到sonicwhatsapp/inbound-----------------------------");
            logger.info(json);
            if(json != null){
                // 存入数据库
                Whatsapp_Webhook webhook = new Whatsapp_Webhook();
                webhook.setJson(json);
                JSONObject jsonobject = JSON.parseObject(json);
                if(jsonobject != null){
                    JSONObject from = jsonobject.getJSONObject("from");
                    webhook.setTimestamp(jsonobject.getTimestamp("timestamp"));
                    webhook.setFrom_number(from.getString("number"));
                    JSONObject content = jsonobject.getJSONObject("message").getJSONObject("content");
                    if(content != null){
                        webhook.setMessage_text(content.getString("text"));
                    }
                }
                whatsapp_webhookDao.save(webhook);
            }
        }catch (Exception e){
            logger.error(e.toString());
        }
        return "ok";
    }

    /**
     * sonic
     * 获取sonicwhatsapp/status
     * @return
     */
    @Transactional
    @RequestMapping("/sonicwhatsapp/status")
    public String whatsappstatus(@RequestBody(required=false) String json)
            throws Exception {
        try{
            logger.info("收到sonicwhatsapp/status-----------------------------");
            logger.info(json);

        }catch (Exception e){
            System.out.println(e);
            logger.error(e.toString());
        }
        return "ok";
    }


    /**
     * 获取全部zoho_list
     * @return
     */
    @ResponseBody
    @Transactional
    @RequestMapping("/whatsapp/sendMessage")
    public String test()throws Exception {
        String url = "https://messages-sandbox.nexmo.com/v0.1/messages";
        //  模拟请求
        String json = "{\n" +
                "    \"from\": { \"type\": \"whatsapp\", \"number\": \"14157386170\" },\n" +
                "    \"to\": { \"type\": \"whatsapp\", \"number\": \"8613286960470\" },\n" +
                "    \"message\": {\n" +
                "      \"content\": {\n" +
                "        \"type\": \"text\",\n" +
                "        \"text\": \"hi Paddy,test13-中国，加油  \"\n" +
                "      }\n" +
                "    }\n" +
                "  }";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringEntity postingString = new StringEntity(json,"UTF-8");
        post.setEntity(postingString);
        post.setHeader("Content-Type","application/json");
        post.setHeader("Accept","application/json");
        post.setHeader("Accept","application/json");
        HttpResponse response = httpClient.execute(post);
        String content = EntityUtils.toString(response.getEntity());
        System.out.println(content);

        return "ok";
    }

    public static void main(String args[]) throws Exception{
        try{
            String test1 = "UO123";
            String test2 = "HKG";
            String test3 = "10:00";
            String test4 = "2020-05-01";
            String test5 = "KIX";
            String test6 = "UO456";
            String test7 = "HKG";
            String test8 = "11:00";
            String test9 = "2020-05-02";
            String test10 = "KIX";
            String test11 = "operatonal reason";

            String from="85252232825";
            // noel 639178343985
            // johnz 8615918450154
            // derek 85295431862
            String to="8613286960470";

            String url = "https://api.nexmo.com/v0.1/messages";
            //  模拟请求
       /*     String json = "{\n" +
                    "    \"from\": { \"type\": \"whatsapp\", \"number\": \"85252232825\" },\n" +
                    "    \"to\": { \"type\": \"whatsapp\", \"number\": \"8613286960470\" },\n" +
                    "    \"message\": {\n" +
                    "      \"content\": {\n" +
                    "        \"type\": \"text\",\n" +
                    "        \"text\": \"hi Paddy,-中国，加油  \"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }";*/

            String input = "{\n" +
                    "   \"from\":{\n" +
                    "      \"type\":\"whatsapp\",\n" +
                    "      \"number\":"+from+"" +
                    "   },\n" +
                    "   \"to\":{\n" +
                    "      \"type\":\"whatsapp\",\n" +
                    "      \"number\":"+to+"" +
                    "   },\n" +
                    "   \"message\":{\n" +
                    "      \"content\":{\n" +
                    "         \"type\":\"template\",\n" +
                    "         \"template\":{\n" +
                    "            \"name\":\"7c129147_c7f0_4b2b_95d8_5c5504f181b0:test_hke_flight_changes1\",\n" +
                    "            \"parameters\":[\n" +
                    "               { \"default\" : \""+test1+"\" } , " +
                    "               { \"default\" : \""+test2+"\" } , " +
                    "               { \"default\" : \""+test3+"\" } , " +
                    "               { \"default\" : \""+test4+"\" } , " +
                    "               { \"default\" : \""+test5+"\" } , " +
                    "               { \"default\" : \""+test6+"\" } , " +
                    "               { \"default\" : \""+test7+"\" } , " +
                    "               { \"default\" : \""+test8+"\" } , " +
                    "               { \"default\" : \""+test9+"\" } , " +
                    "               { \"default\" : \""+test10+"\" } , " +
                    "               { \"default\" : \""+test11+"\" } " +
                    "            ]\n" +
                    "         }\n" +
                    "      },\n" +
                    "      \"whatsapp\": {\n" +
                    "        \"policy\": \"deterministic\",\n" +
                    "        \"locale\": \"en\"\n" +
                    "      }\n" +
                    "   }\n" +
                    "}";


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost post = new HttpPost(url);
            StringEntity postingString = new StringEntity(input,"UTF-8");
            post.setEntity(postingString);
            post.setHeader("Content-Type","application/json");
            post.setHeader("Accept","application/json");

            // eyJ0eXBlIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJhcHBsaWNhdGlvbl9pZCI6IjJkNzg5ZjQ5LTMwY2QtNDVlNi1hNjJjLWY0YTE2ZWI3NDdmNCIsImlhdCI6MTU4NzAyMzgwMywianRpIjoiMmY4Nzg4YWYtM2Q5NC00YzY5LTgxZGMtNjJiYTRlNDM1M2VjIn0.D3UEqdAWPRbqoQj0NlVohAqk71xrq8XfG99RQsBVawOz5qkfye27rYBQ2eSvXrAMcG491D8ccmsShyKkLDC5SArlh2OQf_x2kTbmrSld_RazOmeNvGsSHkFqGMTYRwlagTQIRZ9rz7DVXXB_SEwGW4WAlc3xoj5SNbtMtlqA8p6KFpgcGC_CUQ2o8EdEA8YQ9xZo9rK50cW7W5wpTwRSHXGMXxoabAu_3VTqMiqH0u_Kaj48QmHhoreoidPqSAFUMsYVtxcYJC91Xd-RPzozHXSrszUHaufeL5vXw1r0QcLGDhSwfIWjfd6K3UsYOuW22FRFiDUakkam78sNH8AKGw
         /*   String jws = Jwt.builder()
                    .applicationId("2d789f49-30cd-45e6-a62c-f4a16eb747f4")
                    .privateKeyPath(Paths.get("C:\\Program Files\\nodejs\\private.key"))
                    .addClaim("foo", "bar")
                    .addClaim("bat", "baz")
                    .build()
                    .generate();
            System.out.println(jws);*/
            // Bearer / Basic
            // post.setHeader("Authorization","Bearer "+jws);
            post.setHeader("Authorization","Basic ZjJhZDE0MDg6MmxZemZkR0t2OXFZM3Ntcw==");

            HttpResponse response = httpClient.execute(post);
            String content = EntityUtils.toString(response.getEntity());
            System.out.println(content);

            // ZjJhZDE0MDg6MmxZemZkR0t2OXFZM3Ntcw==
            //System.out.println(getBase64Encode("f2ad1408:2lYzfdGKv9qY3sms"));
            //System.out.println(getBase64Decode("ZjJhZDE0MDg6MmxZemZkR0t2OXFZM3Ntcw"));
        }catch (Exception e){
            System.out.println(e);
        }


    }

    // 加密
    public static String getBase64Encode(String str) {
        byte[] b = null;
        String s = null;
        try {
            b = str.getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (b != null) {
            s = new BASE64Encoder().encode(b);
        }
        return s;
    }

    // 解密
    public static String getBase64Decode(String s) {
        byte[] b = null;
        String result = null;
        if (s != null) {
            BASE64Decoder decoder = new BASE64Decoder();
            try {
                b = decoder.decodeBuffer(s);
                result = new String(b, "utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }



}
