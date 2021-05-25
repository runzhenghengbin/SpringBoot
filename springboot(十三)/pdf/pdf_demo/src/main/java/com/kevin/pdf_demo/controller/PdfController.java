package com.kevin.pdf_demo.controller;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;

import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

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
    public void showPdf(HttpServletResponse response) throws IOException, DocumentException, TemplateException, com.lowagie.text.DocumentException {
        //需要填充的数据
        Map<String, Object> data = new HashMap<>(16);
        data.put("name", "kevin");

        String content = freeMarkerRender(data, html);
        response.reset();
        response.setContentType("application/pdf");
        response.addHeader("Content-Disposition", "inline;filename=" + new String("filename.pdf".getBytes("gbk"), "ISO8859-1"));
        //创建pdf
        ServletOutputStream out = response.getOutputStream();
        createPdf(content, out);
        out.flush();
    }

    public void createPdf(String content, OutputStream dest) throws IOException, com.lowagie.text.DocumentException {
//        // step 1
//        Document document = new Document();
//        // step 2
//        PdfWriter writer = PdfWriter.getInstance(document, dest);
//        // step 3
//        document.open();
//        // step 4
//        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
//        fontImp.register(font);
//        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
//                new ByteArrayInputStream(content.getBytes("UTF-8")), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        ITextRenderer renderer = new ITextRenderer();
        ITextFontResolver fontResolver = renderer.getFontResolver();
        fontResolver.addFont(font, BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

        SharedContext sharedContext = renderer.getSharedContext();
        // 解决base64图片支持问题
        sharedContext.setReplacedElementFactory(new B64ImgReplacedElementFactory());
        sharedContext.getTextRenderer().setSmoothingThreshold(0);
        renderer.setDocumentFromString(content);
        renderer.layout();
        renderer.createPDF(dest);






//        document.close();

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
            System.out.println(out.toString());
            return out.toString();
        } finally {
            out.close();
        }
    }


}
