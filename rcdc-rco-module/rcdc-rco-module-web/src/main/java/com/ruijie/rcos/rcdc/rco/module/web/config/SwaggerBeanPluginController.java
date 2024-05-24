package com.ruijie.rcos.rcdc.rco.module.web.config;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersion;
import com.ruijie.rcos.rcdc.rco.module.web.config.annotation.ApiVersions;
import com.ruijie.rcos.rcdc.rco.module.web.config.define.HtmlTag;
import com.ruijie.rcos.rcdc.rco.module.web.config.util.SwaggerHtmlRenderUtils;

import io.swagger.annotations.ApiOperation;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;
import springfox.documentation.spring.web.DescriptionResolver;
import springfox.documentation.swagger.common.SwaggerPluginSupport;

/**
 * Description: Swagger 封装每个接口页面渲染的切面实现<br/>
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
@Controller
public class SwaggerBeanPluginController implements OperationBuilderPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerBeanPluginController.class);

    /**
     * 描述解析器，用于渲染接口页面
     */
    private final DescriptionResolver descriptions;

    @Autowired
    public SwaggerBeanPluginController(DescriptionResolver descriptions) {
        this.descriptions = descriptions;
    }

    @Override
    public void apply(OperationContext context) {

        Assert.notNull(context, "context must not be null");

        // 通过 url 查找 Method
        Method method = context.getDocumentationContext().getRequestHandlers().stream()
                .filter(handler -> handler.getPatternsCondition().getPatterns().contains(context.requestMappingPattern())).findFirst()
                .map(handler -> handler.getHandlerMethod().getMethod()).orElse(null);
        if (method == null) {
            LOGGER.error("Swagger 通过 url 查找 Method 为空，无法进行页面加强渲染操作");
            return;
        }

        ApiOperation apiOperationOp = context.findAnnotation(ApiOperation.class).orNull();
        if (apiOperationOp == null) {
            LOGGER.debug("接口 {}.{} 未包含版本信息，无须进行页面加强渲染操作", method.getDeclaringClass().getName(), method.getName());
            return;
        }

        LOGGER.info("渲染 Swagger 接口 {}.{} 页面渲染开始", method.getDeclaringClass().getName(), method.getName());

        // 构造
        StringBuilder notes = new StringBuilder(apiOperationOp.notes());
        SwaggerHtmlRenderUtils.addLine(notes);

        // 载入 CSS 样式
        packageCSSProperties(notes);

        // 载入方法信息
        packageMethodInfo(method, notes);

        // 载入 Table 样式
        packageTable(context, notes);

        // 载入编译时间
        packageCompile(notes);

        // 触发渲染解析
        context.operationBuilder().notes(descriptions.resolve(notes.toString()));

        LOGGER.info("渲染 Swagger 接口 {}.{} 页面渲染完成", method.getDeclaringClass().getName(), method.getName());
    }

    /**
     * 输出编译时间
     * 
     * @param notes html 渲染
     */
    private void packageCompile(StringBuilder notes) {

        StringBuilder compileContent = new StringBuilder();

        // 添加标题
        SwaggerHtmlRenderUtils.addElement(compileContent, HtmlTag.B, null, null, "编译时间");
        SwaggerHtmlRenderUtils.overElement(compileContent, HtmlTag.H3);

        // 添加内容
        SwaggerHtmlRenderUtils.addElement(compileContent, HtmlTag.SMALL, "class", "css_compile", SwaggerConfig.COMPLIE_DATE);
        SwaggerHtmlRenderUtils.addLine(compileContent);

        // 合并样式
        notes.append(compileContent);
    }

    /**
     * 输出表单样式
     * 
     * @param context 上下文
     * @param notes html 渲染
     */
    private void packageTable(OperationContext context, StringBuilder notes) {

        ApiVersions apiVersionArr = context.findAnnotation(ApiVersions.class).orNull();
        if (apiVersionArr == null) {
            LOGGER.error("Swagger 未找到版本控制注解，无法处理表单样式");
            return;
        }

        // 版本注解排序
        List<ApiVersion> apiVersionList = Arrays.asList(apiVersionArr.value());
        apiVersionList.sort((version1, version2) -> {

            String[] version1Arr = version1.value().getMavenVersion().split("\\.");
            String[] version2Arr = version2.value().getMavenVersion().split("\\.");

            int minSize = Math.min(version1Arr.length, version2Arr.length);
            for (int index = 0; index < minSize; index++) {
                if (Integer.parseInt(version1Arr[index]) < Integer.parseInt(version2Arr[index])) {
                    return -1;
                } else if (Integer.parseInt(version1Arr[index]) > Integer.parseInt(version2Arr[index])) {
                    return 1;
                }
            }
            return 0;
        });

        // 已处理记录
        List<String> dealedRecordList = new ArrayList<>();
        // 表单标题内容渲染
        StringBuilder tableInfo = new StringBuilder();
        // 表单头部内容渲染
        StringBuilder tableThreadInfo = new StringBuilder();
        // 表单主体内容渲染
        StringBuilder tableTbodyInfo = new StringBuilder();
        // 表单全局内容渲染
        StringBuilder tableContentInfo = new StringBuilder();

        SwaggerHtmlRenderUtils.addElement(tableInfo, HtmlTag.B, null, null, "版本履历信息");
        SwaggerHtmlRenderUtils.overElement(tableInfo, HtmlTag.H3);
        notes.append(tableInfo);

        // 封装展示 HTML
        SwaggerHtmlRenderUtils.addElement(tableThreadInfo, HtmlTag.TH, "style", "text-align: center;width: 8%;", "产品版本");
        SwaggerHtmlRenderUtils.addElement(tableThreadInfo, HtmlTag.TH, "style", "text-align: center;width: 6%;", "代码版本");
        SwaggerHtmlRenderUtils.addElement(tableThreadInfo, HtmlTag.TH, "style", "text-align: center", "修订履历");
        SwaggerHtmlRenderUtils.overElement(tableThreadInfo, HtmlTag.TR);
        SwaggerHtmlRenderUtils.overElement(tableThreadInfo, HtmlTag.THEAD);
        tableContentInfo.append(tableThreadInfo);

        for (ApiVersion apiVersion : apiVersionList) {
            if (dealedRecordList.contains(apiVersion.value().getMavenVersion())) {
                // 已处理的注解不再重复处理
                continue;
            }
            StringBuilder temps = new StringBuilder();
            if (apiVersion.descriptions().length == 0 || (apiVersion.descriptions().length == 1
                    && (StringUtils.isEmpty(apiVersion.descriptions()[0]) || "无".equals(apiVersion.descriptions()[0])))) {
                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, null, null, apiVersion.value().getProductVersion());
                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, "style", "text-align: center;", apiVersion.value().getMavenVersion());
                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, null, null, "无");
            } else {
                // 封装说明
                StringBuilder description = new StringBuilder();

                int index = 1;
                for (String annotationDesc : apiVersion.descriptions()) {
                    SwaggerHtmlRenderUtils.addLine(description.append(String.format("%d. %s", index++, annotationDesc)));
                }

                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, null, null, apiVersion.value().getProductVersion());
                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, "style", "text-align: center;", apiVersion.value().getMavenVersion());
                SwaggerHtmlRenderUtils.addElement(temps, HtmlTag.TD, null, null, description.toString());
            }
            SwaggerHtmlRenderUtils.overElement(temps, HtmlTag.TR);
            tableTbodyInfo.append(temps);
            dealedRecordList.add(apiVersion.value().getMavenVersion());
        }
        SwaggerHtmlRenderUtils.overElement(tableThreadInfo, HtmlTag.TBODY);
        tableContentInfo.append(tableTbodyInfo);

        // 覆盖样式
        SwaggerHtmlRenderUtils.overElement(tableContentInfo, HtmlTag.TABLE, "class", "hovertable", null);

        // 合并至主页面
        notes.append(tableContentInfo);
        SwaggerHtmlRenderUtils.addLine(notes);
    }

    /**
     * 添加注解对应方法相关信息
     * 
     * @param notes html 渲染
     */
    private void packageMethodInfo(Method method, StringBuilder notes) {

        StringBuilder methodInfo = new StringBuilder();

        // 添加标题
        SwaggerHtmlRenderUtils.addElement(notes, HtmlTag.B, null, null, "接口源码信息");
        SwaggerHtmlRenderUtils.overElement(notes, HtmlTag.H3);

        // 添加方法路径内容
        StringBuilder methodPath = new StringBuilder("方法路径");
        SwaggerHtmlRenderUtils.overElement(methodPath, HtmlTag.DT, "class", "col-sm-4");

        final String classPath = method.getDeclaringClass().getName();
        final String methodName = method.getName();
        SwaggerHtmlRenderUtils.addElement(methodPath, HtmlTag.DD, "class", "col-sm-8", String.format("%s.%s", classPath, methodName));
        methodInfo.append(methodPath);

        // 添加异常类别内容
        StringBuilder exceptionClass = new StringBuilder("异常类别");
        SwaggerHtmlRenderUtils.overElement(exceptionClass, HtmlTag.DT, "class", "col-sm-4");

        StringBuilder exceptionTypes = new StringBuilder();
        if (method.getExceptionTypes().length == 0) {
            SwaggerHtmlRenderUtils.addLine(exceptionTypes.append("无"));
        } else {
            for (Class<?> exceptionType : method.getExceptionTypes()) {
                SwaggerHtmlRenderUtils.addLine(exceptionTypes.append(exceptionType));
            }
        }
        SwaggerHtmlRenderUtils.addElement(exceptionClass, HtmlTag.DD, "class", "col-sm-8", exceptionTypes.toString());
        methodInfo.append(exceptionClass);

        // 添加样式
        SwaggerHtmlRenderUtils.overElement(methodInfo, HtmlTag.DL, "class", "row");
        SwaggerHtmlRenderUtils.overElement(methodInfo, HtmlTag.DIV, "class", "card-body");

        // 合并至主页面
        notes.append(methodInfo);
    }

    /**
     * 添加主题样式
     * 
     * @param notes html 渲染
     */
    private void packageCSSProperties(StringBuilder notes) {

        StringBuilder cssStyle = new StringBuilder();

        // 添加 css 标签
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "table.hovertable", "font-family: verdana,arial,sans-serif", "font-size: 11px", "color: #333333",
                "border-color: #999999", "border-collapse: collapse", "width: 100%", "border-width: 1px");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "table.hovertable th", "background-color: #c3dde0", "border-width: 1px", "padding: 8px",
                "border-style: solid", "border-color: #a9c6c9");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "table.hovertable tr", "background-color: #d4e3e5");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "table.hovertable td", "border-width: 1px", "padding: 8px", "border-style: solid",
                "border-color: #a9c6c9");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, ".card-body", "-webkit-flex: 1 1 auto", "-ms-flex: 1 1 auto", "flex: 1 1 auto", "min-height: 1px",
                "padding: 8px");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, ".row", "display: -webkit-flex", "display: -ms-flexbox", "display: flex",
                "-webkit-flex-wrap: wrap", "-ms-flex-wrap: wrap", "flex-wrap: wrap", "margin-right: -7.5px", "margin-left: -7.5px");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, ".col-sm-4", "-webkit-flex: 0 0 10%", "-ms-flex: 0 0 10%", "flex: 0 0 10%", "max-width: 10%");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, ".col-sm-8", "-webkit-flex: 0 0 90%", "-ms-flex: 0 0 90%", "flex: 0 0 90%", "max-width: 90%");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "dd", "margin-bottom: .5rem", "margin-left: 0");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, "dt", "font-weight: 700");
        SwaggerHtmlRenderUtils.addCSSAttr(cssStyle, ".css_compile", "display: inline-block", "padding: .25em .4em", "font-size: 75%",
                "font-weight: 700", "line-height: 1", "text-align: center", "white-space: nowrap", "vertical-align: baseline", "font-size: 15px",
                "transition: color .15s ease-in-out,background-color .15s ease-in-out,border-color .15s ease-in-out,box-shadow .15s ease-in-out",
                "color: #fff", "background-color: #7ca3a7b3");

        // 添加 style 标签
        SwaggerHtmlRenderUtils.overElement(cssStyle, HtmlTag.STYLE, "type", "text/css");

        // 合并至主页面
        notes.append(cssStyle);
    }

    @Override
    public boolean supports(@NonNull DocumentationType delimiter) {

        Assert.notNull(delimiter, "delimiter must be not null");
        return SwaggerPluginSupport.pluginDoesApply(delimiter);
    }
}
