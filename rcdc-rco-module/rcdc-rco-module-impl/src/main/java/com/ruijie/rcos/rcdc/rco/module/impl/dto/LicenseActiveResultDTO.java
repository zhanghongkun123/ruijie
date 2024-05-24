package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * 授权结果
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月19日
 * 
 * @author lin
 */
public class LicenseActiveResultDTO extends EqualsHashcodeSupport {

    private String licenseOsProType;

    public String getLicenseOsProType() {
        return licenseOsProType;
    }

    public void setLicenseOsProType(String licenseOsProType) {
        this.licenseOsProType = licenseOsProType;
    }
    
}
