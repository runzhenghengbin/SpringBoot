

>  swagger是一个功能强大的api框架，它的集成非常简单，不仅提供了在线文档的查阅，而且还提供了在线文档的测试。

(1) 引入依赖,我们选择现在最新的版本

```
<dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.8.0</version>
        </dependency>

        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.8.0</version>
        </dependency>
```

（２）　写配置类

```
/**
 * @Auther: curry
 * @Date: 2018/6/3 12:46
 * @Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerProperties {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.imooc.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("springboot利用swagger构建api文档")
                .description("")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}

```
(3) 在controller 中进行引入注解（当然也可以不写）


```

/**
 * @Auther: curry
 * @Date: 2018/5/28 21:57
 * @Description:
 */
@RestController
public class GirlController {
    private  final static Logger logger = LoggerFactory.getLogger(GirlController.class);
    @Resource
    private GirlRepository girlRepository;

    @Resource
    private GirlService girlService;

    @ApiOperation(value="获取女孩列表", notes="获取女孩列表")
    @GetMapping("/girls")
    public List<Girl> getList(){
        logger.info("getList");
       return girlRepository.findAll();
    }

    @ApiOperation(value = "添加女孩" ,notes="添加女孩")
    @PostMapping("/girls")
    public Result<Girl> girlAdd(@Valid Girl girl, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
          return ResultUtil.error(1,bindingResult.getFieldError().getDefaultMessage());
        }
        return  ResultUtil.success(girlRepository.save(girl));

    }

    @ApiOperation(value = "查找女孩",notes = "查找女孩")
    @ApiImplicitParam(name = "id" ,value ="查找女孩" ,required = true,dataType = "int",paramType = "path")
    @GetMapping(value = "/girls/{id}")
    public  Girl find(@PathVariable(value = "id") Integer id){
        return girlRepository.findById(id).get();
    }


    @ApiOperation(value = "修改女孩",notes = "根据id查找女孩并修改")
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

    @ApiOperation(value = "删除女孩",notes = "根据id删除女孩")
    @DeleteMapping(value = "/girls/{id}")
    public void  delete(@PathVariable(value = "id") Integer id){
        girlRepository.deleteById(id);
    }

    @ApiOperation(value = "根据年龄查询女孩")
    @GetMapping(value = "/girls/age/{age}")
    public List<Girl> findByAge(@PathVariable(value = "age") Integer age){
        return girlRepository.findByAge(age);
    }



    @GetMapping(value = "/girls/getAge/{id}")
    public void getAge(@PathVariable("id") Integer id) throws Exception {
        girlService.getAge(id);
    }
}

```

(4) 访问http://localhost:8099/swagger-ui.html 
![enter image description here](http://p7zk4x9pv.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180603172418.png)
![enter image description here](http://p7zk4x9pv.bkt.clouddn.com/TIM%E6%88%AA%E5%9B%BE20180603172621.png)

（5）当然，你也可以不在controller 中增加这个注解，是不是感觉很方便，很能测试，just do it!