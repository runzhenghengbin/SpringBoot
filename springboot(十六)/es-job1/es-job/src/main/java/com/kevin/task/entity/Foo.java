package com.kevin.task.entity;

/**
 * @author: kevin
 * @Date: 2020/1/30
 */
public class Foo {

    private String id;
    private String name;

    public Foo() {
    }
    public Foo(String id, String name) {
        super();
        this.id = id;
        this.name = name;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

}
