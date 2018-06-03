package com.imooc.controller;

import com.imooc.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


/**
 * @Auther: curry
 * @Date: 2018/5/24 06:42
 * @Description:
 */
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
   // @RequestMapping(value = "/say",method = RequestMethod.GET)
    @GetMapping("/say")
    public String say(@RequestParam(value = "id",required = false,defaultValue = "0") Integer id){
        return "id :"+id;
    }
}
