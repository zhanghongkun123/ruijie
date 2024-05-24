package com.ruijie.rcos.rcdc.rco.module.def.hardwarecertification.dto;

import com.ruijie.rcos.sk.base.annotation.NotNull;
import org.springframework.lang.Nullable;

import java.util.UUID;

/**
 * Description: 校验用户硬件特征码是否通过请求参数
 * Copyright: Copyright (c) 2022
 * Company: Ruijie Co., Ltd.
 * Create Time: 2022/2/15
 *
 * @author linke
 */
public class UserHardwareCheckDTO {

    @NotNull
    private UUID userId;

    @Nullable
    private String userName;

    @NotNull
    private String terminalId;

    public UserHardwareCheckDTO(UUID userId, String terminalId) {
        this.userId = userId;
        this.terminalId = terminalId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }
}