package com.ruijie.rcos.rcdc.rco.module.web.ctrl.rca.strategy.peripheral.validation;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.rca.module.def.dto.config.StorageDeviceConfig;
import com.ruijie.rcos.rcdc.rca.module.def.dto.strategy.RcaPeripheralStrategyDTO;
import com.ruijie.rcos.rcdc.rco.module.def.api.DeskStrategyAPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;

/**
 * Description: 云应用外设策略入参校验
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月06日
 *
 * @author lcy
 */

@Service
public class RcaPeripheralStrategyValidation {

    @Autowired
    private DeskStrategyAPI deskStrategyAPI;

    /**
     * 创建外设策略入参校验
     * @param request web入参
     * @throws BusinessException 业务异常
     */
    public void createAndUpdateRcaPeripheralStrategyValidate(RcaPeripheralStrategyDTO request) throws BusinessException {
        Assert.notNull(request, "request is not null");
        // 存储设备配置相关
        storageDeviceConfigValidate(request.getStorageDeviceConfig());
    }

    private void storageDeviceConfigValidate(StorageDeviceConfig storageDeviceConfig) throws BusinessException {
        if (Objects.isNull(storageDeviceConfig)) {
            return;
        }
        // 带宽控制配置
        deskStrategyAPI.usbBandWidthValidation(storageDeviceConfig.getEnableUsbBandwidth(), storageDeviceConfig.getUsbBandwidthInfo());
    }

}
