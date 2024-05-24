package com.ruijie.rcos.rcdc.rco.module.def.annotation;

import com.ruijie.rcos.rcdc.rco.module.def.BusinessKey;
import com.ruijie.rcos.rcdc.rco.module.def.servermodel.enums.ServerModelEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description: 服务器部署模式外部接口权限控制
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年09月04日
 *
 * @author wjp
 */

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ServerModel {

    /**
     * 允许执行的部署模式，默认为VDI
     *
     * @return ServerModelEnum
     */
    ServerModelEnum[] supportServerMode() default {ServerModelEnum.VDI_SERVER_MODEL};

    /**
     * 抛出异常的key，rest请求默认抛出异常，web请求默认不抛出
     *
     * @return key
     */
    String businessExKey() default BusinessKey.RCDC_RCO_CURRENT_SERVER_MODEL_NOT_SUPPORT_THIS_OPERATE;
}
