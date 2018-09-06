package com.zhb.topic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: curry
 * @Date: 2018/9/5
 */
@Slf4j
@Component
@RabbitListener(queues = "topic.messages")
public class Receiver2 {

    @RabbitHandler
    public void process(String message) {
        log.info("topic Receive2:{}", message);
    }
}
