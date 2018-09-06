package com.zhb.direct;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: curry
 * @Date: 2018/9/5
 */
@Data
public class UserEntity implements Serializable {

    private String name;

    private String address;
}
