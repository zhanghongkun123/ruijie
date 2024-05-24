package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/25 20:35
 *
 * @author linrenjian
 */
public class AdminInfoDTO {
    /**
     * 管理员会话ID
     */
    private UUID adminSessionId;

    public UUID getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }
}
