/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dummyloanbroker;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.AMQP.Channel;
import com.rabbitmq.client.AMQP.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import dk.dummyloanbroker.dto.LoanRequestDTO;
import java.io.IOException;

/**
 *
 * @author Jon
 */
public class DummyLoanBroker {

    private static final String TASK_QUEUE_NAME = "queue_getCreditScore";
    
    private QueueingConsumer consumer;
    
    
    public static void main(String[] args) throws IOException 
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("datdb.cphbusiness.dk");
        factory.setUsername("student");
        factory.setPassword("cph");
         com.rabbitmq.client.Connection connection = factory.newConnection();
        com.rabbitmq.client.Channel channel = connection.createChannel();
        String corrId = java.util.UUID.randomUUID().toString();
        
        LoanRequestDTO loanRequest = new LoanRequestDTO("123456-7890", 456289.0, 25, -1);
        
        Gson gson = new Gson();
        
        String message = gson.toJson(loanRequest);
        
        AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().correlationId(corrId).build();
        channel.queueDeclare(TASK_QUEUE_NAME, true, false, false, null);
        
        channel.basicPublish( "", TASK_QUEUE_NAME, 
                props,
                message.getBytes());
        
        channel.close();
        connection.close();
    }
    
}
