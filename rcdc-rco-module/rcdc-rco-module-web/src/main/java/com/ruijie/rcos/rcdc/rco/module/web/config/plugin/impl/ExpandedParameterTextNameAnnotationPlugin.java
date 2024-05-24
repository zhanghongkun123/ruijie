package com.ruijie.rcos.rcdc.rco.module.web.config.plugin.impl;

import com.ruijie.rcos.rcdc.rco.module.web.config.plugin.ExpandedParameterBuilderPluginAPI;
import com.ruijie.rcos.sk.base.annotation.TextName;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import springfox.documentation.builders.ParameterBuilder;

import java.util.regex.Pattern;

/**
 * Description: Swagger 入参注解扩展插件<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @author linhongjie
 * @see TextName
 */
@Controller
public class ExpandedParameterTextNameAnnotationPlugin implements ExpandedParameterBuilderPluginAPI<TextName> {

    /**
     * 名称校验实现
     *
     * @see com.ruijie.rcos.sk.base.validator.ValidatorUtil
     */
    private static final Pattern TEXT_NAME_PATTERN = Pattern.compile("^[0-9a-zA-Z\\u4e00-\\u9fa5@.-][0-9a-zA-Z\\u4e00-\\u9fa5_@.-]*$");

    /**
     * 设置参数范围
     *
     * @param var0 入参扩展
     * @param var1 注解
     */
    @Override
    public void invoke(ParameterBuilder var0, TextName var1) {
        Assert.notNull(var0, "var0 must not be null");
        Assert.notNull(var1, "var1 must not be null");
        var0.scalarExample(TEXT_NAME_PATTERN.pattern());
        var0.required(true);
    }
}
