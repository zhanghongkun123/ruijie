package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.dto.CbbDeskWindowLicenseNotifyDTO;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.CbbWindowLicenseSPI;
import com.ruijie.rcos.rcdc.clouddesktop.module.def.spi.request.CbbDeskWindowLicenseNotifyRequest;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.dto.LicenseActiveResultDTO;
import com.ruijie.rcos.rcdc.rco.module.impl.service.LicenseService;
import com.ruijie.rcos.sk.modulekit.api.comm.DefaultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 处理rcolicense
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月19日
 * 
 * @author lin
 */
public class CbbDeskWindowLicenseSPIImpl implements CbbWindowLicenseSPI {

    @Autowired
    private LicenseService licenseService;
    
    @Override
    public CbbDeskWindowLicenseNotifyDTO getWindowsLicenseType(CbbDeskWindowLicenseNotifyRequest request) {
        Assert.notNull(request, "request can not be null");
        LicenseActiveDTO licenseActiveDTO = new LicenseActiveDTO();
        licenseActiveDTO.setOsProType(request.getOsProType());
        licenseActiveDTO.setOsType(request.getOsType());
        LicenseActiveResultDTO result = licenseService.matchWindowsLicense(licenseActiveDTO);
        CbbDeskWindowLicenseNotifyDTO response = new CbbDeskWindowLicenseNotifyDTO();
        if (result != null) {
            response.setOsProType(result.getLicenseOsProType());
        }
        return response;
    }

    @Override
    public synchronized DefaultResponse notifyWindowLicenseResult(Boolean active) {
        Assert.notNull(active, "active can not be null");
        if (Boolean.TRUE.equals(active)) {
            licenseService.activeSuccess();
        }
        return DefaultResponse.Builder.success();
    }

}
