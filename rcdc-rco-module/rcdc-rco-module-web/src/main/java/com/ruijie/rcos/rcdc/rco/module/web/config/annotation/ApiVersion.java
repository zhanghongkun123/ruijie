package com.ruijie.rcos.rcdc.rco.module.web.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021.07.10
 *
 * @author linhj
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface ApiVersion {

    /**
     * 标记当前接口涉及的项目版本号
     */
    Version value() default Version.V1_0_0;

    /**
     * 针对标记版本的修订履历
     */
    String[] descriptions() default "无";

}

