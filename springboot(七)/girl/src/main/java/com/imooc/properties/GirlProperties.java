package com.imooc.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Auther: curry
 * @Date: 2018/5/27 15:29
 * @Description:
 */

@Component
@ConfigurationProperties(prefix = "girl")
public class GirlProperties {

    private  String name;

    private int age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
