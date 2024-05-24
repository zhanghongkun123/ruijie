package com.ruijie.rcos.rcdc.rco.module.def.api.request;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.sk.base.annotation.NotBlank;
import com.ruijie.rcos.sk.modulekit.api.comm.Request;
import org.springframework.lang.Nullable;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.UUID;

/**
 *
 * Description: 用户终端关系Request
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020年3月9日
 *
 * @author brq
 */
public class UserTerminalRequest implements Request {

    @NotBlank
    private String terminalId;

    @Nullable
    private UUID userId;

    @Nullable
    private String userName;

    @Nullable
    private UUID deskId;

    @Nullable
    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @Nullable
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(@Nullable UUID userId) {
        this.userId = userId;
    }

    @Nullable
    public String getUserName() {
        return userName;
    }

    public void setUserName(@Nullable String userName) {
        this.userName = userName;
    }

    @Nullable
    public UUID getDeskId() {
        return deskId;
    }

    public void setDeskId(@Nullable UUID deskId) {
        this.deskId = deskId;
    }

    @Nullable
    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(@Nullable IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }
}
