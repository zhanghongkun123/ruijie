package com.ruijie.rcos.rcdc.rco.module.web.config.plugin.impl;

import com.ruijie.rcos.rcdc.rco.module.web.config.plugin.ExpandedParameterBuilderPluginAPI;
import com.ruijie.rcos.sk.base.annotation.Size;
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
 * @see Size
 */
@Controller
public class ExpandedParameterSizeAnnotationPlugin implements ExpandedParameterBuilderPluginAPI<Size> {

    /**
     * 设置参数范围
     *
     * @param var0 入参扩展
     * @param var1 注解
     */
    @Override
    public void invoke(ParameterBuilder var0, Size var1) {
        Assert.notNull(var0, "var0 must not be null");
        Assert.notNull(var1, "var1 must not be null");
        var0.allowableValues(new AllowableRangeValues(String.valueOf(var1.min()), false, String.valueOf(var1.max()), false));
        var0.required(true);
    }
}
