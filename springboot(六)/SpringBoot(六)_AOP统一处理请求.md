
##### 什么是AOP

 - AOP 是一种编程范式，与编程语言无关；
 - 将通用逻辑从业务逻辑中分离出来（假如你的业务是一条线，我们不在业务线上写一行代码就能完成附加任务！我们会把代码写在其他的地方）；

##### 具体实现

(1) 引入依赖

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
```
(2) 创建HttpAspect.java 文件
  -  类上加入@Aspect @Component 注解
 - 使用@Pointcut 定义一个公共的方法，定义切哪个点
 -  @Before @After    @AfterReturning 这三个注解是切的时间点
 -  使用org.slf4j.Logger 进行日志记录
```
package com.imooc.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;

/**
 * @Auther: curry
 * @Date: 2018/6/2 13:45
 * @Description:
 */
@Aspect
@Component
public class HttpAspect {

    private  final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);

    @Pointcut("execution( public * com.imooc.controller.GirlController.*(..))")
    public void log(){

    }

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        ServletRequestAttributes attributes = (ServletRequestAttributes)RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        // url
        logger.info("url={}",request.getRequestURL());
        //method
        logger.info("method = {}",request.getMethod());
        //ip
        logger.info("ip = {}",request.getRemoteAddr());
        //类方法
        logger.info("class_method={}",joinPoint.getSignature().getDeclaringTypeName()+"."+ joinPoint.getSignature().getName());
        //参数
        logger.info("args = {}",joinPoint.getArgs());

    }


    @After("log()")
    public void doAfter(){

    }

    @AfterReturning(pointcut = "log()",returning = "object")
    public void doAfterReturning(Object object){
        logger.info("response = {}",object);
    }
}

```

（3）进行测试


```
//前面省略 以下代码
2018-06-02 19:53:17.874  INFO 10088 --- [nio-8099-exec-1] com.imooc.aspect.HttpAspect   

: url=http://localhost:8099/girls
: method = POST
: ip = 0:0:0:0:0:0:0:1
: class_method=com.imooc.controller.GirlController.girlAdd
: args = Girl{id=0, name='maomao', age=7}
: response = com.imooc.entity.Result@6a84c72f
```

##### 小彩蛋
> springboot  支持打印自定义banner，只要在resources 下面新建一个banner.txt 文件

文件内容，每次启动，就会显示下面的图，是不是很酷
```
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
         佛祖保佑       永无BUG
```