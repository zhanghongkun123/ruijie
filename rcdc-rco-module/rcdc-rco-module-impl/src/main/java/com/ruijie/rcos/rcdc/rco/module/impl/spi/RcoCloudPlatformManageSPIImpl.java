package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbCloudPlatformInnerDataAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.enums.CloudPlatformStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.api.CbbPhysicalServerMgmtAPI;
import com.ruijie.rcos.rcdc.hciadapter.module.def.dto.cloudplatform.CloudPlatformDTO;
import com.ruijie.rcos.rcdc.hciadapter.module.def.spi.CloudPlatformManageSPI;
import com.ruijie.rcos.sk.base.exception.BusinessException;
import org.springframework.util.Assert;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024年03月11日
 *
 * @author xgx
 */
public class RcoCloudPlatformManageSPIImpl implements CloudPlatformManageSPI {

    @Autowired
    private CbbPhysicalServerMgmtAPI cbbPhysicalServerMgmtAPI;

    @Autowired
    private CbbCloudPlatformInnerDataAPI cbbCloudPlatformInnerDataAPI;

    @Override
    public boolean remove(CloudPlatformDTO cloudPlatformDTO) throws BusinessException {
        Assert.notNull(cloudPlatformDTO, "cloudPlatformDTO can not be null");
        // 1.物理机数据位涉及云平台，优先删除
        cbbPhysicalServerMgmtAPI.clearCloudPlatformData(cloudPlatformDTO.getId());
        // 2.删除内置模板，若云平台在线则正常删除，否则强删
        boolean shouldForceDelete = cloudPlatformDTO.getStatus() == CloudPlatformStatus.OFFLINE;
        if (shouldForceDelete) {
            cbbCloudPlatformInnerDataAPI.deleteCloudPlatformReplicationFromDb(cloudPlatformDTO.getId());
        } else {
            cbbCloudPlatformInnerDataAPI.deleteCloudPlatformReplication(cloudPlatformDTO.getId());
        }
        return true;
    }
}