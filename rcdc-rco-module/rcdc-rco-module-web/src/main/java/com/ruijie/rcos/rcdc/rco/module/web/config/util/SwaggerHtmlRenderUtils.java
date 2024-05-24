package com.ruijie.rcos.rcdc.rco.module.web.config.util;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.ruijie.rcos.rcdc.rco.module.web.config.define.HtmlTag;

import javax.annotation.Nullable;

/**
 * Description: HTML 渲染标签实现<br/>
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
public class SwaggerHtmlRenderUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SwaggerHtmlRenderUtils.class);

    /**
     * 增加行
     *
     * @param builder 内容
     */
    public static void addLine(StringBuilder builder) {

        Assert.notNull(builder, "builder must be not null");
        try {
            builder.append(HtmlTag.LOPENING.show()).append(HtmlTag.BR.show()).append(HtmlTag.ROPENING_WITH_LSPRIT.show());
        } catch (Exception ex) {
            LOGGER.error("swagger html render error.", ex);
        }
    }

    /**
     * 包含元素
     * 
     * @param builder 内容
     * @param element 元素类型
     */
    public static void overElement(StringBuilder builder, HtmlTag element) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        overElement(builder, element, null, null, null);
    }

    /**
     * 增加元素
     *
     * @param builder 内容
     * @param element 元素类型
     */
    @SuppressWarnings("unused")
    public static void addElement(StringBuilder builder, HtmlTag element) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        addElement(builder, element, null, null, null);
    }

    /**
     * 包含元素
     * 
     * @param builder 内容
     * @param element 元素类型
     * @param key 元素属性键
     * @param value 元素属性值
     */
    public static void overElement(StringBuilder builder, HtmlTag element, @Nullable String key, @Nullable String value) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        overElement(builder, element, key, value, null);
    }

    /**
     * 增加元素
     * 
     * @param builder 内容
     * @param element 元素类型
     * @param key 元素属性键
     * @param value 元素属性值
     */
    @SuppressWarnings("unused")
    public static void addElement(StringBuilder builder, HtmlTag element, @Nullable String key, @Nullable String value) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        addElement(builder, element, key, value, null);
    }

    /**
     * 包含元素
     * 
     * @param builder 内容
     * @param element 元素类型
     * @param key 元素属性键
     * @param value 元素属性值
     * @param content 上下文
     */
    public static void overElement(StringBuilder builder, HtmlTag element, @Nullable String key, @Nullable String value, @Nullable String content) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        try {
            builder.insert(0, HtmlTag.ROPENING.show()).insert(0, packageTagProperty(key, value)).insert(0, element.show()).insert(0,
                    HtmlTag.LOPENING.show());
            builder.append(Optional.ofNullable(content).orElse(HtmlTag.ONEBLANK.show()));
            builder.append(HtmlTag.LOPENING_WITH_RSPRIT.show()).append(element.show()).append(HtmlTag.ROPENING.show());
        } catch (Exception ex) {
            LOGGER.error("swagger html render error.", ex);
        }
    }

    /**
     * 增加元素
     *
     * @param builder 内容
     * @param element 元素类型
     * @param key 元素属性键
     * @param value 元素属性值
     * @param content 上下文
     */
    public static void addElement(StringBuilder builder, HtmlTag element, @Nullable String key, @Nullable String value, @Nullable String content) {

        Assert.notNull(builder, "builder must be not null");
        Assert.notNull(element, "element must be not null");
        try {
            builder.append(HtmlTag.LOPENING.show()).append(element).append(packageTagProperty(key, value)).append(HtmlTag.ROPENING.show());
            builder.append(Optional.ofNullable(content).orElse(HtmlTag.ONEBLANK.show()));
            builder.append(HtmlTag.LOPENING_WITH_RSPRIT.show()).append(element).append(HtmlTag.ROPENING.show());
        } catch (Exception ex) {
            LOGGER.error("swagger html render error.", ex);
        }
    }

    /**
     * 增加样式
     * 
     * @param builder 内容
     * @param key 样式
     * @param properties 样式属性
     */
    public static void addCSSAttr(StringBuilder builder, @Nullable String key, @Nullable String... properties) {

        Assert.notNull(builder, "builder must be not null");
        try {
            if (StringUtils.isEmpty(key) || StringUtils.isEmpty(properties) || properties.length == 0) {
                builder.append(HtmlTag.ONEBLANK.show());
            } else {
                StringBuilder inline = new StringBuilder();
                for (String property : properties) {
                    inline.append(property).append(";");
                }
                builder.append(HtmlTag.ONEBLANK.show()).append(key).append(HtmlTag.LBRACE.show()).append(inline).append(HtmlTag.RBRACE.show())
                        .append(HtmlTag.ONEBLANK.show());
            }

        } catch (Exception ex) {
            LOGGER.error("swagger html render error.", ex);
        }
    }

    /**
     * 增加样式
     * 
     * @param builder 内容
     * @param key 样式
     * @param properties 样式属性
     */
    @SuppressWarnings("unused")
    public static void addCSSAttr(StringBuilder builder, @Nullable String key, @Nullable List<String> properties) {

        Assert.notNull(builder, "builder must be not null");
        addCSSAttr(builder, key, Optional.ofNullable(properties).map(t -> t.toArray(new String[0])).orElse(null));
    }

    private static String packageTagProperty(String key, String value) {

        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(value)) {
            return HtmlTag.ONEBLANK.show();
        } else {
            return HtmlTag.ONEBLANK.show() + key + "=\"" + value + "\"" + HtmlTag.ONEBLANK.show();
        }
    }
}
