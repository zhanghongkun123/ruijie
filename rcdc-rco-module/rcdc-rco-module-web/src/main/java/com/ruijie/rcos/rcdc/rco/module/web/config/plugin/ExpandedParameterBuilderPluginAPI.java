package com.ruijie.rcos.rcdc.rco.module.web.config.plugin;

import springfox.documentation.builders.ParameterBuilder;

/**
 * Description: Swagger 入参注解扩展插件<br/>
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.05.20
 *
 * @param <T> 数据类型
 * @author linhongjie
 */
public interface ExpandedParameterBuilderPluginAPI<T> {

    /**
     * invoke
     *
     * @param var0 入参扩展
     * @param var1 泛型
     */
    void invoke(ParameterBuilder var0, T var1);
}
