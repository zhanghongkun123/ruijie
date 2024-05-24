package com.ruijie.rcos.rcdc.rco.module.openapi.rest.migration.prepare.dto;

/**
 * Description:
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/4/11
 *
 * @author zhangsiming
 */

public class IDVLicenseRemainCheckResponse {

    private String code;

    private int license;

    public IDVLicenseRemainCheckResponse(String code, int license) {
        this.code = code;
        this.license = license;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getLicense() {
        return license;
    }

    public void setLicense(int license) {
        this.license = license;
    }
}
