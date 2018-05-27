

> 针对controller 中 如何使用注解进行解析

#### @RestController

- 返回数据类型为 Json 字符串，特别适合我们给其他系统提供接口时使用。

#### @RequestMapping

(1) 不同前缀访问同一个方法,此时访问hello和hi 都可以访问到say（）这个方法

```
    @RequestMapping(value = {"/hello","/hi"},method = RequestMethod.GET)
    public String say(){
        return girlProperties.getName();
    }
```
（2）给类一个RequestMapping, 访问时就是：http://localhost:8099/hello/say

```
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = "/say",method = RequestMethod.GET)
    public String say(){
        return girlProperties.getName();
    }
}
```

#### @PathVariable：获取url中的数据

```
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = "/say/{id}",method = RequestMethod.GET)
    public String say(@PathVariable("id") Integer id){
        return "id :"+id;
    }
}
```
访问http://localhost:8099/hello/say/100,  结果如下

```
id :100
```

#### @RequestParam :获取请求参数的值
(1) 正常请求
```
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = "/say",method = RequestMethod.GET)
    public String say(@RequestParam("id") Integer id){
        return "id :"+id;
    }
}
```
访问 http://localhost:8099/hello/say?id=111 结果如下
```
id :111
```
（2）设置参数非必须的，并且设置上默认值
```
@RestController
@RequestMapping("/hello")
public class HelloController {

    @Resource
    private  GirlProperties girlProperties;
    @RequestMapping(value = "/say",method = RequestMethod.GET)
    public String say(@RequestParam(value = "id",required = false,defaultValue = "0") Integer id){
        return "id :"+id;
    }
}

```
访问http://localhost:8099/hello/say  结果如下
```
id :0
```
#### @GetMapping  ，当然也有对应的Post等请求的简化写法

- 这里对应的就是下面这句代码
```
 @GetMapping("/say")
 //等同于下面代码
@RequestMapping(value = "/say",method = RequestMethod.GET)
```