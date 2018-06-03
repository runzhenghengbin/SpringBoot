package com.imooc.service;

import com.imooc.entity.Girl;
import com.imooc.enums.ResultEnum;
import com.imooc.exception.GirlException;
import com.imooc.repository.GirlRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Auther: curry
 * @Date: 2018/5/28 23:03
 * @Description:
 */
@Service
public class GirlService {
    @Resource
    private GirlRepository girlRepository;
    public void getAge(Integer id) throws Exception {
        Girl girl = girlRepository.getOne(id);
        Integer age = girl.getAge();
        if(age<10){
            throw new GirlException(ResultEnum.PRIMARY_SCHOOL);
        }else if(age >10 && age< 16){
            throw new GirlException(ResultEnum.MIDDLE_SCHOOL);
        }

    }
}
