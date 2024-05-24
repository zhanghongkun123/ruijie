package com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;

/**
 * @author luojianmo
 * @Description:
 * @Company: Ruijie Co., Ltd.
 * @CreateTime: 2020-02-26 0:14
 */
@MQ
@ApiGroup("cmsDockingMq")
public interface CmsDockingProducerAPI {

    /**
     * 同步数据到cms
     *
     * @param syncMessage 同步数据
     */
    @ApiAction("syncMessageToCMS")
    void syncMessageToCMS(@NotNull String syncMessage);

}
