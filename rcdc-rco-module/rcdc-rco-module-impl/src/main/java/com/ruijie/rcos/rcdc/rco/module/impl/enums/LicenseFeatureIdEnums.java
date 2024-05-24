package com.ruijie.rcos.rcdc.rco.module.impl.enums;

import org.springframework.util.Assert;

import com.ruijie.rcos.sk.base.util.StringUtils;

/**
 * 授权码feature枚举
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月17日
 * 
 * @author lin
 */
public enum LicenseFeatureIdEnums {

    CML_DESKTOP("RG-CML-Desktop-VDI", null), // vdi授权证书
    RG_CDC_VDI_LIC("RG-CDC-VDI-Lic", null), // vdi授权证书新名称
    CMWINPRO_EDU("RG-CMWinPro-EDU", "RG-CMWinPro"), CMWINPRO_NOEDU("RG-CMWinPro-NonEDU", "RG-CMWinPro"), CMWIN_ENT("RG-CMWinEnt",
            "RG-CMWinEnt"), RG_CML_PLATFORM_CPU("RG-CML-Platform-CPU", null), // cpu授权证书
    RG_CCP_DCP_LIC("RG-CCP-DCP-Lic", null), // cpu授权证书新名称
    IDV_TERMINAL("RG-CDC-IDV-Lic", null), // 未知
    RG_CCP_DCP_LIC_EDU("RG-CCP-DCP-Lic-EDU", null), // 教育场景CPU授权名称
    // 教育场景的TCI授权.
    RG_CDC_VOI_LIC_EDU("RG-CDC-TCI-Lic-EDU", null),
    // 非教育场景 TCI授权
    RG_CDC_VOI_LIC("RG-CDC-TCI-Lic", null),
    // TCI到IDV升级授权
    RG_CDC_IDV_UP("RG-CDC-IDV-UP", null);

    private String featureId;

    private String osProType;

    /**
     * 获取
     *
     * @param featureId featureId
     * @return LicenseFeatureIdEnums
     */
    public static LicenseFeatureIdEnums getByFeatureId(String featureId) {
        Assert.notNull(featureId, "featureId can not be null");
        for (LicenseFeatureIdEnums temp : LicenseFeatureIdEnums.values()) {
            if (StringUtils.equals(featureId, temp.getFeatureId())) {
                return temp;
            }
        }
        // 未匹配返回null
        return null;
    }


    LicenseFeatureIdEnums(String featureId, String osProType) {
        this.featureId = featureId;
        this.osProType = osProType;
    }

    public String getFeatureId() {
        return featureId;
    }

    public String getOsProType() {
        return osProType;
    }
}
