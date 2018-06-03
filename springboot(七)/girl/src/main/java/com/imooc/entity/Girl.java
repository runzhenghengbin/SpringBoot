package com.imooc.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

/**
 * @Auther: curry
 * @Date: 2018/5/27 22:18
 * @Description:
 */

@Entity
public class Girl  {


    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @Min(value = 18,message = "未成年禁止入内")
    private int age;

    public Girl() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "Girl{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }
}
