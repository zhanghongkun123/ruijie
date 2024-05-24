package com.ruijie.rcos.rcdc.rco.module.openapi.service.common;

import java.lang.annotation.*;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.11
 *
 * @author linhj
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServerBusinessName {

    // 备注
    String value() default "";
}
