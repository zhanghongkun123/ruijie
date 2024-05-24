package com.ruijie.rcos.rcdc.rco.module.def.api.dto;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Description:
 * Copyright: Copyright (c) 2019
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021/7/25 20:35
 *
 * @author linrenjian
 */
public class TokenContextDTO {
    /**
     * token
     */
    private UUID token;

    /**
     * 失效时间
     */
    private LocalDateTime invalidDate;

    /**
     * 管理员id
     */
    private UUID adminId;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public LocalDateTime getInvalidDate() {
        return invalidDate;
    }

    public void setInvalidDate(LocalDateTime invalidDate) {
        this.invalidDate = invalidDate;
    }

    public UUID getAdminId() {
        return adminId;
    }

    public void setAdminId(UUID adminId) {
        this.adminId = adminId;
    }
}
