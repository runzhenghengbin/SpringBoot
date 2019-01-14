> 现在项目上要求实现套打，结果公司里有个人建议用JaperReport进行实现，就进入这个东西的坑中。好歹经过挣扎现在已经脱离此坑中。现在我也是仅能实现读取数据库数据转成pdf进行展示，包括中文的展示。于是记录下整个过程。

###  1.下载 安装 Jaspersoft Studio

下载地址：https://community.jaspersoft.com/community-download

![](https://img2018.cnblogs.com/blog/891580/201901/891580-20190114182413464-839905257.png)


我下载的就是6.6.0这个版本，Jasper Report 分为专业版（收费）和社区版（免费），这里下载的社区版本。

### 2.设计模板

 对这个模板设计我也不是很熟悉，这里我就不展开说明了。大家自行设计吧

#### 2.1 导入并设置字体

这里需要注意一点就是，这个设计出的不显示中文，需要导入字体。

点击window->Preferences->jaspersoft Studio->font->add

![](https://img2018.cnblogs.com/blog/891580/201901/891580-20190114191800124-2112897974.png)




设置完成后，点击Finish.

#### 2.2 设计模板选择设置的myfont字体

查看jrxml文件后，设置后会多出`font`标签

```
                <font fontName="myfont" size="26" pdfFontName="" pdfEncoding="UniGB-UCS2-H" isPdfEmbedded="true"/>
                </textElement>
                <text><![CDATA[三年二班学生信息]]></text>
```
#### 2.3 导出myfont字体jar包

点击window->Preferences->jaspersoft Studio->font

选择myfont 点击export 导出。这里我保存为kevin.jar

#### 2.4 将kevin.jar 安装到本地maven库

```
mvn install:install-file -Dfile=kevin.jar -DgroupId=com.kevin -DartifactId=myfont -Dversion=1.0.0 -Dpackaging=jar
```

至此，前期的准备工作都已经完成。

### 3.构建springboot项目
这里我使用的版本是 `2.1.1.RELEASE`

#### 3.1 pom文件

```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jdbc</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>net.sf.jasperreports</groupId>
            <artifactId>jasperreports</artifactId>
            <version>6.6.0</version>
        </dependency>
        <dependency>
            <groupId>com.kevin</groupId>
            <artifactId>myfont</artifactId>
            <version>1.0.0</version>
        </dependency>
    </dependencies>
```

**注意 ,我这里引用的是我设置的`com.kevin.myfont`版本，个人根据自己步骤2.4设置的情况进行修改**

#### 3.2 application.yml

```
# Server settings
server:
  port: 8080

# SPRING PROFILES
spring:
  http:
    encoding.charset: UTF-8
    encoding.enable: true
    encoding.force: true
  datasource:
    url: jdbc:mysql://127.0.0.1:3306/kevin?characterEncoding=utf8&useSSL=false&serverTimezone=GMT%2B8
    username: root
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver


```

#### 3.3 ReportController 代码

```
@RestController
public class ReportController {

    @Resource
    private DataSource dataSource;


    /**
     * 转换为pdf展示
     *
     * @param reportName
     * @param parameters
     * @param response
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws JRException
     * @throws IOException
     */
    @GetMapping("/{reportName}")
    public void getReportByParam(
            @PathVariable("reportName") final String reportName,
            @RequestParam(required = false) Map<String, Object> parameters,
            HttpServletResponse response) throws SQLException, ClassNotFoundException, JRException, IOException {

        parameters = parameters == null ? new HashMap<>() : parameters;
        //获取文件流
        ClassPathResource resource = new ClassPathResource("jaspers" + File.separator + reportName + ".jasper");
        InputStream jasperStream = resource.getInputStream();

        JasperReport jasperReport = (JasperReport) JRLoader.loadObject(jasperStream);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource.getConnection());
        // JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, new JREmptyDataSource());
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline;");
        final OutputStream outputStream = response.getOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
    }
}
```

#### 3.4 完整目录结构

![](https://img2018.cnblogs.com/blog/891580/201901/891580-20190114185908162-419343926.png)



### 4.运行效果

启动项目，运行`http://localhost:8080/demo`

![](https://img2018.cnblogs.com/blog/891580/201901/891580-20190114190035597-1779703786.png)

### 完整代码

github：https://github.com/runzhenghengbin/SpringBoot

玩的开心！