package com.imooc.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;



/**
 * @Auther: curry
 * @Date: 2018/5/24 06:42
 * @Description:
 */
@RestController
public class HelloController {
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return "Hello Spring Boot!";
    }
}
