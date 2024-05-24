package com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Description: 消息处理器
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/12
 *
 * @param <T> 请求参数对象
 * @author Jarman
 */
public abstract class AbstractMessageHandler<T> {

    protected final Handlers handlers = new Handlers();

    /**
     * 注册消息处理器
     * 
     */
    protected abstract void register();

    /**
     * spi消息处理
     * 
     * @param request request
     * @throws Exception Exception
     */
    public final void execute(T request) throws Exception {
        Assert.notNull(request, "request cannot null");
        List<AbstractMessageHandlerTemplate> handlerList = handlers.getHandlerList();
        if (CollectionUtils.isEmpty(handlerList)) {
            throw new IllegalArgumentException("消息事件处理类不能为空");
        }
        for (AbstractMessageHandlerTemplate handler : handlerList) {
            if (handler.isNeedHandleMessage(request)) {
                handler.handleMessage(request);
                // 找到对应的处理类执行完后，不继续遍历剩余的处理类
                break;
            }
        }
    }

    /**
     * 预处理处理桌面状态与虚机状态（创建备份中时桌面状态需要查询虚机状态）
     *
     * @param request request
     */
    public final void preHandleMessageNoExecute(T request) {
        Assert.notNull(request, "request cannot null");
        List<AbstractMessageHandlerTemplate> handlerList = handlers.getHandlerList();
        if (CollectionUtils.isEmpty(handlerList)) {
            return;
        }
        for (AbstractMessageHandlerTemplate handler : handlerList) {
            if (handler.isNeedHandleMessage(request)) {
                // 找到对应的处理类执行完后，不继续遍历剩余的处理类
                return;
            }
        }
    }
}
