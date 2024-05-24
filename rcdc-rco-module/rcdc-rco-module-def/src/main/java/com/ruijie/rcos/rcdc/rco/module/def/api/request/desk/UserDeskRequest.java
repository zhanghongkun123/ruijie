package com.ruijie.rcos.rcdc.rco.module.def.api.request.desk;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 用户桌面查询条件
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2021-11-17 16:43:00
 *
 * @author zjy
 */
public class UserDeskRequest {

    @NotNull
    private UUID userId;

    @Nullable
    private String terminalId;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(@Nullable String terminalId) {
        this.terminalId = terminalId;
    }
}
