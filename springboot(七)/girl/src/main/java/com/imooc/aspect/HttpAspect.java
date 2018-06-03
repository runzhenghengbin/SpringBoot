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
