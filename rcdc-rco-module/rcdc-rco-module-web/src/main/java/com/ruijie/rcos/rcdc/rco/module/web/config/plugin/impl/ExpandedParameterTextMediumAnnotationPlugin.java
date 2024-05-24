package com.ruijie.rcos.rcdc.rco.module.web.config.plugin.impl;

import com.ruijie.rcos.rcdc.rco.module.web.config.plugin.ExpandedParameterBuilderPluginAPI;
import com.ruijie.rcos.sk.base.annotation.TextMedium;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.service.AllowableRangeValues;

/**
 * Description: Swagger 入参注解扩展插件<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @author linhongjie
 * @see TextMedium
 */
@Controller
public class ExpandedParameterTextMediumAnnotationPlugin implements ExpandedParameterBuilderPluginAPI<TextMedium> {

    /**
     * 设置参数范围
     *
     * @param var0 入参扩展
     * @param var1 注解
     */
    @Override
    public void invoke(ParameterBuilder var0, TextMedium var1) {
        Assert.notNull(var0, "var0 must not be null");
        Assert.notNull(var1, "var1 must not be null");
        var0.allowableValues(new AllowableRangeValues(String.valueOf(0), false, String.valueOf(TextMedium.TEXT_SIZE), false));
        var0.required(true);
    }
}
