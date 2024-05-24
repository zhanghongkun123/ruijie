package com.ruijie.rcos.rcdc.rco.module.impl.spi.dto;

import java.util.Date;
import java.util.UUID;

/**
 * Description: 退出虚机到终端
 * Copyright: Copyright (c) 2018
 * Company: Ruijie Co., Ltd.
 * Create Time: 2019/4/18
 *
 * @author linrenjain
 */
public class QuitVmToTerminalDTO {

    /**
     * 生效结束时间
     */
    private Date endTime;

    /**
     * 桌面ID
     */
    private UUID desktopId;

    public UUID getDesktopId() {
        return desktopId;
    }

    public void setDesktopId(UUID desktopId) {
        this.desktopId = desktopId;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
