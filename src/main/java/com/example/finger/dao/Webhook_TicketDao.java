package com.example.finger.dao;

import com.example.finger.bean.Webhook_Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component(value = "webhook_TicketDao")
public interface Webhook_TicketDao extends JpaRepository<Webhook_Ticket,Long> {


}
