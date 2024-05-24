package com.ruijie.rcos.rcdc.rco.module.impl.dto;

import java.util.UUID;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.ruijie.rcos.rcdc.rco.module.def.api.enums.IdvTerminalModeEnums;
import com.ruijie.rcos.rcdc.terminal.module.def.spi.request.CbbTerminalAuthRequest;

/**
 * Description: IDV终端配置向导数据上报
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/03/03
 *
 * @author brq
 */
public class IDVTerminalReportConfigWizardDTO {

    private UUID userId;

    private String userName;

    private UUID terminalGroupId;

    @Enumerated(EnumType.STRING)
    private IdvTerminalModeEnums idvTerminalMode;

    /**
     * 终端是否支持TC引导模式
     */
    private Boolean canSupportTC;

    private CbbTerminalAuthRequest terminalAuthDTO;

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UUID getTerminalGroupId() {
        return terminalGroupId;
    }

    public void setTerminalGroupId(UUID terminalGroupId) {
        this.terminalGroupId = terminalGroupId;
    }

    public IdvTerminalModeEnums getIdvTerminalMode() {
        return idvTerminalMode;
    }

    public void setIdvTerminalMode(IdvTerminalModeEnums idvTerminalMode) {
        this.idvTerminalMode = idvTerminalMode;
    }

    public Boolean getCanSupportTC() {
        return canSupportTC;
    }

    public void setCanSupportTC(Boolean canSupportTC) {
        this.canSupportTC = canSupportTC;
    }

    public CbbTerminalAuthRequest getTerminalAuthDTO() {
        return terminalAuthDTO;
    }

    public void setTerminalAuthDTO(CbbTerminalAuthRequest terminalAuthDTO) {
        this.terminalAuthDTO = terminalAuthDTO;
    }
}
