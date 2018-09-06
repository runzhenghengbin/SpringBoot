package com.zhb.direct;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @author: curry
 * @Date: 2018/8/27
 */

@RestController
public class HelloSender {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping(value = "/sendString")
    public String sendString() {
        //发送消息 String routingKey, Object object
        amqpTemplate.convertAndSend("hello", "hello rabbitMQ");
        return "消息已发送";
    }

    @GetMapping(value = "/sendObject")
    public String sendObject() {
        //发送消息 String routingKey, Object object
        UserEntity userEntity = new UserEntity();
        userEntity.setName("allen");
        userEntity.setAddress("山东济南");
        amqpTemplate.convertAndSend("hello", userEntity);
        return "消息已发送";
    }
}
