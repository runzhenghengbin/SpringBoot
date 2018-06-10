



> MyBatis 是一款标准的 ORM 框架，被广泛的应用于各企业开发中。具体细节这里就不在叙述，大家自行查找资料进行学习下。

####   加载依赖

```
<dependency>
    <groupId>org.mybatis.spring.boot</groupId>
    <artifactId>mybatis-spring-boot-starter</artifactId>
    <version>1.3.1</version>
</dependency>
```
####  application 配置(application.yml)

```
mybatis:
  config-location: classpath:mybatis/mybatis-config.xml
  mapper-locations: classpath:mybatis/mapper/*.xml
  type-aliases-package: com.zhb.entity
spring:
  datasource:
    driver-class-name: com.mysql.jdbc.Driver
    url: jdbc:mysql://localhost:3306/test?useUnicode=true&characterEncoding=utf-8
    password: root
    username: root
```

####  启动类
在启动类中添加对 Mapper 包扫描@MapperScan，Spring Boot 启动的时候会自动加载包路径下的 Mapper。

```
@Spring BootApplication
@MapperScan("com.zhb.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```
或者直接在 Mapper 类上面添加注解@Mapper，建议使用上面那种，不然每个 Mapper 加个注解会很麻烦。

####  代码展示
这里只说下，我们在controller 中 尽量使用 Restful 风格

```
@RestController
public class UserController {

    @Resource
    private UserMapper userMapper;


    @GetMapping(value = "/users")
    public List<UserEntity> getUsers() {
        List<UserEntity> users=userMapper.getAll();
        return users;
    }



    @GetMapping(value = "/users/{id}")
    public UserEntity getUser(@PathVariable(value = "id") Long id) {
        UserEntity user=userMapper.getOne(id);
        return user;
    }

    @PostMapping("/users")
    public void save(UserEntity user) {
        userMapper.insert(user);
    }

    @PutMapping("/users")
    public void update(UserEntity user) {
        userMapper.update(user);
    }

    @DeleteMapping(value="/users/{id}")
    public void delete(@PathVariable("id") Long id) {
        userMapper.delete(id);
    }


}
```

#### 实际开发常遇问题

在平常开发中，我们经常会遇到返回树结构，如下展示

```
[
  {
    "id": "1",
    "name": "山东",
    "pid": "0",
    "children": [
      {
        "id": "2",
        "name": "济南",
        "pid": "1",
        "children": [
          {
            "id": "3",
            "name": "高新区",
            "pid": "2",
            "children": null
          }
        ]
      }
    ]
  }
]
```

##### (1) java中 通过对list数据进行拼接成树

如下面的方法（这里直接声明了list，往里面添加数据，来演示查询出的list集合）

```
@GetMapping(value = "/area")
    public   List<AreaTreeEntity> get(){
        
        List<AreaTreeEntity> res= new ArrayList<>();
        Map<String,List<AreaTreeEntity>> childMap = new HashMap<>(16);

		//为了方便测试，构建list数据
		List<AreaTreeEntity> s =  new ArrayList<>();
        AreaTreeEntity a = new AreaTreeEntity();
        a.setId("1");
        a.setName("山东");
        a.setPid("0");
        s.add(a);

        AreaTreeEntity a1 = new AreaTreeEntity();
        a1.setId("2");
        a1.setName("济南");
        a1.setPid("1");
        s.add(a1);

        AreaTreeEntity a2 = new AreaTreeEntity();
        a2.setId("3");
        a2.setName("高新区");
        a2.setPid("2");
        s.add(a2);

        for(AreaTreeEntity entity : s){

            if ("0".equals(entity.getPid())) {

                res.add(entity);
            }
            else {

                List<AreaTreeEntity> childList = (childMap.containsKey(entity.getPid())) ? childMap.get(entity.getPid()) : new ArrayList<>();
                childList.add(entity);

                if (!childMap.containsKey(entity.getPid())){
                    childMap.put(entity.getPid(),childList);
                }
            }
        }

        for(AreaTreeEntity entity : res){
            findChild(entity,childMap);
        }

        return res;
    }

    public void findChild(AreaTreeEntity entity,Map<String,List<AreaTreeEntity>> childMap){

        if (childMap.containsKey(entity.getId())){
            List<AreaTreeEntity> chidList = childMap.get(entity.getId());
            for (AreaTreeEntity childEntity : chidList){
                findChild(childEntity,childMap);
            }
            entity.setChildren(chidList);
        }
    }
```

经过验证，返回数据如下
```
[
  {
    "id": "1",
    "name": "山东",
    "pid": "0",
    "children": [
      {
        "id": "2",
        "name": "济南",
        "pid": "1",
        "children": [
          {
            "id": "3",
            "name": "高新区",
            "pid": "2",
            "children": null
          }
        ]
      }
    ]
  }
]
```

##### (2) 使用mabatis的collection   直接查询出树结构

```

<mapper namespace="com.zhb.mapper.AreaTreeMapper" >
    <resultMap id="TreeResultMap" type="com.zhb.entity.AreaTreeEntity" >
        <result column="id" property="id" jdbcType="VARCHAR" />
        <result column="name" property="name" jdbcType="VARCHAR" />
        <collection property="children" column="id" ofType="com.zhb.entity.AreaTreeEntity" javaType="ArrayList" select="selectChildrenById"/>
    </resultMap>


    <select id="getAreaTree" resultMap="TreeResultMap">
        select  id,name  from  area where parent_id  = '0'
    </select>

    <select id="selectChildrenById" parameterType="java.lang.String" resultMap="TreeResultMap">
        select id,name from  area where parent_id  = #{id}
    </select>

</mapper>
```

完整代码下载：[github](https://github.com/runzhenghengbin/SpringBoot.git)
 
 注：项目中加入swagger ，方便大家测试，直接访问 http://localhost:8080/swagger-ui.html