package com.ruijie.rcos.rcdc.rco.module.web.config.plugin.impl;

import org.springframework.util.Assert;
import springfox.documentation.builders.ParameterBuilder;

/**
 * Description: Swagger 入参注解扩展插件<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @author linhongjie
 */
public abstract class AbstractExpandedParameterRequiredAnnotationPlugin {

    /**
     * 设置参数强制必须
     *
     * @param var0 入参扩展
     */
    public void invoke(ParameterBuilder var0) {
        Assert.notNull(var0, "var0 must not be null");
        var0.required(true);
    }
}
