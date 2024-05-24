package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import com.ruijie.rcos.rcdc.rco.module.common.annotation.TargetHost;
import com.ruijie.rcos.rcdc.rco.module.common.annotation.TcpAction;
import com.ruijie.rcos.rcdc.rco.module.common.enums.HostTypeEnums;
import com.ruijie.rcos.rcdc.rco.module.def.constants.RcoOneAgentToRcdcAction;
import com.ruijie.rcos.sk.base.annotation.NotNull;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2024
 * Company: Ruijie Co., Ltd.
 * Create Time: 2024/2/21
 *
 * @author zqj
 */

public class WinServerAuthorityDTO {

    /**
     * 应用主机ID
     */
    @NotNull
    private UUID hostId;

    @NotNull
    private Integer daysLeft;

    public UUID getHostId() {
        return hostId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }
}
