package com.ruijie.rcos.rcdc.rco.module.impl.dto;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/11/24
 *
 * @author liangyifeng
 */
public class ActiveLicenseDTO {
    String featureId;

    Integer activeLicenseNum;

    public ActiveLicenseDTO(String featureId, Integer activeLicenseNum) {
        this.featureId = featureId;
        this.activeLicenseNum = activeLicenseNum;
    }

    public String getFeatureId() {
        return featureId;
    }

    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    public Integer getActiveLicenseNum() {
        return activeLicenseNum;
    }

    public void setActiveLicenseNum(Integer activeLicenseNum) {
        this.activeLicenseNum = activeLicenseNum;
    }
}
