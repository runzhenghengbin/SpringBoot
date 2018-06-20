package com.zhb.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;


/**
 * @Auther: curry
 * @Date: 2018/6/20 11:02
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
    @Resource
    private RedisService redisService;

    @Test
    public void testString() throws Exception {
        redisService.set("maomao", "hello");
        System.out.println(redisService.get("maomao"));
    }



}