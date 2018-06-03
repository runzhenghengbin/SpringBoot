

> 我感觉看了这节课，给我的思考还是很多的，感觉受益良多。废话不多说，一起学习。

##### 统一的 外层结构返回
> 这样利于代码看着也规范，前端处理也统一

```
# 错误返回
{
"code": 1,
"msg": "未成年禁止入内",
"data": null
}

# 正确返回
{
"code": 0,
"msg": "成功",
"data":{
		"id": 8,
		"name": "maomao",
		"age": 19
		}
}
```

（1） 实现这个我们要定义一个返回结果的实体类

```
package com.imooc.entity;

/**
 * http请求做外层对象
 * @Auther: curry
 * @Date: 2018/6/2 14:35
 * @Description:
 */
public class Result<T> {

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private T data;

	//get和set省略
}

```
（2）定义返回结果的工具类

```
package com.imooc.utils;

import com.imooc.entity.Result;

/**
 * @Auther: curry
 * @Date: 2018/6/2 14:39
 * @Description:
 */
public class ResultUtil {

    public static Result success(Object object){
        Result result = new Result();
        result.setCode(0);
        result.setMsg("成功");
        result.setData(object);
        return result;
    }

    public static Result success(){
        return  success(null);
    }
    public static Result error(Integer code,String msg){
        Result result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }
}

```
(3) 使用规定的返回结果
> 注意：controller 只是用于请求和参数的传递，业务处理应该在service进行处理。这只是方便演示

```
    @PostMapping("/girls")
    public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
          return ResultUtil.error(1,bindingResult.getFieldError().getDefaultMessage());
        }
        return  ResultUtil.success(girlRepository.save(girl));

    }
```
(4) 这样就使我们返回结果如上面所示的一样了

##### 定义统一的异常处理

> 实现业务：获取女生的年龄，
> 如果小于10岁:返回：还在小学
> 如果大于10岁小于16岁:返回：还在初中

controller层

```
    @GetMapping(value = "/girls/getAge/{id}")
    public void getAge(@PathVariable("id") Integer id) throws Exception {
        girlService.getAge(id);
    }
```
service层
```
@Service
public class GirlService {
    @Resource
    private GirlRepository girlRepository;
    public void getAge(Integer id) throws Exception {
        Girl girl = girlRepository.getOne(id);
        Integer age = girl.getAge();
        if(age<10){
            throw  new Exception("还在小学");
        }else if(age >10 && age< 16){
            throw new Exception("还在初中");
        }

    }
}

```
监听异常

```
package com.imooc.handle;

import com.imooc.aspect.HttpAspect;
import com.imooc.entity.Result;
import com.imooc.exception.GirlException;
import com.imooc.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Auther: curry
 * @Date: 2018/6/2 15:05
 * @Description:
 */
@ControllerAdvice
public class ExceptionHandle {
    private  final static Logger logger = LoggerFactory.getLogger(HttpAspect.class);
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result handle(Exception e){
        return ResultUtil.error(100,e.getMessage());
    }
}

```
测试返回结果,控制台不会报错

```
{
"code": 100,
"msg": "还在初中",
"data": null
}
```

##### 实现自己的Exception

创建自己的Exception
 > 继承自RuntimeException 是因为 spring 这个框架对运行时异常会进行数据回滚，如果是Exception .则不会
```
package com.imooc.exception;

import com.imooc.enums.ResultEnum;

/**
 * @Auther: curry
 * @Date: 2018/6/2 15:30
 * @Description:
 */
public class GirlException extends RuntimeException{

    private Integer code;

    // 这里使用了枚举
    public GirlException(ResultEnum resultEnum) {
        super(resultEnum.getMsg());
        this.code = resultEnum.getCode();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

```

创建枚举类

```
package com.imooc.enums;

/**
 * @Auther: curry
 * @Date: 2018/6/2 15:46
 * @Description:
 */
public enum ResultEnum {
    UNKNOW_ERROR(-1,"未知错误"),
    SUCCESS(0,"成功"),
    PRIMARY_SCHOOL(100,"还在小学"),
    MIDDLE_SCHOOL(101,"还在初中"),

    ;


    private Integer code;

    private String msg;

    ResultEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }



    public String getMsg() {
        return msg;
    }


}

```
修改异常捕获类

```
    public Result handle(Exception e){
      //  return ResultUtil.error(100,e.getMessage());

        if (e instanceof GirlException){
            GirlException girlException = (GirlException)e;
            return  ResultUtil.error(girlException.getCode(),girlException.getMessage());
        }else {
            logger.info("【系统异常】{}",e);
            return  ResultUtil.error(-1,"未知错误");
        }
    }
```
修改service类

```
    public void getAge(Integer id) throws Exception {
        Girl girl = girlRepository.getOne(id);
        Integer age = girl.getAge();
        if(age<10){
            throw new GirlException(ResultEnum.PRIMARY_SCHOOL);
        }else if(age >10 && age< 16){
            throw new GirlException(ResultEnum.MIDDLE_SCHOOL);
        }

    }
```
测试

```
{
"code": 101,
"msg": "还在初中",
"data": null
}
```

玩的开心！