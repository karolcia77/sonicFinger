package com.example.finger.dao;

import com.example.finger.bean.Whatsapp_Webhook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component(value = "whatsapp_WebhookDao")
public interface Whatsapp_WebhookDao extends JpaRepository<Whatsapp_Webhook,Long> {


}
