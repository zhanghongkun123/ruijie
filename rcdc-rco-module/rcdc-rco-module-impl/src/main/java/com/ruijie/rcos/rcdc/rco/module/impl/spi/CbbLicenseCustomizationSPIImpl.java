package com.ruijie.rcos.rcdc.rco.module.impl.spi;

import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseDurationEnum;
import com.ruijie.rcos.rcdc.license.module.def.enums.CbbLicenseTargetEnum;
import com.ruijie.rcos.rcdc.license.module.def.spi.CbbLicenseCustomizationSPI;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoGlobalParameterConstants;
import com.ruijie.rcos.sk.base.log.Logger;
import com.ruijie.rcos.sk.base.log.LoggerFactory;
import com.ruijie.rcos.sk.modulekit.api.tool.GlobalParameterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Description: license定制化spi，比如办公不支持授权共存特性、IDV终端默认送5个、VDI授权可以给终端用
 * Copyright: Copyright (c) 2023
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/2/22 17:34
 *
 * @author zsm
 */
public class CbbLicenseCustomizationSPIImpl implements CbbLicenseCustomizationSPI {

    @Autowired
    private GlobalParameterAPI globalParameterAPI;

    private final static Logger LOGGER = LoggerFactory.getLogger(CbbLicenseCustomizationSPIImpl.class);

    @Override
    public CbbLicenseDurationEnum determineDuration(CbbLicenseTargetEnum licenseTarget, String resourceId) {
        Assert.notNull(licenseTarget, "licenseTarget can not be null");
        Assert.hasText(resourceId, "resourceId can not be empty");
        return CbbLicenseDurationEnum.DEFAULT;
    }

    @Override
    public Boolean isSupportDurationCoexistence() {
        return false;
    }


    @Override
    public Boolean isSupportVdiCompatibility() {
        String vdiCompatibility = globalParameterAPI.findParameter(RcoGlobalParameterConstants.ENABLE_AUTH_COMPATIBLE);
        if (StringUtils.hasText(vdiCompatibility)) {
            return Boolean.parseBoolean(vdiCompatibility);
        } else {
            return false;
        }
    }

    @Override
    public Boolean wasSupportVdiCompatibility() {
        String vdiCompatibility = globalParameterAPI.findParameter(RcoGlobalParameterConstants.ENABLE_AUTH_COMPATIBLE);
        return StringUtils.hasText(vdiCompatibility);
    }

    @Override
    public Boolean addExtraFivePerpetualIdvLicenseWhenNotExistTemporaryIdvLicense() {
        return true;
    }

    @Override
    public Boolean isSupportLicenseReplacement() {
        return true;
    }

    @Override
    public boolean isActiveUserLicenseMode() {
        String activeUserLicenseModeStr = globalParameterAPI.findParameter(RcoGlobalParameterConstants.KEY_ACTIVE_USER_LICENSE_MODE);
        if (StringUtils.hasText(activeUserLicenseModeStr)) {
            return Boolean.parseBoolean(activeUserLicenseModeStr);
        } else {
            return false;
        }
    }
}
