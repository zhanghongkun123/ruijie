package com.ruijie.rcos.rcdc.rco.module.impl.service.impl.connector.mq.api;

import com.ruijie.rcos.rcdc.rco.module.def.api.dto.WatermarkConfigDTO;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiAction;
import com.ruijie.rcos.sk.connectkit.api.annotation.ApiGroup;
import com.ruijie.rcos.sk.connectkit.api.annotation.mq.MQ;

/**
 * Description: 通用信息通知
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-3-7 10:20:00
 *
 * @author zjy
 */
@MQ
@ApiGroup("commonInfo")
public interface CommonInfoProducerAPI {

    /**
     * 水印配置更新通知
     *
     * @param watermarkConfigDTO 水印配置信息
     * @Author: zjy
     * @Date: 2022/3/7 10:23
     **/
    @ApiAction("watermarkUpdate")
    void notifyWatermarkUpdate(WatermarkConfigDTO watermarkConfigDTO);


}
