package com.imooc.controller;

import com.imooc.entity.Girl;
import com.imooc.entity.Result;
import com.imooc.repository.GirlRepository;
import com.imooc.service.GirlService;
import com.imooc.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Auther: curry
 * @Date: 2018/5/28 21:57
 * @Description:
 */
@RestController
public class GirlController {
    private  final static Logger logger = LoggerFactory.getLogger(GirlController.class);
    @Resource
    private GirlRepository girlRepository;

    @Resource
    private GirlService girlService;

    @GetMapping("/girls")
    public List<Girl> getList(){
        logger.info("getList");
       return girlRepository.findAll();
    }

    @PostMapping("/girls")
    public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
          return ResultUtil.error(1,bindingResult.getFieldError().getDefaultMessage());
        }
        return  ResultUtil.success(girlRepository.save(girl));

    }

    @GetMapping(value = "/girls/{id}")
    public  Girl find(@PathVariable(value = "id") Integer id){
        return girlRepository.findById(id).get();
    }

    @PostMapping(value = "/girls/{id}")
    public Girl update(@PathVariable(value = "id") Integer id,
                       @RequestParam("name") String name,
                       @RequestParam("age") int age){
        Girl girl = new Girl();
        girl.setId(id);
        girl.setAge(age);
        girl.setName(name);
        return girlRepository.save(girl);

    }

    @DeleteMapping(value = "/girls/{id}")
    public void  delete(@PathVariable(value = "id") Integer id){
        girlRepository.deleteById(id);
    }

    @GetMapping(value = "/girls/age/{age}")
    public List<Girl> findByAge(@PathVariable(value = "age") Integer age){
        return girlRepository.findByAge(age);
    }
    @GetMapping(value = "/girls/getAge/{id}")
    public void getAge(@PathVariable("id") Integer id) throws Exception {
        girlService.getAge(id);
    }
}
