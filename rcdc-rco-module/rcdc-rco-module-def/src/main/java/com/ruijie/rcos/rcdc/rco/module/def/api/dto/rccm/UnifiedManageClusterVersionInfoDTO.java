package com.ruijie.rcos.rcdc.rco.module.def.api.dto.rccm;

/**
 * Description: 集群版本信息DTO
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2023/4/11
 *
 * @author WuShengQiang
 */
public class UnifiedManageClusterVersionInfoDTO {

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
