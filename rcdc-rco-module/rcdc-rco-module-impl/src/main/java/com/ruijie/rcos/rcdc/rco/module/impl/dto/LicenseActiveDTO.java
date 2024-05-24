package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import com.ruijie.rcos.sk.base.support.EqualsHashcodeSupport;

/**
 * 授权激活
 * Description: Function Description
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月19日
 * 
 * @author lin
 */
public class LicenseActiveDTO extends EqualsHashcodeSupport {

    private String osType;
    
    private String osProType;

    public String getOsType() {
        return osType;
    }

    public void setOsType(String osType) {
        this.osType = osType;
    }

    public String getOsProType() {
        return osProType;
    }

    public void setOsProType(String osProType) {
        this.osProType = osProType;
    }
    
    
}
