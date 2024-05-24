package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import com.ruijie.rcos.rcdc.appcenter.module.def.api.CbbAppSoftwarePackageMgmtAPI;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.AppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.appcenter.module.def.dto.request.CbbSearchObtainVmAppSoftwarePackageDTO;
import com.ruijie.rcos.rcdc.maintenance.module.def.spi.CbbBusinessMaintenanceVmFilterSPI;

/**
 * Description:
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023年11月28日
 *
 * @author luoyuanbin
 */
public class CbbBusinessMaintenanceVmFilterSPIImpl implements CbbBusinessMaintenanceVmFilterSPI {

    @Autowired
    private CbbAppSoftwarePackageMgmtAPI cbbAppSoftwarePackageMgmtAPI;

    @Override
    public boolean filterImageTemplateHasUamAppTempVm(UUID imageId) {
        Assert.notNull(imageId, "imageId cannot be null");
        CbbSearchObtainVmAppSoftwarePackageDTO cbbSearchObtainVmAppSoftwarePackageDTO = new CbbSearchObtainVmAppSoftwarePackageDTO();
        cbbSearchObtainVmAppSoftwarePackageDTO.setImageTemplateId(imageId);
        List<AppSoftwarePackageDTO> appSoftwarePackageDTOList =
                cbbAppSoftwarePackageMgmtAPI.searchObtainVmAppSoftwarePackage(cbbSearchObtainVmAppSoftwarePackageDTO);
        return appSoftwarePackageDTOList.isEmpty();
    }
}
