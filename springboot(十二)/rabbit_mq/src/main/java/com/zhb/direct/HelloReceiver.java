package com.zhb.direct;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * @author: curry
 * @Date: 2018/8/27
 */
@Component
@RabbitListener(queues = "hello")
@Slf4j
public class HelloReceiver {

    @RabbitHandler
    public void process(String hello) {
        log.info("消息接受为：{}",hello);
    }

    @RabbitHandler
    public void process(UserEntity userEntity) {
        log.info("消息接受为：{}",userEntity);
    }
}
