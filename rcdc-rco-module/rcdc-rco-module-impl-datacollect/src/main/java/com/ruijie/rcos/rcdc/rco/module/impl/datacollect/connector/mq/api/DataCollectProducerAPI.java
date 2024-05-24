package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.api;

import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto.CollectDataResponseDTO;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MqConsumer;

/**
 * Description: 上传采集数据
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/10
 *
 * @author jarman
 */
@MQ
@ApiGroup("SDP-Agent")
public interface DataCollectProducerAPI {

    /**
     * 上传采集数据
     *
     * @param responseDTO 要上传的数据对象
     */
    @ApiAction("dataResponse")
    @MqConsumer(name = "${mq_prefix}-dataResponse")
    void sendData(CollectDataResponseDTO responseDTO);
}
