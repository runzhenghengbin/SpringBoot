package com.imooc.controller;

import com.imooc.entity.Girl;
import com.imooc.repository.GirlRepository;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: curry
 * @Date: 2018/5/28 21:57
 * @Description:
 */
@RestController
public class GirlController {

    @Resource
    private GirlRepository girlRepository;

    @GetMapping("/girls")
    public List<Girl> getList(){
       return girlRepository.findAll();
    }

    @PostMapping("/girls")
    public Girl girlAdd(@RequestParam("name") String name,
                          @RequestParam("age") int age){

        Girl girl = new Girl();
        girl.setAge(age);
        girl.setName(name);
        return  girlRepository.save(girl);

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
}
