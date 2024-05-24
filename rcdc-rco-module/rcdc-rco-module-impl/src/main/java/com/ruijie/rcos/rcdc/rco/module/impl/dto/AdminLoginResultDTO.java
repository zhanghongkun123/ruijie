package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/2/19 11:49
 *
 * @author zhangyichi
 */
public class AdminLoginResultDTO {
    private UUID adminSessionId;

    public AdminLoginResultDTO() {
    }

    public AdminLoginResultDTO(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }

    public UUID getAdminSessionId() {
        return adminSessionId;
    }

    public void setAdminSessionId(UUID adminSessionId) {
        this.adminSessionId = adminSessionId;
    }
}
