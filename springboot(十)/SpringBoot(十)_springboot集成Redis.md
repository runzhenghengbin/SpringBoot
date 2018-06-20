


#### Redis 介绍
Redis是一款开源的使用ANSI C语言编写、遵守BSD协议、支持网络、可基于内存也可持久化的日志型、Key-Value高性能数据库。

##### 数据模型

Redis 数据模型不仅与关系数据库管理系统（RDBMS）不同，也不同于任何简单的 NoSQL 键-值数据存储。Redis 数据类型类似于编程语言的基础数据类型，所以开发人员感觉很自然。每个数据类型都支持适用于其类型的操作。受支持的数据类型包括：

- string（字符串）
- hash（哈希）
- list（列表）
- set（集合）
- zset（sorted set：有序集合）

##### 关键优势

Redis 的优势包括它的速度、它对富数据类型的支持、它的操作的原子性，以及它的通用性：

- 读速度为110000次/s，写速度为81000次/s，性能极高。
- 丰富的数据类型，Redis 对大多数开发人员已知道的大多数数据类型提供了原生支持，这使得各种问题得以轻松解决。
- 原子性，因为所有 Redis 操作都是原子性的，所以多个客户端会并发地访问一个 Redis 服务器，获取相同的更新值。
- 丰富的特性，Redis 是一个多效用工具，有非常多的应用场景，包括缓存、消息队列（Redis 原生支持发布/订阅）、短期应用程序数据（如 Web 会话、Web 页面命中计数）等。

#### springboot集成Redis

##### 1、创建集成redis的springboot项目

 我直接用idea创建一个新的工程，在创建的时候，直接集成Redis，如下图

![enter image description here](http://p7zk4x9pv.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180620135454.png)

##### 2.application 配置(application.yml)
```
spring:
  redis:
    # Redis 服务器地址
    host: 192.168.142.128
    # Redis 服务器连接端口
    port: 6379
    # Redis 服务器连接密码（默认为空）
    password:
    # Redis 数据库索引（默认为0）
    database: 1
    jedis:
      pool:
        # 连接池最大连接数（使用负值表示没有限制）
        max-active: 8
        # 连接池最大阻塞等待时间（使用负值表示没有限制）
        max-wait: -1
        # 连接池中的最大空闲连接
        max-idle: 8

```

##### 3、Redis 对多种数据类型的操作

> 这里我直接在Test类中进行操作，注入RedisTemplate

```
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDemo01ApplicationTests {


    @Resource
    private RedisTemplate redisTemplate;


    @Test
    public void contextLoads() {
    }

```

- String类型

```
    /**
     * 测试存储String
     */
    @Test
    public  void testString(){
        redisTemplate.opsForValue().set("maomao", "hello");
        System.out.println(redisTemplate.opsForValue().get("maomao").toString());
    }
    //输出hello
```

- hash类型

> hash set 的时候需要传入三个参数，第一个为 key，第二个为 field，第三个为存储的值。一般情况下 Key 代表一组数据，field 为 key 相关的属性，而 value 就是属性对应的值.
```
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
    //输出 hash value :java
```



- list类型

> Redis list 的应用场景非常多，也是 Redis 最重要的数据结构之一。 使用 List 可以轻松的实现一个队列，List 典型的应用场景就是消息队列，可以利用 list 的 PUSH 操作，将任务存在 list 中，然后工作线程再用 POP 操作将任务取出进行执行。

```
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
    //输出结果 list value :it
```
> 使用 range 来读取,range 后面的两个参数就是插入数据的位置，输入不同的参数就可以取出队列中对应的数据。


```
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
    //输出结果
    //list range :just
	//list range :do
	//list range :it
```

- set类型

> Redis set 对外提供的功能与 list 类似是一个列表的功能，特殊之处在于 set 是可以自动排重的，当需要存储一个列表数据，又不希望出现重复数据时，set 是一个很好的选择，并且 set 提供了判断某个成员是否在一个 set 集合内的重要接口，这个也是 list 所不能提供的。

```
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
    //输出结果
    //set value :just
	//set value :it
	//set value :do
```
>Redis 为集合提供了求交集、并集、差集等操作，可以非常方便的使用

```
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
        //输出 
        //diffs set value :it
		//diffs set value :just

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
        //输出
        //unions value :do
		//unions value :java
		//unions value :just
		//unions value :it
		//unions value :hello
    }
```

- zset

> Redis sorted set 的使用场景与 set 类似，区别是 set 不是自动有序的，而 sorted set 可以通过用户额外提供一个优先级（score）的参数来为成员排序，并且是插入有序，即自动排序。

```
    @Test
    public void testZset(){
        String key="zset";
        redisTemplate.delete(key);
        ZSetOperations<String, String> zset = redisTemplate.opsForZSet();
        zset.add(key,"just",1);
        zset.add(key,"now",5);
        zset.add(key,"it",4);
        zset.add(key,"do",3);

        Set<String> zsets=zset.range(key,0,3);
        for (String v:zsets){
            System.out.println("zset value :"+v);
        }
        //zset value :just
		//zset value :do
		//zset value :it
		//zset value :now


        Set<String> zsetB=zset.rangeByScore(key,0,3);
        for (String v:zsetB){
            System.out.println("zsetB value :"+v);
        }
        //zsetB value :just
		//zsetB value :do
    }
```
##### 4.Redis 其他的操作

- 超时失效
```

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
    // 输出 exists is false
```

- 删除数据

```
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
    //输出 exists is false
```

##### 5、封装redisTemplate 
> 在实际的使用过程中，不会给每一个使用的类都注入 redisTemplate 来直接使用，一般都会对业务进行简单的包装，最后提供出来对外使用. 这里就不在展示

完整代码下载：[github](https://github.com/runzhenghengbin/SpringBoot)

