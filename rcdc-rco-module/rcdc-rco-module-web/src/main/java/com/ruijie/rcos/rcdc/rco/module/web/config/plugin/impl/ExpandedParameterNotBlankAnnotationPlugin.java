package com.ruijie.rcos.rcdc.rco.module.web.config.plugin.impl;

import com.ruijie.rcos.rcdc.rco.module.web.config.plugin.ExpandedParameterBuilderPluginAPI;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import springfox.documentation.builders.ParameterBuilder;

/**
 * Description: Swagger 入参注解扩展插件<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @author linhongjie
 * @see NotBlank
 */
@Controller
public class ExpandedParameterNotBlankAnnotationPlugin extends AbstractExpandedParameterRequiredAnnotationPlugin
        implements ExpandedParameterBuilderPluginAPI<NotBlank> {

    /**
     * 设置参数强制必须
     *
     * @param var0 入参扩展
     * @param var1 注解
     */
    @Override
    public void invoke(ParameterBuilder var0, NotBlank var1) {
        Assert.notNull(var0, "var0 must not be null");
        Assert.notNull(var1, "var1 must not be null");
        super.invoke(var0);
    }
}
