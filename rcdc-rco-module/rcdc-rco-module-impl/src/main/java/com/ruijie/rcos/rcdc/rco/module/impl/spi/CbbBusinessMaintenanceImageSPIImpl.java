package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.maintenance.module.def.dto.CbbImageAutoPublishResultDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintenanceImageSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;

import java.util.UUID;

/**
 * Description: 镜像是否支持自动升级，办公支持
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022年09月27日
 *
 * @author zhengjingyong
 */

public class CbbBusinessMaintenanceImageSPIImpl implements CbbBusinessMaintenanceImageSPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(CbbBusinessMaintenanceImageSPIImpl.class);

    @Override
    public CbbImageAutoPublishResultDTO supportAutoPublish(UUID imageId) throws BusinessException {
        return new CbbImageAutoPublishResultDTO();
    }

    @Override
    public Boolean customOperateBeforePublish(UUID imageId) throws BusinessException {
        return true;
    }
}
