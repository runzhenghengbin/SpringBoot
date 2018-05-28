
> JPA 绝对是简化数据库操作的一大利器。

#### 概念
##### 首先了解 JPA 是什么？

JPA（Java Persistence API）是 Sun 官方提出的 Java 持久化规范。它为 Java 开发人员提供了一种对象/关联映射工具来管理 Java 应用中的关系数据。

> 注意：JPA 是一套规范，不是一套产品，那么像 Hibernate、TopLink、JDO 它们是一套产品，如果说这些产品实现了这个 JPA 规范，那么就可以叫它们为 JPA 的实现产品。

#### 实例操作

##### 添加依赖
```
       <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
        </dependency>
```

##### 配置文件
application.yml
```
spring:
  profiles:
    active: dev
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://127.0.0.1:3306/dbgirl?characterEncoding=utf8&useSSL=false
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
```
其实这个 hibernate.hbm2ddl.auto 参数的作用主要用于：自动创建 | 更新 | 验证数据库表结构，有四个值：

> create：每次加载 hibernate 时都会删除上一次的生成的表，然后根据 model 类再重新来生成新表，哪怕两次没有任何改变也要这样执行，这就是导致数据库表数据丢失的一个重要原因。

> create-drop：每次加载 hibernate 时根据 model 类生成表，但是 sessionFactory 一关闭，表就自动删除。

> update：最常用的属性，第一次加载 hibernate 时根据 model 类会自动建立起表的结构（前提是先建立好数据库），以后加载 hibernate 时根据 model 类自动更新表结构，即使表结构改变了，但表中的行仍然存在，不会删除以前的行。要注意的是当部署到服务器后，表结构是不会被马上建立起来的，是要等应用第一次运行起来后才会。

> validate：每次加载 hibernate 时，验证创建数据库表结构，只会和数据库中的表进行比较，不会创建新表，但是会插入新值。



##### 实体类和 Dao

```
@Entity
public class Girl  {


    @Id
    @GeneratedValue
    private int id;

    private String name;

    private int age;

	//添加无参构造
    public Girl() {
    }
	// get和set方法省略
}

```
Dao 只要继承 JpaRepository 类,不需要写方法

```
public interface GirlRepository extends JpaRepository<Girl,Integer> {
   
}
```
##### GirlController类

```
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

```

注意：
- 数据库建库dbgirl
- 我使用的是springboot2.0.2版本，已经没有findOne 方法，使用findById(id).get() 来获得
- 测试大家可以使用谷歌浏览器插件Restlet Client - REST API Testing
- 源码下载：[github](https://github.com/runzhenghengbin/SpringBoot)
