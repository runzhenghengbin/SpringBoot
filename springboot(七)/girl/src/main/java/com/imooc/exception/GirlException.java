package com.imooc.exception;

import com.imooc.enums.ResultEnum;

/**
 * @Auther: curry
 * @Date: 2018/6/2 15:30
 * @Description:
 */
public class GirlException extends RuntimeException{

    private Integer code;

    public GirlException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}
