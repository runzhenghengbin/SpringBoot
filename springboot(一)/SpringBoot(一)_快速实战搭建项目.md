

> 现在在学习springboot 相关的知识，感觉真的很好用，用idea 进行开发，根据慕课网和纯洁的微笑的课程。进行总结下。

##### 使用idea创建springboot项目

（1）单击 File | New | Project… 命令，弹出新建项目框。

（2）选择 Spring Initializr 选项，单击 Next 按钮，也会出现上述类似的配置界面，Idea 帮我们做了集成。

![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524062212.png)

（3）选择web,这里我选择的版本是2.0.2，单击 Next 按钮，最后确定信息无误单击 Finish 按钮。

![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524062558.png)

（4）删除无用的文件

![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524062727.png)

##### 项目结构
- src/main/java：程序开发以及主程序入口
- src/main/resources：配置文件
- src/test/java：测试程序

##### 简单web实战

（1） 创建controller 类

```
@RestController
public class HelloController {
    @RequestMapping(value = {"/hello"},method = RequestMethod.GET)
    public String say(){
        return "Hello Spring Boot!";
    }
}
```
（2）启动主程序，打开浏览器访问 http://localhost:8080/hello，就可以看到以下内容
```
Hello Spring Boot!
```
##### 三种启动方式

（1）启动主程序进行启动

（2）进入工程目录, mvn  spring-boot:run

![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524064821.png)

 (3) jar包启动

- 执行mvn install
![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524064906.png)
- 执行 java -jar 
![enter image description here](http://p9d3cdwaf.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180524065026.png)

##### 单元测试
在 Spring Boot 中，Spring 给出了一个简单的解决方案；使用 mockmvc 进行 web 测试，mockmvc 内置了很多工具类和方法，可以模拟 post、get 请求，并且判断返回的结果是否正确等，也可以利用print()打印执行结果。

```
@SpringBootTest
public class GirlApplicationTests {

    private MockMvc mockMvc;

    @Before
    public void setup(){
        mockMvc = MockMvcBuilders.standaloneSetup( new HelloController()).build();
    }
    @Test
    public void contextLoads() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/hello").accept(MediaType.APPLICATION_JSON_UTF8)).andDo(print());
    }

}
```
运行结果

```
MockHttpServletRequest:
      HTTP Method = GET
      Request URI = /hello
       Parameters = {}
          Headers = {Accept=[application/json;charset=UTF-8]}
             Body = <no character encoding set>
    Session Attrs = {}

Handler:
             Type = com.imooc.controller.HelloController
           Method = public java.lang.String com.imooc.controller.HelloController.say()

...
MockHttpServletResponse:
           Status = 200
    Error message = null
          Headers = {Content-Type=[application/json;charset=UTF-8], Content-Length=[18]}
     Content type = application/json;charset=UTF-8
             Body = Hello Spring Boot!
    Forwarded URL = null
   Redirected URL = null
          Cookies = []
```

##### 总结
使用 Spring Boot 可以非常方便、快速搭建项目，而不用关心框架之间的兼容性、适用版本等各种问题，我们想使用任何东西，仅仅添加一个配置就可以，所以使用 Sping Boot 非常适合构建微服务。