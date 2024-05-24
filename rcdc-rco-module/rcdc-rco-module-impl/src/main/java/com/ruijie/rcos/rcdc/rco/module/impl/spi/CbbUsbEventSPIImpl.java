package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.alibaba.fastjson.JSON;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbUSBTypeMgmtAPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbUsbEventSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbUsbEventRequest;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyTciNotifyAPI;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/11/17 14:11
 *
 * @author yxq
 */
public class CbbUsbEventSPIImpl implements CbbUsbEventSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbUsbEventSPIImpl.class);

    @Autowired
    private DeskStrategyTciNotifyAPI deskStrategyTciNotifyAPI;

    @Autowired
    private CbbUSBTypeMgmtAPI cbbUSBTypeMgmtAPI;

    @Override
    public void usbTypeRelateDeviceModified(CbbUsbEventRequest request) {
        Assert.notNull(request, "request must not be null");

        LOGGER.info("收到USB类型关联USB设备变更事件，请求信息为[{}]", JSON.toJSONString(request));

        List<UUID> stratetyIdList = cbbUSBTypeMgmtAPI.getDeskStrategyListByUsbTypeId(request.getUsbTypeId());
        if (CollectionUtils.isEmpty(stratetyIdList)) {
            LOGGER.info("USB类型[{}]关联云桌面策略为空，无需处理", request.getUsbTypeId());
            return;
        }

        for (UUID strategyId : stratetyIdList) {
            try {
                deskStrategyTciNotifyAPI.notifyFetchStartParams(strategyId);
            } catch (Exception e) {
                LOGGER.warn("处理请求[{}]异常，捕获异常，不影响后续逻辑执行", strategyId, e);
            }
        }

    }
}
