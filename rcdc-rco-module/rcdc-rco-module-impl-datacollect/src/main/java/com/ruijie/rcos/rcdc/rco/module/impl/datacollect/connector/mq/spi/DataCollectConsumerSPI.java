package com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.spi;

import com.ruijie.rcos.rcdc.rco.module.impl.datacollect.connector.mq.dto.CollectRequestDTO;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MqConsumer;

/**
 * Description: MQ消费者接口
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/6/10
 *
 * @author jarman
 */
@MQ
@ApiGroup("SDP-Agent")
public interface DataCollectConsumerSPI {

    /**
     * 采集数据请求
     * @param collectRequestDTO 参数
     * @throws BusinessException 异常
     */
    @ApiAction("dataRequest")
    @MqConsumer(name = "${mq_prefix}-cmc-data-collect")
    void collect(CollectRequestDTO collectRequestDTO) throws BusinessException;
}
