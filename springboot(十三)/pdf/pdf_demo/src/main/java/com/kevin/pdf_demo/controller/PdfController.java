package com.kevin.pdf_demo.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
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
    @Value("${HTML}")
    private String html;
    @Value("${FONT}")
    private String font;
    @Resource
    private Configuration freemarkerConfig;


    @RequestMapping(value = "helloPdf")
    public void showPdf(HttpServletResponse response) throws IOException, DocumentException, TemplateException {
        //需要填充的数据
        Map<String, Object> data = new HashMap<>(16);
        data.put("name", "kevin");

        String content = freeMarkerRender(data, html);
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment;filename=" + new String("filename.pdf".getBytes("gbk"), "ISO8859-1"));
        //创建pdf
        ServletOutputStream out = response.getOutputStream();
        createPdf(content, out);
        out.flush();
    }

    public void createPdf(String content, OutputStream dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, dest);
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
    public String freeMarkerRender(Map<String, Object> data, String htmlTmp) throws IOException, TemplateException {
        Writer out = new StringWriter();
        try {
            Template template = freemarkerConfig.getTemplate(htmlTmp);
            //将合并后的数据和模板写入到流中，这里使用的字符流
            template.process(data, out);
            out.flush();
            return out.toString();
        } finally {
            out.close();
        }
    }


}
