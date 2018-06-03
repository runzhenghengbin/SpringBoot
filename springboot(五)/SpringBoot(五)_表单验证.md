
> 参数校验在我们日常开发中非常常见，最基本的校验有判断属性是否为空、长度是否符合要求等，在传统的开发模式中需要写一堆的 if else 来处理这些逻辑，很繁琐，效率也低。使用 @Valid + BindingResult 就可以优雅地解决这些问题

（1）首先在实体类中增加注解

```
@Entity
public class Girl  {


    @Id
    @GeneratedValue
    private int id;

    @NotEmpty(message = "姓名不能为空")
    private String name;

    @Min(value = 18,message = "未成年禁止入内")
    private int age;

    public Girl() {
    }

```

(2) 在controller 中增加@Valid + BindingResult 

```
    @PostMapping("/girls")
    public Girl girlAdd(@Valid Girl girl, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
 
            System.out.println(bindingResult.getFieldError().getDefaultMessage());
            return  null;
        }
        return girlRepository.save(girl);
    }
```

(3) 测试结果
```
未成年禁止入内
```

> 结果显示均已经触发了校验规则，返回了错误信息，在实际使用过程中可以对错误信息进行包装，最后返回到前端进行展示。