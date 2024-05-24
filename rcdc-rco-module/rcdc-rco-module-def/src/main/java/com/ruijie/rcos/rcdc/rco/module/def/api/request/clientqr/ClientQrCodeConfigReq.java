package com.ruijie.rcos.rcdc.rco.module.def.api.request.clientqr;

/**
 * Description:
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd
 * Create Time: 2024-03-14 13:11
 *
 * @author wanglianyun
 */
public class ClientQrCodeConfigReq {

    private Integer order;

    private Boolean switchStatus;

    private Long expireTime;

    private String advanceConfig;

    private String contentPrefix;

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Boolean getSwitchStatus() {
        return switchStatus;
    }

    public void setSwitchStatus(Boolean switchStatus) {
        this.switchStatus = switchStatus;
    }

    public Long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public String getAdvanceConfig() {
        return advanceConfig;
    }

    public void setAdvanceConfig(String advanceConfig) {
        this.advanceConfig = advanceConfig;
    }

    public String getContentPrefix() {
        return contentPrefix;
    }

    public void setContentPrefix(String contentPrefix) {
        this.contentPrefix = contentPrefix;
    }
}
