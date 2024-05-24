package com.ruijie.rcos.rcdc.rco.module.openapi.rest.rccm.response;

/**
 * Description: 集群版本响应体
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author WuShengQiang
 */
public class UnifiedManageClusterVersionInfoResponse {

    /**
     * 集群RCDC版本
     */
    private String version;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
