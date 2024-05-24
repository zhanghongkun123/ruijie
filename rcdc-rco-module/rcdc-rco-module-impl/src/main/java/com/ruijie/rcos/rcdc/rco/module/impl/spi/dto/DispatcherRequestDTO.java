package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.codec.adapter.def.dto.CbbDispatcherRequest;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年04月28日
 * @param <T> requestParam
 * @author 徐国祥
 */
public class DispatcherRequestDTO<T> extends CbbDispatcherRequest {
    private T requestParam;

    public T getRequestParam() {
        return requestParam;
    }

    public void setRequestParam(T requestParam) {
        this.requestParam = requestParam;
    }
}
