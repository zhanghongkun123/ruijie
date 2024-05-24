package com.ruijie.rcos.rcdc.rco.module.impl.spi.spimsghandler;

import com.google.common.collect.Lists;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/7/15
 *
 * @author Jarman
 */
public final class Handlers {

    private final List<AbstractMessageHandlerTemplate> handlerList = Lists.newArrayList();


    /**
     * 添加消息处理
     *
     * @param handler handler
     * @return Handlers
     */
    public Handlers add(AbstractMessageHandlerTemplate handler) {
        Assert.notNull(handler, "handler can not be null");
        this.handlerList.add(handler);
        return this;
    }

    /**
     * 获取处理类列表
     *
     * @return Set<AbstractMessageHandlerTemplate>
     */
    public List<AbstractMessageHandlerTemplate> getHandlerList() {
        return this.handlerList;
    }
}
