package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

import com.ruijie.rcos.rcdc.clouddesktop.module.def.enums.CbbImageType;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/30 11:11
 *
 * @author linrenjian
 */
public class ShineRequestImageDTO {

    /**
     * 镜像类型
     */
    private CbbImageType type;

    /**
     * 管理员sessionId
     */
    private UUID adminSessionId;

    public UUID getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    public CbbImageType getType() {
        return type;
    }

    public void setType(CbbImageType type) {
        this.type = type;
    }
}
