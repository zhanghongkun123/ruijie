package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.io.Serializable;
import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2021
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021年06月30日
 *
 * @author zhanghongkun
 */
public class StartVmDTO implements Serializable {

    /**
     * 云桌面id
     */
    private UUID id;

    /**
     * 是否开启代理协议
     */
    private Boolean enableAgreementAgency;

    /**
     * 是否安卓
     */
    private Boolean android;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Boolean getEnableAgreementAgency() {
        return enableAgreementAgency;
    }

    public void setEnableAgreementAgency(Boolean enableAgreementAgency) {
        this.enableAgreementAgency = enableAgreementAgency;
    }

    public Boolean getAndroid() {
        return android;
    }

    public void setAndroid(Boolean android) {
        this.android = android;
    }
}
