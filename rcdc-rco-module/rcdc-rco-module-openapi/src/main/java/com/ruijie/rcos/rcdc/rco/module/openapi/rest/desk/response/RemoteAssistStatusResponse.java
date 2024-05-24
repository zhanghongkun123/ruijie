package com.ruijie.rcos.rcdc.rco.module.openapi.rest.desk.response;

import java.util.UUID;

/**
 * 云桌面请求协助信息
 * Description: Function Description
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022-04-20
 *
 * @author zqj
 */
public class RemoteAssistStatusResponse {

    private UUID clusterId;

    private Integer businessCode;

    private Boolean hasRequest;

    public Integer getBusinessCode() {
        return businessCode;
    }

    public void setBusinessCode(Integer businessCode) {
        this.businessCode = businessCode;
    }

    public UUID getClusterId() {
        return clusterId;
    }

    public void setClusterId(UUID clusterId) {
        this.clusterId = clusterId;
    }

    public Boolean getHasRequest() {
        return hasRequest;
    }

    public void setHasRequest(Boolean hasRequest) {
        this.hasRequest = hasRequest;
    }
}
