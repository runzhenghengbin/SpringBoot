> 最近，项目上要做个打印的东西，还要预览。我想就直接生成pdf预览，然后用户选择打印

于是，昨天找了找资料。一般用itext 进行转pdf。于是我就用springboot试了试，代码比较简单，现在只是简单的一个实现。

整体流程如下

- 1.获取数据（比如从数据库获取数据）
- 2.通过freemarker 渲染页面
- 3.将渲染的页面进行转换为pdf，放在本地
- 4.读取pdf，实现预览

### 主要代码

#### pom文件
```
<dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <version>RELEASE</version>
            <scope>compile</scope>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.itextpdf/itextpdf -->
        <dependency>
            <groupId>com.itextpdf</groupId>
            <artifactId>itextpdf</artifactId>
            <version>5.5.13</version>
        </dependency>

        <!-- https://mvnrepository.com/artifact/com.itextpdf.tool/xmlworker -->
        <dependency>
            <groupId>com.itextpdf.tool</groupId>
            <artifactId>xmlworker</artifactId>
            <version>5.5.13</version>
        </dependency>


        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>2.5</version>
        </dependency>
    </dependencies>
```

#### PdfController代码

```
package com.kevin.pdf_demo.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: kevin
 * @Date: 2018/11/16
 */
@RestController
public class PdfController {
    @Value("${DEST}")
    private String dest;
    @Value("${HTML}")
    private String html;
    @Value("${FONT}")
    private String font;
    private static Configuration freemarkerCfg = null;


    @RequestMapping(value = "helloPdf")
    public void showPdf(HttpServletResponse response) throws IOException, DocumentException {
        //需要填充的数据
        Map<String, Object> data = new HashMap<>(16);
        data.put("name", "kevin");

        String content = freeMarkerRender(data,html);
        //创建pdf
        createPdf(content, dest);

        // 读取pdf并预览
        readPdf(response);

    }

    public  void createPdf(String content,String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(font);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(content.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        document.close();

    }

    /**
     * freemarker渲染html
     */
    public  String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        Writer out = new StringWriter();

        try {
            // 获取模板,并设置编码方式
            setFreemarkerCfg();
            Template template = freemarkerCfg.getTemplate(htmlTmp);
            template.setEncoding("UTF-8");
            //将合并后的数据和模板写入到流中，这里使用的字符流
            template.process(data, out);
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    /**
     * 设置freemarkerCfg
     */
    private void setFreemarkerCfg() {
        freemarkerCfg = new Configuration();
        //freemarker的模板目录
        try {
            freemarkerCfg.setDirectoryForTemplateLoading(new ClassPathResource("template").getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取本地pdf,这里设置的是预览
     */
    private void readPdf(HttpServletResponse response) {
        response.reset();
        response.setContentType("application/pdf");
        try {
            File file = new File(dest);
            FileInputStream fileInputStream = new FileInputStream(file);
            OutputStream outputStream = response.getOutputStream();
            IOUtils.write(IOUtils.toByteArray(fileInputStream), outputStream);
            response.setHeader("Content-Disposition",
                    "inline; filename= file");
            outputStream.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}

```

### 运行效果
![](https://img2018.cnblogs.com/blog/891580/201811/891580-20181117163612091-1579251056.png)

### 设置头信息使浏览器下载文件或预览
#### 强制浏览器下载
```
response.setHeader("content-disposition", "attachment;filename=" + realName);
```
#### 浏览器尝试打开,支持office online或浏览器预览pdf功能
```
response.setHeader("content-disposition", "inline;filename=" + realName);
```

完整代码 : [github](https://github.com/runzhenghengbin/SpringBoot)

玩的开心！