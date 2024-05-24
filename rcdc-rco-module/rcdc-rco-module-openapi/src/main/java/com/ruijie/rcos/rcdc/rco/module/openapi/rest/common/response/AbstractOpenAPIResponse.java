package com.ruijie.rcos.rcdc.rco.module.openapi.rest.common.response;

/**
 * Description: OpenAPI响应抽象类
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/9/24 15:41
 *
 * @param <O> 泛型
 * @author lyb
 */
public abstract class AbstractOpenAPIResponse<O> {

    /**
     * dto转换
     * @param o 类型
     */
    public abstract void dtoToResponse(O o);
}
