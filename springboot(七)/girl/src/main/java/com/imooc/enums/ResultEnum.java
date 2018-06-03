package com.imooc.enums;

/**
 * @Auther: curry
 * @Date: 2018/6/2 15:46
 * @Description:
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    PRIMARY_SCHOOL(100,"还在小学"),
    MIDDLE_SCHOOL(101,"还在初中"),

    ;


    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


}
