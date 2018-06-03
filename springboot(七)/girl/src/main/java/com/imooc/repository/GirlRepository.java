package com.imooc.repository;

import com.imooc.entity.Girl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @Auther: curry
 * @Date: 2018/5/28 21:59
 * @Description:
 */
public interface GirlRepository extends JpaRepository<Girl,Integer> {
    public List<Girl> findByAge(Integer age);
}
