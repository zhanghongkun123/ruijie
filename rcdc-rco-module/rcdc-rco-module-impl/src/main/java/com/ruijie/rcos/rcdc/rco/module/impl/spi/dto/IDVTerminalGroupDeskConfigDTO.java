package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.UUID;

/**
 * Description: Function Description
 * Copyright: Copyright (c) 2020
 * Company: Ruijie Co., Ltd.
 * Create Time: 2020/3/9 12:41
 *
 * @author zhangyichi
 */
public class IDVTerminalGroupDeskConfigDTO {

    private UUID cbbTerminalGroupId;

    private UUID cbbIdvDesktopStrategyId;

    private UUID cbbIdvDesktopImageId;

    public UUID getCbbTerminalGroupId() {
        return cbbTerminalGroupId;
    }

    public void setCbbTerminalGroupId(UUID cbbTerminalGroupId) {
        this.cbbTerminalGroupId = cbbTerminalGroupId;
    }

    public UUID getCbbIdvDesktopStrategyId() {
        return cbbIdvDesktopStrategyId;
    }

    public void setCbbIdvDesktopStrategyId(UUID cbbIdvDesktopStrategyId) {
        this.cbbIdvDesktopStrategyId = cbbIdvDesktopStrategyId;
    }

    public UUID getCbbIdvDesktopImageId() {
        return cbbIdvDesktopImageId;
    }

    public void setCbbIdvDesktopImageId(UUID cbbIdvDesktopImageId) {
        this.cbbIdvDesktopImageId = cbbIdvDesktopImageId;
    }
}
