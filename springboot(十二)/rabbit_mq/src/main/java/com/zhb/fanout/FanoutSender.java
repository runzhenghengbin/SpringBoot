package com.zhb.fanout;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author: curry
 * @Date: 2018/9/6
 */
@RestController
public class FanoutSender {

    @Resource
    private AmqpTemplate rabbitTemplate;

    @GetMapping(value = "fanoutSend")
    public void send(){
        String context = "hi, fanout msg";
        rabbitTemplate.convertAndSend("fanoutExchange","",context);
    }

}
