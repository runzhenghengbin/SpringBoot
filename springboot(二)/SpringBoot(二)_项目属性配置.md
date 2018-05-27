

#### 修改端口

在main/resources/application.properties修改端口
```
server.port=8088
```
此时启动访问localhost:8088/hello 就会看到
```
Hello Spring Boot!
```

#### 使用yml文件替换properties 文件

（1）在main/resources 文件下新建一个application.yml 文件

（2）在yml文件中修改端口
```
server:
  port: 8099
```
(3) 删除掉application.properties文件，只保留yml文件

(4) 运行程序，此时访问8099端口即可

#### 获取配置文件的值
(1) 在application.yml 文件中，编写上其他内容
```
server:
  port: 8099
name: maomao
age: 18
```
(2) 利用@Value 注解
```
@RestController
public class HelloController {

    @Value("${name}")
    private String name;

    @Value("${age}")
    private int age;
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return name+age;
    }
}
```
（3）访问8099端口,就获取到值
```
maomao 18
```
#### 使用自定义配置类

> 如果属性很多，我们每个属性都需要写，显得有些费事，我们可以利用自定义配置类进行获取

(1) 修改yml 文件
```
server:
  port: 8099
girl:
  name: maomao
  age: 18

```
(2) 创建properties/GirlProperties.java
```
@Component
@ConfigurationProperties(prefix = "girl")
public class GirlProperties {

    private  String name;

    private int age;

	//get和set方法省略
}

```
(3) 我在使用@ConfigurationProperties 这个注解时，提示找不到class，需在pom文件中引入

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
```

(4) 修改controller文件

```
@RestController
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return girlProperties.getName();
    }
}
```
（5）验证结果
```
maomao
```
#### 开发环境和生成环境配置不同的问题

> 这个问题经常见，比如我们开发环境 name 是maomao ，生成环境是 毛毛，我们大部分都是修改配置文件，但是这样还是很麻烦。

(1) 复制2个yml文件，分别是application-dev.yml  (开发环境) application-prod.yml（生产环境）
 
(2) 修改application-prod.yml（生产环境）文件

```
server:
  port: 8088
girl:
  name: 毛毛
  age: 18

```
(3) application-dev.yml  (开发环境)文件内容
```
server:
  port: 8099
girl:
  name: maomao
  age: 18

```
（4）application.yml文件内容,这个就代表使用dev的配置文件

```
spring:
  profiles:
    active: dev
```
（5）上篇文章讲过java -jar 的启动方式

- 先执行 mvn install
- 在执行启动 java -jar girl-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod

（6）此时就是访问的prod 的配置8088，（注意我们配置的application.yml 中用的是dev 这个配置文件，但是我们启动的时候加上后面的参数就自动切换到 prod 文件上）


#### 总结

在使用yml进行配置更加简单方便，使用java -jar 启动 加上参数，就可以避免我们来回修改配置文件，有漏掉的情况。

源码下载：[github](https://github.com/runzhenghengbin/SpringBoot.git)