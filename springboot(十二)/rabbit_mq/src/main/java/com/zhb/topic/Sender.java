package com.zhb.topic;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: curry
 * @Date: 2018/9/5
 */
@RestController
public class Sender {
    @Resource
   private AmqpTemplate rabbitTemplate;

    @GetMapping(value = "send1")
    public void send1(){
        String context = "hi, i am message1";
        rabbitTemplate.convertAndSend("exchange","topic.message",context);
    }

    @GetMapping(value = "send2")
    public void send2(){
        String context = "hi, i am message2";
        rabbitTemplate.convertAndSend("exchange","topic.messages",context);
    }


}
