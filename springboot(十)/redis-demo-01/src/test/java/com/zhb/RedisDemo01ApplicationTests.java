package com.zhb;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemo01ApplicationTests {


    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void contextLoads() {
    }

    /**
     * 测试存储String
     */
    @Test
    public  void testString(){
        redisTemplate.opsForValue().set("maomao", "hello");
        System.out.println(redisTemplate.opsForValue().get("maomao").toString());
    }

    /**
     * 测试超时失效
     * @throws InterruptedException
     */
    @Test
    public void testExpire() throws InterruptedException {
        ValueOperations<String, String> operations=redisTemplate.opsForValue();
        operations.set("expire", "java",100,TimeUnit.MILLISECONDS);
        Thread.sleep(1000);
        boolean exists=redisTemplate.hasKey("expire");
        if(exists){
            System.out.println("exists is true");
        }else{
            System.out.println("exists is false");
        }
    }

    /**
     * 删除数据
     */
    @Test
    public void testDelete() {
        ValueOperations<String, String> operations=redisTemplate.opsForValue();
        operations.set("deletekey", "springboot");
        redisTemplate.delete("deletekey");
        //判断key是否还在
        boolean exists=redisTemplate.hasKey("deletekey");
        if(exists){
            System.out.println("exists is true");
        }else{
            System.out.println("exists is false");
        }
    }

    /**
     * hash
     */
    @Test
    public void testHash() {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put("hash","hello","java");
        String value=(String) hash.get("hash","hello");
        System.out.println("hash value :"+value);
    }

    @Test
    public void testListPop() {
        String key="list";
        redisTemplate.delete(key);
        ListOperations<String, String> list = redisTemplate.opsForList();
        list.leftPush(key,"just");
        list.leftPush(key,"do");
        list.leftPush(key,"it");
        String value=list.leftPop(key);
        System.out.println("list value :"+value);
    }

    @Test
    public void testListRange() {
        String key="list";
        redisTemplate.delete(key);
        ListOperations<String, String> list = redisTemplate.opsForList();
        list.rightPush(key,"just");
        list.rightPush(key,"do");
        list.rightPush(key,"it");

        List<String> values=list.range(key,0,3);
        for (String v:values){
            System.out.println("list range :"+v);
        }
    }

    @Test
    public void testSet(){
        String key = "set";
        redisTemplate.delete(key);
        SetOperations<String,String> set = redisTemplate.opsForSet();
        set.add(key,"just");
        set.add(key,"do");
        set.add(key,"do");
        set.add(key,"it");
        Set<String> values = set.members(key);
        for (String value:values){
            System.out.println("set value :"+value);
        }
    }

    @Test
    public void testSetMore() {
        SetOperations<String, String> set = redisTemplate.opsForSet();
        String key1="setMore1";
        String key2="setMore2";

        set.add(key1,"just");
        set.add(key1,"do");
        set.add(key1,"do");
        set.add(key1,"it");

        set.add(key2,"java");
        set.add(key2,"do");

        Set<String> diffs=set.difference(key1,key2);
        for (String v:diffs){
            System.out.println("diffs set value :"+v);
        }

        String key3="setMore3";
        String key4="setMore4";
        set.add(key3,"just");
        set.add(key3,"do");
        set.add(key3,"java");
        set.add(key4,"it");
        set.add(key4,"do");
        set.add(key4,"hello");
        Set<String> unions=set.union(key3,key4);
        for (String v:unions){
            System.out.println("unions value :"+v);
        }
    }

    @Test
    public void testZset(){
        String key="zset";
        redisTemplate.delete(key);
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.add(key,"just",1);
        zset.add(key,"now",5);
        zset.add(key,"it",4);
        zset.add(key,"do",3);

        Set<String> zsets=zset.range(key,0,2);
        for (String v:zsets){
            System.out.println("zset value :"+v);
        }

        Set<String> zsetB=zset.rangeByScore(key,0,3);
        for (String v:zsetB){
            System.out.println("zsetB value :"+v);
        }
    }

}
