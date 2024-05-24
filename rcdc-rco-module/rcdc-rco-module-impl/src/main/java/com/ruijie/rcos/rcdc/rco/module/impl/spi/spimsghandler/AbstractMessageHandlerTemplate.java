package com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler;

import org.springframework.util.Assert;

/**
 * Description: 事件消息处理模板
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 * 
 * @param <T> 请求参数对象
 * @author Jarman
 */
public abstract class AbstractMessageHandlerTemplate<T> {

    /**
     * 是否需要处理当前事件消息
     *
     * @param request request
     *
     * @return true 需要处理本次消息
     */
    protected abstract boolean isNeedHandleMessage(T request);

    /**
     * 处理消息
     *
     * @param request request
     * @throws Exception Exception
     */
    protected abstract void processMessage(T request) throws Exception;

    /**
     * 事件消息处理
     *
     * @param request request
     */
    final void handleMessage(T request) throws Exception {
        Assert.notNull(request, "request cannot null");
        boolean isNeedHandle = isNeedHandleMessage(request);
        if (isNeedHandle) {
            processMessage(request);
        }
    }
}
