package com.imooc.controller;

import com.imooc.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


/**
 * @Auther: curry
 * @Date: 2018/5/24 06:42
 * @Description:
 */
@RestController
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return girlProperties.getName();
    }
}
