package com.zhb.mapper;

import com.zhb.entity.UserEntity;

import java.util.List;

/**
 * @Auther: curry
 * @Date: 2018/6/8 21:10
 * @Description:
 */
public interface UserMapper {

    List<UserEntity> getAll();

    UserEntity getOne(Long id);

    void insert(UserEntity user);

    void update(UserEntity user);

    void delete(Long id);
}
