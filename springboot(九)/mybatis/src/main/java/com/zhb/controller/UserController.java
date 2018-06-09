package com.zhb.controller;

import com.zhb.entity.UserEntity;
import com.zhb.mapper.UserMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Auther: curry
 * @Date: 2018/6/8 21:13
 * @Description:
 */
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;


    @GetMapping(value = "/users")
    public List<UserEntity> getUsers() {
        List<UserEntity> users=userMapper.getAll();
        return users;
    }



    @GetMapping(value = "/users/{id}")
    public UserEntity getUser(@PathVariable(value = "id") Long id) {
        UserEntity user=userMapper.getOne(id);
        return user;
    }

    @PostMapping("/users")
    public void save(UserEntity user) {
        userMapper.insert(user);
    }

    @PutMapping("/users")
    public void update(UserEntity user) {
        userMapper.update(user);
    }

    @DeleteMapping(value="/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
    }


}
