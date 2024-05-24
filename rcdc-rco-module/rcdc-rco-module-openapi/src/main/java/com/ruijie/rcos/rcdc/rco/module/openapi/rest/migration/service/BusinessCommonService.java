package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.service;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.strategy.CbbDeskStrategyIDVDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbCloudDeskPattern;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbDeskPersonalConfigStrategyType;
import com.ruijie.rcos.sk.base.exception.BusinessException;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022.04.02
 *
 * @author chenl
 */
public interface BusinessCommonService {

    /**
     *
     * @param request 旧平台的桌面策略参数
     * @param preName 桌面策略名称前缀
     * @return UUID 桌面策略ID
     * @throws BusinessException 业务异常
     */
    UUID getRepeatDeskStrategy(CbbDeskStrategyIDVDTO request, String preName) throws BusinessException;

    /**
     *
     * @param cbbCloudDeskPattern 桌面类型
     * @return CbbDeskPersonalConfigStrategyType
     */
    CbbDeskPersonalConfigStrategyType getCbbDeskPersonalConfigStrategyType(CbbCloudDeskPattern cbbCloudDeskPattern);
}
